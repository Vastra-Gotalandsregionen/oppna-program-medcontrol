package se.vgregion.portal.medcontrol;

import javax.portlet.PortletPreferences;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

@Controller
@RequestMapping("EDIT")
public class MedControlEditController {

  
  
  private static final String CONFIG_JSP = "MedControlEdit";

  @RenderMapping
  public String showPreferences(ModelMap model, PortletPreferences preferences) {
    return CONFIG_JSP;
  }

}
