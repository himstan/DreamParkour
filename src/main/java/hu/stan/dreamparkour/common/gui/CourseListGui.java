package hu.stan.dreamparkour.common.gui;

import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamparkour.service.course.CourseService;
import hu.stan.dreamplugin.core.gui.builder.GuiItemBuilder;
import hu.stan.dreamplugin.core.gui.model.GuiItem;
import hu.stan.dreamplugin.core.gui.model.ListGui;
import hu.stan.dreamplugin.core.service.TextInputService;
import hu.stan.dreamplugin.core.translation.Translate;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;

public class CourseListGui extends ListGui {

  private final CourseService courseService;
  private final TextInputService textInputService;

  public CourseListGui(final CourseService courseService, TextInputService textInputService) {
    this.courseService = courseService;
    this.textInputService = textInputService;
  }

  @Override
  protected void updateGui(final Player player) {
    setItems(getItems(courseService.findAll()));
    super.updateGui(player);
  }

  private List<GuiItem> getItems(final List<Course> courses) {
    return courses.stream()
            .sorted(Comparator.comparing(Course::getCourseName))
            .map(this::getItem)
            .toList();
  }

  private GuiItem getItem(final Course course) {
    return new GuiItemBuilder()
        .displayName(course.getCourseName())
        .material(Material.OAK_LOG)
        .onClick((player, slot) -> {
          final var detailsGui = new CourseDetailsGui(textInputService, course, courseService);
          player.closeInventory();
          detailsGui
              .withTitle(Translate.translate("gui.course.details.title", player, "course_name", course.getCourseName()))
              .open(player, this);
        })
        .build();
  }
}
