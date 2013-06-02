package tools.customizable;

import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A property for holding text, which is designed for holding longer text than
 * does a {@link TextProperty}.
 * 
 * @author William Chargin
 * 
 */
public class LongTextProperty extends
		AbstractSwingProperty<String, JScrollPane, JScrollPane> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates the property with a blank name and empty string value.
	 */
	public LongTextProperty() {
		this(new String(), new String());
	}

	/**
	 * Creates the property with the given name and value.
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public LongTextProperty(String name, String value) {
		super(name, value);
	}

	@Override
	protected JScrollPane createEditor() {
		final JTextArea txtr = new JTextArea();
		txtr.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				setValue(txtr.getText());
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				setValue(txtr.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				setValue(txtr.getText());
			}
		});
		txtr.setRows(5);
		txtr.setLineWrap(true);
		txtr.setWrapStyleWord(true);
		txtr.setCaretPosition(0);
		return new JScrollPane(txtr);

	}

	@Override
	protected JScrollPane createViewer() {
		JTextArea txtr = new JTextArea();
		txtr.setRows(5);
		txtr.setLineWrap(true);
		txtr.setWrapStyleWord(true);
		txtr.setEditable(false);
		txtr.setCaretPosition(0);
		return new JScrollPane(txtr);
	}

	@Override
	public void setValue(String value) {
		super.setValue(value == null ? new String() : value);
	}

	@Override
	protected void updateEditor(JScrollPane editor) {
		Component c = editor.getViewport().getView();
		if (c instanceof JTextArea
				&& !((JTextArea) c).getText().equals(getValue())) {
			((JTextArea) c).setText(getValue() == null ? new String()
					: getValue());
			c.setEnabled(isEnabled());
		}
	}

	@Override
	protected void updateViewer(JScrollPane viewer) {
		Component c = viewer.getViewport().getView();
		if (c instanceof JTextArea) {
			JTextArea txtr = (JTextArea) c;
			int caretPos = txtr.getCaretPosition();
			txtr.setText(getValue() == null ? new String() : getValue());
			txtr.setCaretPosition(caretPos);
		}
	}

}
