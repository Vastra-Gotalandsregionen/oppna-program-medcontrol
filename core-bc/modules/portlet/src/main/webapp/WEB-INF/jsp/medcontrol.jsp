<%--

    Copyright 2010 Västra Götalandsregionen

      This library is free software; you can redistribute it and/or modify
      it under the terms of version 2.1 of the GNU Lesser General Public
      License as published by the Free Software Foundation.

      This library is distributed in the hope that it will be useful,
      but WITHOUT ANY WARRANTY; without even the implied warranty of
      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
      GNU Lesser General Public License for more details.

      You should have received a copy of the GNU Lesser General Public
      License along with this library; if not, write to the
      Free Software Foundation, Inc., 59 Temple Place, Suite 330,
      Boston, MA 02111-1307  USA


--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<style type="text/css">
    <%@ include file="/style/style.css"%>
</style>


<fmt:setBundle basename="se.vgregion.portal.medcontrol.MedControl"/>

<portlet:resourceURL var="medcontrolUrl" escapeXml="false"/>
<portlet:actionURL var="refreshCache" name="refreshCache"/>

<div class="notify-issues">
    <c:if test="${totalCnt > 0}">
        <div id="notify-user">
            <c:if test="${showOthers eq true}">
                <c:if test="${othersCnt > 0}">
                    <h2 class="notify-head">Mina &auml;renden (${mineCnt})</h2>
                </c:if>
            </c:if>
            <ul id="notify-user-list">
                <div id="notify-shortlist">
                    <c:forEach items="${mine}" var="item" varStatus="cnt">
                        <li>
                            <span class="notify-phase"
                                  title="${item.phaseName}">${item.phaseType}</span>
                            <c:if test="${item.actingRole}">
                                <span class="notify-A" title="Acting">A</span>
                            </c:if>
                            <a href="${item.url}"
                               class="notify-link"
                               title="${item.caseNumber}: ${item.registeredDate}">
                                    ${item.description}&nbsp;<img src="/regionportalen-theme/images/external_link_icon.gif"
                                                                  alt="Direkt länk">
                            </a>
                        </li>
                        <c:if test="${cnt.count == 5 && mineCnt > 5}">
                            </div>
                            <h3 id="notify-open-longlist">Visa fler...</h3>
                            <div id="notify-longlist">
                        </c:if>
                    </c:forEach>
                </div>
            </ul>
        </div>
    </c:if>
    <c:if test="${showOthers eq true}">
        <c:if test="${othersCnt > 0}">
            <div id="notify-other">
                <h2 class="notify-head">&Ouml;vriga &auml;renden (${othersCnt})</h2>
                <ul id="notify-other-list">
                    <c:forEach items="${others}" var="item">
                        <li>
                            <span class="notify-phase"
                                  title="${item.phaseName}">${item.phaseType}</span>
                            <a href="${item.url}"
                               class="notify-link"
                               title="${item.caseNumber}: ${item.registeredDate}">
                                    ${item.description}&nbsp;<img src="/regionportalen-theme/images/external_link_icon.gif"
                                                                  alt="Direkt länk">
                            </a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <a class="notify-refresh" title="Ladda om" href="${refreshCache}">
                <img src="/regionportalen-theme/images/portlet/refresh.png" alt="Ladda om">
            </a>
        </c:if>
    </c:if>
</div>

<script type="text/javascript">
    AUI().ready('node', 'event', 'transition', function(A) {

        function toggle(id) {
            var list = A.one(id);
            var h2 = list.previous();
            var more = A.one('#notify-open-longlist');
            var longlist = A.one('#notify-longlist');
            if (h2 != null) {
                if (h2.hasClass('notify-folded')) {
                    h2.removeClass('notify-folded');
                    list.show();
                    if (more != null) {
                        more.show();
                        longlist.hide();
                    }
                } else {
                    h2.addClass('notify-folded');
                    list.hide();
                }
            }
        }
        if (${showAutoHide eq "true"}) {
            if ('${totalCnt}' == 0) {
                A.one('#p_p_id_MedControl_WAR_medcontrolportlet_').hide();
            }
        }

        var user = A.one('#notify-user h2');
        if (user != null) {
            user.on('click', function(e) {
                e.preventDefault();
                toggle('#notify-user-list');
            });
        }

        var other = A.one('#notify-other h2');
        if (other != null) {
            other.on('click', function(e) {
                e.preventDefault();
                toggle('#notify-other-list');
            });
        }

        var openLonglist = A.one('#notify-open-longlist');
        var longlist = A.one('#notify-longlist');
        if (openLonglist != null) {
            openLonglist.on('click', function(e) {
                e.preventDefault();
                e.target.hide();
                longlist.show();
            });
            longlist.hide();
        }

        //hide groups on load
        var otherlist = A.one('#notify-other-list');
        if (otherlist != null) {
            otherlist.hide();
            otherlist.previous().addClass('notify-folded');
        }
    });

</script>
