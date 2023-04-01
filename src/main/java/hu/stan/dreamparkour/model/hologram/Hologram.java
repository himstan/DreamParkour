package hu.stan.dreamparkour.model.hologram;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.List;

public class Hologram {
  private Location location;
  private List<String> lines;
  private List<ArmorStand> armorStands;

  public Hologram(Location location, List<String> lines) {
    this.location = location;
    this.lines = lines;
  }

  public Location getLocation() {
    return location;
  }

  public List<String> getLines() {
    return lines;
  }

  public void setArmorStands(List<ArmorStand> armorStands) {
    this.armorStands = armorStands;
  }

  public void remove() {
    this.armorStands.forEach(Entity::remove);
    this.armorStands.clear();
  }
}