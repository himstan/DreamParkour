package hu.stan.dreamparkour.mapper;

import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamparkour.model.entity.DbCourse;
import hu.stan.dreamplugin.annotation.core.Component;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CourseMapper {

  private final CheckpointMapper checkpointMapper;

  public Course toCourse(final DbCourse dbCourse) {
    final var checkpoints = new ArrayList<>(dbCourse.getCheckpoints().stream().map(
        checkpointMapper::toCheckpoint
    ).toList());
    final var course = new Course(UUID.fromString(dbCourse.getCourseId()), dbCourse.getCourseName(), checkpoints);
    checkpoints.forEach(checkpoint -> checkpoint.setCourse(course));
    return course;
  }

  public DbCourse toDbCourse(final Course course) {
    final var dbCheckpoints = course.getCheckpointsWithDeleted().stream().map(
        checkpointMapper::toDbCheckpoint
    ).toList();
    final var dbCourse = new DbCourse();
    dbCourse.setCourseId(course.getCourseId().toString());
    dbCourse.setCourseName(course.getCourseName());
    dbCheckpoints.forEach(dbCheckpoint -> dbCheckpoint.setCourse(dbCourse));
    dbCourse.setCheckpoints(dbCheckpoints);
    return dbCourse;
  }
}
