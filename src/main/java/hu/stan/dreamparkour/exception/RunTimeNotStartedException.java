package hu.stan.dreamparkour.exception;

import hu.stan.dreamweaver.exception.DreamWeaverException;

public class RunTimeNotStartedException extends DreamWeaverException {

  public RunTimeNotStartedException() {
    super("No run timer has been started yet!");
  }
}
