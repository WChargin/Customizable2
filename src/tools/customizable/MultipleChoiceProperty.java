package tools.customizable;

import java.util.LinkedHashSet;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * A property that allows for selection of one value from a set of given values.
 * 
 * @author William Chargin
 * 
 * @param <T>
 *            the type of values being selected
 */
public class MultipleChoiceProperty<T> extends
		AbstractSwingProperty<T, JComboBox, JLabel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The list of possible values.
	 */
	private final ArrayList<T> values = new ArrayList<T>();

	/**
	 * The renderer used in the editors.
	 */
	private ListCellRenderer renderer;

	@Override
	protected JComboBox createEditor() {
		final JComboBox cmbx = new JComboBox();
		cmbx.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent ae) {
				if (cmbx.getSelectedItem() == null) {
					setValue(null);
				} else {
					setValue((T) cmbx.getSelectedItem());
				}
			}
		});
		return cmbx;
	}

	@Override
	protected JLabel createViewer() {
		return new JLabel();
	}

	@Override
	protected void updateEditor(JComboBox editor) {
		editor.setModel(new DefaultComboBoxModel(new Vector<T>(values)));
		editor.setSelectedItem(getValue());
		if (renderer != null) {
			editor.setRenderer(renderer);
		}
		editor.setEnabled(isEnabled());
	}

	@Override
	protected void updateViewer(JLabel viewer) {
		viewer.setText(getValue().toString());
	}

	/**
	 * Creates the property with a blank name and the first value from the set
	 * of given values.
	 * 
	 * @param values
	 *            the possible choices
	 * @param def
	 *            the default value to use if {@code values} is empty
	 * @throws IllegalArgumentException
	 *             if the list of values is {@code null}
	 */
	public MultipleChoiceProperty(Collection<? extends T> values, T def)
			throws IllegalArgumentException {
		this(new String(), values, values.isEmpty() ? def : null);
	}

	/**
	 * Creates the property with a blank name, the given set of possible
	 * choices, and the given starting value. If the starting value is not in
	 * the list of possibilities, it will be added.
	 * 
	 * @param name
	 *            the name of this parameter
	 * @param values
	 *            the possible choices
	 * @param value
	 *            the starting value. If this is {@code null}, the first value
	 *            from the given list will be used
	 * @throws IllegalArgumentException
	 *             if the list of values is {@code null}
	 */
	public MultipleChoiceProperty(String name, Collection<? extends T> values,
			T value) throws IllegalArgumentException {
		if (values == null) {
			throw new IllegalArgumentException("values == null"); //$NON-NLS-1$
		}
		this.values.addAll(new LinkedHashSet<T>(values));
		if (value != null && !values.contains(value)) {
			this.values.add(value);
		}
		setValue(value == null ? this.values.get(0) : value);
		setName(name);
	}

	/**
	 * Creates a {@code MultipleChoiceProperty} from the given enum. The default
	 * value will be the first value in the enum.
	 * 
	 * @param name
	 *            the name of the property
	 * @param clazz
	 *            the class of the enum containing the desired possibilities
	 * @return the multiple choice property
	 */
	public static <T extends Enum<T>> MultipleChoiceProperty<T> createFromEnum(
			String name, Class<T> clazz) throws IllegalArgumentException {
		final MultipleChoiceProperty<T> property = new MultipleChoiceProperty<T>(
				name, Arrays.<T> asList(clazz.getEnumConstants()), null);
		property.setRenderer(createEnumRenderer());
		return property;
	}

	/**
	 * Gets the current renderer.
	 * 
	 * @return the renderer used in the editors
	 */
	public ListCellRenderer getRenderer() {
		return renderer;
	}

	/**
	 * Sets the renderer for the editors to the given renderer, and updates all
	 * editors.
	 * 
	 * @param renderer
	 *            the new renderer
	 */
	public void setRenderer(ListCellRenderer renderer) {
		this.renderer = renderer;
		updateEditors();
	}

	/**
	 * Generates a renderer optimized for enums. The renderer will render the
	 * enum as a string, with the first character capitalized and the rest
	 * lowercase.
	 * 
	 * @return the renderer
	 */
	public static ListCellRenderer createEnumRenderer() {
		return new ListCellRenderer() {
			/**
			 * The default list cell renderer.
			 */
			private final DefaultListCellRenderer dlcr = new DefaultListCellRenderer();

			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				String string = (value == null ? new String() : value
						.toString());
				switch (string.length()) {
				case 0:
					break;
				case 1:
					string = string.toUpperCase();
					break;
				default:
					string = Character.toString(
							Character.toUpperCase(string.charAt(0))).concat(
							string.substring(1).toLowerCase());
					break;
				}
				return dlcr.getListCellRendererComponent(list, string, index,
						isSelected, cellHasFocus);
			}
		};
	}

	/**
	 * Sets the list of possible values to the given list
	 * 
	 * @param values
	 *            the list of new values
	 */
	public void setValues(Collection<T> values) {
		this.values.clear();
		this.values.addAll(values);
		if (!this.values.isEmpty()) {
			setValue(this.values.get(0));
		}
		updateEditors();
	}
}
