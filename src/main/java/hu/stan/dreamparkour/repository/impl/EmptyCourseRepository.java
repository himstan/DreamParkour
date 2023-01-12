package hu.stan.dreamparkour.repository.impl;

import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamparkour.repository.CourseRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EmptyCourseRepository implements CourseRepository {

  public EmptyCourseRepository() {
  }

  @Override
  public void saveCourse(final Course course) {
  }

  @Override
  public void removeCourse(final Course course) {
  }

  @Override
  public Optional<Course> findById(final UUID courseId) {
    return Optional.empty();
  }

  @Override
  public List<Course> findAll() {
    return Collections.emptyList();
  }
}
