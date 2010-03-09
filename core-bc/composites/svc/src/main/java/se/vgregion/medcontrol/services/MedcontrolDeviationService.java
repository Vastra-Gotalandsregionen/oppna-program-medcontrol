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

package se.vgregion.medcontrol.services;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.vgregion.medcontrol.domain.DeviationCase;
import se.vgregion.portal.medcontrol.ws.ArrayOfCase;
import se.vgregion.portal.medcontrol.ws.Case;
import se.vgregion.portal.medcontrol.ws.MyCasesService;
import se.vgregion.portal.medcontrol.ws.MyCasesServiceSoap;

/**
 * DeviationService implementation for MedControl WebService access.
 * 
 * @author David Bennehult
 * @author Anders Bergkvist
 */
public class MedcontrolDeviationService implements DeviationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MedcontrolDeviationService.class);
    private static final String CULTURE_LOCALE = "sv-SE";
    private static final Boolean CHECK_FOR_ACTING_ROLE = Boolean.FALSE;
    private static final Boolean INCLUDE_ACTING_ROLE_ONLY = Boolean.TRUE;
    private MyCasesServiceSoap myCasesServiceSoap;
    private String webServiceWsdlUrl = "http://medcontrol.vgregion.se/MyCasesService/MyCasesService.asmx?WSDL";

    /**
     * Constructor that accepts the webservice wsdl URL.
     * 
     * @param webServiceWsdlUrl the webServiceWsdlUrl to set
     */
    public void setWebServiceWsdlUrl(String webServiceWsdlUrl) {
        this.webServiceWsdlUrl = webServiceWsdlUrl;
    }

    /**
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
        ArrayOfCase arrayOfCase = null;

        // * userId = [lämpligt VGR-id]
        // * checkForActingRole = true (anger om man vill kolla om man har en "aktiv" roll, bra, men lite "dyrt")
        // * includeOnlyActingRole = false (anger att man bara vill ha ärenden där man har en aktiv roll)
        // * culture = sv-SE
        //
        // För att förtydliga de två booleska parametrarna:
        // * False, false -> Hämtar alla mina ärenden (motsvarar pågående-fliken,
        // värdet för HasActingRole är inte satt och säger inget).
        // * False, true -> Hämtar bara ärenden där jag har en aktiv roll (motsvarar todo-fliken).
        // * True, false -> Hämtar alla mina ärenden och anger i vilka jag har en aktiv roll (motsvarar
        // pågående- och todo-fliken, värdet för HasActingRole anger vilka ärenden som även visas som "todo").
        // * True, true - Meningslös kombination, funkar säkert men svaret är lite odefinierat.

        try {
            // Check if we have access to the WebService
            if (myCasesServiceSoap == null) {
                // No, try to access it again in case it was temporarily down
                myCasesServiceSoap = getMyCasesServiceSoap();
                if (myCasesServiceSoap == null) {
                    // Still null...nothing we can do...
                    throw new DeviationServiceException(
                            "Cannot get user cases, WebService seems to be unavailable.");
                }
            }

            // Get user cases
            arrayOfCase =
                    myCasesServiceSoap.getUserCases(userId, CHECK_FOR_ACTING_ROLE.booleanValue(),
                            INCLUDE_ACTING_ROLE_ONLY.booleanValue(), CULTURE_LOCALE);

        } catch (Exception e) {
            // We got an exception, reset so we try to reinit it again the next time
            myCasesServiceSoap = null;
            LOGGER.error("Exception when trying to fetch user cases from Webservice.", e);
            throw new DeviationServiceException("Exception when trying to fetch user cases from Webservice.", e);
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

    private MyCasesServiceSoap getMyCasesServiceSoap() {
        URL wsdlUrl = null;
        QName qName = new QName("http://mycasesservice.munkeby.com/", "MyCasesService");

        MyCasesServiceSoap myCasesServiceSoapLocal = null;
        try {
            MyCasesService myCasesService = null;
            try {
                wsdlUrl = new URL(webServiceWsdlUrl);
                myCasesService = new MyCasesService(wsdlUrl, qName);
            } catch (Exception e) {
                LOGGER.error("Exception trying to create URL", e);
                myCasesService = new MyCasesService();
            }
            myCasesServiceSoapLocal = myCasesService.getMyCasesServiceSoap();
        } catch (Exception e) {
            LOGGER.error("Exception trying to lookup MyCasesServiceSoap", e);
        }
        return myCasesServiceSoapLocal;
    }
}