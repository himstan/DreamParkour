package hu.stan.dreamparkour.repository.impl;

import hu.stan.dreamparkour.event.checkpoint.RemoveCheckpointEvent;
import hu.stan.dreamparkour.mapper.CourseMapper;
import hu.stan.dreamparkour.model.checkpoint.Checkpoint;
import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamparkour.model.entity.DbCourse;
import hu.stan.dreamparkour.repository.CourseRepository;
import hu.stan.dreamparkour.repository.util.HibernateUtils;
import hu.stan.dreamweaver.DreamWeaver;
import hu.stan.dreamweaver.core.dependency.injector.DependencyInjector;
import org.bukkit.Bukkit;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class JpaCourseRepository implements CourseRepository {

  private final SessionFactory sessionFactory;
  private final CourseMapper courseMapper;

  public JpaCourseRepository() {
    this.courseMapper = DependencyInjector.getInstance().getInstanceOf(CourseMapper.class);
    this.sessionFactory = HibernateUtils.getInstance().getSessionFactory();
  }

  @Override
  public void saveCourse(final Course course) {
    saveCourse(course, null);
  }

  @Override
  public void saveCourse(final Course course, Consumer<DbCourse> callback) {
    Bukkit.getScheduler().runTaskAsynchronously(DreamWeaver.getInstance(), () -> {
      final var dbCourse = courseMapper.toDbCourse(course);
      final var session = sessionFactory.openSession();
      session.beginTransaction();
      session.merge(dbCourse);
      session.getTransaction().commit();
      session.close();
      if (Objects.nonNull(callback)) {
        Bukkit.getScheduler().runTask(DreamWeaver.getInstance(), () -> callback.accept(dbCourse));
      }
    });
  }

  @Override
  public void removeCourse(final Course course) {
    removeCourse(course, null);
  }

  @Override
  public void removeCourse(final Course course, Consumer<DbCourse> callback) {
    Bukkit.getScheduler().runTaskAsynchronously(DreamWeaver.getInstance(), () -> {
      final var dbCourse = courseMapper.toDbCourse(course);
      final var session = sessionFactory.openSession();
      session.beginTransaction();
      session.remove(dbCourse);
      session.getTransaction().commit();
      session.close();
      if (Objects.nonNull(callback)) {
        Bukkit.getScheduler().runTask(DreamWeaver.getInstance(), () -> callback.accept(dbCourse));
      }
    });
  }

  @Override
  public Optional<Course> findById(final UUID courseId) {
    try (final var session = sessionFactory.openSession()) {
      session.beginTransaction();
      final var query = session.createQuery(
          "SELECT course FROM DbCourse course WHERE course.courseId = :courseId", DbCourse.class);
      query.setParameter("courseId", courseId);
      final var course = query.getSingleResult();
      session.getTransaction().commit();
      return mapToOptionalCourse(course);
    }
  }

  @Override
  public List<Course> findAll() {
    try (final var session = sessionFactory.openSession()) {
      session.beginTransaction();
      final var query = session.createQuery(
          "SELECT course FROM DbCourse course", DbCourse.class);
      final var courses = query.list();
      session.getTransaction().commit();
      return courses.stream().map(courseMapper::toCourse).toList();
    }
  }

  private Optional<Course> mapToOptionalCourse(final DbCourse dbCourse) {
    return Optional.ofNullable(dbCourse).map(courseMapper::toCourse);
  }

  private void handleCheckpointRemoval(final Course course) {
    final var deletedCheckpoints = course.getCheckpointsWithDeleted().stream()
        .filter(Checkpoint::isDeleted)
        .peek(checkpoint -> Bukkit.getPluginManager().callEvent(new RemoveCheckpointEvent(checkpoint)))
        .toList();
    course.getCheckpointsWithDeleted().removeAll(deletedCheckpoints);
  }
}
