/**
 * Copyright 2009 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 */
package se.vgregion.portal.medcontrol;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import se.vgregion.medcontrol.domain.DeviationCase;
import se.vgregion.medcontrol.services.DeviationService;
import se.vgregion.medcontrol.services.DeviationServiceException;

@Controller
@RequestMapping("VIEW")
public class MedControlViewController {

    private static final String VIEW_JSP = "medcontrol";

    private static final String VIEW_ERROR_JSP = "fatal_error";

    @Autowired
    private DeviationService deviationService = null;

    public void setDeviationService(DeviationService deviationService) {
        this.deviationService = deviationService;
    }

    /**
     * RenderMapping handler, for display of MedControl notifications.
     * 
     * @param model
     *            A Spring MVC ModelMap
     * @param request
     *            RenderRequest
     * @param preferences
     *            PortletPreferences for current portlet
     * @return view (jsp) to be rendered
     */
    @RenderMapping
    public String showMedControlNotifications(ModelMap model, RenderRequest request, PortletPreferences preferences) {
        String returnView = VIEW_JSP;
        @SuppressWarnings("unchecked")
        Map<String, ?> attributes = (Map<String, ?>) request.getAttribute(PortletRequest.USER_INFO);
        String userId = getUserId(attributes);

        try {
            List<DeviationCase> devCaseList = deviationService.listDeviationCases(userId);
            model.addAttribute("devCaseList", devCaseList);
        } catch (DeviationServiceException e) {
            returnView = VIEW_ERROR_JSP;
        }

        return returnView;
    }

    private String getUserId(Map<String, ?> attributes) {
        String userId = "";
        if (attributes != null) {
            userId = (String) attributes.get(PortletRequest.P3PUserInfos.USER_LOGIN_ID.toString());
        }
        return userId;
    }
}
