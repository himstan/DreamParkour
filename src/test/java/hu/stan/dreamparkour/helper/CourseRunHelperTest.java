package hu.stan.dreamparkour.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import hu.stan.dreamparkour.BaseUnitTest;
import hu.stan.dreamparkour.common.helper.CourseRunHelper;
import hu.stan.dreamparkour.model.Checkpoint;
import hu.stan.dreamparkour.model.CheckpointLocation;
import hu.stan.dreamparkour.model.Course;
import hu.stan.dreamparkour.service.CourseService;
import hu.stan.dreamparkour.util.CourseRunUtils;
import java.time.LocalDateTime;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class CourseRunHelperTest extends BaseUnitTest {

  private static final UUID firstCheckpointId = UUID.fromString("bbd3d910-49b0-4674-acbf-0a028edc7cfd");
  private static final UUID secondCheckpointId = UUID.fromString("bbd3d910-49b0-4674-acbf-0a038edc7cfd");
  private static final UUID thirdCheckpointId = UUID.fromString("bbd3d910-49b0-4674-acbf-0a048edc7cfd");

  @Mock
  private CourseService courseService;

  @InjectMocks
  private CourseRunHelper courseRunHelper;

  @Test
  public void testBuildCourseRun() {
    final var player = mock(Player.class);
    final var course = buildMockCourse();

    final var result = courseRunHelper.buildCourseRun(player, course);

    assertNotNull(result);
    assertEquals(3, result.getCourse().getCheckpoints().size());
    assertEquals(firstCheckpointId, result.getCurrentCheckpoint().getCheckpoint().getCheckpointId());
    assertEquals(secondCheckpointId, result.getCurrentCheckpoint().getNextCheckpoint().getCheckpoint().getCheckpointId());
    assertEquals(thirdCheckpointId, result.getCurrentCheckpoint().getNextCheckpoint().getNextCheckpoint().getCheckpoint().getCheckpointId());
  }

  @Test
  public void testRewindCheckpointToStart() {
    final var player = mock(Player.class);
    final var course = buildMockCourse();

    final var result = courseRunHelper.buildCourseRun(player, course);
    final var lastCheckpoint = result.getCurrentCheckpoint().getNextCheckpoint().getNextCheckpoint();

    final var firstCheckpoint = CourseRunUtils.rewindCheckpointToStart(lastCheckpoint);

    assertTrue(lastCheckpoint.isLastCheckpoint());
    assertTrue(firstCheckpoint.isFirstCheckpoint());
  }

  private Course buildMockCourse() {
    final var course = new Course("test");
    course.addCheckpoint(buildCheckpoint(firstCheckpointId, getLocation(1, 1, 1)));
    course.addCheckpoint(buildCheckpoint(secondCheckpointId, getLocation(2, 2, 2)));
    course.addCheckpoint(buildCheckpoint(thirdCheckpointId, getLocation(3, 3, 3)));
    return course;
  }

  private Checkpoint buildCheckpoint(final UUID checkpointId, final Location location) {
    return new Checkpoint(
        checkpointId, false, new CheckpointLocation(location), new CheckpointLocation(location), LocalDateTime.now());
  }

  private Location getLocation(final double x, final double y, final double z) {
    return new Location(mock(World.class), x, y, z);
  }
}
