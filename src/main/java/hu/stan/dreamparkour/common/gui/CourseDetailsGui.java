package hu.stan.dreamparkour.common.gui;

import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamplugin.core.gui.model.Gui;
import hu.stan.dreamplugin.core.gui.model.GuiItem;
import org.bukkit.entity.Player;

public class CourseDetailsGui extends Gui {

  public CourseDetailsGui(final Course course) {
    super(String.format("%s's details", course.getCourseName()), 36);
  }

  @Override
  public void handleClick(final Player player, final int slot, final GuiItem item) {
    item.onClick(player, slot);
  }
}
