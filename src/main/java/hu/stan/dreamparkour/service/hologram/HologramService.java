package hu.stan.dreamparkour.service.hologram;

import hu.stan.dreamparkour.model.hologram.Hologram;
import hu.stan.dreamparkour.model.hologram.HologramReference;
import hu.stan.dreamweaver.DreamWeaver;
import hu.stan.dreamweaver.core.cache.BaseCache;
import hu.stan.dreamweaver.exception.DreamWeaverException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class HologramService<T extends HologramReference> {

  private final BaseCache<Object, Hologram> hologramCache = new BaseCache<>();

  private static final String METADATA_KEY = "dream_holo";
  private static final FixedMetadataValue METADATA_VALUE =
      new FixedMetadataValue(DreamWeaver.getInstance(), true);

  public void createHologram(final T hologramRef, final Hologram hologram) {
    List<ArmorStand> armorStands = new ArrayList<>();
    Location location = hologram.getLocation();
    List<String> lines = hologram.getLines();
    for (String line : lines) {
      final var armorStand = spawnArmorStand(location, line);
      armorStands.add(armorStand);
      location = location.clone().add(0, -0.25, 0);
    }
    hologram.setArmorStands(armorStands);
    addHologram(hologramRef, hologram);
  }

  public void clearEveryDreamHologram() {
    Bukkit.getServer().getWorlds().forEach(this::clearEveryDreamHolo);
  }

  private boolean isDreamHolo(final ArmorStand armorStand) {
    final var metaData = armorStand.getMetadata(METADATA_KEY);
    return !metaData.isEmpty();
  }

  private void clearEveryDreamHolo(final World world) {
    world.getEntitiesByClass(ArmorStand.class).stream()
        .filter(this::isDreamHolo)
        .forEach(Entity::remove);
  }

  private ArmorStand spawnArmorStand(final Location location, final String line) {
    if (Objects.isNull(location) || Objects.isNull(location.getWorld())) {
      throw new DreamWeaverException("The world you are trying to spawn the hologram in doesn't exist!");
    }
    final var armorStand =
        (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
    armorStand.setCustomName(line);
    armorStand.setCustomNameVisible(true);
    armorStand.setVisible(false);
    armorStand.setGravity(false);
    armorStand.setMarker(true);
    armorStand.setPersistent(false);
    armorStand.setMetadata(METADATA_KEY, METADATA_VALUE);
    return armorStand;
  }

  protected void addHologram(final T hologramRef, final Hologram hologram) {
    hologramCache.add(hologramRef.getKey(), hologram);
  }

  public boolean hasHologram(final T hologramRef) {
    return hasHologram(hologramRef.getKey());
  }

  public boolean hasHologram(final Object keyRef) {
    return this.hologramCache.exists(keyRef);
  }

  public void removeHologram(final T hologramRef) {
    if (hasHologram(hologramRef)) {
      final var hologram = hologramCache.get(hologramRef.getKey());
      hologram.remove();
      hologramCache.remove(hologramRef.getKey());
    }
  }
}