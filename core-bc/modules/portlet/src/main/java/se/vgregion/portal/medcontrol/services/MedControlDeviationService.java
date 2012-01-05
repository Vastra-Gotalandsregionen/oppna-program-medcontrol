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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import se.vgregion.portal.medcontrol.domain.DeviationCase;
import se.vgregion.portal.medcontrol.ws.Case;
import se.vgregion.portal.medcontrol.ws.MyCasesService;
import se.vgregion.portal.medcontrol.ws.MyCasesServiceSoap;

import javax.xml.namespace.QName;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * DeviationService implementation for MedControl WebService access.
 *
 * @author David Bennehult
 * @author Anders Bergkvist
 */
public class MedControlDeviationService implements DeviationService {
    private static DateFormat DF = null;

    {
        DF = new SimpleDateFormat("yyyy-MM-dd");
        DF.setLenient(false);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MedControlDeviationService.class);


    @Value("${medcontrol.linkout.base}")
    private String linkoutBase;

    private String webServiceWsdlUrl = "http://medcontrol.vgregion.se/MyCasesService/MyCasesService.asmx?WSDL";

    private MyCasesServiceSoap myCasesServiceSoap;

    /**
     * Set the webservice wsdl URL.
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
        if (StringUtils.isBlank(userId)) return null;

        List<Case> cases = callService(userId, true, false, "sv-SE");
        if (cases == null) return null;

        List<DeviationCase> deviationCases = new ArrayList<DeviationCase>();
        for (Case medControlCase : cases) {
            deviationCases.add(map(medControlCase));
        }
        processUrl(deviationCases);
        return deviationCases;
    }

    private void processUrl(List<DeviationCase> deviationCases) {
        if (deviationCases != null && StringUtils.isNotBlank(linkoutBase)) {
            for (DeviationCase deviation : deviationCases) {
                System.out.println("Deviation Url: ["+deviation.getUrl()+"]");
                String correctedUrl = deviation.getUrl() + "&page=Context";
                System.out.println("Corrected Url: ["+correctedUrl+"]");

                String base64Url = Base64.encodeBase64URLSafeString(correctedUrl.getBytes());

                deviation.setUrl(linkoutBase + base64Url);
            }
        }
    }

    private DeviationCase map(Case medControlCase) {
        DeviationCase deviationCase = new DeviationCase();
        deviationCase.setCaseNumber(medControlCase.getCaseNo());
        deviationCase.setDescription(medControlCase.getDescription());
        deviationCase.setPhaseName(medControlCase.getPhaseName());
        deviationCase.setPhaseType(mapPhaseType(medControlCase.getPhaseName()));
        deviationCase.setRegisteredBy(medControlCase.getRegisteredBy());
        deviationCase.setTypeAlias(medControlCase.getTypeAlias());
        deviationCase.setTypeDisplayName(medControlCase.getTypeDisplayName());
        deviationCase.setUrl(medControlCase.getUrl());
        deviationCase.setActingRole(medControlCase.isHasActingRole());
        deviationCase.setRegisteredDate(medControlCase.getRegisteredDate());

        return deviationCase;
    }

    private String mapPhaseType(String phaseName) {
        if ("InvestigationPhase".equals(phaseName)) return "I";
        if ("CoordinationPhase".equals(phaseName)) return "C";
        if ("FollowUpPhase".equals(phaseName)) return "F";
        if ("ActionPhase".equals(phaseName)) return "A";
        else return phaseName;
    }

    /**
     * userId = [lämpligt VGR-id]
     * <p/>
     * checkForActingRole = true (anger om man vill kolla om man har en "aktiv" roll, bra, men lite "dyrt")
     * includeOnlyActingRole = false (anger att man bara vill ha ärenden där man har en aktiv roll)
     * culture = sv-SE
     * <p/>
     * För att förtydliga de två booleska parametrarna:
     * False, false -> Hämtar alla mina ärenden (motsvarar pågående-fliken,
     * värdet för HasActingRole är inte satt och säger inget).
     * False, true -> Hämtar bara ärenden där jag har en aktiv roll (motsvarar todo-fliken).
     * True, false -> Hämtar alla mina ärenden och anger i vilka jag har en aktiv roll (motsvarar
     * pågående- och todo-fliken, värdet för HasActingRole anger vilka ärenden som även visas som
     * "todo").
     * True, true - Meningslös kombination, funkar säkert men svaret är lite odefinierat.
     */
    private List<Case> callService(String userId, boolean checkForActingRole, boolean includeActingRoleOnly,
            String locale) {
        try {
            checkWSAccessible();
        } catch (Exception ex) {
            String msg = String.format("WebService unavailable [%s]", webServiceWsdlUrl);
            LOGGER.error(msg, ex);
            throw new RuntimeException(ex);
        }

        try {
            return myCasesServiceSoap.getUserCases(userId, checkForActingRole, includeActingRoleOnly, locale)
                    .getCase();
        } catch (Exception e) {
            // We got an exception, reset so we try to reinit it again
            // the next time, in case it was temporarily down
            myCasesServiceSoap = null;
            return null;
        }
    }

    /**
     * Check if we have access to the WebService.
     */
    private void checkWSAccessible() throws Exception {
        if (myCasesServiceSoap == null) {
            myCasesServiceSoap = initMyCasesServiceSoap();
        }
    }

    /**
     * Initialize the WebService.
     *
     * @return MyCasesServiceSoap
     * @throws Exception if initialization fail
     */
    private MyCasesServiceSoap initMyCasesServiceSoap() throws Exception {
        URL wsdlUrl = new URL(webServiceWsdlUrl);
        QName qName = new QName("http://mycasesservice.munkeby.com/", "MyCasesService");

        MyCasesService myCasesService = new MyCasesService(wsdlUrl, qName);
        MyCasesServiceSoap myCasesServiceSoapLocal = myCasesService.getMyCasesServiceSoap();

        return myCasesServiceSoapLocal;
    }
}