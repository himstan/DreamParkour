package hu.stan.dreamparkour.util;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;

public class LocationUtils {

  public static Location getMidpoint(Location loc1, Location loc2) {
    if (loc1 == null || loc2 == null || !Objects.equals(loc1.getWorld(), loc2.getWorld())) {
      throw new IllegalArgumentException("Both locations must be non-null and in the same world.");
    }
    if (loc1.equals(loc2)) {
      return getMidpoint(loc1);
    }

    World world = loc1.getWorld();
    double x = (loc1.getX() + loc2.getX()) / 2;
    double z = (loc1.getZ() + loc2.getZ()) / 2;

    // Keep the Y-coordinate from the first location (or whichever location you prefer)
    double y = loc1.getY();

    return new Location(world, x, y, z);
  }

  public static Location getMidpoint(Location loc) {
    if (loc == null || loc.getWorld() == null) {
      throw new IllegalArgumentException("The location and the world must be non-null.");
    }

    final var world = loc.getWorld();
    final var loc2 = loc.add(1, 0, 1);
    double x = (loc.getX() + loc2.getX()) / 2;
    double z = (loc.getZ() + loc2.getZ()) / 2;
    double y = loc.getY();

    return new Location(world, x, y, z);
  }
}
