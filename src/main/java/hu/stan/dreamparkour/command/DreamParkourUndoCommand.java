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
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(
    parentCommand = DreamParkourCommand.class,
    name = "undo",
    description = "Undos the last placed checkpoint in the course",
    usage = "/dreamparkour undo <course name>"
)
@RequiredArgsConstructor
public class DreamParkourUndoCommand implements DreamCommandExecutor, DreamTabCompleter {

  private final CourseService courseService;

  @Override
  public void onCommand(final Player player, final String[] args) {
    if (args.length == 0) {
      Translate.sendTo(player, "commands.dreamparkour.undo.missing-course-name");
      return;
    }
    final var courseName = args[0];
    courseService.findCourseBy(courseName).ifPresent(
        course -> {
          if (course.getCheckpoints().isEmpty()) {
            Translate.sendTo(player, "commands.dreamparkour.undo.no-checkpoints");
          } else {
            course.removeLastCheckpoint();
            courseService.saveCourse(course);
            Translate.sendTo(player, "commands.dreamparkour.undo.success");
          }
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
    Translate.sendTo(player, "commands.dreamparkour.undo.course-doesnt-exist",
        "recommended_name", StringHelper.findClosestString(getCourseNamesInArray(), courseName));
  }

  private String[] getCourseNamesInArray() {
    return getCourseNames().toArray(new String[0]);
  }

  private List<String> getCourseNames() {
    return courseService.findAll().stream().map(Course::getCourseName).toList();
  }
}
