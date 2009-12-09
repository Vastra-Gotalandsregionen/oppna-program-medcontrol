package se.vgregion.portal.medcontrol.service;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class MailServiceImpl implements MailService<Mail> {

  private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
  private RestTemplate restTemplate;
  private String url;
  private DocumentBuilderFactory factory;

  public void setFactory(DocumentBuilderFactory factory) {
    this.factory = factory;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setRestTemplate(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public List<Mail> getNewMails() {
    List<Mail> mails = new ArrayList<Mail>();
    Document mailsFromWebService = getMailsFromWebService();
    
    return mails;
  }

  private Document getMailsFromWebService() {
    Document document = null;
    String xmlString = restTemplate.getForObject(url, String.class, "andbo7", "10");
    InputSource source = new InputSource(new StringReader(xmlString));
    try {
      document = factory.newDocumentBuilder().parse(source);
    } catch (SAXException e) {
      logger.error("Error in parsing xml" , e);
    } catch (IOException e) {
      logger.error("Error in parsing xml" , e);
    } catch (ParserConfigurationException e) {
      logger.error("Error in parsing xml" , e);
    }
    return document;
  }
}
