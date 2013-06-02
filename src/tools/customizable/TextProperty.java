package tools.customizable;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A property for a short amount of text (e.g., a first name or street address).
 * 
 * @author William Chargin
 * 
 */
public class TextProperty extends
		AbstractSwingProperty<String, JTextField, JLabel> {

	/**
	 * Creates the property with an empty name and value.
	 */
	public TextProperty() {
		super();
	}

	/**
	 * Creates the property with the given name and value.
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public TextProperty(String name, String value) {
		super(name, value);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected JTextField createEditor() {
		final JTextField txt = new JTextField(20);
		txt.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent de) {
				setValue(txt.getText());
			}

			@Override
			public void insertUpdate(DocumentEvent de) {
				setValue(txt.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent de) {
				setValue(txt.getText());
			}
		});
		return txt;
	}

	@Override
	protected JLabel createViewer() {
		return new JLabel();
	}

	@Override
	protected void updateEditor(JTextField editor) {
		// We don't want any concurrent modification errors, so we'll check to
		// see if this editor's text is the same as the desired text before
		// updating.
		// ... but try-catch just in case.
		try {
			if (editor.getText().equals(getValue())) {
				// This is the editor whose text was modified.
				// Do nothing.
			} else {
				editor.setText(getValue());
			}
			editor.setEnabled(isEnabled());
		} catch (IllegalStateException ise) {
			// Do nothing.
			// That's weird, though.
		}
	}

	@Override
	protected void updateViewer(JLabel viewer) {
		viewer.setText(getValue());
	}

	@Override
	public void setValue(String value) {
		super.setValue(value == null ? new String() : value);
	}

}
