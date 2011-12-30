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

package se.vgregion.medcontrol.domain;

import java.io.Serializable;

/**
 * Domain object for a deviation case.
 *
 * @author David Rosell
 * @author Anders Bergkvist
 */
public class DeviationCase implements Comparable<DeviationCase>, Serializable {
    private String caseNumber;
    private String description;
    private Boolean actingRole;
    private String url;
    private String registeredDate;
    private String phaseName;
    private String phaseType;
    private String registeredBy;
    private String typeAlias;
    private String typeDisplayName;

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

    public Boolean isActingRole() {
        return actingRole;
    }

    public boolean getActingRole() {
        return actingRole;
    }

    public void setActingRole(Boolean actingRole) {
        this.actingRole = actingRole;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    public String getPhaseType() {
        return phaseType;
    }

    public void setPhaseType(String phaseType) {
        this.phaseType = phaseType;
    }

    public String getRegisteredBy() {
        return registeredBy;
    }

    public void setRegisteredBy(String registeredBy) {
        this.registeredBy = registeredBy;
    }

    public String getTypeAlias() {
        return typeAlias;
    }

    public void setTypeAlias(String typeAlias) {
        this.typeAlias = typeAlias;
    }

    public String getTypeDisplayName() {
        return typeDisplayName;
    }

    public void setTypeDisplayName(String typeDisplayName) {
        this.typeDisplayName = typeDisplayName;
    }

    public String getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(String registeredDate) {
        this.registeredDate = registeredDate;
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(DeviationCase dc2) {
        if (caseNumber != null && dc2.getCaseNumber() != null) {
            if (actingRole != dc2.isActingRole()) {
                return actingRole.compareTo(dc2.isActingRole());
            }

            return caseNumber.compareTo(dc2.getCaseNumber());
        } else {
            throw new IllegalArgumentException("Cannot compare, at least one of the given case numbers is null");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviationCase that = (DeviationCase) o;

        if (caseNumber != null ? !caseNumber.equals(that.caseNumber) : that.caseNumber != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return caseNumber != null ? caseNumber.hashCode() : 0;
    }
}