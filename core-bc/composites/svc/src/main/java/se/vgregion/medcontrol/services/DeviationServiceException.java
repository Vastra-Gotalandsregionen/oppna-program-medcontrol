package se.vgregion.medcontrol.services;

public class DeviationServiceException extends RuntimeException {

  private static final long serialVersionUID = 5737322772912614407L;

  public DeviationServiceException(String message, Throwable e) {
    super(message, e);
  }

}
