package hu.stan.dreamparkour.common.gui;

import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamparkour.service.course.CourseService;
import hu.stan.dreamplugin.core.gui.builder.GuiItemBuilder;
import hu.stan.dreamplugin.core.gui.model.Gui;
import hu.stan.dreamplugin.core.gui.model.GuiItem;
import org.bukkit.Material;

public class CourseDetailsGui extends Gui {

  private final CourseService courseService;

  public CourseDetailsGui(final Course course, final CourseService courseService) {
    super(String.format("%s's details", course.getCourseName()), 27);
    this.courseService = courseService;
    setBorders();
    setCheckpointsButton(course);
  }

  private void setCheckpointsButton(final Course course) {
    final var guiItem = new GuiItemBuilder()
            .material(Material.MAP)
            .displayName("Checkpoints")
            .onClick((player, slot) -> {
              final var gui = new CheckpointListGui(course, courseService);
              player.closeInventory();
              gui.open(player);
            })
            .build();
    setItem(ROW_COUNT + 2, guiItem);
  }

  private void setBorders() {
    final var borderItem = new GuiItemBuilder()
            .material(Material.BLACK_STAINED_GLASS_PANE)
            .displayName("§7")
            .build();
    setHorizontalBorders(borderItem);
    setVerticalBorders(borderItem);
  }

  private void setHorizontalBorders(final GuiItem guiItem) {
    for (int i = 0; i < 9; i++) {
      setItem(i, guiItem);
      setItem(i + ROW_COUNT * 2, guiItem);
    }
  }

  private void setVerticalBorders(final GuiItem guiItem) {
    for (int i = 0; i < 2; i++) {
      setItem(9 * (i + 1), guiItem);
      setItem(9 * (i + 1) + 8, guiItem);
    }
  }
}
