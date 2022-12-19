package hu.stan.dreamparkour.mapper;

import hu.stan.dreamparkour.model.Checkpoint;
import hu.stan.dreamparkour.model.entity.DbCheckpoint;
import hu.stan.dreamplugin.annotation.core.Component;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CheckpointMapper {

  private final LocationMapper locationMapper;

  public DbCheckpoint toDbCheckpoint(final Checkpoint checkpoint) {
    final var dbCheckpoint = new DbCheckpoint();
    dbCheckpoint.setCheckpointId(checkpoint.getCheckpointId().toString());
    dbCheckpoint.setEnabled(checkpoint.isEnabled());
    dbCheckpoint.setStartingLocation(locationMapper.toDbLocation(checkpoint.getStartLocation()));
    dbCheckpoint.setEndingLocation(locationMapper.toDbLocation(checkpoint.getEndLocation()));
    dbCheckpoint.setCreatedAt(checkpoint.getCreatedAt());
    return dbCheckpoint;
  }

  public Checkpoint toCheckpoint(final DbCheckpoint dbCheckpoint) {
    return new Checkpoint(
        UUID.fromString(dbCheckpoint.getCheckpointId()),
        dbCheckpoint.getEnabled(),
        locationMapper.toLocation(dbCheckpoint.getStartingLocation()),
        locationMapper.toLocation(dbCheckpoint.getEndingLocation()),
        dbCheckpoint.getCreatedAt());
  }
}
