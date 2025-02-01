package hu.stan.dreamparkour.exception;

import hu.stan.dreamweaver.exception.DreamWeaverException;
import lombok.Getter;

@Getter
public class CourseAlreadyExistsException extends DreamWeaverException {

  private final String courseName;

  public CourseAlreadyExistsException(final String courseName) {
    super("Course already exists with name: " + courseName);
    this.courseName = courseName;
  }
}
