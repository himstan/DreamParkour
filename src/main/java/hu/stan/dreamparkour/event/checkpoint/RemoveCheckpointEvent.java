package hu.stan.dreamparkour.event.checkpoint;

import hu.stan.dreamparkour.event.DreamEvent;
import hu.stan.dreamparkour.model.checkpoint.Checkpoint;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RemoveCheckpointEvent extends DreamEvent {

  private final Checkpoint removedCheckpoint;
}
