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
      ArrayOfCase userCases = myCasesServiceSoap.getUserCases(userId, false, false, "");
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
