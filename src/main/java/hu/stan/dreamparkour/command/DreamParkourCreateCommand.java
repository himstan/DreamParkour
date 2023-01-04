package hu.stan.dreamparkour.command;

import hu.stan.dreamparkour.exception.CourseAlreadyExistsException;
import hu.stan.dreamparkour.service.CourseService;
import hu.stan.dreamplugin.annotation.command.Command;
import hu.stan.dreamplugin.annotation.command.ErrorHandler;
import hu.stan.dreamplugin.core.command.DreamCommandExecutor;
import hu.stan.dreamplugin.core.translation.Translate;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(
    parentCommand = DreamParkourCommand.class,
    name = "create",
    description = "Creates a parkour course with the given name",
    usage = "/dreamparkour create <course name>"
)
@RequiredArgsConstructor
public class DreamParkourCreateCommand implements DreamCommandExecutor {

  private final CourseService courseService;

  @Override
  public void onCommand(final Player player, final String[] args) {
    if (args.length == 0) {
      player.sendRawMessage(Translate.translateByDefaultLocale(
          "commands.dreamparkour.create.missing-course-name"));
      return;
    }
    final var courseName = args[0];
    courseService.createCourse(courseName);
    player.sendRawMessage(Translate.translateByDefaultLocale(
        "commands.dreamparkour.create.success"));
  }

  @ErrorHandler(exception = CourseAlreadyExistsException.class)
  public void handleCourseAlreadyExists(final Player player) {
    player.sendRawMessage(Translate.translateByDefaultLocale(
        "commands.dreamparkour.create.already-exists"));
  }
}
