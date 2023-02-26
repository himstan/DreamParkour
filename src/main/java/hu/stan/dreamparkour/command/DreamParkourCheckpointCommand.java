package hu.stan.dreamparkour.command;

import hu.stan.dreamparkour.exception.CourseNotFoundException;
import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamparkour.service.course.CourseBuilderService;
import hu.stan.dreamparkour.service.course.CourseService;
import hu.stan.dreamplugin.annotation.command.Command;
import hu.stan.dreamplugin.annotation.command.ErrorHandler;
import hu.stan.dreamplugin.common.helper.StringHelper;
import hu.stan.dreamplugin.core.command.DreamCommandExecutor;
import hu.stan.dreamplugin.core.command.DreamTabCompleter;
import hu.stan.dreamplugin.core.translation.Translate;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

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
      Translate.sendTo(player, "commands.dreamparkour.checkpoint.missing-course-name");
      return;
    }
    final var courseName = args[0];
    courseService.findCourseBy(courseName).ifPresent(
        course -> {
          courseBuilderService.setPlayerAsCourseBuilder(player, course);
          Translate.sendTo(player, "commands.dreamparkour.checkpoint.success");
          }
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

  @ErrorHandler(exception = CourseNotFoundException.class)
  public void handleCourseNotFound(final Player player, final CourseNotFoundException exception) {
    final var courseName = exception.getCourseName();
    Translate.sendTo(player, "commands.dreamparkour.remove.course-doesnt-exist",
        "recommended_name", StringHelper.findClosestString(getCourseNamesInArray(), courseName));
  }

  private String[] getCourseNamesInArray() {
    return getCourseNames().toArray(new String[0]);
  }

  private List<String> getCourseNames() {
    return courseService.findAll().stream().map(Course::getCourseName).toList();
  }
}
