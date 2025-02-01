package hu.stan.dreamparkour.exception;

import hu.stan.dreamweaver.exception.DreamWeaverException;
import lombok.Getter;

@Getter
public class CourseNotFoundException extends DreamWeaverException {

  private final String courseName;

  public CourseNotFoundException(final String courseName) {
    super("Course not found with name: " + courseName);
    this.courseName = courseName;
  }
}
