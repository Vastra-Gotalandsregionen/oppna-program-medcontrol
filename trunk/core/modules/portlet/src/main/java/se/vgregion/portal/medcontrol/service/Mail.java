package se.vgregion.portal.medcontrol.service;

import java.util.Date;

public class Mail {

  private String mailSubject;
  private String mailSender;
  private Date sendDate;

  public String getMailSubject() {
    return mailSubject;
  }

  public void setMailSubject(String mailSubject) {
    this.mailSubject = mailSubject;
  }

  public String getMailSender() {
    return mailSender;
  }

  public void setMailSender(String mailSender) {
    this.mailSender = mailSender;
  }

  public Date getSendDate() {
    return sendDate;
  }

  public void setSendDate(Date sendDate) {
    this.sendDate = sendDate;
  }
}
