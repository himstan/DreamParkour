package hu.stan.dreamparkour.command;

import hu.stan.dreamparkour.service.CourseBuilderService;
import hu.stan.dreamparkour.service.CourseService;
import hu.stan.dreamplugin.core.translation.TranslationService;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class SetStartCommand implements CommandExecutor {

  private final CourseService courseService;
  private final CourseBuilderService courseBuilderService;

  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    if (!(sender instanceof Player)) {
      return true;
    }
    final var player = (Player) sender;
    if (args.length == 0) {
      player.sendRawMessage(TranslationService.translate("commands.dreamparkour.zero-arguments"));
      return true;
    }
    if (args.length == 1) {
      player.sendRawMessage(TranslationService.translate("commands.dreamparkour.one-argument"));
      return true;
    }
    switch (args[0]) {
      case "enable": {
        final var courseName = args[1];
        if (!courseService.hasCourse(courseName)) {
          notifyAboutCourseNotFound(player);
          return true;
        }
        courseService.enableCourse(courseName);
        courseBuilderService.removePlayerFromCourseBuilder(player);
        player.sendRawMessage(TranslationService.translate("commands.dreamparkour.enable"));
        break;
      }
      case "disable": {
        final var courseName = args[1];
        if (!courseService.hasCourse(courseName)) {
          notifyAboutCourseNotFound(player);
          return true;
        }
        courseService.disableCourse(courseName);
        player.sendRawMessage(TranslationService.translate("commands.dreamparkour.disable"));
        break;
      }
      case "create": {
        final var courseName = args[1];
        if (courseService.hasCourse(courseName)) {
          notifyAboutCourseAlreadyExists(player);
          return true;
        }
        player.sendRawMessage(TranslationService.translate("commands.dreamparkour.create"));
        courseService.createCourse(courseName);
        break;
      }
      case "checkpoint": {
        final var courseName = args[1];
        if (!courseService.hasCourse(courseName)) {
          notifyAboutCourseNotFound(player);
          return true;
        }
        courseService.findCourseBy(courseName).ifPresentOrElse(
            course -> courseBuilderService.setPlayerAsCourseBuilder(player, course),
            () -> notifyAboutCourseNotFound(player)
        );
        break;
      }
      case "undo": {
        final var courseName = args[1];
        if (!courseService.hasCourse(courseName)) {
          notifyAboutCourseNotFound(player);
          return true;
        }
        courseService.findCourseBy(courseName).ifPresentOrElse(
            course -> {
              if (course.getCheckpoints().isEmpty()) {

                player.sendRawMessage(TranslationService.translate("commands.dreamparkour.no-checkpoints"));
              } else {
                course.removeLastCheckpoint();
                player.sendRawMessage(TranslationService.translate("commands.dreamparkour.undo"));
              }
            },
            () -> notifyAboutCourseNotFound(player)
        );
        break;
      }
    }
    return true;
  }

  private void notifyAboutCourseAlreadyExists(final Player player) {
    player.sendRawMessage(TranslationService.translate("commands.dreamparkour.already-exists"));
  }

  private void notifyAboutCourseNotFound(final Player player) {
    player.sendRawMessage(TranslationService.translate("commands.dreamparkour.not-found"));
  }
}
