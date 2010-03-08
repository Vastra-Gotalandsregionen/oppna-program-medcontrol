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

package se.vgregion.medcontrol.aspects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;
import org.aspectj.runtime.internal.AroundClosure;
import org.junit.Before;
import org.junit.Test;

public class DeviationServiceCacheAspectTest {

  private DeviationServiceCacheAspect deviationServiceCacheAspect;
  private MockCache mockCache;
  private MockProceedingJoinPoint mockProceedingJoinPoint;

  @Before
  public void setUp() throws Exception {
    mockCache = new MockCache();
    deviationServiceCacheAspect = new DeviationServiceCacheAspect();
    deviationServiceCacheAspect.setCache(mockCache);
    mockProceedingJoinPoint = new MockProceedingJoinPoint();
    mockProceedingJoinPoint.arguments.add("user1");
  }

  @Test
  public void testCacheDeviationCases() throws Throwable {
    Object cacheDeviationCases = deviationServiceCacheAspect.cacheDeviationCases(mockProceedingJoinPoint);
    assertEquals("Proceed value", cacheDeviationCases);
    cacheDeviationCases = deviationServiceCacheAspect.cacheDeviationCases(mockProceedingJoinPoint);
    assertEquals("Proceed value", cacheDeviationCases);
  }
  
  @Test
  public void testNullProceedValue() throws Throwable {
    mockProceedingJoinPoint.proceedValue = null;
    Object cacheDeviationCases = deviationServiceCacheAspect.cacheDeviationCases(mockProceedingJoinPoint);
    assertNull(cacheDeviationCases);
  }

  class MockProceedingJoinPoint implements ProceedingJoinPoint {

    private List<Object> arguments = new ArrayList<Object>();
    private Object proceedValue = "Proceed value";

    @Override
    public Object proceed() throws Throwable {
      return proceedValue;
    }

    @Override
    public Object proceed(Object[] args) throws Throwable {
      return null;
    }

    @Override
    public void set$AroundClosure(AroundClosure arc) {
    }

    @Override
    public Object[] getArgs() {
      return arguments.toArray();
    }

    @Override
    public String getKind() {
      return null;
    }

    @Override
    public Signature getSignature() {
      return null;
    }

    @Override
    public SourceLocation getSourceLocation() {
      return null;
    }

    @Override
    public StaticPart getStaticPart() {
      return null;
    }

    @Override
    public Object getTarget() {
      return null;
    }

    @Override
    public Object getThis() {
      return null;
    }

    @Override
    public String toLongString() {
      return null;
    }

    @Override
    public String toShortString() {
      return null;
    }
  }
}