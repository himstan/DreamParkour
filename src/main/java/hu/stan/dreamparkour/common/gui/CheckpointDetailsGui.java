package hu.stan.dreamparkour.common.gui;

import hu.stan.dreamparkour.model.checkpoint.Checkpoint;
import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamparkour.service.course.CourseService;
import hu.stan.dreamweaver.core.gui.builder.GuiItemBuilder;
import hu.stan.dreamweaver.core.gui.model.ConfirmGui;
import hu.stan.dreamweaver.core.gui.model.Gui;
import hu.stan.dreamweaver.core.gui.model.GuiItem;
import hu.stan.dreamweaver.core.gui.model.NavigableGui;
import hu.stan.dreamweaver.core.translation.Translate;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static hu.stan.dreamweaver.core.gui.util.GuiConstants.ROW_LENGTH;

public class CheckpointDetailsGui extends Gui implements NavigableGui {

  private final CourseService courseService;
  private final Checkpoint checkpoint;
  private final Course course;

  public CheckpointDetailsGui(final Course course, final Checkpoint checkpoint, final CourseService courseService) {
    super(3);
    this.checkpoint = checkpoint;
    this.course = course;
    this.courseService = courseService;
  }

  @Override
  protected void updateGui(Player player) {
    setBorders();
    setRemoveButton(player);
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

  private void setRemoveButton(final Player player) {
    final var guiItem = new GuiItemBuilder()
            .material(Material.BARRIER)
            .displayName(Translate.translate("gui.checkpoint.details.delete", player))
            .onClick((clicker, clickType) -> confirmCheckpointDelete(clicker))
            .build();
    setItem(ROW_LENGTH + 2, guiItem);
  }

  private void confirmCheckpointDelete(final Player player) {
    final var confirmGui = new ConfirmGui(
        accepter -> {
          removeCheckpointFromCourse();
          openPreviousGui(accepter);
        }
    );
    confirmGui
        .withTitle(Translate.translate("gui.confirmation.checkpoint.delete", player))
        .open(player, this);
  }

  private void removeCheckpointFromCourse() {
    course.removeCheckpoint(checkpoint);
    this.courseService.saveCourse(course);
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
