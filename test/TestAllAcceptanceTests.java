import java.io.File;

import junit.framework.Test;
import junitx.util.DirectorySuiteBuilder;
import junitx.util.SimpleTestFilter;

import com.technoetic.xplanner.acceptance.web.FeaturePageTestScript;


/**
 * The Class TestAllAcceptanceTests.
 */
public class TestAllAcceptanceTests {

   /** The excluded tests. */
   static Class[] excludedTests = {FeaturePageTestScript.class};

   /** Suite.
     *
     * @return the test
     * @throws Exception
     *             the exception
     */
   public static Test suite() throws Exception {
      return buildTestSuite("Acceptance Tests", getClassRootForClass(TestAllAcceptanceTests.class.getName()), excludedTests);
   }

   /** Gets the class root for class.
     *
     * @param fqClassName
     *            the fq class name
     * @return the class root for class
     */
   private static String getClassRootForClass(String fqClassName) {
      String packagePath = fqClassName.replaceAll("\\.", "/")+".class";
      String classFilePath = TestAllAcceptanceTests.class.getResource("/"+packagePath).getFile();
      return classFilePath.substring(0, classFilePath.length() - packagePath.length());
   }

   /** Builds the test suite.
     *
     * @param name
     *            the name
     * @param rootDir
     *            the root dir
     * @param excludedTests
     *            the excluded tests
     * @return the test
     * @throws Exception
     *             the exception
     */
   private static Test buildTestSuite(String name, String rootDir, final Class[] excludedTests) throws Exception {
      return new DirectorySuiteBuilder(new SimpleTestFilter() {
         public boolean include(String classpath) {
             return classpath.endsWith("TestScript.class") && !isExcludedTest(classpath, excludedTests);
         }
      }).suite(rootDir);
   }

   /** Checks if is excluded test.
     *
     * @param classpath
     *            the classpath
     * @param excludedTests
     *            the excluded tests
     * @return true, if is excluded test
     */
   private static boolean isExcludedTest(String classpath, Class[] excludedTests) {
      for (int i = 0; i < excludedTests.length; i++) {
         if (classpath.endsWith(getClassPath(excludedTests[i]))) {
            return true;
         }
      }
      return false;
   }

   /** Gets the class path.
     *
     * @param excludedTest
     *            the excluded test
     * @return the class path
     */
   private static String getClassPath(Class excludedTest) {
      return excludedTest.getName().replace('.', File.separatorChar) + ".class";
   }

    /** The main method.
     *
     * @param args
     *            the arguments
     * @throws Exception
     *             the exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println(suite().countTestCases());

    }
}

