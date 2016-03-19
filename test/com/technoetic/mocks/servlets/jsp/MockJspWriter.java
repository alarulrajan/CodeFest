package com.technoetic.mocks.servlets.jsp;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.jsp.JspWriter;


/**
 * The Class MockJspWriter.
 */
public class MockJspWriter extends JspWriter {

    /** The output. */
    public StringWriter output = new StringWriter();
    
    /** The out. */
    public PrintWriter out = new PrintWriter(output);

    /** Instantiates a new mock jsp writer.
     */
    public MockJspWriter() {
        super(1, true);
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#println(char)
     */
    public void println(char x) throws java.io.IOException {
        out.println(x);
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#println(long)
     */
    public void println(long x) throws java.io.IOException {
        out.println(x);
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#newLine()
     */
    public void newLine() throws java.io.IOException {
        out.println();
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#print(char[])
     */
    public void print(char[] parm1) throws java.io.IOException {
        out.print(parm1);
    }

    /* (non-Javadoc)
     * @see java.io.Writer#write(char[], int, int)
     */
    public void write(char[] parm1, int parm2, int parm3) throws java.io.IOException {
        out.write(parm1);
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#println(float)
     */
    public void println(float x) throws java.io.IOException {
        out.println(x);
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#println(double)
     */
    public void println(double x) throws java.io.IOException {
        out.println(x);
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#flush()
     */
    public void flush() throws java.io.IOException {
        out.flush();
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#println(java.lang.Object)
     */
    public void println(Object x) throws java.io.IOException {
        out.println(x);
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#clearBuffer()
     */
    public void clearBuffer() throws java.io.IOException {
        //out.clearBuffer();
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#getRemaining()
     */
    public int getRemaining() {
        return 0;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#println(char[])
     */
    public void println(char[] parm1) throws java.io.IOException {
        out.println(parm1);
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#println(boolean)
     */
    public void println(boolean x) throws java.io.IOException {
        out.println(x);
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#print(char)
     */
    public void print(char c) throws java.io.IOException {
        out.print(c);
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#print(long)
     */
    public void print(long l) throws java.io.IOException {
        out.print(l);
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#println(java.lang.String)
     */
    public void println(String x) throws java.io.IOException {
        out.println(x);
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#print(java.lang.Object)
     */
    public void print(Object obj) throws java.io.IOException {
        out.print(obj);
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#close()
     */
    public void close() throws java.io.IOException {
        out.close();
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#print(double)
     */
    public void print(double d) throws java.io.IOException {
        out.print(d);
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#isAutoFlush()
     */
    public boolean isAutoFlush() {
        return super.isAutoFlush();
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#print(boolean)
     */
    public void print(boolean b) throws java.io.IOException {
        out.print(b);
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#println(int)
     */
    public void println(int x) throws java.io.IOException {
        out.println(x);
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#print(float)
     */
    public void print(float f) throws java.io.IOException {
        out.print(f);
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#getBufferSize()
     */
    public int getBufferSize() {
        return super.getBufferSize();
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#println()
     */
    public void println() throws java.io.IOException {
        out.println();
    }

    /** The print called. */
    public boolean printCalled;
    
    /** The print value. */
    public String printValue;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#print(java.lang.String)
     */
    public void print(String value) throws java.io.IOException {
        printCalled = true;
        printValue = value;
        out.print(value);
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#clear()
     */
    public void clear() throws java.io.IOException {
        //output.clear();
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.JspWriter#print(int)
     */
    public void print(int i) throws java.io.IOException {
        out.print(i);
    }
}