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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import se.vgregion.portal.medcontrol.domain.MedControlFormBacker;
import se.vgregion.portal.medcontrol.mocks.MockBindingResult;

public class MedControlFormBackerValidatorTest {

  private MedControlFormBackerValidator medControlFormBackerValidator;
  private MedControlFormBacker medControlFormBacker;

  @Before
  public void setUp() throws Exception {
    medControlFormBackerValidator = new MedControlFormBackerValidator();
    medControlFormBacker = new MedControlFormBacker();
  }

  @Test
  public void testSupports() {
    assertTrue(medControlFormBackerValidator.supports(MedControlFormBacker.class));
  }

  @Test
  public void testValidate() {
    medControlFormBacker.setListItemLimitation(1);
    MockBindingResult mockBindingResult = new MockBindingResult("");
    medControlFormBackerValidator.validate(medControlFormBacker, mockBindingResult);
    assertFalse(mockBindingResult.hasErrors());
  }
  
  @Test
  @Ignore
  public void testValidateWithError() {
    // Test lower limit.
    medControlFormBacker.setListItemLimitation(1);
    MockBindingResult mockBindingResult = new MockBindingResult("");
    medControlFormBackerValidator.validate(medControlFormBacker, mockBindingResult);
    String defaultMessage = mockBindingResult.getFieldError().getDefaultMessage();
    assertEquals("Tillåtna värden är heltal mellan 1 och 20.", defaultMessage);
    assertTrue(mockBindingResult.hasErrors());
    
    // Test upper limit.
    mockBindingResult = new MockBindingResult("");
    medControlFormBackerValidator.validate(medControlFormBacker, mockBindingResult);
    defaultMessage = mockBindingResult.getFieldError().getDefaultMessage();
    assertEquals("Tillåtna värden är heltal mellan 1 och 20.", defaultMessage);
    assertTrue(mockBindingResult.hasErrors());
  }
}