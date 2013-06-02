package tools.customizable;

import java.io.Serializable;

import javax.swing.JLabel;

import tools.customizable.CounterEditor.EditorType;

/**
 * A counter property, which allows selection of non-negative integers.
 * 
 * @author William Chargin
 * 
 */
public class CounterProperty extends
		AbstractSwingProperty<Integer, CounterEditor, JLabel> {

	/**
	 * The model for a counter, with a minimum, default, and maximum value.
	 * 
	 * @author William Chargin
	 * 
	 */
	protected static class CounterModel implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * The minimum value.
		 */
		protected int minimum;

		/**
		 * The default value.
		 */
		protected int def;

		/**
		 * The maximum value.
		 */
		protected int maximum;

		/**
		 * Creates the model with the given values.
		 * 
		 * @param minimum
		 *            the minimum value
		 * @param def
		 *            the default value
		 * @param maximum
		 *            the maximum value
		 */
		private CounterModel(int minimum, int def, int maximum) {
			super();
			this.minimum = minimum;
			this.def = def;
			this.maximum = maximum;
		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The model for this property.
	 */
	private final CounterModel model;

	/**
	 * The editor type for new {@code CounterEditor}s.
	 */
	private EditorType defaultEditorType;

	/**
	 * The number format.
	 */
	private String format;

	/**
	 * Creates the property with all required parameters.
	 * 
	 * @param name
	 *            the name of this property
	 * @param minimum
	 *            the minimum value
	 * @param def
	 *            the default value
	 * @param maximum
	 *            the maximum value
	 */
	public CounterProperty(String name, int minimum, int def, int maximum) {
		super(name, def);
		this.model = new CounterModel(minimum, def, maximum);
	}

	/**
	 * Creates the property with the given name, a minimum value of {@code 0},
	 * the given default value, and a maximum of {@value Integer#MAX_VALUE}.
	 * 
	 * @param name
	 *            the name
	 * @param def
	 *            the default value
	 */
	public CounterProperty(String name, int def) {
		this(name, 0, def, Integer.MAX_VALUE);
	}

	@Override
	protected CounterEditor createEditor() {
		final CounterEditor editor = new CounterEditor(this, model);
		editor.setEditorType(defaultEditorType);
		return editor;
	}

	@Override
	protected JLabel createViewer() {
		return new JLabel();
	}

	/**
	 * Gets the default editor type for new editors.
	 * 
	 * @return the default editor type
	 */
	public EditorType getDefaultEditorType() {
		return defaultEditorType;
	}

	/**
	 * Sets the default editor type for new editors.
	 * 
	 * @param defaultEditorType
	 *            the new default editor type
	 */
	public void setDefaultEditorType(EditorType defaultEditorType) {
		this.defaultEditorType = defaultEditorType;
	}

	@Override
	protected void updateEditor(CounterEditor editor) {
		editor.updateValue(getValue());
		editor.setEnabled(isEnabled());
		editor.setFormat(getFormat());
	}

	@Override
	protected void updateViewer(JLabel viewer) {
		viewer.setText(Integer.toString(getValue()));
	}

	/**
	 * Gets the number format used in spinner editors.
	 * 
	 * @return the number format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * Sets the number format and updates the editors.
	 * 
	 * @param format
	 *            the new format
	 */
	public void setFormat(String format) {
		this.format = format;
		updateEditors();
	}
}