package hu.stan.dreamparkour.common.gui;

import hu.stan.dreamparkour.model.checkpoint.Checkpoint;
import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamparkour.service.course.CourseService;
import hu.stan.dreamplugin.DreamPlugin;
import hu.stan.dreamplugin.core.gui.builder.GuiItemBuilder;
import hu.stan.dreamplugin.core.gui.model.GuiItem;
import hu.stan.dreamplugin.core.gui.model.ListGui;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CheckpointListGui extends ListGui {

  private final static Material ACTIVE_CHECKPOINT = Material.GREEN_STAINED_GLASS_PANE;
  private final static Material INACTIVE_CHECKPOINT = Material.RED_STAINED_GLASS_PANE;

  private final Course course;
  private final CourseService courseService;

  public CheckpointListGui(final Course course, final CourseService courseService) {
    super(String.format("%s's checkpoints", course.getCourseName()));
    this.courseService = courseService;
    this.course = course;
    setItems(getItems(course.getCheckpoints()));
  }

  private List<GuiItem> getItems(final List<Checkpoint> checkpoints) {
    final AtomicInteger i = new AtomicInteger(1);
    return checkpoints.stream()
            .sorted(Comparator.comparing(Checkpoint::getCreatedAt))
            .map(checkpoint -> getItem(checkpoint, i.getAndIncrement()))
            .toList();
  }

  private GuiItem getItem(final Checkpoint checkpoint, final int i) {
    return new GuiItemBuilder()
        .displayName(String.format("%d. checkpoint", i))
        .material(getMaterial(checkpoint))
        .itemLore(getCheckpointLore())
        .onClick((player, type) -> {
          switch (type) {
            case LEFT -> showCheckpoint(player, checkpoint);
            case RIGHT -> teleportToCheckpoint(player, checkpoint);
            case SHIFT_RIGHT -> removeCheckpointFromCourse(checkpoint);
          }
        })
        .build();
  }

  private void removeCheckpointFromCourse(final Checkpoint checkpoint) {
    course.removeCheckpoint(checkpoint);
    this.courseService.saveCourse(course);
    setItems(getItems(course.getCheckpoints()));
    super.updateGui();
  }

  private List<String> getCheckpointLore() {
    return List.of(
        String.format("%sLEFT CLICK %s- %sShow checkpoint.", ChatColor.GOLD, ChatColor.WHITE, ChatColor.GOLD),
        String.format("%sRIGHT CLICK %s- %sTeleport to checkpoint.", ChatColor.GOLD, ChatColor.WHITE, ChatColor.GOLD),
        String.format("%sSHIFT+RIGHT CLICK %s- %sRemove the checkpoint.", ChatColor.GOLD, ChatColor.WHITE, ChatColor.GOLD));
  }

  private void showCheckpoint(final Player player, final Checkpoint checkpoint) {
    final var scheduler = Bukkit.getScheduler();
    final var plugin = DreamPlugin.getInstance();
    final var task = scheduler.runTaskTimerAsynchronously(plugin, () ->
        spawnFireParticlesOnBorder(player, checkpoint), 5, 10);
    scheduler.runTaskLaterAsynchronously(plugin, task::cancel, 205);
  }

  private void spawnFireParticlesOnBorder(final Player player, final Checkpoint checkpoint) {
    final var startLocation = checkpoint.getStartLocation().getLocation();
    final var endLocation = checkpoint.getEndLocation().getLocation();
    final double minX = Math.min(startLocation.getX(), endLocation.getX());
    final double minY = Math.min(startLocation.getY(), endLocation.getY());
    final double minZ = Math.min(startLocation.getZ(), endLocation.getZ());
    final double maxX = Math.max(startLocation.getX(), endLocation.getX());
    final double maxY = Math.max(startLocation.getY(), endLocation.getY());
    final double maxZ = Math.max(startLocation.getZ(), endLocation.getZ());
    for (double x = minX; x <= maxX; x++) {
      for (double y = minY; y <= maxY; y++) {
        for (double z = minZ; z <= maxZ; z++) {
          if (x == minX || x == maxX || y == minY || y == maxY || z == minZ || z == maxZ) {
            player.spawnParticle(Particle.FLAME, x, y, z, 0, 0, 0, 0);
          }
        }
      }
    }
  }

  private void teleportToCheckpoint(final Player player, final Checkpoint checkpoint) {
    final var location = checkpoint.getStartLocation().getLocation();
    final var playerLocation = player.getLocation();
    location.setPitch(playerLocation.getPitch());
    location.setYaw(playerLocation.getYaw());
    player.teleport(findClosestLocation(location));
  }

  private Location findClosestLocation(final Location location) {
    for (int y = location.getBlockY(); y < location.getBlockY() + 3; y++) {
      location.setY(y);
      if (isLocationEligibleToTeleport(location)) {
        break;
      }
    }
    return location;
  }

  private boolean isLocationEligibleToTeleport(final Location location) {
    final var locationBelow = new Location(location.getWorld(), location.getX(), location.getY() - 1, location.getZ());
    return locationBelow.getBlock().getType().equals(Material.AIR);
  }

  private Material getMaterial(final Checkpoint checkpoint) {
    return checkpoint.isEnabled() ? ACTIVE_CHECKPOINT : INACTIVE_CHECKPOINT;
  }
}
