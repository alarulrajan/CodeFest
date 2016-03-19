/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Aug 29, 2004
 * Time: 11:33:47 AM
 */
package com.technoetic.xplanner.acceptance.web.console;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.SwingUtilities;
import javax.swing.text.html.HTMLDocument;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.sourceforge.jwebunit.api.ITestingEngine;

import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.acceptance.web.XPlannerWebTester;

/**
 * The Class VisualTesterHandler.
 */
public class VisualTesterHandler implements InvocationHandler {
    
    /** The tester. */
    private XPlannerWebTester tester;
    
    /** The http. */
    private HttpFrame http;
    
    /** The continue lock. */
    private Object continueLock = new Object();
    
    /** The stepping. */
    private boolean stepping = false;
    
    /** The base file url. */
    private String baseFileUrl;
    
    /** The base http url. */
    private String baseHttpUrl;

    /** Instantiates a new visual tester handler.
     */
    public VisualTesterHandler() {
        baseFileUrl =  "file:///"+ (new File("").getAbsolutePath().replaceAll("\\\\", "/")) + "/war";
        baseHttpUrl = XPlannerTestSupport.getAbsoluteTestURL();
    }

    /** Sets the tester.
     *
     * @param tester
     *            the new tester
     */
    public void setTester(XPlannerWebTester tester) { this.tester = tester; }
    
    /** Sets the stepping.
     *
     * @param stepping
     *            the new stepping
     */
    public void setStepping(boolean stepping) { this.stepping = stepping;}

    /* (non-Javadoc)
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object res = null;
        Throwable exception = null;
        try {
            res = method.invoke(tester, args);
        } catch (InvocationTargetException e) {
            stepping = true;
            exception = e.getCause();
        }
        updateUI(exception, method, args);
        if (exception != null) throw exception;
        return res;
    }

    /** Update ui.
     *
     * @param exception
     *            the exception
     * @param method
     *            the method
     * @param args
     *            the args
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void updateUI(Throwable exception, Method method, Object[] args)
        throws IOException {
        String url = "";
        String htmlResponse = "";
        ITestingEngine dialog = tester.getDialog();
        Assert.fail();
//        if (dialog != null) {
//            url = dialog.getResponse().getURL().toString();
//            htmlResponse = dialog.getResponse().getText();
//        }
        updateUI(getTestName(), url, htmlResponse, getMethodCall(method, args), exception);
    }

    /** Gets the test name.
     *
     * @return the test name
     */
    private String getTestName() {
        StackTraceElement[] stackTrace = new Exception().getStackTrace();
        for (int i = 0; i < stackTrace.length; i++) {
            StackTraceElement element = stackTrace[i];
            Class frameClass = getClass(element);
            if (TestCase.class.isAssignableFrom(frameClass) &&
                element.getMethodName().startsWith("test") ||
                element.getMethodName().equals("setUp") ||
                element.getMethodName().equals("tearDown")) {
                return frameClass.getName() + "." + element.getMethodName();
            }
        }
        return "<unknown>";
    }

    /** Gets the class.
     *
     * @param element
     *            the element
     * @return the class
     */
    private Class getClass(StackTraceElement element) {
        try {
            return Class.forName(element.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return Object.class;
        }
    }

    /** Gets the method call.
     *
     * @param method
     *            the method
     * @param args
     *            the args
     * @return the method call
     */
    private String getMethodCall(Method method, Object[] args) {
        return method.getName() + "(" + formatCallArgs(args) + ")";
    }

    /** Format call args.
     *
     * @param args
     *            the args
     * @return the string
     */
    private String formatCallArgs(Object[] args) {
        if (args == null) return "";
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < args.length; i++) {
            if (i > 0 ) buf.append(", ");
            buf.append(args[i]);
        }
        return buf.toString();
    }

    /** Update ui.
     *
     * @param testName
     *            the test name
     * @param url
     *            the url
     * @param htmlContent
     *            the html content
     * @param methodCall
     *            the method call
     * @param exception
     *            the exception
     */
    private void updateUI(final String testName, final String url,
                          final String htmlContent,
                          final String methodCall,
                          final Throwable exception) {
        initUI();
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    http.setTitle(testName);
                    String relativeUrl = XPlannerTestSupport.getRelativeTestURL();
                    String content = htmlContent.replaceAll(relativeUrl + "/", baseFileUrl +"/");

                    http.setDocumentContent(content);
                    loadPxUnitOnlyStyleSheet();
                    http.setURL(url);
                    http.setInstruction(methodCall);
                    http.setException(exception);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (exception != null) {
            exception.printStackTrace();
            breakOnException();
        }
        waitForContinue();
    }

    /** The Constant EM_TO_PX_RATIO. */
    public static final double EM_TO_PX_RATIO = 11.0/0.8;

    /** Load px unit only style sheet.
     */
    private void loadPxUnitOnlyStyleSheet() {
        try {
            URL url = new URL(baseFileUrl+"/default.css");
            String ssContent = readFileIntoString(new File(url.getFile()));
            ssContent = convertEmUnitIntoPxUnit(ssContent);
            StringReader reader = new StringReader(ssContent);
            ((HTMLDocument) http.getHtmlPane().getDocument()).getStyleSheet().loadRules(reader, url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Convert em unit into px unit.
     *
     * @param ssContent
     *            the ss content
     * @return the string
     */
    private String convertEmUnitIntoPxUnit(String ssContent) {
        NumberFormat emFormat = getEmFormat();
        NumberFormat pxFormat = getPxFormat();
        for (double em = 0.1; em <= 2.0; em+= 0.1) {
            String pxStr = pxFormat.format(em * EM_TO_PX_RATIO) + "px";
            String emStr = emFormat.format(em).replaceFirst("\\.", "\\\\.") + "em";
            ssContent = ssContent.replaceAll(emStr, pxStr);
        }
        return ssContent;
    }

    /** Gets the em format.
     *
     * @return the em format
     */
    private NumberFormat getEmFormat() {
        NumberFormat format = DecimalFormat.getNumberInstance();
        format.setMinimumFractionDigits(1);
        format.setMaximumFractionDigits(1);
        format.setMinimumIntegerDigits(1);
        format.setMaximumIntegerDigits(1);
        return format;
    }

    /** Gets the px format.
     *
     * @return the px format
     */
    private NumberFormat getPxFormat() {
        NumberFormat format = DecimalFormat.getIntegerInstance();
        return format;
    }


    /** Read file into string.
     *
     * @param file
     *            the file
     * @return the string
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static String readFileIntoString(File file) throws IOException {
        byte bytes[];
        FileInputStream istream = new FileInputStream(file);
        bytes = new byte[istream.available()];
        istream.read(bytes);
        istream.close();

        return new String(bytes);
    }

    /** Break on exception.
     */
    private void breakOnException() {

    }

    /** Wait for continue.
     */
    public void waitForContinue() {
        synchronized(continueLock) {
            if (!stepping) return;
            try {
                continueLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /** Notify to continue.
     */
    public void notifyToContinue() {
        synchronized(continueLock) {
            continueLock.notifyAll();
        }
    }

    /** Inits the ui.
     */
    public void initUI() {
        if (http != null) return;
        ActionListener stepAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stepping = true;
                notifyToContinue();
            }
        };
        ActionListener continueAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stepping = false;
                notifyToContinue();
            }
        };
        ActionListener pauseAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stepping = true;
            }
        };
        http = new HttpFrame(stepAction, continueAction, pauseAction);
        http.setSize(800,600);
        http.setVisible(true);
        http.requestFocus();
    }

    /** The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
        VisualTesterHandler handler = new VisualTesterHandler();
        handler.setStepping(true);
//        handler.updateUI("testMethod", "test", htmlTest, "method1", null);
        handler.updateUI("testMethod", "test", "<html><table><tr><td>2</tr></td></table>" + "</html>", "method2()", null);
        handler.updateUI("testMethod", "test", "<html><b>3</b>" + "</html>", "method3()", null);
        handler.updateUI("testMethod", "test", "<html><b>4</b>" + "</html>", "method4()", null);
    }
}