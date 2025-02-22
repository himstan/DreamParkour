package hu.stan.dreamparkour.mapper;

import hu.stan.dreamparkour.model.checkpoint.Checkpoint;
import hu.stan.dreamparkour.model.entity.DbCheckpoint;
import hu.stan.dreamweaver.annotation.core.Component;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

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
    dbCheckpoint.setDeleted(checkpoint.isDeleted());
    return dbCheckpoint;
  }

  public Checkpoint toCheckpoint(final DbCheckpoint dbCheckpoint) {
    return new Checkpoint(
        UUID.fromString(dbCheckpoint.getCheckpointId()),
        dbCheckpoint.getEnabled(),
        locationMapper.toLocation(dbCheckpoint.getStartingLocation()),
        locationMapper.toLocation(dbCheckpoint.getEndingLocation()),
        dbCheckpoint.getCreatedAt(),
        dbCheckpoint.isDeleted());
  }
}
