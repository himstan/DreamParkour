package hu.stan.dreamparkour.event;

import hu.stan.dreamparkour.model.course.CourseRun;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public class PlayerHitCheckpoint extends Event {

  private static final HandlerList HANDLERS_LIST = new HandlerList();

  private final Player player;
  private final CourseRun courseRun;


  @Override
  public HandlerList getHandlers() {
    return HANDLERS_LIST;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS_LIST;
  }
}
