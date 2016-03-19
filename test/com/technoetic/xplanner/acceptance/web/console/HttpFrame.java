/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Aug 29, 2004
 * Time: 11:33:01 AM
 */
package com.technoetic.xplanner.acceptance.web.console;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;

import org.apache.batik.ext.swing.GridBagConstants;

/**
 * The Class HttpFrame.
 */
class HttpFrame extends JFrame implements GridBagConstants {

    /** The response. */
    private JTextArea response;
    
    /** The html pane. */
    private JEditorPane htmlPane;
    
    /** The url field. */
    private JTextField urlField;
    
    /** The instruction field. */
    private JTextField instructionField;
    
    /** The status field. */
    private JTextField statusField;

    /** Gets the html pane.
     *
     * @return the html pane
     */
    public JEditorPane getHtmlPane() {
        return htmlPane;
    }

    /** Instantiates a new http frame.
     *
     * @param stepAction
     *            the step action
     * @param continueAction
     *            the continue action
     * @param pauseAction
     *            the pause action
     */
    public HttpFrame(ActionListener stepAction, ActionListener continueAction, ActionListener pauseAction) {
        JSplitPane responsePanel = createResponsePanel();
        JPanel toolbar = createToolbar(stepAction, continueAction, pauseAction, response);
        JPanel panInput = createInputPanel();

        this.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = HORIZONTAL;
        c.weightx = 1;
        this.getContentPane().add(panInput, c);
        c.gridy = 1;
        this.getContentPane().add(toolbar, c);
        c.gridy = 2;
        c.fill = BOTH;
        c.weighty = 1;
        this.getContentPane().add(responsePanel, c);
    }

    /** Creates the input panel.
     *
     * @return the j panel
     */
    private JPanel createInputPanel() {
        JPanel panInput = new JPanel(new GridBagLayout());
        urlField = createTextField();
        instructionField = createTextField();
        statusField = createTextField();

        int row = 0;
        createLabeledField(panInput, "Instruction", instructionField, row++);
        createLabeledField(panInput, "Url", urlField, row++);
        createLabeledField(panInput, "Status", statusField, row++);

        return panInput;
    }

    /** Creates the text field.
     *
     * @return the j text field
     */
    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setEditable(false);
        textField.setBackground(Color.WHITE);
        return textField;
    }

    /** Creates the labeled field.
     *
     * @param panInput
     *            the pan input
     * @param title
     *            the title
     * @param field
     *            the field
     * @param row
     *            the row
     */
    private void createLabeledField(JPanel panInput, String title, JTextField field, int row) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 1;
        c.fill = NONE;
        c.anchor = EAST;
        c.weightx = 0;
        panInput.add(new JLabel(title + ":"),c);

        c.gridx = 1;
        c.gridwidth = REMAINDER;
        c.fill = HORIZONTAL;
        c.weightx = 1;
        panInput.add(field,c);
    }

    /** Creates the toolbar.
     *
     * @param stepAction
     *            the step action
     * @param continueAction
     *            the continue action
     * @param pauseAction
     *            the pause action
     * @param textAreaToSearch
     *            the text area to search
     * @return the j panel
     */
    private JPanel createToolbar(ActionListener stepAction,
                                 ActionListener continueAction,
                                 ActionListener pauseAction,
                                 JTextArea textAreaToSearch) {
        final JButton stepBtn = new JButton("Step");
        stepBtn.addActionListener(stepAction);
        final JButton continueBtn = new JButton("Continue");
        continueBtn.addActionListener(continueAction);
        final JButton pauseBtn = new JButton("Pause");
        pauseBtn.addActionListener(pauseAction);
        JPanel searchField = new SearchPanel(textAreaToSearch);

        JPanel toolbar = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridwidth = 1;
        c.fill = NONE;
        c.weightx = 0;

        toolbar.add(stepBtn, c);

        c.gridx++;
        toolbar.add(continueBtn, c);

        c.gridx++;
        toolbar.add(pauseBtn, c);

        c.gridx++;
        c.insets = new Insets(0, 5, 0, 0);
        c.fill = HORIZONTAL;
        c.weightx = 1;
        toolbar.add(searchField,c);

        return toolbar;
    }

    /** Creates the response panel.
     *
     * @return the j split pane
     */
    private JSplitPane createResponsePanel() {
        response = new JTextArea();
        response.setEditable(false);
        response.setCaretPosition(0);

        htmlPane = new JEditorPane();
        htmlPane.setContentType("text/html");
        htmlPane.setEditable(false);

        JSplitPane splitResponsePane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            new JScrollPane(response),
            new JScrollPane(htmlPane)
        );
        splitResponsePane.setOneTouchExpandable(false);
        splitResponsePane.setResizeWeight(0.5);
        return splitResponsePane;
    }

    /** Gets the CSS url.
     *
     * @return the CSS url
     */
    private URL getCSSUrl() {
        URL url = null;
        try {
            url = new URL("file:///c:/dev/xplanner/xplanner-sf/xplanner/war/default.css");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /** Sets the instruction.
     *
     * @param instruction
     *            the new instruction
     */
    public void setInstruction(String instruction) {
        instructionField.setText(instruction);
    }

    /** Sets the exception.
     *
     * @param e
     *            the new exception
     */
    public void setException(Throwable e) {
        if (e == null) {
            statusField.setText("Ok");
            statusField.setForeground(Color.BLACK);
        } else {
            statusField.setText("Exception thrown: " + e.getMessage());
            statusField.setForeground(Color.RED);
        }
    }

    /** Sets the document content.
     *
     * @param content
     *            the new document content
     */
    public void setDocumentContent(String content) {

        HTMLDocument doc = new HTMLDocument();
        try {
            doc.remove(0, doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
//        doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);

        try {
            htmlPane.read(new ByteArrayInputStream(content.getBytes()), doc);
        } catch (IOException e) {
            e.printStackTrace();
        }

        htmlPane.setDocument(doc);
        htmlPane.setCaretPosition(0);

        response.setText(content);
        response.setCaretPosition(0);
        response.requestFocus();
    }


    /** Sets the url.
     *
     * @param url
     *            the new url
     */
    public void setURL(String url) {
        urlField.setText(url);
    }

}