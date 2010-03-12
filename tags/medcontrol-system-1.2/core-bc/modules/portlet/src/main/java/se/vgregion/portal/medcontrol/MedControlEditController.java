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

import java.io.IOException;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import se.vgregion.portal.medcontrol.domain.MedControlFormBacker;

@Controller
@RequestMapping("EDIT")
public class MedControlEditController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MedControlEditController.class);

    private static final String PREFERENCES_VIEW = "medcontrol_preferences";

    @Autowired
    private Validator validator;

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    /**
     * PortletPreferences attribute key for list max size.
     */
    public static final String MEDCONTROL_PREFS_LIST_SIZE = "listItemLimitation";

    /**
     * Set values for formBackingObject from the PortletPreferences.
     * 
     * @param preferences
     *            PortletPreferences
     * @return MedControlPreferences object with values saved in preferences.
     */
    @ModelAttribute("medControlFormBacker")
    public MedControlFormBacker formBackingObject(PortletPreferences preferences) {
        MedControlFormBacker medControlFormBacker = new MedControlFormBacker();
        medControlFormBacker.setListItemLimitation(Integer.valueOf(preferences.getValue(
                MEDCONTROL_PREFS_LIST_SIZE, "5")));
        return medControlFormBacker;
    }

    /**
     * RenderMapping handler, for display of portlet preferences.
     * 
     * @param medControlFormBacker
     *            Command bean.
     * @param bindingResult
     *            Spring BindingResult bean.
     * @return view (jsp) to be rendered
     */
    @RenderMapping
    public String showPreferences(
            @ModelAttribute("medControlFormBacker") MedControlFormBacker medControlFormBacker,
            BindingResult bindingResult) {
        validator.validate(medControlFormBacker, bindingResult);
        return PREFERENCES_VIEW;
    }

    /**
     * Save portlet preferences values to the PortletPreferences.
     * 
     * @param medControlFormBacker
     *            Command bean.
     * @param bindingResult
     *            Spring BindingResult bean.
     * @param preferences
     *            PortletPreferences to save portlet preferences to.
     * @throws ReadOnlyException
     *             Throws if value couldn't be saved to the PortletPreferences.
     */
    @ActionMapping
    public void savePreferences(@ModelAttribute("medControlFormBacker") MedControlFormBacker medControlFormBacker,
            BindingResult bindingResult, PortletPreferences preferences) throws ReadOnlyException {

        validator.validate(medControlFormBacker, bindingResult);
        if (!bindingResult.hasErrors()) {
            preferences.setValue(MEDCONTROL_PREFS_LIST_SIZE, Integer.toString(medControlFormBacker
                    .getListItemLimitation()));
            try {
                preferences.store();
            } catch (ValidatorException e) {
                LOGGER.error("Validation error when storing preferences.", e);
            } catch (IOException e) {
                LOGGER.error("Error when storing preferences.", e);
            }
        }
    }
}