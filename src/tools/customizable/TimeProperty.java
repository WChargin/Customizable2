package tools.customizable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import tools.customizable.TimeProperty.TimePanel;

public class TimeProperty extends
		AbstractSwingProperty<Time, TimePanel, JLabel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TimeProperty(String name, Time value) {
		super(name, value);
	}

	@Override
	protected void updateEditor(TimePanel editor) {
		Time t = getValue();
		editor.hours.setValue(t.hours);
		editor.minutes.setValue(t.minutes);
		editor.seconds.setValue(t.seconds);
		editor.setEnabled(isEnabled());
	}

	@Override
	protected void updateViewer(JLabel viewer) {
		Time t = getValue();
		viewer.setText(t.toString());
	}

	@Override
	protected TimePanel createEditor() {
		return new TimePanel(getValue());
	}

	@Override
	protected JLabel createViewer() {
		return new JLabel();
	}

	/**
	 * A panel to allow manipulation of a {@code Time} object.
	 * 
	 * @author William Chargin
	 * 
	 */
	protected class TimePanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * The format string for the spinners.
		 */
		private static final String PATTERN = "00"; //$NON-NLS-1$

		/**
		 * The spinner representing the hours.
		 */
		private final JSpinner hours;

		/**
		 * The spinner representing the miuntes.
		 */
		private final JSpinner minutes;

		/**
		 * The spinner representing the seconds.
		 */
		private final JSpinner seconds;

		/**
		 * Creates the panel with the given time.
		 * 
		 * @param time
		 *            the starting time
		 */
		public TimePanel(Time time) {
			super(new MigLayout());

			CC cc = new CC().growX().pushX();

			final SpinnerNumberModel hoursModel = new SpinnerNumberModel(
					time.hours, 0, Integer.MAX_VALUE, 1);
			hours = new JSpinner(hoursModel);
			final JSpinner.NumberEditor hoursEditor = new JSpinner.NumberEditor(
					hours, PATTERN);
			hoursEditor.getTextField().setColumns(3);
			hours.setEditor(hoursEditor);
			add(hours, cc);

			final SpinnerNumberModel minutesModel = new SpinnerNumberModel(
					time.minutes, -1, 60, 1);
			minutes = new JSpinner(minutesModel);
			final JSpinner.NumberEditor minutesEditor = new JSpinner.NumberEditor(
					minutes, PATTERN);
			minutesEditor.getTextField().setColumns(2);
			minutes.setEditor(minutesEditor);
			add(minutes, cc);

			final SpinnerNumberModel secondsModel = new SpinnerNumberModel(
					time.seconds, -1, 60, 1);
			seconds = new JSpinner(secondsModel);
			final JSpinner.NumberEditor secondsEditor = new JSpinner.NumberEditor(
					seconds, PATTERN);
			secondsEditor.getTextField().setColumns(2);
			seconds.setEditor(secondsEditor);
			add(seconds, cc);

			secondsModel.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent ce) {
					final int hoursValue = hoursModel.getNumber().intValue();
					final int minutesValue = minutesModel.getNumber()
							.intValue();
					final int secondsValue = secondsModel.getNumber()
							.intValue();

					if (secondsValue == 60) {
						if (hoursValue < Integer.MAX_VALUE || minutesValue < 60) {
							minutesModel.setValue(minutesModel.getNextValue());
							secondsModel.setValue(0);
						} else {
							secondsModel.setValue(59);
						}
					} else if (secondsValue == 0) {
						// Went from 1 -> 0.
						if (hoursValue > 0 || minutesValue > 0) {
							// Okay.
							return;
						} else {
							secondsModel.setValue(1);
						}
					} else if (secondsValue == -1) {
						// Went down from 0.
						if (minutesValue > 0) {
							minutesModel.setValue(minutesModel
									.getPreviousValue());
							secondsModel.setValue(59);
						} else if (hoursValue > 0) {
							hoursModel.setValue(hoursModel.getPreviousValue());
							minutesModel.setValue(59);
							secondsModel.setValue(59);
						} else {
							secondsModel.setValue(0);
						}
					}
				}
			});

			minutesModel.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent ce) {
					final int hoursValue = hoursModel.getNumber().intValue();
					final int minutesValue = minutesModel.getNumber()
							.intValue();
					final int secondsValue = secondsModel.getNumber()
							.intValue();

					if (minutesValue == 60) {
						if (hoursValue < Integer.MAX_VALUE) {
							hoursModel.setValue(hoursModel.getNextValue());
							minutesModel.setValue(0);
						} else {
							minutesModel.setValue(59);
						}
					} else if (minutesValue == 0) {
						// Went from 1 -> 0.
						if (hoursValue > 0 || secondsValue > 0) {
							// Okay.
							return;
						} else {
							secondsModel.setValue(1);
						}
					} else if (minutesValue == -1) {
						// Went down from 0.
						if (hoursValue > 0) {
							hoursModel.setValue(hoursModel.getPreviousValue());
							minutesModel.setValue(59);
						} else {
							minutesModel.setValue(0);
						}
					}
				}
			});

			hoursModel.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent ce) {
					final int hoursValue = hoursModel.getNumber().intValue();
					final int minutesValue = minutesModel.getNumber()
							.intValue();
					final int secondsValue = secondsModel.getNumber()
							.intValue();

					if (hoursValue == 0) {
						// Went from 1 -> 0.
						if (minutesValue > 0 || secondsValue > 0) {
							// Okay.
							return;
						} else {
							secondsModel.setValue(1);
						}
					}
				}
			});

			ChangeListener update = new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					setValue(new Time(hoursModel.getNumber().intValue(),
							minutesModel.getNumber().intValue(), secondsModel
									.getNumber().intValue()));
				}
			};

			hoursModel.addChangeListener(update);
			minutesModel.addChangeListener(update);
			secondsModel.addChangeListener(update);
		}

		@Override
		public void setEnabled(boolean enabled) {
			super.setEnabled(enabled);
			hours.setEnabled(enabled);
			minutes.setEnabled(enabled);
			seconds.setEnabled(enabled);
		}
	}

}
