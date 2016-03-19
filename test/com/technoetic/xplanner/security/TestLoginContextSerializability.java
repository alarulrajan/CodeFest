package com.technoetic.xplanner.security;
import java.io.Serializable;

import junitx.extensions.SerializabilityTestCase;

import com.technoetic.xplanner.security.module.LoginModuleLoader;
import com.technoetic.xplanner.security.module.LoginSupportImpl;
import com.technoetic.xplanner.security.module.MockLoginModule;

/**
 * The Class TestLoginContextSerializability.
 */
public class TestLoginContextSerializability extends SerializabilityTestCase {
   
   /** The login module. */
   public static MockLoginModule loginModule;

   /** Instantiates a new test login context serializability.
     *
     * @param name
     *            the name
     */
   public TestLoginContextSerializability(String name) {
      super(name);
   }

   /* (non-Javadoc)
    * @see junitx.extensions.SerializabilityTestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      loginModule = new MockLoginModule();
      loginModule.authenticateReturn = new LoginSupportImpl().createSubject();
   }

   /* (non-Javadoc)
    * @see junitx.extensions.SerializabilityTestCase#createInstance()
    */
   protected Serializable createInstance() throws Exception {
      LoginModuleLoader moduleLoader = new DummyLoginModuleLoader();
      LoginContext context = new DummyLoginContext(moduleLoader);
      return context;
   }

   /* (non-Javadoc)
    * @see junitx.extensions.SerializabilityTestCase#checkThawedObject(java.io.Serializable, java.io.Serializable)
    */
   protected void checkThawedObject(Serializable expected, Serializable actual) throws Exception {
      LoginContext expectedContext = (LoginContext) expected;
      LoginContext actualContext = (LoginContext) actual;
      assertEquals(expectedContext.getSubject(), actualContext.getSubject());
      assertNotNull(actualContext.getLoginModules());
   }

   /** The Class DummyLoginModuleLoader.
     */
   private static class DummyLoginModuleLoader extends LoginModuleLoader {
      
      /* (non-Javadoc)
       * @see com.technoetic.xplanner.security.module.LoginModuleLoader#loadLoginModules()
       */
      public LoginModule[] loadLoginModules() { return new LoginModule[]{loginModule}; }
   }

   /** The Class DummyLoginContext.
     */
   private static class DummyLoginContext extends LoginContext {
      
      /** Instantiates a new dummy login context.
         *
         * @param moduleLoader
         *            the module loader
         */
      public DummyLoginContext(LoginModuleLoader moduleLoader) {super(moduleLoader);}

      /** Creates the module loader.
         *
         * @return the login module loader
         */
      protected LoginModuleLoader createModuleLoader() {
         return new DummyLoginModuleLoader();
      }
   }
}