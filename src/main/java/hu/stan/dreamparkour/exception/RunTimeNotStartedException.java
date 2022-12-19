package hu.stan.dreamparkour.exception;

import hu.stan.dreamplugin.exception.DreamPluginException;

public class RunTimeNotStartedException extends DreamPluginException {

  public RunTimeNotStartedException() {
    super("No run timer has been started yet!");
  }
}
