package hu.stan.dreamparkour.command.gui;

import hu.stan.dreamparkour.command.DreamParkourCommand;
import hu.stan.dreamparkour.common.gui.CourseListGui;
import hu.stan.dreamparkour.service.course.CourseService;
import hu.stan.dreamplugin.annotation.command.Command;
import hu.stan.dreamplugin.core.command.DreamCommandExecutor;
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

  private final CourseService courseService;

  @Override
  public void onCommand(final Player sender, final String[] args) {
    final var gui = new CourseListGui(courseService.findAll(), courseService);
    gui.open(sender);
  }
}
