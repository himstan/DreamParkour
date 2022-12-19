package hu.stan.dreamparkour.mapper;

import hu.stan.dreamparkour.model.TotalRunTime;
import hu.stan.dreamparkour.model.entity.DbCourse;
import hu.stan.dreamparkour.model.entity.DbTotalRunTime;
import hu.stan.dreamplugin.annotation.core.Component;
import java.util.UUID;

@Component
public class TotalRunTimeMapper {

  public DbTotalRunTime toDbTotalRunTime(final TotalRunTime totalRunTime) {
    final var dbTotalRunTime = new DbTotalRunTime();
    dbTotalRunTime.setRunId(totalRunTime.runId().toString());
    dbTotalRunTime.setRunTime(totalRunTime.runTime());
    final var dbCourse = new DbCourse();
    dbCourse.setCourseId(totalRunTime.courseId().toString());
    dbTotalRunTime.setCourse(dbCourse);
    dbTotalRunTime.setPlayerId(totalRunTime.playerId().toString());
    return dbTotalRunTime;
  }

  public TotalRunTime toTotalRunTime(final DbTotalRunTime dbTotalRunTime) {
    return new TotalRunTime(
        UUID.fromString(dbTotalRunTime.getRunId()),
        UUID.fromString(dbTotalRunTime.getCourse().getCourseId()),
        UUID.fromString(dbTotalRunTime.getPlayerId()),
        dbTotalRunTime.getRunTime());
  }
}
