package hu.stan.dreamparkour.command;

import hu.stan.dreamparkour.model.Course;
import hu.stan.dreamparkour.service.CourseBuilderService;
import hu.stan.dreamparkour.service.CourseService;
import hu.stan.dreamplugin.annotation.command.Command;
import hu.stan.dreamplugin.common.helper.StringHelper;
import hu.stan.dreamplugin.core.command.DreamCommandExecutor;
import hu.stan.dreamplugin.core.command.DreamTabCompleter;
import hu.stan.dreamplugin.core.translation.Translate;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(
    parentCommand = DreamParkourCommand.class,
    name = "checkpoint",
    description = "Places the player in checkpoint builder mode",
    usage = "/dreamparkour checkpoint <course name>"
)
@RequiredArgsConstructor
public class DreamParkourCheckpointCommand implements DreamCommandExecutor, DreamTabCompleter {

  private final CourseBuilderService courseBuilderService;
  private final CourseService courseService;

  @Override
  public void onCommand(final Player player, final String[] args) {
    if (args.length == 0) {
      player.sendRawMessage(Translate.translateByDefaultLocale(
          "commands.dreamparkour.checkpoint.missing-course-name"));
      return;
    }
    final var courseName = args[0];
    courseService.findCourseBy(courseName).ifPresentOrElse(
        course -> {
          courseBuilderService.setPlayerAsCourseBuilder(player, course);
          player.sendRawMessage(Translate.translateByDefaultLocale(
              "commands.dreamparkour.checkpoint.success"));
          },
        () -> player.sendRawMessage(Translate.translateByDefaultLocale(
            "commands.dreamparkour.checkpoint.course-doesnt-exist",
            "recommended_name", StringHelper.findClosestString(getCourseNamesInArray(), courseName)))
    );
  }

  @Override
  public List<String> onTabComplete(final Player player, final String[] args) {
    if (args.length == 1) {
      return getCourseNames();
    } else {
      return Collections.emptyList();
    }
  }

  private String[] getCourseNamesInArray() {
    return getCourseNames().toArray(new String[0]);
  }

  private List<String> getCourseNames() {
    return courseService.findAll().stream().map(Course::getCourseName).toList();
  }
}
