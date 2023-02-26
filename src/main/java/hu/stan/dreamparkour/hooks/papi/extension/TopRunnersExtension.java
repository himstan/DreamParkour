package hu.stan.dreamparkour.hooks.papi.extension;

import hu.stan.dreamparkour.service.course.CourseService;
import hu.stan.dreamparkour.service.toprunners.TopRunnersService;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class TopRunnersExtension extends PlaceholderExpansion {

  private final CourseService courseService;
  private final TopRunnersService topRunnersService;

  @Override
  public @NotNull String getIdentifier() {
    return "toprun";
  }

  @Override
  public @NotNull String getAuthor() {
    return "StanHUN";
  }

  @Override
  public @NotNull String getVersion() {
    return "1.0.0";
  }

  @Override
  public boolean persist() {
    return true;
  }

  @Override
  public String onRequest(final OfflinePlayer p, final @NotNull String identifier) {
    final var args = getArguments(identifier);
    if (args.size() < 3) {
      return "Not enough arguments were passed to the placeholder!";
    }
    final var courseName = args.get(0);
    if (!courseService.hasCourse(courseName)) {
      return "Couldn't find course with name: " + courseName;
    }
    final var courseId = courseService.getIdForCourse(courseName);
    final var place = Integer.parseInt(args.get(1));
    final var displayType = args.get(2);
    final var runData = topRunnersService.getRunForCourseAtPlace(courseId, place);
    return isDisplayTypeName(displayType)
        ? runData.playerName()
        : runData.formattedRunTime();
  }

  private boolean isDisplayTypeName(final String displayType) {
    return displayType.equalsIgnoreCase("name");
  }

  private List<String> getArguments(final String identifier) {
    return Arrays.asList(identifier.split(":"));
  }
}
