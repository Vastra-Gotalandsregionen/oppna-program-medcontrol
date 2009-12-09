package se.vgregion.portal.medcontrol;

import javax.portlet.PortletPreferences;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

@Controller
@RequestMapping("VIEW")
public class MedControlViewController {

  
  public static final String VIEW_JSP_URL = "MedControl";
  
  @RenderMapping
  public String showMedControlNotifications(ModelMap model, PortletPreferences preferences) {
    return VIEW_JSP_URL; 
  }
}
