package tools.customizable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

/**
 * A property whose value is either yes/on/{@code true} or no/off/{@code false}.
 * 
 * @author William Chargin
 * 
 */
public class TrueFalseProperty extends
		AbstractSwingProperty<Boolean, JCheckBox, JLabel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The text shown when the property is {@code true}.
	 */
	private String trueText;

	/**
	 * The text shown when the property is {@code false}.
	 */
	private String falseText;

	/**
	 * Creates the property with a blank name, a value of {@code false}, and the
	 * given true and false texts.
	 * 
	 * @param trueText
	 *            the text shown when the property's value is {@code true}
	 * @param falseText
	 *            the text shown when the property's value is {@code false}
	 */
	public TrueFalseProperty(String trueText, String falseText) {
		this(new String(), false, trueText, falseText);
	}

	/**
	 * Creates the property with the given name, value, and true and false
	 * texts.
	 * 
	 * @param name
	 *            the property name
	 * @param value
	 *            the property value
	 * @param trueText
	 *            the text shown when the property's value is {@code true}
	 * @param falseText
	 *            the text shown when the property's value is {@code false}
	 */
	public TrueFalseProperty(String name, Boolean value, String trueText,
			String falseText) {
		super(name, value);
		this.trueText = trueText;
		this.falseText = falseText;
	}

	@Override
	protected JCheckBox createEditor() {
		final JCheckBox chbx = new JCheckBox();
		chbx.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				setValue(chbx.isSelected());
			}
		});
		return chbx;
	}

	@Override
	protected JLabel createViewer() {
		return new JLabel();
	}

	@Override
	protected void updateEditor(JCheckBox editor) {
		editor.setSelected(getValue());
		editor.setToolTipText(getValue() == true ? trueText : falseText);
		editor.setEnabled(isEnabled());
	}

	@Override
	protected void updateViewer(JLabel viewer) {
		viewer.setText(getValue() == true ? trueText : falseText);
	}

}
