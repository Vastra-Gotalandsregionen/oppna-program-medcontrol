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
/**
 * 
 */
package se.vgregion.medcontrol.domain;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import se.vgregion.kivtools.mocks.PojoTester;

/**
 * @author Anders Bergkvist
 * @author David Bennehult
 * 
 */
public class DeviationCaseTest {

    private DeviationCase deviationCase;

    @Before
    public void setUp() throws Exception {
        deviationCase = new DeviationCase();
    }

    @Test
    public void testInstantiation() {
        DeviationCase deviationCase = new DeviationCase();
        assertNotNull(deviationCase);
    }

    @Test
    public void testBasicProperties() {
        Calendar cal1 = Calendar.getInstance();
        cal1.set(2001, 1, 1);
        Calendar cal2 = Calendar.getInstance();
        cal2.set(2002, 2, 2);

        PojoTester.testProperty(deviationCase, "caseNumber", String.class, null, "Test", "Test2");
        PojoTester.testProperty(deviationCase, "description", String.class, null, "Test", "Test2");
        PojoTester.testProperty(deviationCase, "hasActingRole", boolean.class, false, false, true);
        PojoTester.testProperty(deviationCase, "registeredDate", Date.class, null, cal1.getTime(), cal2.getTime());
        PojoTester.testProperty(deviationCase, "registeredDate", Date.class, cal2.getTime(), null, cal1.getTime());
        PojoTester.testProperty(deviationCase, "url", String.class, null, "Test", "Test2");
    }
    
    @Test
    public void testCompareTo() {
        DeviationCase case1 = new DeviationCase();
        case1.setCaseNumber("av-0001");
        DeviationCase case2 = new DeviationCase();
        case2.setCaseNumber("av-0010");
        assertEquals(-1, case1.compareTo(case2));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        deviationCase.compareTo(null);
    }
}
