package tools.customizable;

import javax.swing.JLabel;

/**
 * The message property is special in that it does not allow editing - it is
 * just a message. It would be most commonly used as a note after another
 * property in a {@link PropertySet}.
 * 
 * @author William Chargin
 * 
 */
public class MessageProperty extends
		AbstractSwingProperty<String, JLabel, JLabel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates the property with the given name and value.
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public MessageProperty(String name, String value) {
		super(name, value);
		setEnabled(false);
	}

	@Override
	protected void updateEditor(JLabel editor) {
		editor.setText(getValue());
	}

	@Override
	protected void updateViewer(JLabel viewer) {
		viewer.setText(getValue());
	}

	@Override
	protected JLabel createEditor() {
		JLabel editor = new JLabel(getValue());
		return editor;
	}

	@Override
	protected JLabel createViewer() {
		JLabel viewer = new JLabel(getValue());
		return viewer;
	}

}
