package hu.stan.dreamparkour.common.gui;

import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamparkour.service.course.CourseService;
import hu.stan.dreamplugin.core.gui.builder.GuiItemBuilder;
import hu.stan.dreamplugin.core.gui.model.GuiItem;
import hu.stan.dreamplugin.core.gui.model.ListGui;
import org.bukkit.Material;

import java.util.Comparator;
import java.util.List;

public class CourseListGui extends ListGui {

  private final CourseService courseService;

  public CourseListGui(final List<Course> courses, final CourseService courseService) {
    super("Courses");
    this.courseService = courseService;
    setItems(getItems(courses));
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
          final var detailsGui = new CourseDetailsGui(course, courseService);
          player.closeInventory();
          detailsGui.open(player);
        })
        .build();
  }
}
