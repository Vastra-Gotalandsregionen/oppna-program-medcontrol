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

import java.util.ArrayList;
import java.util.List;

import se.vgregion.portal.medcontrol.ws.ArrayOfCase;
import se.vgregion.portal.medcontrol.ws.Case;
import se.vgregion.portal.medcontrol.ws.MyCasesServiceSoap;

class MyCasesServiceSoapDummy implements MyCasesServiceSoap {

  MockArrayOfCase mockArrayOfCase = new MockArrayOfCase();

  public MockArrayOfCase getMockArrayOfCase() {
    return mockArrayOfCase;
  }

  @Override
  public ArrayOfCase getUserCases(String userId, boolean checkForActingRole, boolean includeOnlyActingRole,
      String culture) {
    return mockArrayOfCase;
  }

  static class MockArrayOfCase extends ArrayOfCase {
    List<Case> Cases = new ArrayList<Case>();

    public MockArrayOfCase() {
      populateList();
    }
    
    public List<Case> getCases() {
      return Cases;
    }

    @Override
    public List<Case> getCase() {
      return Cases;
    }
    
    private void populateList() {
      for (int i = 0; i < 5; i++) {
        Case case1 = new Case();
        case1.setCaseNo("caseNo" + i);
        case1.setDescription("case desc" + i);
        case1.setUrl("case url" +i);
        Cases.add(case1);
      }
    }
  }
}
