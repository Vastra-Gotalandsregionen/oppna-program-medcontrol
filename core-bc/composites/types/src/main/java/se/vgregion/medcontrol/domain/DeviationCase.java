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
package se.vgregion.medcontrol.domain;

import java.util.Date;

/**
 * Domain object for a deviation case.
 * 
 * @author David Bennehult
 * @author Anders Bergkvist
 */
public class DeviationCase implements Comparable<DeviationCase> {

    private String caseNumber;
    private String description;
    private boolean hasActingRole;
    private String url;
    private Date registeredDate;

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isHasActingRole() {
        return hasActingRole;
    }

    public void setHasActingRole(boolean hasActingRole) {
        this.hasActingRole = hasActingRole;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Date cloning getter.
     * 
     * @return cloned date
     */
    public Date getRegisteredDate() {
        if (registeredDate == null) {
            return null;
        } else {
            return (Date) registeredDate.clone();
        }
    }

    /**
     * Date cloning setter.
     * 
     * @param registeredDate
     *            date to clone and set
     */
    public void setRegisteredDate(Date registeredDate) {
        if (registeredDate == null) {
            this.registeredDate = null;
        } else {
            this.registeredDate = (Date) registeredDate.clone();
        }
    }

    public int compare(DeviationCase dc1, DeviationCase dc2) {
        return dc1.compareTo(dc2);
    }

    public int compareTo(DeviationCase dc2) {
        if (getCaseNumber() != null && dc2.getCaseNumber() != null) {
            return getCaseNumber().compareTo(dc2.getCaseNumber());
        } else {
            throw new IllegalArgumentException("Cannot compare, at least one of the given case numbers is null");
        }
    }
}
