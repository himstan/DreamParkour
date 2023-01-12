package hu.stan.dreamparkour.repository.impl;

import hu.stan.dreamparkour.mapper.CourseMapper;
import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamparkour.model.entity.DbCourse;
import hu.stan.dreamparkour.repository.CourseRepository;
import hu.stan.dreamparkour.repository.util.HibernateUtils;
import hu.stan.dreamplugin.core.dependency.injector.DependencyInjector;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.hibernate.SessionFactory;

public class JpaCourseRepository implements CourseRepository {

  private final SessionFactory sessionFactory;
  private final CourseMapper courseMapper;

  public JpaCourseRepository() {
    this.courseMapper = DependencyInjector.getInstance().getInstanceOf(CourseMapper.class);
    this.sessionFactory = HibernateUtils.getInstance().getSessionFactory();
  }

  @Override
  public void saveCourse(final Course course) {
    final var dbCourse = courseMapper.toDbCourse(course);
    final var session = sessionFactory.openSession();
    session.beginTransaction();
    session.merge(dbCourse);
    session.getTransaction().commit();
    session.close();
  }

  @Override
  public void removeCourse(final Course course) {
    final var dbCourse = courseMapper.toDbCourse(course);
    final var session = sessionFactory.openSession();
    session.beginTransaction();
    session.remove(dbCourse);
    session.getTransaction().commit();
    session.close();
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
}
