package hu.stan.dreamparkour.event.course;

import hu.stan.dreamparkour.event.DreamEvent;
import hu.stan.dreamparkour.model.course.Course;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateCourseEvent extends DreamEvent {

  private final Course createdCourse;
}
