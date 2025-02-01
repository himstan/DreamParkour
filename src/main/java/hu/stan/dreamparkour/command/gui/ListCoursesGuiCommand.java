package hu.stan.dreamparkour.command.gui;

import hu.stan.dreamparkour.command.DreamParkourCommand;
import hu.stan.dreamparkour.common.gui.CourseListGui;
import hu.stan.dreamparkour.service.course.CourseService;
import hu.stan.dreamweaver.annotation.command.Command;
import hu.stan.dreamweaver.core.command.DreamCommandExecutor;
import hu.stan.dreamweaver.core.service.TextInputService;
import hu.stan.dreamweaver.core.translation.Translate;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(
    parentCommand = DreamParkourCommand.class,
    name = "list",
    description = "Opens the list of courses",
    usage = "/dreamparkour list"
)
@RequiredArgsConstructor
public class ListCoursesGuiCommand implements DreamCommandExecutor {

  private final TextInputService textInputService;
  private final CourseService courseService;

  @Override
  public void onCommand(final Player sender, final String[] args) {
    new CourseListGui(courseService, textInputService)
        .withTitle(Translate.translate("gui.course.list.title", sender))
        .open(sender);
  }
}
