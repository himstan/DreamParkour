package hu.stan.dreamparkour.repository;

import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamparkour.model.entity.DbCourse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface CourseRepository {

  void saveCourse(final Course course);

  void saveCourse(final Course course, final Consumer<DbCourse> callback);

  void removeCourse(final Course course);

  void removeCourse(final Course course, final Consumer<DbCourse> callback);

  Optional<Course> findById(final UUID courseId);

  List<Course> findAll();
}
