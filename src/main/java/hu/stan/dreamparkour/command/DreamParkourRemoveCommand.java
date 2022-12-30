package hu.stan.dreamparkour.command;

import hu.stan.dreamparkour.model.Course;
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
    name = "remove",
    description = "Removes a parkour course with the given name",
    usage = "/dreamparkour remove <course name>"
)
@RequiredArgsConstructor
public class DreamParkourRemoveCommand implements DreamCommandExecutor, DreamTabCompleter {

  private final CourseService courseService;

  @Override
  public void onCommand(final Player player, final String[] args) {
    if (args.length == 0) {
      player.sendRawMessage(Translate.translateByDefaultLocale(
          "commands.dreamparkour.remove.missing-course-name"));
      return;
    }
    final var courseName = args[0];
    if (!courseService.hasCourse(courseName)) {
      player.sendRawMessage(Translate.translateByDefaultLocale(
          "commands.dreamparkour.remove.course-doesnt-exist",
          "recommended_name", StringHelper.findClosestString(getCourseNamesInArray(), courseName)));
      return;
    }
    player.sendRawMessage(Translate.translateByDefaultLocale(
        "commands.dreamparkour.remove.success"));
    courseService.removeCourse(courseName);
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
