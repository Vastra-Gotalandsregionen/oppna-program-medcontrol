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

import se.vgregion.portal.medcontrol.ws.MyCasesService;
import se.vgregion.portal.medcontrol.ws.MyCasesServiceSoap;

import javax.xml.namespace.QName;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class MedControlServiceFactory {

    protected MedControlServiceFactory() {
        throw new UnsupportedOperationException();
    }

    public static MyCasesServiceSoap getMyCasesServiceSoap() {
        URL wsdlUrl = null;
        QName qName = new QName("http://mycasesservice.munkeby.com/", "MyCasesService");

        MyCasesService myCasesService = null;
        try {
            wsdlUrl = new URL("file:../webapps/medcontrol-core-bc-module-portlet/WEB-INF/classes/wsdl/medcontrol.wsdl");
            myCasesService = new MyCasesService(wsdlUrl, qName);
        } catch (Exception e) {
            e.printStackTrace();
            myCasesService = new MyCasesService();
        }
//        return new MyCasesService().getMyCasesServiceSoap();
        return myCasesService.getMyCasesServiceSoap();
    }

}
