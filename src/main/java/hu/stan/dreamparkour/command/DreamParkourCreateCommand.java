package hu.stan.dreamparkour.command;

import hu.stan.dreamparkour.exception.CourseAlreadyExistsException;
import hu.stan.dreamparkour.service.course.CourseService;
import hu.stan.dreamweaver.annotation.command.Command;
import hu.stan.dreamweaver.annotation.command.ErrorHandler;
import hu.stan.dreamweaver.core.command.DreamCommandExecutor;
import hu.stan.dreamweaver.core.translation.Translate;
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
      Translate.sendTo(player, "commands.dreamparkour.create.missing-course-name");
      return;
    }
    final var courseName = args[0];
    courseService.createCourse(courseName);
    Translate.sendTo(player, "commands.dreamparkour.create.success");
  }

  @ErrorHandler(exception = CourseAlreadyExistsException.class)
  public void handleCourseAlreadyExists(final Player player) {
    Translate.sendTo(player, "commands.dreamparkour.create.already-exists");
  }
}
