package hu.stan.dreamparkour.repository;

import hu.stan.dreamparkour.model.course.Course;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository {

  void saveCourse(final Course course);

  void removeCourse(final Course course);

  Optional<Course> findById(final UUID courseId);

  List<Course> findAll();
}
