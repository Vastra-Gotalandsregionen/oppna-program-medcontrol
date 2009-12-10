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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.ws.WebServiceException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.vgregion.medcontrol.domain.DeviationCase;
import se.vgregion.portal.medcontrol.ws.ArrayOfCase;
import se.vgregion.portal.medcontrol.ws.Case;
import se.vgregion.portal.medcontrol.ws.MyCasesServiceSoap;

/**
 * 
 * @author David Bennehult
 * @author Anders Bergkvist
 */
public class MedcontrolDeviationService implements DeviationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MedcontrolDeviationService.class);
    private MyCasesServiceSoap myCasesServiceSoap;

    @Autowired
    public void setMyCasesServiceSoap(MyCasesServiceSoap myCasesServiceSoap) {
        this.myCasesServiceSoap = myCasesServiceSoap;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public List<DeviationCase> listDeviationCases(String userId) {
        return populateDeviations(userId);
    }

    private List<DeviationCase> populateDeviations(String userId) {
        List<DeviationCase> deviationCases = new ArrayList<DeviationCase>();
        if (!StringUtils.isBlank(userId)) {
            ArrayOfCase userCases = callService(userId);
            List<Case> cases = userCases.getCase();
            DeviationCase deviationCase = null;
            for (Case medcontrolCase : cases) {
                deviationCase = new DeviationCase();
                deviationCase.setCaseNumber(medcontrolCase.getCaseNo());
                deviationCase.setDescription(medcontrolCase.getDescription());
                deviationCase.setHasActingRole(medcontrolCase.isHasActingRole());
                deviationCase.setRegisteredDate(parseDate(medcontrolCase.getRegisteredDate()));
                deviationCase.setUrl(medcontrolCase.getUrl());
                deviationCases.add(deviationCase);
            }
        }
        return deviationCases;
    }

    private ArrayOfCase callService(String userId) {
        ArrayOfCase arrayOfCase = new ArrayOfCase();

        // userId = [lämpligt VGR-id]
        // checkForActingRole = true (anger om man vill kontrollera om man har en "aktiv" roll, bra, men lite
        // "dyrt") includeOnlyActingRole = false (anger att man bara vill ha ärenden där man har en aktiv roll)
        // culture = sv-SE
        //
        // För att förtydliga de två booleska parametrarna:
        // False, false -> Hämtar alla mina ärenden (motsvarar pågående-fliken, värdet för HasActingRole är inte
        // satt och säger inget).
        // False, true -> Hämtar bara ärenden där jag har en aktiv roll (motsvarar todo-fliken).
        // True, false -> Hämtar alla mina ärenden och anger i vilka jag har en aktiv roll (motsvarar pågående- och
        // todo-fliken, värdet för HasActingRole anger vilka ärenden som även visas som "todo").
        // True, true - Meningslös kombination, funkar säkert men svaret är lite odefinierat.

        try {
            arrayOfCase = myCasesServiceSoap.getUserCases(userId, false, true, "sv-SE");
        } catch (WebServiceException e) {
            LOGGER.error("MedControl webservice exception", e);
            throw new DeviationServiceException("Unhandled exception from webservice", e);
        }

        return arrayOfCase;
    }

    private Date parseDate(String date) {

        Date caseDate = null;
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat.setLenient(false);
            try {
                caseDate = simpleDateFormat.parse(date);
            } catch (ParseException e) {
                LOGGER.error("Not valid date", e);
            }
        }
        return caseDate;
    }

}
