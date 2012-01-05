/**
 * Copyright 2010 Västra Götalandsregionen
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
 *
 */

package se.vgregion.portal.medcontrol;

import se.vgregion.portal.medcontrol.domain.DeviationCase;
import net.sf.ehcache.Ehcache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import se.vgregion.portal.medcontrol.services.DeviationService;

import javax.annotation.Resource;
import javax.portlet.*;
import java.util.*;

@Controller
@RequestMapping("VIEW")
public class MedControlViewController {

    private static final String VIEW_JSP = "medcontrol";

    private static final String VIEW_ERROR_JSP = "fatal_error";

    @Autowired
    private DeviationService deviationService = null;

    @Resource(name = "deviationCasesCache")
    private Ehcache cache;

    public void setDeviationService(DeviationService deviationService) {
        this.deviationService = deviationService;
    }

    /**
     * RenderMapping handler, for display of MedControl notifications.
     *
     * @param model       A Spring MVC ModelMap
     * @param request     RenderRequest
     * @param response    RenderResponse
     * @return view (jsp) to be rendered
     */
    @RenderMapping
    public String showMedControlNotifications(ModelMap model, RenderRequest request, RenderResponse response) {
        String userId = getRemoteUserId(request);

        List<DeviationCase> deviationCases = null;
        try {
            deviationCases = deviationService.listDeviationCases(userId);
        } catch (Exception e) {
            return VIEW_ERROR_JSP;
        }

        if (deviationCases == null) {
            String title = String.format("Ingen MedControl access [%s]", userId);
            response.setTitle(title);
            model.addAttribute("mine", Collections.emptyList());
            model.addAttribute("others", Collections.emptyList());
            model.addAttribute("totalCnt", -1);
            model.addAttribute("showAutoHide", "");
        } else {
            DeviationCaseHandler deviationCaseHandler = new DeviationCaseHandler(userId, deviationCases).invoke();
            List<DeviationCase> acting = deviationCaseHandler.getMine();
            List<DeviationCase> other = deviationCaseHandler.getOthers();

            String title = String.format("Mina MedControl-ärenden (%s/%s)", acting.size(), deviationCases.size());
            response.setTitle(title);
            model.addAttribute("mine", acting);
            model.addAttribute("others", other);
            model.addAttribute("totalCnt", deviationCases.size());
            model.addAttribute("mineCnt", acting.size());
            model.addAttribute("othersCnt", other.size());
            model.addAttribute("showOthers", "true");
            model.addAttribute("showAutoHide", "true");
        }
        if ("lifra1".equals(userId)) {
            model.addAttribute("showAutoHide", "");
        }

        return VIEW_JSP;
    }

    @ActionMapping
    public void refreshCache(ActionRequest request) {
        String userId = getRemoteUserId(request);
        cache.remove(userId);
    }

    private String getRemoteUserId(final PortletRequest request) {
        Map<String, ?> userInfo = (Map<String, ?>) request.getAttribute(PortletRequest.USER_INFO);
        String userId = "";
        if (userInfo != null && userInfo.get(PortletRequest.P3PUserInfos.USER_LOGIN_ID.toString()) != null) {
            userId = (String) userInfo.get(PortletRequest.P3PUserInfos.USER_LOGIN_ID.toString());
        }

        return userId;
    }

    private class DeviationCaseHandler {
        private String userId;
        private List<DeviationCase> deviationCases;
        private List<DeviationCase> mine;
        private List<DeviationCase> others;

        public DeviationCaseHandler(String userId, List<DeviationCase> deviationCases) {
            this.deviationCases = deviationCases;
            this.userId = userId;
        }

        public List<DeviationCase> getMine() {
            return mine;
        }

        public List<DeviationCase> getOthers() {
            return others;
        }

        public DeviationCaseHandler invoke() {
            mine = new ArrayList<DeviationCase>();
            others = new ArrayList<DeviationCase>();
            for (DeviationCase devCase : deviationCases) {
                String filter = String.format("(%s)", userId);
                if (devCase.isActingRole() || devCase.getRegisteredBy().endsWith(filter)) {
                    mine.add(devCase);
                } else {
                    others.add(devCase);
                }
            }

            // Sort descending on caseNumber
            Comparator<DeviationCase> comparator = Collections.reverseOrder();
            Collections.sort(mine, comparator);
            Collections.sort(others, comparator);
            return this;
        }
    }
}
