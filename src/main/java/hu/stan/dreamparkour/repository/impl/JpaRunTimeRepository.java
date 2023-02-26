package hu.stan.dreamparkour.repository.impl;

import hu.stan.dreamparkour.configuration.ParkourConfiguration;
import hu.stan.dreamparkour.mapper.SplitRunTimeMapper;
import hu.stan.dreamparkour.mapper.TotalRunTimeMapper;
import hu.stan.dreamparkour.model.TotalRunTime;
import hu.stan.dreamparkour.model.checkpoint.CheckpointNode;
import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamparkour.model.entity.DbSplitRunTime;
import hu.stan.dreamparkour.model.entity.DbTotalRunTime;
import hu.stan.dreamparkour.repository.RunTimeRepository;
import hu.stan.dreamparkour.repository.util.HibernateUtils;
import hu.stan.dreamplugin.core.configuration.registry.ConfigurationRegistrar;
import hu.stan.dreamplugin.core.dependency.injector.DependencyInjector;
import org.bukkit.entity.Player;
import org.hibernate.SessionFactory;

import java.util.List;

public class JpaRunTimeRepository implements RunTimeRepository {

  private final SessionFactory sessionFactory;
  private final TotalRunTimeMapper totalRunTimeMapper;
  private final SplitRunTimeMapper splitRunTimeMapper;
  private final ParkourConfiguration parkourConfiguration;

  public JpaRunTimeRepository() {
    this.totalRunTimeMapper = DependencyInjector.getInstance().getInstanceOf(TotalRunTimeMapper.class);
    this.splitRunTimeMapper = DependencyInjector.getInstance().getInstanceOf(SplitRunTimeMapper.class);
    this.parkourConfiguration = ConfigurationRegistrar.getConfiguration(ParkourConfiguration.class);
    this.sessionFactory = HibernateUtils.getInstance().getSessionFactory();
  }

  @Override
  public void save(final TotalRunTime totalRunTime) {
    final var dbTotalRunTime = totalRunTimeMapper.toDbTotalRunTime(totalRunTime);
    final var session = sessionFactory.openSession();
    session.beginTransaction();
    session.merge(dbTotalRunTime);
    session.getTransaction().commit();
    session.close();
  }

  @Override
  public void save(final CheckpointNode checkpointNode, final TotalRunTime totalRunTime) {
    final var dbSplitTimes =
        splitRunTimeMapper.toDbSplitRunTimes(checkpointNode, totalRunTime.runId());
    final var session = sessionFactory.openSession();
    session.beginTransaction();
    for (final DbSplitRunTime dbSplitTime : dbSplitTimes) {
      session.merge(dbSplitTime);
    }
    session.getTransaction().commit();
    session.close();
  }

  @Override
  public CheckpointNode getBestSplitTimesForCourseAndPlayer(
      final Course course,
      final Player player) {
    try (final var session = sessionFactory.openSession()) {
      session.beginTransaction();
      final var query = session.createQuery(
          "SELECT srt "
              + "FROM DbSplitRunTime srt "
              + "WHERE totalRun = (SELECT dtrt FROM DbTotalRunTime dtrt "
              + "WHERE course.courseId = :courseId "
              + "AND dtrt.playerId = :playerId "
              + "AND runTime = (SELECT MIN(runTime) FROM DbTotalRunTime dtrt2 WHERE playerId = dtrt.playerId "
              + "AND dtrt2.course = dtrt.course)"
              + "GROUP BY playerId) "
              + "ORDER BY splitTime ASC"
          , DbSplitRunTime.class);
      query.setParameter("courseId", course.getCourseId().toString());
      query.setParameter("playerId", player.getUniqueId().toString());
      final var splitRunTime = query.getResultList();
      session.getTransaction().commit();
      return splitRunTimeMapper.toCheckpointNode(splitRunTime);
    }
  }

  @Override
  public List<TotalRunTime> getBestTimesForCoursePerPlayer(final Course course) {
    try (final var session = sessionFactory.openSession()) {
      session.beginTransaction();
      final var query = session.createQuery(
          "SELECT dtrt "
              + "FROM DbTotalRunTime dtrt "
              + "WHERE course.courseId = :courseId "
              + "AND runTime = (SELECT MIN(runTime) FROM DbTotalRunTime dtrt2 WHERE playerId = dtrt.playerId) "
              + "GROUP BY playerId", DbTotalRunTime.class);
      query.setParameter("courseId", course.getCourseId().toString());
      final var bestTimesForCourse = query.getResultList();
      session.getTransaction().commit();
      return mapToTotalRunTimes(bestTimesForCourse);
    }
  }

  @Override
  public List<TotalRunTime> getTopRunsForCourse(final Course course) {
    try (final var session = sessionFactory.openSession()) {
      session.beginTransaction();
      final var query = session.createQuery(
          "SELECT dtrt "
              + "FROM DbTotalRunTime dtrt "
              + "WHERE course.courseId = :courseId "
              + "ORDER BY dtrt.runTime ASC ", DbTotalRunTime.class);
      query.setMaxResults(parkourConfiguration.topRunsPerCourseInCache);
      query.setParameter("courseId", course.getCourseId().toString());
      final var bestTimesForCourse = query.getResultList();
      session.getTransaction().commit();
      return mapToTotalRunTimes(bestTimesForCourse);
    }
  }

  private List<TotalRunTime> mapToTotalRunTimes(final List<DbTotalRunTime> dbTotalRunTimes) {
    return dbTotalRunTimes.stream().map(totalRunTimeMapper::toTotalRunTime).toList();
  }
}
