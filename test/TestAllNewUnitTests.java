import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * The Class TestAllNewUnitTests.
 */
public class TestAllNewUnitTests extends TestCase {

   /** The excluded test candidate classes. */
   private static Class[] excludedTestCandidateClasses = {
   };
   
   /** The excluded dirs. */
   private static String[] excludedDirs = { ".dependency-info", ".svn", "acceptance"};
   
   /** The included patterns. */
   private static String[] includedPatterns = { "Test.*" };
   
   /** The excluded patterns. */
   private static String[] excludedPatterns = { "TestAll.*" };

   /** Instantiates a new test all new unit tests.
     *
     * @param name
     *            the name
     */
   public TestAllNewUnitTests(String name) {
      super(name);
   }

   /** Suite.
     *
     * @return the test
     */
   public static Test suite() {
      BasicConfigurator.configure();
      Logger.getRootLogger().setLevel(Level.FATAL);
      return makeTestSuite();
   }

   /** Prints the tests in suite.
     */
   public static void printTestsInSuite() {
      TestSuite suite = makeTestSuite();
      java.util.Enumeration enumeration = suite.tests();
      while (enumeration.hasMoreElements()) {
         Test test = (Test) enumeration.nextElement();
         System.out.println("test = " + test.toString());
      }
   }            

   /** Make test suite.
     *
     * @return the test suite
     */
   private static TestSuite makeTestSuite() {
      TestSuite suite = new TestSuite("All Tests in classpath");
      File file = new File(getTestClassPath().replaceAll("%20", " "));
      List currentPath = new ArrayList();
      addTestsInHierarchyToSuite(suite, file, currentPath);
      return suite;
   }

   /** Gets the test path.
     *
     * @return the test path
     */
   private static String getTestPath() {
      String relativeClassFilePath = TestAllNewUnitTests.class.getName().replaceAll("\\.", "/") + ".class";
      String absoluteClassFilePath = TestAllNewUnitTests.class.getResource("/" + relativeClassFilePath).getFile();
      return absoluteClassFilePath.substring(0, absoluteClassFilePath.length() - relativeClassFilePath.length());
   }

   /** Gets the test class path.
     *
     * @return the test class path
     */
   private static String getTestClassPath() {
      String classPath = System.getProperty("TestPath");
      if (classPath != null) return classPath;
      return getTestPath();
   }

   /** Adds the tests in hierarchy to suite.
     *
     * @param suite
     *            the suite
     * @param classPathRoot
     *            the class path root
     * @param currentPath
     *            the current path
     */
   private static void addTestsInHierarchyToSuite(TestSuite suite, File classPathRoot, List currentPath) {
      addTestsInDirectoryToSuite(suite, classPathRoot, currentPath);
      File[] tests = classPathRoot.listFiles(directoryFilter);
      if (tests == null) {
         return;
      }
      for (int i = 0; i < tests.length; i++) {
         File directory = tests[i];
         currentPath.add(directory.getName());
         try {
            addTestsInHierarchyToSuite(suite,
                                       new File(classPathRoot.getCanonicalPath() +
                                                File .separator +
                                                directory.getName()),
                                       currentPath);
         } catch (Exception ex) {
            System.out.println("ex = " + ex);
         }
         currentPath.remove(currentPath.size() - 1);
      }
   }

   /** Adds the tests in directory to suite.
     *
     * @param suite
     *            the suite
     * @param classPathRoot
     *            the class path root
     * @param currentPath
     *            the current path
     */
   private static void addTestsInDirectoryToSuite(TestSuite suite, File classPathRoot, List currentPath) {
      File[] tests = classPathRoot.listFiles(testFilter);
      if (tests == null) {
         return;
      }
      for (int i = 0; i < tests.length; i++) {
         addTestToSuite(suite, currentPath, tests[i]);
      }
   }

   /** Make fully qualified class name.
     *
     * @param currentPath
     *            the current path
     * @param testcaseFile
     *            the testcase file
     * @return the string
     */
   private static String makeFullyQualifiedClassName(List currentPath, File testcaseFile) {
      StringBuffer fullyQualified = new StringBuffer();
      for (Iterator iterator = currentPath.iterator(); iterator.hasNext();) {
         fullyQualified.append((String) iterator.next());
         fullyQualified.append(".");
      }
      fullyQualified.append(stripClassSuffix(testcaseFile.getName()));
      return fullyQualified.toString();
   }

   /** Adds the test to suite.
     *
     * @param suite
     *            the suite
     * @param currentPath
     *            the current path
     * @param testcaseFile
     *            the testcase file
     */
   private static void addTestToSuite(TestSuite suite, List currentPath, File testcaseFile) {
      String fullClassName = makeFullyQualifiedClassName(currentPath, testcaseFile);
      try {
          Class<?> testClass = Class.forName(fullClassName);
          if (testClass.isAssignableFrom(Test.class)) {
              suite.addTest(new TestSuite(testClass));
          }else{
              suite.addTest(new JUnit4TestAdapter(testClass));
          }
      } catch (ClassNotFoundException ex) {
         throw new RuntimeException("TestAll: could not load class " + fullClassName);
      }
   }

   /** Strip class suffix.
     *
     * @param filename
     *            the filename
     * @return the string
     */
   private static String stripClassSuffix(String filename) {
      int endingIndex = filename.indexOf(".class");
      return filename.substring(0, endingIndex);
   }

   /** The Constant directoryFilter. */
   private static final FileFilter directoryFilter = new FileFilter() {
      public boolean accept(File pathname) {
         String path = pathname.getName();
         if (!pathname.isDirectory()) return false;
         for (int i = 0; i < excludedDirs.length; i++) {
            if (path.endsWith(excludedDirs[i])) return false;
         }
         return true;
      }
   };

   /** The Constant testFilter. */
   private static final FilenameFilter testFilter = new FilenameFilter() {
      public boolean accept(File dir, String name) {
         return isTestClass(name, dir.getPath() + File.separator + name);
      }
   };

   /** Checks if is test class.
     *
     * @param filename
     *            the filename
     * @param path
     *            the path
     * @return true, if is test class
     */
   private static boolean isTestClass(String filename, String path) {
      return (path.contains("net/sf/xplanner") && path.endsWith("Test.class"));
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

}
