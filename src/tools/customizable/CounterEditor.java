package tools.customizable;

import java.awt.CardLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tools.customizable.CounterProperty.CounterModel;

/**
 * An editor for a {@link CounterProperty}. It has both a slider and a spinner.
 * 
 * @author William Chargin
 * 
 */
public class CounterEditor extends JPanel {

	/**
	 * The different types of editors for the {@code CounterEdiftor}.
	 * 
	 * @author William Chargin
	 * 
	 */
	public enum EditorType {
		/**
		 * Indicates a spinner (likely a {@link JSpinner}) as an editor.
		 */
		SPINNER,

		/**
		 * Indicates a slider (likely a {@link JSlider}) as an editor,
		 */
		SLIDER;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The {@code CounterModel} used in this editor.
	 */
	private CounterModel model;

	/**
	 * The slider used in this editor.
	 */
	private JSlider slider;

	/**
	 * The spinner used in this editor.
	 */
	private JSpinner spinner;

	/**
	 * The current value of the editors.
	 */
	private int value;

	/**
	 * The card layout used for this component.
	 */
	private final transient CardLayout cardLayout = new CardLayout();

	/**
	 * Creates the editor with the given model and property.
	 * 
	 * @param property
	 *            the property this is controlling
	 * @param model
	 *            the model to use
	 */
	public CounterEditor(final CounterProperty property, CounterModel model) {
		super();
		this.model = model;
		slider = new JSlider(this.model.minimum, this.model.maximum,
				this.model.def);
		slider.setSnapToTicks(true);
		spinner = new JSpinner(new SpinnerNumberModel(this.model.def,
				this.model.minimum, this.model.maximum, 1));

		setLayout(cardLayout);
		add(spinner, EditorType.SPINNER.toString());
		add(slider, EditorType.SLIDER.toString());

		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				property.setValue((Integer) spinner.getValue());
			}
		});

		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				property.setValue(slider.getValue());
			}
		});

	}

	/**
	 * Sets the GUI representation of this editor type.
	 * 
	 * @param type
	 *            the type of editor
	 */
	public void setEditorType(EditorType type) {
		if (cardLayout != null && type != null) {
			cardLayout.show(this, type.toString());
		}
	}

	/**
	 * Sets the number format pattern of the spinner.
	 * 
	 * @param nf
	 *            the new number format pattern
	 */
	public void setSpinnerPattern(String nfp) {
		if (spinner != null) {
			spinner.setEditor(new JSpinner.NumberEditor(spinner, nfp));
		}
	}

	/**
	 * Updates the spinner and slider with the new value.
	 * 
	 * @param value
	 *            the new value
	 */
	protected void updateValue(int value) {
		this.value = value;
		spinner.setValue(this.value);
		slider.setValue(this.value);
	}

	@Override
	public String getToolTipText() {
		return Integer.toString(value);
	}

	/**
	 * Sets the format of the number editor.
	 * 
	 * @param format
	 *            the new format
	 */
	public void setFormat(String format) {
		if (format != null) {
			spinner.setEditor(new JSpinner.NumberEditor(spinner, format));
		}
	}

}