package tools.customizable;

import tools.customizable.FileProperty.FileEditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

/**
 * A property for selecting an existing file reference.
 * 
 * @author William Chargin
 * 
 */
public class FileProperty extends
		AbstractSwingProperty<File, FileEditor, JLabel> {

	/**
	 * The editor for a file property.
	 * 
	 * @author William Chargin
	 * 
	 */
	public class FileEditor extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * The clear icon.
		 */
		private final ImageIcon iconClear = new ImageIcon(
				FileProperty.class
						.getResource("/tools/customizable/rsc/fp_clear.png")); //$NON-NLS-1$

		/**
		 * The "Select File" button.
		 */
		private final JButton btnSelect;

		/**
		 * The clear button.
		 */
		private final JButton btnClear;

		@SuppressWarnings("serial")
		public FileEditor() {
			super(new MigLayout(new LC().insetsAll(Integer.toString(0))));

			setBorder(BorderFactory.createEmptyBorder());

			btnSelect = new JButton(); //$NON-NLS-1$
			btnSelect.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					JFileChooser fc = new JFileChooser(getValue());
					if (filter != null) {
						fc.setFileFilter(filter);
						fc.setAcceptAllFileFilterUsed(false);
					}
					int ret = fc.showOpenDialog(SwingUtilities
							.getWindowAncestor(btnSelect));
					if (ret == JFileChooser.APPROVE_OPTION) {
						setValue(fc.getSelectedFile());
					}
				}
			});
			add(btnSelect, new CC().growX().pushX());

			btnClear = new JButton(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					setValue(null);
				}
			});
			btnClear.setMaximumSize(new Dimension(25, 25));
			btnClear.setIcon(iconClear);
			add(btnClear);
		}

		@Override
		public void setEnabled(boolean enabled) {
			super.setEnabled(enabled);
			btnSelect.setEnabled(enabled);
			btnClear.setEnabled(enabled);
		}

	}

	/**
	 * The maximum number of characters to display for the file name.
	 */
	private static final int MAX_NAME_LENGTH = 20;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The file filter used in the editor.
	 */
	private FileFilter filter;

	/**
	 * Creates the property with a blank name and {@code null} value.
	 */
	public FileProperty() {
		super();
	}

	/**
	 * Creates the given name and value.
	 * 
	 * @param name
	 *            the property name
	 * @param value
	 *            the value
	 */
	public FileProperty(String name, File value) {
		super(name, value);
	}

	@Override
	protected FileEditor createEditor() {
		return new FileEditor();
	}

	@Override
	protected JLabel createViewer() {
		return new JLabel();
	}

	/**
	 * Gets the file filter.
	 * 
	 * @return the file filter, or {@code null} if none has been set
	 */
	public FileFilter getFilter() {
		return filter;
	}

	/**
	 * Sets the file filter.
	 * 
	 * @param filter
	 *            the new file filter, or {@code null} if none should be used
	 */
	public void setFilter(FileFilter filter) {
		this.filter = filter;
	}

	@Override
	protected void updateEditor(FileEditor editor) {
		editor.btnSelect
				.setText(Messages.getString("FileProperty.SelectFile") //$NON-NLS-1$
						+ (getValue() == null ? "" //$NON-NLS-1$
								: (" (" //$NON-NLS-1$
										+ (getValue().getName().length() > MAX_NAME_LENGTH ? getValue()
												.getName()
												.substring(
														0,
														MAX_NAME_LENGTH
																- "...".length()) //$NON-NLS-1$
												+ "..." : getValue().getName()) + ")"))); //$NON-NLS-1$ //$NON-NLS-2$
		editor.setEnabled(isEnabled());
	}

	@Override
	protected void updateViewer(JLabel viewer) {
		viewer.setText(getValue() == null ? Messages
				.getString("FileProperty.NoFileSelected") : getValue() //$NON-NLS-1$
				.getPath());
	}

}
