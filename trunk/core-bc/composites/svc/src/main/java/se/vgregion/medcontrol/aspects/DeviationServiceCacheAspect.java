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
package se.vgregion.medcontrol.aspects;

import javax.annotation.Resource;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("deviationServiceCacheAspect")
@Aspect
@Order(2)
public class DeviationServiceCacheAspect {
  @Resource(name = "deviationCasesCache")
  private Ehcache cache;

  public void setCache(Ehcache cache) {
    this.cache = cache;
  }

  /**
   * 
   * @param joinPoint Used to get method parameters value(s)
   * @return Method return value
   * @throws Throwable If something goes wrong
   */
  @Around("execution(* se.vgregion.medcontrol.services.MedcontrolDeviationService.listDeviationCases(java.lang.String))")
  public Object cacheDeviationCases(ProceedingJoinPoint joinPoint) throws Throwable {
    Object[] arguments = joinPoint.getArgs();
    String location = (String) arguments[0];
    Element element = cache.get(location);
    if (null == element) {
      Object result = joinPoint.proceed();
      if (null != result) {
        element = new Element(location, result);
        cache.put(element);
      }
    }
    Object returnValue = null;
    if (element != null) {
      returnValue = element.getValue();
    }
    return returnValue;
  }
}
