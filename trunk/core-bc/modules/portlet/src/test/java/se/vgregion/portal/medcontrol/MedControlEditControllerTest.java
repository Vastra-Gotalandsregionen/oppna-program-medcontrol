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
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;

import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.portlet.MockPortletPreferences;
import org.springframework.validation.BindingResult;

import se.vgregion.portal.medcontrol.domain.MedControlFormBacker;
import se.vgregion.portal.medcontrol.mocks.MockBindingResult;

public class MedControlEditControllerTest {

  private static final int INVALID_LIST_SIZE = 12345;
  private static final Integer LIST_SIZE = 5;
  private MedControlEditController medControlEditController;
  private MockPortletPreferences mockPortletPreferences;
  private MedControlFormBacker medControlFormBacker;
  private BindingResult bindingResult;

  @Before
  public void setUp() throws Exception {
    medControlEditController = new MedControlEditController();
    medControlEditController.setValidator(new MedControlFormBackerValidator());
    mockPortletPreferences = new MockPortletPreferences();
    mockPortletPreferences.setValue(MedControlEditController.MEDCONTROL_PREFS_LIST_SIZE, LIST_SIZE.toString());
    medControlFormBacker = new MedControlFormBacker();
    medControlFormBacker.setListItemLimitation(LIST_SIZE);
    bindingResult =  new MockBindingResult("");
  }

  @Test
  public void testFormBackingObject() throws ReadOnlyException {
    MedControlFormBacker formBackingObject = medControlEditController.formBackingObject(mockPortletPreferences);
    assertEquals(LIST_SIZE.intValue(), formBackingObject.getListItemLimitation());
  }

  @Test
  public void testShowPreferences() {
    BindingResult bindingResult = new MockBindingResult("");
    MedControlFormBacker medControlFormBacker = new MedControlFormBacker();
    assertEquals("medcontrol_preferences", medControlEditController
        .showPreferences(medControlFormBacker, bindingResult));
  }

  @Test
  public void testShowPreferencesFailValidation() throws Exception {
    BindingResult bindingResult = new MockBindingResult("");
    MedControlFormBacker medControlFormBacker = new MedControlFormBacker();
    medControlFormBacker.setListItemLimitation(INVALID_LIST_SIZE);
    medControlEditController.showPreferences(medControlFormBacker, bindingResult);
    assertTrue(bindingResult.hasErrors());
  }

  @Test
  public void testSavePreferences() throws ReadOnlyException {
    MedControlFormBacker medControlFormBacker = new MedControlFormBacker();
    Integer expectedListItemLimitation = 3;

    medControlFormBacker.setListItemLimitation(expectedListItemLimitation);
    medControlEditController.savePreferences(medControlFormBacker, bindingResult, mockPortletPreferences);

    String listItemLimitationResult = mockPortletPreferences.getValue(
        MedControlEditController.MEDCONTROL_PREFS_LIST_SIZE, "5");

    assertEquals(expectedListItemLimitation, Integer.valueOf(listItemLimitationResult));
  }

  @Test
  public void testSavePreferencesFailValidation() throws ReadOnlyException {
    MedControlFormBacker medControlFormBacker = new MedControlFormBacker();
    BindingResult bindingResult = new MockBindingResult("");
    Integer expectedListItemLimitation = INVALID_LIST_SIZE;

    medControlFormBacker.setListItemLimitation(expectedListItemLimitation);
    medControlEditController.savePreferences(medControlFormBacker, bindingResult, mockPortletPreferences);

    String listItemLimitationResult = mockPortletPreferences.getValue(
        MedControlEditController.MEDCONTROL_PREFS_LIST_SIZE, LIST_SIZE.toString());

    assertEquals(LIST_SIZE, Integer.valueOf(listItemLimitationResult));
    assertTrue(bindingResult.hasErrors());
  }

  @Test
  public void testPreferencesIOExceptions() throws ReadOnlyException {
    StringWriter logWriter = getLoggerView(Level.ERROR);
    MockPreferencesExceptionGenerator mockPreferencesExceptionGenerator = new MockPreferencesExceptionGenerator();
    mockPreferencesExceptionGenerator.throwIOException = true;
    medControlEditController.savePreferences(medControlFormBacker, bindingResult,
        mockPreferencesExceptionGenerator);

    assertTrue(logWriter.toString().contains("Error when storing preferences."));
  }

  @Test
  public void testPreferencesValidatorException() throws ReadOnlyException {
    StringWriter logWriter = getLoggerView(Level.ERROR);
    MockPreferencesExceptionGenerator mockPreferencesExceptionGenerator = new MockPreferencesExceptionGenerator();
    mockPreferencesExceptionGenerator.throwValidatorException = true;
    medControlEditController.savePreferences(medControlFormBacker, bindingResult,
        mockPreferencesExceptionGenerator);

    assertTrue(logWriter.toString().contains("Validation error when storing preferences."));
  }

  private StringWriter getLoggerView(Level logLevel) {
    Logger logger = Logger.getLogger(MedControlEditController.class);
    logger.setLevel(logLevel);
    final StringWriter writer = new StringWriter();
    Appender appender = new WriterAppender(new SimpleLayout(), writer);
    logger.addAppender(appender);

    return writer;
  }

  class MockPreferencesExceptionGenerator extends MockPortletPreferences {
    boolean throwIOException;
    boolean throwValidatorException;

    @Override
    public void store() throws IOException, ValidatorException {
      if (throwIOException) {
        throw new IOException();
      }
      if (throwValidatorException) {
        throw new ValidatorException("ew", null);
      }
    }
  }
}