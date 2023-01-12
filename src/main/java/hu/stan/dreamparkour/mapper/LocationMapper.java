package hu.stan.dreamparkour.mapper;

import hu.stan.dreamparkour.model.checkpoint.CheckpointLocation;
import hu.stan.dreamparkour.model.entity.DbLocation;
import hu.stan.dreamplugin.annotation.core.Component;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Component
public class LocationMapper {

  public CheckpointLocation toLocation(final DbLocation dbLocation) {
    final var location = new Location(Bukkit.getWorld(dbLocation.getWorldName()),
        dbLocation.getX(), dbLocation.getY(), dbLocation.getZ());
    return new CheckpointLocation(UUID.fromString(dbLocation.getLocationId()), location);
  }

  public DbLocation toDbLocation(final CheckpointLocation location) {
    final var dbLocation = new DbLocation();
    dbLocation.setLocationId(location.getLocationId().toString());
    dbLocation.setWorldName(getWorldName(location));
    dbLocation.setX(location.getLocation().getBlockX());
    dbLocation.setY(location.getLocation().getBlockY());
    dbLocation.setZ(location.getLocation().getBlockZ());
    return dbLocation;
  }

  private String getWorldName(final CheckpointLocation location) {
    return Objects.nonNull(location.getLocation().getWorld())
        ? location.getLocation().getWorld().getName()
        : "undefined";
  }
}
