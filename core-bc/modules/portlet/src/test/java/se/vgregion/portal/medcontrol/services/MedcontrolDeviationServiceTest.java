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

package se.vgregion.portal.medcontrol.services;

import static org.junit.Assert.*;

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
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import se.vgregion.portal.medcontrol.domain.DeviationCase;
import se.vgregion.portal.medcontrol.ws.Case;

public class MedcontrolDeviationServiceTest {

    private static final String USER_1 = "user-1";
    private MedControlDeviationService medControlDeviationService;
    private MockMyCasesServiceSoap mockMyCasesServiceSoap;
    private List<Case> cases;

    @Before
    public void setUp() throws Exception {
        medControlDeviationService = new MedControlDeviationService();
        mockMyCasesServiceSoap = new MockMyCasesServiceSoap();
        ReflectionTestUtils.setField(medControlDeviationService, "myCasesServiceSoap", mockMyCasesServiceSoap);
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
        mockMyCasesServiceSoap.getMockArrayOfCase().getCase().addAll(cases);

    }

    @Test
    public void testListDeviationCases() throws MedControlDeviationServiceException {
        List<DeviationCase> listDeviationCases = medControlDeviationService.listDeviationCases(USER_1);
        assertEquals(2, listDeviationCases.size());
        // Assert case 1
        assertEquals(cases.get(0).getCaseNo(), listDeviationCases.get(0).getCaseNumber());
        assertEquals(cases.get(0).getDescription(), listDeviationCases.get(0).getDescription());
        assertEquals(cases.get(0).isHasActingRole(), listDeviationCases.get(0).isActingRole());
        assertNotNull(listDeviationCases.get(0).getRegisteredDate());
        assertEquals(cases.get(0).getUrl(), listDeviationCases.get(0).getUrl());

        // Assert case 2
        assertEquals(cases.get(1).getCaseNo(), listDeviationCases.get(1).getCaseNumber());
        assertEquals(cases.get(1).getDescription(), listDeviationCases.get(1).getDescription());
        assertEquals(cases.get(1).isHasActingRole(), listDeviationCases.get(1).isActingRole());
        assertNotNull(listDeviationCases.get(1).getRegisteredDate());
        assertEquals(cases.get(1).getUrl(), listDeviationCases.get(1).getUrl());

    }

    @Test
    public void testNullUser() throws MedControlDeviationServiceException {
        List<DeviationCase> listDeviationCases = medControlDeviationService.listDeviationCases(null);
        assertNull(listDeviationCases);
    }

    @Test
    public void testNullDateValue() throws MedControlDeviationServiceException {
        mockMyCasesServiceSoap.getMockArrayOfCase().getCase().clear();
        mockMyCasesServiceSoap.getMockArrayOfCase().getCase().addAll(Arrays.asList(new Case()));
        List<DeviationCase> listDeviationCases = medControlDeviationService.listDeviationCases(USER_1);
        assertEquals(1, listDeviationCases.size());
    }

    @Test(expected = MedControlDeviationServiceException.class)
    public void testWebServiceException() throws MedControlDeviationServiceException {
        mockMyCasesServiceSoap.setThrowException(true);
        StringWriter loggerView = getLoggerView(Level.ERROR);
        medControlDeviationService.listDeviationCases(USER_1);
//        assertTrue(loggerView.toString().contains("MedControl webservice exception"));
    }

    private StringWriter getLoggerView(Level logLevel) {
        Logger logger = Logger.getLogger(MedControlDeviationService.class);
        logger.setLevel(logLevel);
        final StringWriter writer = new StringWriter();
        Appender appender = new WriterAppender(new SimpleLayout(), writer);
        logger.addAppender(appender);
        return writer;
    }
}