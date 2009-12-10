package se.vgregion.medcontrol.services;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import se.vgregion.portal.medcontrol.ws.MyCasesServiceSoap;

public class MedControlServiceFactoryTest {

  
  @Test
  public void testGetMyCasesServiceSoap() {
    MedControlServiceFactory medControlServiceFactory = new MedControlServiceFactory();
    assertTrue(medControlServiceFactory.getMyCasesServiceSoap() instanceof MyCasesServiceSoap);
  }

}
