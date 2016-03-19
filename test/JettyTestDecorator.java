/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 15, 2006
 * Time: 4:26:22 PM
 */

import junit.framework.Test;
import junitx.extensions.TestSetup;

import com.technoetic.xplanner.webservers.JettyServer;

/**
 * The Class JettyTestDecorator.
 */
public class JettyTestDecorator extends TestSetup {

  /**
     * Instantiates a new jetty test decorator.
     *
     * @param test
     *            the test
     */
  public JettyTestDecorator(Test test) {
    super(test);
  }

  /* (non-Javadoc)
   * @see junitx.extensions.TestSetup#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
    JettyServer.start();
  }

  /* (non-Javadoc)
   * @see junitx.extensions.TestSetup#tearDown()
   */
  protected void tearDown() throws Exception {
    super.tearDown();
//    JettyServer.stop();
  }
}