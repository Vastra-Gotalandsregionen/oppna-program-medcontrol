package se.vgregion.portal.medcontrol;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MedControlViewControllerTest {

  private MedControlViewController medControlViewController;

  @Before
  public void setUp() throws Exception {
    medControlViewController = new MedControlViewController();
  }

  @Test
  public void testShowMedControlNotifications() {
    assertEquals("MedControl" , medControlViewController.showMedControlNotifications(null, null));
  }

}
