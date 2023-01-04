package hu.stan.dreamparkour.exception;

import hu.stan.dreamplugin.exception.DreamPluginException;
import lombok.Getter;

@Getter
public class CourseAlreadyExistsException extends DreamPluginException {

  private final String courseName;

  public CourseAlreadyExistsException(final String courseName) {
    super("Course already exists with name: " + courseName);
    this.courseName = courseName;
  }
}
