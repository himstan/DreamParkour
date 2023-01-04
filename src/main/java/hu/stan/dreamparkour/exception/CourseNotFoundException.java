package hu.stan.dreamparkour.exception;

import hu.stan.dreamplugin.exception.DreamPluginException;
import lombok.Getter;

@Getter
public class CourseNotFoundException extends DreamPluginException {

  private final String courseName;

  public CourseNotFoundException(final String courseName) {
    super("Course not found with name: " + courseName);
    this.courseName = courseName;
  }
}
