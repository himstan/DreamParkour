package hu.stan.dreamparkour.service;

import hu.stan.dreamparkour.cache.course.CourseCache;
import hu.stan.dreamparkour.cache.course.CourseIdCache;
import hu.stan.dreamparkour.configuration.DatabaseConfiguration;
import hu.stan.dreamparkour.model.Course;
import hu.stan.dreamparkour.repository.CourseRepository;
import hu.stan.dreamparkour.repository.impl.CourseRepositoryImpl;
import hu.stan.dreamplugin.DreamPlugin;
import hu.stan.dreamplugin.annotation.core.Service;
import hu.stan.dreamplugin.core.dependency.injector.DependencyInjector;
import hu.stan.dreamplugin.exception.DreamPluginException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;

@Slf4j
@Service
public class CourseService {

  private final DatabaseConfiguration databaseConfiguration;
  private final CourseCache courseCache;
  private final CourseIdCache courseIdCache;
  private final CourseRepository courseRepository;

  public CourseService(final DatabaseConfiguration databaseConfiguration,
      final CourseCache courseCache,
      final CourseIdCache courseIdCache) {
    this.databaseConfiguration = databaseConfiguration;
    this.courseCache = courseCache;
    this.courseIdCache = courseIdCache;
    this.courseRepository =
        (CourseRepository) DependencyInjector.getInstance().initializeClass(CourseRepositoryImpl.class);
    if (databaseConfiguration.enabled) {
      this.setupCourses();
    }
  }

  public Course createCourse(final String courseName) {
    return saveCourse(new Course(courseName));
  }

  public void enableCourse(final String courseName) {
    findCourseBy(courseName).ifPresent(
        course -> {
          final var checkpoints = course.getCheckpoints();
          checkpoints.forEach(checkpoint -> checkpoint.setEnabled(true));
          saveCourse(course);
        }
    );
  }

  public void disableCourse(final String courseName) {
    findCourseBy(courseName).ifPresent(
        course -> {
          final var checkpoints = course.getCheckpoints();
          checkpoints.forEach(checkpoint -> checkpoint.setEnabled(false));
          saveCourse(course);
        }
    );
  }

  public Course saveCourse(final Course course) {
    if (hasCourse(course.getCourseId())) {
      log.info("Updating course with id: [{}]", course.getCourseId());
    } else {
      log.info("Creating new course with id: [{}]", course.getCourseId());
    }
    courseCache.add(course.getCourseId(), course);
    courseIdCache.add(course.getCourseName(), course.getCourseId());
    persistCourse(course);
    return course;
  }

  public Optional<Course> findCourseBy(final UUID courseId) {
    return Optional.of(courseCache.get(courseId));
  }

  public Optional<Course> findCourseBy(final String courseName) {
    final var courseId = Optional.ofNullable(courseIdCache.get(courseName));
    return findCourseBy(courseId.orElseThrow(
        () -> new DreamPluginException("No course with the name: " + courseName)));
  }

  public List<Course> findAll() {
    return courseCache.getAll();
  }

  public boolean hasCourse(final UUID courseId) {
    return courseCache.exists(courseId);
  }

  public boolean hasCourse(final String courseName) {
    return courseIdCache.exists(courseName);
  }

  private void persistCourse(final Course course) {
    if (databaseConfiguration.enabled) {
      Bukkit.getScheduler().runTaskAsynchronously(DreamPlugin.getInstance(), () -> {
        log.info("Saving course to database: [{}]", course.getCourseId());
        courseRepository.saveCourse(course);
      });
    }
  }

  private void setupCourses() {
    log.info("Loading courses from database...");
    final var courses = courseRepository.findAll();
    log.info("Found {} courses, adding them to cache.", courses.size());
    courses.forEach(course -> {
      courseCache.add(course.getCourseId(), course);
      courseIdCache.add(course.getCourseName(), course.getCourseId());
    });
  }
}
