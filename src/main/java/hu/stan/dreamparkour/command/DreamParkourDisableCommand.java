package hu.stan.dreamparkour.command;

import hu.stan.dreamparkour.exception.CourseNotFoundException;
import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamparkour.service.course.CourseService;
import hu.stan.dreamweaver.annotation.command.Command;
import hu.stan.dreamweaver.annotation.command.ErrorHandler;
import hu.stan.dreamweaver.common.helper.StringHelper;
import hu.stan.dreamweaver.core.command.DreamCommandExecutor;
import hu.stan.dreamweaver.core.command.DreamTabCompleter;
import hu.stan.dreamweaver.core.translation.Translate;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

@Command(
    parentCommand = DreamParkourCommand.class,
    name = "disable",
    description = "Disables a parkour course with the given name",
    usage = "/dreamparkour disable <course name>"
)
@RequiredArgsConstructor
public class DreamParkourDisableCommand implements DreamCommandExecutor, DreamTabCompleter {

  private final CourseService courseService;

  @Override
  public void onCommand(final Player player, final String[] args) {
    if (args.length == 0) {
      Translate.sendTo(player, "commands.dreamparkour.disable.missing-course-name");
      return;
    }
    final var courseName = args[0];
    courseService.disableCourse(courseName);
    Translate.sendTo(player, "commands.dreamparkour.disable.success");
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
    Translate.sendTo(player, "commands.dreamparkour.disable.course-doesnt-exist",
        "recommended_name", StringHelper.findClosestString(getCourseNamesInArray(), courseName));
  }

  private List<String> getCourseNames() {
    return courseService.findAll().stream().map(Course::getCourseName).toList();
  }

  private String[] getCourseNamesInArray() {
    return getCourseNames().toArray(new String[0]);
  }
}
