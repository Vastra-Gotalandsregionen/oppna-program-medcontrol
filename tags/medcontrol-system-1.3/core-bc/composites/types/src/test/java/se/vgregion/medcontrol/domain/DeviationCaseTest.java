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

    @Test
    public void testIllegalArgumentException() {
        // Test deviation case number is null
        try {
            deviationCase.compareTo(null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot compare, at least one of the given case numbers is null", e.getMessage());
        }
        // Test compare object case number is null
        try {
            DeviationCase deviationCase2 = new DeviationCase();
            deviationCase.setCaseNumber("1");
            deviationCase.compareTo(deviationCase2);
            fail("Should throw IllegalArgumentException");
        } catch (Exception e) {
            assertEquals("Cannot compare, at least one of the given case numbers is null", e.getMessage());
        }

    }

    @Test
    public void testHashCode() {
        int hashCode1 = deviationCase.hashCode();
        assertEquals(hashCode1, deviationCase.hashCode());
        deviationCase.setCaseNumber("3");
        assertFalse(hashCode1 == deviationCase.hashCode());
    }

    @Test
    public void testEquals() {
        // Same object
        assertTrue(deviationCase.equals(deviationCase));
        // Null
        assertFalse(deviationCase.equals(null));
        // Different class
        assertFalse(deviationCase.equals("string"));
        // DeviationCase1 field with null
        DeviationCase deviationCase2 = new DeviationCase();
        deviationCase2.setCaseNumber("2");
        assertFalse(deviationCase.equals(deviationCase2));
        // Different caseNumber
        deviationCase.setCaseNumber("1");
        assertFalse(deviationCase.equals(deviationCase2));
        // DeviationCase2 field is with null
        deviationCase.setCaseNumber(null);
        deviationCase2.setCaseNumber(null);
        assertTrue(deviationCase.equals(deviationCase2));
        // Same case number
        deviationCase2.setCaseNumber("1");
        deviationCase.setCaseNumber("1");
        assertTrue(deviationCase.equals(deviationCase2));
    }
}
