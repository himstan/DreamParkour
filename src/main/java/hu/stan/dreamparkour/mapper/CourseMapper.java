package hu.stan.dreamparkour.mapper;

import hu.stan.dreamparkour.model.Course;
import hu.stan.dreamparkour.model.entity.DbCourse;
import hu.stan.dreamplugin.annotation.core.Component;
import java.util.ArrayList;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CourseMapper {

  private final CheckpointMapper checkpointMapper;

  public Course toCourse(final DbCourse dbCourse) {
    final var checkpoints = new ArrayList<>(dbCourse.getCheckpoints().stream().map(
        checkpointMapper::toCheckpoint
    ).toList());
    return new Course(UUID.fromString(dbCourse.getCourseId()), dbCourse.getCourseName(), checkpoints);
  }

  public DbCourse toDbCourse(final Course course) {
    final var dbCheckpoints = course.getCheckpoints().stream().map(
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
