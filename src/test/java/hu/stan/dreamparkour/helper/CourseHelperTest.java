package hu.stan.dreamparkour.helper;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hu.stan.dreamparkour.BaseUnitTest;
import hu.stan.dreamparkour.common.helper.CourseHelper;
import hu.stan.dreamparkour.model.Checkpoint;
import hu.stan.dreamparkour.model.CheckpointLocation;
import hu.stan.dreamparkour.service.CourseService;
import java.time.LocalDateTime;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class CourseHelperTest extends BaseUnitTest {

  @Mock
  private CourseService courseService;

  @InjectMocks
  private CourseHelper courseHelper;

  @Test
  public void testIsPlayerInCheckpointTrue() {
    final var world = mock(World.class);
    final var player = mock(Player.class);
    final var locationOne = new CheckpointLocation(new Location(world, 1, 1, 1));
    final var locationTwo = new CheckpointLocation(new Location(world, 3, 1, 1));
    final var checkPoint = new Checkpoint(UUID.randomUUID(), false, locationOne, locationTwo, LocalDateTime.now());
    final var playerLocation = new Location(world, 2, 1, 1);
    when(player.getLocation())
        .thenReturn(playerLocation);

    final var result = courseHelper.isPlayerInCheckpoint(player, checkPoint);

    assertTrue(result);
  }

  @Test
  public void testIsPlayerInCheckpointFalse() {
    final var world = mock(World.class);
    final var player = mock(Player.class);
    final var locationOne = new CheckpointLocation(new Location(world, 1, 1, 1));
    final var locationTwo = new CheckpointLocation(new Location(world, 3, 1, 1));
    final var checkPoint = new Checkpoint(UUID.randomUUID(), false, locationOne, locationTwo, LocalDateTime.now());
    final var playerLocation = new Location(world, 4, 1, 1);
    when(player.getLocation())
        .thenReturn(playerLocation);

    final var result = courseHelper.isPlayerInCheckpoint(player, checkPoint);

    assertFalse(result);
  }
}
