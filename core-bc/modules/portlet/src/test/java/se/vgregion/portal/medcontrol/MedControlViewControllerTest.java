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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.portlet.MockRenderRequest;
import org.springframework.ui.ModelMap;

import se.vgregion.medcontrol.domain.DeviationCase;
import se.vgregion.medcontrol.services.DeviationService;
import se.vgregion.medcontrol.services.DeviationServiceException;

public class MedControlViewControllerTest {

  private MedControlViewController medControlViewController;
  private DeviationServiceMock mockDeviationService;

  @Before
  public void setUp() throws Exception {
    medControlViewController = new MedControlViewController();
    mockDeviationService = new DeviationServiceMock();
    medControlViewController.setDeviationService(mockDeviationService);
  }

  @Test
  public void testNullUserId() {
    // PortletPreferences
    ModelMap model = new ModelMap();
    MockRenderRequest request = new MockRenderRequest();
    assertEquals("medcontrol", medControlViewController.showMedControlNotifications(model, request, null));
    assertEquals(0, ((List) model.get("devCaseList")).size());
  }

  @Test
  public void testShowMedControlNotifications() {
    // PortletPreferences
    ModelMap model = new ModelMap();
    MockRenderRequest request = new MockRenderRequest();
    Map<String, String> attributeMap = new HashMap<String, String>();
    attributeMap.put(PortletRequest.P3PUserInfos.USER_LOGIN_ID.toString(), "myUserId");
    request.setAttribute(PortletRequest.USER_INFO, attributeMap);

    assertEquals("medcontrol", medControlViewController.showMedControlNotifications(model, request, null));
    assertNotNull(model.get("devCaseList"));
  }

  @Test
  public void testFatalErrorView() {

    // PortletPreferences
    ModelMap model = new ModelMap();
    MockRenderRequest request = new MockRenderRequest();
    Map<String, String> attributeMap = new HashMap<String, String>();
    attributeMap.put(PortletRequest.P3PUserInfos.USER_LOGIN_ID.toString(), "myUserId");
    request.setAttribute(PortletRequest.USER_INFO, attributeMap);

    mockDeviationService.throwException = true;
    assertEquals("fatal_error", medControlViewController.showMedControlNotifications(model, request, null));
  }

  class DeviationServiceMock implements DeviationService {

    boolean throwException;

    @Override
    public List<DeviationCase> listDeviationCases(String userId) {
      if (!throwException) {
        return new ArrayList<DeviationCase>();
      } else {
        throw new DeviationServiceException("Test error", null);
      }
    }
  }

}
