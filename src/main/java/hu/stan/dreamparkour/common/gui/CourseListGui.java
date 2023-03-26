package hu.stan.dreamparkour.common.gui;

import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamplugin.core.gui.builder.GuiItemBuilder;
import hu.stan.dreamplugin.core.gui.model.GuiItem;
import hu.stan.dreamplugin.core.gui.model.PaginatedGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class CourseListGui extends PaginatedGui {

  public CourseListGui(final List<Course> courses) {
    super("Courses", 36, getItems(courses), 9);
    setYOffset(1);
  }

  @Override
  public void handlePaginatedItemClick(final Player player, final int slot, final GuiItem item) {
    item.onClick(player, slot);
  }

  private static List<GuiItem> getItems(final List<Course> courses) {
    return courses.stream().map(CourseListGui::getItem).toList();
  }

  private static GuiItem getItem(final Course course) {
    return new GuiItemBuilder()
        .displayName(course.getCourseName())
        .material(Material.OAK_LOG)
        .onClick((player, slot) -> {
          final var detailsGui = new CourseDetailsGui(course);
          player.closeInventory();
          detailsGui.open(player);
        })
        .build();
  }
}
