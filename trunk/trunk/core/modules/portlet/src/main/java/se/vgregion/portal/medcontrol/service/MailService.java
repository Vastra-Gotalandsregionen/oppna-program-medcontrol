package se.vgregion.portal.medcontrol.service;

import java.util.List;

public interface MailService <T>{

  public List<T> getNewMails();
}
