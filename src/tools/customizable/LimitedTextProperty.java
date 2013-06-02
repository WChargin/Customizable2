package tools.customizable;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * A text property of limited length.
 * 
 * @author William Chargin
 * 
 */
public class LimitedTextProperty extends TextProperty {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The maximum number of characters permitted.
	 */
	private final int maxChars;

	/**
	 * Creates the property with a blank name and text, and the given maximum
	 * character count.
	 * 
	 * @param maxChars
	 *            the maximum character count
	 */
	public LimitedTextProperty(int maxChars) {
		super();
		this.maxChars = maxChars;
	}

	/**
	 * Creates the property with the given name, starting value, and maximum
	 * character count.
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 * @param maxChars
	 *            the maximum character count
	 */
	public LimitedTextProperty(String name, String value, int maxChars) {
		super(name, value);
		this.maxChars = maxChars;
	}

	@Override
	protected JTextField createEditor() {
		final JTextField editor = super.createEditor();
		editor.setColumns(maxChars);
		((AbstractDocument) editor.getDocument())
				.setDocumentFilter(new DocumentFilter() {
					@Override
					public void replace(FilterBypass fb, int offset,
							int length, String text, AttributeSet attrs)
							throws BadLocationException {
						super.replace(fb, offset, length, new String(), attrs);
						int offsetoffset = 0;
						for (char c : text.toCharArray()) {
							if (editor.getText().length() < maxChars) {
								super.replace(fb, offset + (offsetoffset++), 0,
										Character.toString(c), attrs);
							}
						}
					}

				});
		return editor;
	}
}
