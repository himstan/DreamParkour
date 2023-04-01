package hu.stan.dreamparkour.event.checkpoint;

import hu.stan.dreamparkour.event.DreamEvent;
import hu.stan.dreamparkour.model.course.CourseRun;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public class PlayerHitCheckpoint extends DreamEvent {

  private final Player player;
  private final CourseRun courseRun;
}
