package com.technoetic.mocks.servlets.jsp.tagext;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;

/**
 * The Class MockBodyContent.
 */
public class MockBodyContent extends BodyContent {

    /** Instantiates a new mock body content.
     *
     * @param previousWriter
     *            the previous writer
     */
    public MockBodyContent(JspWriter previousWriter) {
        super(previousWriter);
    }

    /** The flush called. */
    public boolean flushCalled;
    
    /** The flush io exception. */
    public java.io.IOException flushIOException;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyContent#flush()
     */
    public void flush() throws java.io.IOException {
        flushCalled = true;
        if (flushIOException != null) {
            throw flushIOException;
        }
    }

    /** The clear body called. */
    public boolean clearBodyCalled;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyContent#clearBody()
     */
    public void clearBody() {
        clearBodyCalled = true;
    }

    /** The get reader called. */
    public boolean getReaderCalled;
    
    /** The get reader return. */
    public java.io.Reader getReaderReturn;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyContent#getReader()
     */
    public java.io.Reader getReader() {
        getReaderCalled = true;
        return getReaderReturn;
    }

    /** The get string called. */
    public boolean getStringCalled;
    
    /** The get string return. */
    public java.lang.String getStringReturn;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyContent#getString()
     */
    public java.lang.String getString() {
        getStringCalled = true;
        return getStringReturn;
    }

    /** The write out called. */
    public boolean writeOutCalled;
    
    /** The write out io exception. */
    public java.io.IOException writeOutIOException;
    
    /** The write out out. */
    public java.io.Writer writeOutOut;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyContent#writeOut(java.io.Writer)
     */
    public void writeOut(java.io.Writer out) throws java.io.IOException {
        writeOutCalled = true;
        writeOutOut = out;
        if (writeOutIOException != null) {
            throw writeOutIOException;
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyContent#getEnclosingWriter()
     */
    public javax.servlet.jsp.JspWriter getEnclosingWriter() {
        return super.getEnclosingWriter();
    }

    /** The output. */
    public StringWriter output = new StringWriter();
    
    /** The out. */
    public PrintWriter out = new PrintWriter(output);

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