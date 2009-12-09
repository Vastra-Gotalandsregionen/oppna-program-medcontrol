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
package se.vgregion.medcontrol.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.junit.Before;
import org.junit.Test;

import se.vgregion.medcontrol.domain.DeviationCase;
import se.vgregion.portal.medcontrol.ws.ArrayOfCase;
import se.vgregion.portal.medcontrol.ws.Case;
import se.vgregion.portal.medcontrol.ws.MyCasesServiceSoap;

public class MedcontrolDeviationServiceTest {

  private static final String USER_1 = "user-1";
  private MedcontrolDeviationService medcontrolDeviationService;
  private MockArrayOfCase mockArrayOfCase;
  private MockMyCasesServiceSoap mockMyCasesServiceSoap;
  private List<Case> cases;

  @Before
  public void setUp() throws Exception {
    medcontrolDeviationService = new MedcontrolDeviationService();
    mockArrayOfCase = new MockArrayOfCase();
    mockMyCasesServiceSoap = new MockMyCasesServiceSoap();
    // Add ArrayOfCase mock to soap mock
    mockMyCasesServiceSoap.mockArrayOfCase = mockArrayOfCase;
    medcontrolDeviationService.setMyCasesServiceSoap(mockMyCasesServiceSoap);
    generateCaseList();
  }

  private void generateCaseList() {
    cases = new ArrayList<Case>();
    Case mockCase1 = new Case();
    mockCase1.setCaseNo("mockCase1");
    mockCase1.setDescription("desc1");
    mockCase1.setHasActingRole(true);
    mockCase1.setRegisteredDate("2009-12-09");
    mockCase1.setUrl("http://test.com");

    Case mockCase2 = new Case();
    mockCase2.setCaseNo("mockCase2");
    mockCase2.setDescription("desc2");
    mockCase2.setHasActingRole(true);
    mockCase2.setRegisteredDate("2009-12-10");
    mockCase2.setUrl("http://test2.com");

    cases.add(mockCase1);
    cases.add(mockCase2);
    // Add mock case to list
    mockArrayOfCase.Cases = cases;

  }

  @Test
  public void testListDeviationCases() {
    List<DeviationCase> listDeviationCases = medcontrolDeviationService.listDeviationCases(USER_1);
    assertEquals(2, listDeviationCases.size());
    // Assert case 1
    assertEquals(cases.get(0).getCaseNo(), listDeviationCases.get(0).getCaseNumber());
    assertEquals(cases.get(0).getDescription(), listDeviationCases.get(0).getDescription());
    assertEquals(cases.get(0).isHasActingRole(), listDeviationCases.get(0).isHasActingRole());
    assertNotNull(listDeviationCases.get(0).getRegisteredDate());
    assertEquals(cases.get(0).getUrl(), listDeviationCases.get(0).getUrl());

    // Assert case 2
    assertEquals(cases.get(1).getCaseNo(), listDeviationCases.get(1).getCaseNumber());
    assertEquals(cases.get(1).getDescription(), listDeviationCases.get(1).getDescription());
    assertEquals(cases.get(1).isHasActingRole(), listDeviationCases.get(1).isHasActingRole());
    assertNotNull(listDeviationCases.get(1).getRegisteredDate());
    assertEquals(cases.get(1).getUrl(), listDeviationCases.get(1).getUrl());

  }

  @Test
  public void testNullUser() {
    List<DeviationCase> listDeviationCases = medcontrolDeviationService.listDeviationCases(null);
    assertEquals(0, listDeviationCases.size());
  }

  @Test
  public void testNullDateValue() {
    mockArrayOfCase.Cases = Arrays.asList(new Case());
    List<DeviationCase> listDeviationCases = medcontrolDeviationService.listDeviationCases(USER_1);
    assertEquals(1, listDeviationCases.size());
  }

  @Test
  public void testInvalidDateFormat() {
    Logger logger = Logger.getLogger(MedcontrolDeviationService.class);
    logger.setLevel(Level.ERROR);
    final StringWriter writer = new StringWriter();
    Appender appender = new WriterAppender(new SimpleLayout(), writer);
    logger.addAppender(appender);

    Case case1 = new Case();
    case1.setRegisteredDate("12-12-2009");
    mockArrayOfCase.Cases = Arrays.asList(case1);
    List<DeviationCase> listDeviationCases = medcontrolDeviationService.listDeviationCases(USER_1);
    assertEquals(1, listDeviationCases.size());
    assertTrue(writer.toString().contains("Not valid date"));
  }

  static class MockMyCasesServiceSoap implements MyCasesServiceSoap {

    MockArrayOfCase mockArrayOfCase;

    @Override
    public ArrayOfCase getUserCases(String userId, boolean checkForActingRole, boolean includeOnlyActingRole,
        String culture) {
      return mockArrayOfCase;
    }
  }

  static class MockArrayOfCase extends ArrayOfCase {
    List<Case> Cases = new ArrayList<Case>();

    @Override
    public List<Case> getCase() {
      return Cases;
    }
  }

}
