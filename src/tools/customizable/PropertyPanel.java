package tools.customizable;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

public class PropertyPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Indicates that the relevant property panel is in edit mode.
	 */
	private final String EDIT = "EDIT"; //$NON-NLS-1$

	/**
	 * Indicates that the relevant property panel is in viewing mode.
	 */
	private final String VIEW = "VIEW"; //$NON-NLS-1$

	/**
	 * Indicates that the relevant property panel is enabled.
	 */
	private final String ENABLED = "ENABLED"; //$NON-NLS-1$

	/**
	 * Indicates that the relevant property panel is disabled.
	 */
	private final String DISABLED = "DISABLED"; //$NON-NLS-1$

	/**
	 * Whether the panel is changeable between edit and view modes.
	 */
	private final boolean isChangeable;

	/**
	 * The properties displayed in this panel.
	 */
	private final List<AbstractProperty<?>> propertySet;

	/**
	 * Creates the property panel with the given parameters.
	 * 
	 * @param properties
	 *            the set of properties
	 * @param editMode
	 *            whether this panel is for editing ({@code true}) or viewing (
	 *            {@code false})
	 * @param changeableMode
	 *            whether this panel can toggle between editing and viewing (
	 *            {@code true}) or is fixed ({@code false})
	 */
	public PropertyPanel(Collection<? extends AbstractProperty<?>> properties,
			boolean editMode, boolean changeableMode) {
		super(new MigLayout());
		ArrayList<AbstractProperty<?>> ps = new ArrayList<AbstractProperty<?>>();
		for (AbstractProperty<?> property : properties) {
			ps.add(property);
		}
		ps.addAll(properties);
		propertySet = ps;
		isChangeable = changeableMode;
		if (changeableMode) {
			for (AbstractProperty<?> property : properties) {
				if (!(property instanceof AbstractSwingProperty)
						&& property != null) {
					continue;
				}
				if (property == null) {
					add(new JSeparator(JSeparator.HORIZONTAL), new CC().growX()
							.pushX().spanX().wrap());
				} else {
					final AbstractSwingProperty<?, ?, ?> sp = (AbstractSwingProperty<?, ?, ?>) property;
					final CardLayout clName = new CardLayout();
					final JPanel pnlName = new JPanel(clName);
					add(pnlName, new CC().alignX("right")); //$NON-NLS-1$
					pnlName.setOpaque(false);

					final JToggleButton tglbtnName = new JToggleButton(
							sp.getName());
					pnlName.add(tglbtnName, ENABLED);
					tglbtnName.setSelected(editMode);
					tglbtnName.setToolTipText(sp.getDescription());
					tglbtnName.setFocusable(false);
					tglbtnName.setFont(tglbtnName.getFont().deriveFont(
							Font.BOLD));
					tglbtnName.setHorizontalAlignment(JLabel.TRAILING);

					JLabel lblName = new JLabel(sp.getName());
					pnlName.add(lblName, DISABLED);
					lblName.setOpaque(false);
					lblName.setFont(lblName.getFont().deriveFont(Font.BOLD));
					lblName.setHorizontalAlignment(JLabel.TRAILING);

					clName.show(pnlName, sp.isEnabled() ? ENABLED : DISABLED);
					sp.addChangeListener(new ChangeListener() {
						@Override
						public void stateChanged(ChangeEvent ce) {
							clName.show(pnlName, sp.isEnabled() ? ENABLED
									: DISABLED);
							if (sp.getEditAction().equals(EditAction.ACTION)) {
								tglbtnName.setSelected(false);
							}
						}
					});

					final CardLayout clValue = new CardLayout();
					final JPanel pnlValue = new JPanel(clValue);
					pnlValue.setOpaque(false);
					pnlValue.setBackground(new Color(255, 255, 255, 0));
					pnlValue.add(sp.getEditor(), EDIT);
					pnlValue.add(sp.getViewer(), VIEW);
					clValue.show(pnlValue, editMode ? EDIT : VIEW);
					tglbtnName.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent ae) {
							switch (sp.getEditAction()) {
							case EDIT:
								clValue.show(pnlValue,
										tglbtnName.isSelected() ? EDIT : VIEW);
								break;
							case ACTION:
								tglbtnName.setSelected(false);
								sp.getAction().actionPerformed(
										new ActionEvent(PropertyPanel.this,
												ActionEvent.ACTION_PERFORMED,
												sp.getName()));
								break;
							}
						}
					});
					add(pnlValue, new CC().grow().wrap());
				}
			}
		} else {
			for (AbstractProperty<?> property : properties) {
				if (!(property instanceof AbstractSwingProperty)
						&& property != null) {
					continue;
				}
				if (property == null) {
					add(new JSeparator(JSeparator.HORIZONTAL), new CC().growX()
							.pushX().spanX().wrap());
				} else {
					AbstractSwingProperty<?, ?, ?> sp = (AbstractSwingProperty<?, ?, ?>) property;
					JLabel label = new JLabel(sp.getName());
					label.setFont(label.getFont().deriveFont(Font.BOLD));
					label.setHorizontalAlignment(JLabel.TRAILING);
					label.setToolTipText(sp.getDescription());
					add(label, new CC().alignX("right")); //$NON-NLS-1$
					add(editMode ? sp.getEditor() : sp.getViewer(), new CC()
							.grow().wrap().pushX());
				}
			}
		}
	}

	/**
	 * Sets the edit/view mode. Has no effect if the panel was initialized
	 * without changeability.
	 * 
	 * @param editing
	 *            whether this panel is for editing ({@code true}) or viewing (
	 *            {@code false})
	 * @return {@code true} if the panel is changeable, or {@code false} if it
	 *         is not
	 */
	public boolean setEditing(boolean editing) {
		if (!isChangeable) {
			return false;
		} else {
			for (Component component : getComponents()) {
				if (component instanceof JPanel) {
					LayoutManager lm = ((JPanel) component).getLayout();
					if (lm instanceof CardLayout) {
						((CardLayout) lm).show((Container) component,
								editing ? EDIT : VIEW);
					}
				}
			}
			return true;
		}
	}

	/**
	 * Gets an unmodifiable copy of the properties in this panel.
	 * 
	 * @return the properties
	 */
	public Collection<? extends AbstractProperty<?>> getProperties() {
		return Collections.unmodifiableList(propertySet);
	}

}
