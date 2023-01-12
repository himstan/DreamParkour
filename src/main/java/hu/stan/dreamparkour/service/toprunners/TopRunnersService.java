package hu.stan.dreamparkour.service.toprunners;

import hu.stan.dreamparkour.cache.toprunners.CourseTopRunnerCache;
import hu.stan.dreamparkour.model.runtime.PlayerRunTimePlaceholderData;
import hu.stan.dreamplugin.annotation.core.Service;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TopRunnersService {

  private final CourseTopRunnerCache courseTopRunnerCache;

  public PlayerRunTimePlaceholderData getRunForCourseAtPlace(final UUID courseId, final int place) {
    if (!isCourseAndPlaceValid(courseId, place)) {
      return getInvalidPlayerTotalRunTime();
    }
    return courseTopRunnerCache.get(courseId).get(place);
  }

  private PlayerRunTimePlaceholderData getInvalidPlayerTotalRunTime() {
    return new PlayerRunTimePlaceholderData(
        "EMPTY",
        "0:00"
    );
  }

  private boolean isCourseAndPlaceValid(final UUID courseId, final int place) {
    return courseTopRunnerCache.exists(courseId) && courseTopRunnerCache.get(courseId).exists(place);
  }
}
