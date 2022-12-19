package hu.stan.dreamparkour.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@Getter
@AllArgsConstructor
public final class CheckpointLocation {

  public CheckpointLocation(final Location location) {
    this(UUID.randomUUID(), location);
  }

  @Getter
  private final UUID locationId;
  @Getter
  @Setter
  private Location location;
}
