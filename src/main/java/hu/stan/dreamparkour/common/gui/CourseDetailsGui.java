package hu.stan.dreamparkour.common.gui;

import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamparkour.service.course.CourseService;
import hu.stan.dreamplugin.core.gui.builder.GuiItemBuilder;
import hu.stan.dreamplugin.core.gui.model.Gui;
import hu.stan.dreamplugin.core.gui.model.GuiItem;
import hu.stan.dreamplugin.core.gui.model.NavigableGui;
import hu.stan.dreamplugin.core.translation.Translate;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static hu.stan.dreamplugin.core.gui.util.GuiConstants.ROW_LENGTH;

public class CourseDetailsGui extends Gui implements NavigableGui {

  private final CourseService courseService;
  private final Course course;

  public CourseDetailsGui(final String title, final Course course, final CourseService courseService) {
    super(title, 3);
    this.courseService = courseService;
    this.course = course;
  }

  @Override
  public void setGuiNavigationButton(final Player player) {
    setItem(18, new GuiItemBuilder()
        .material(Material.BARRIER)
        .displayName(Translate.translate("gui.navigate-back", player))
        .onClick((clicker, clickType) -> {
          this.openPreviousGui(clicker);
        })
        .build());
  }

  @Override
  protected void updateGui(Player player) {
    setBorders();
    setCheckpointsButton(player, course);
  }

  private void setCheckpointsButton(final Player player, final Course course) {
    final var guiItem = new GuiItemBuilder()
            .material(Material.MAP)
            .displayName(Translate.translate("gui.course.list.buttons.checkpoints", player))
            .onClick((clicker, slot) -> {
              final var gui = new CheckpointListGui(getCheckpointListTitle(clicker), course, courseService);
              gui.open(clicker, this);
            })
            .build();
    setItem(ROW_LENGTH + 2, guiItem);
  }

  private String getCheckpointListTitle(final Player player) {
    return Translate.translate(
        "gui.checkpoint.list.title",
        player,
        "course_name",
        course.getCourseName());
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
      setItem(i + ROW_LENGTH * 2, guiItem);
    }
  }

  private void setVerticalBorders(final GuiItem guiItem) {
    for (int i = 0; i < 2; i++) {
      setItem(9 * (i + 1), guiItem);
      setItem(9 * (i + 1) + 8, guiItem);
    }
  }
}
