package tools.customizable;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import tools.customizable.ColorProperty.ColorViewer;

/**
 * A property for a color.
 * 
 * @author William Chargin
 * 
 */
public class ColorProperty extends
		AbstractSwingProperty<Color, JButton, ColorViewer> {

	/**
	 * A viewer for the {@link ColorProperty}.
	 * 
	 * @author William Chargin
	 * 
	 */
	protected static class ColorViewer extends JComponent {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * The color used in the viewer.
		 */
		private Color color;

		/**
		 * Creates the viewer.
		 */
		public ColorViewer() {
			super();
			setPreferredSize(new Dimension(60, 20));
		}

		@Override
		public void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			if (color == null) {
				color = Color.BLACK;
			}
			Color dark = new Color((int) (color.getRed() * 0.9),
					(int) (color.getGreen() * 0.9),
					(int) (color.getBlue() * 0.9));
			GradientPaint gp = new GradientPaint(getWidth() / 2, getHeight(),
					dark, getWidth() / 2, (int) (0), color);
			g2d.setPaint(gp);
			g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
			g2d.setColor(Color.BLACK);
			g2d.setStroke(new BasicStroke(1));
			g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		}

		/**
		 * Sets the color and repaints the display.
		 * 
		 * @param color
		 *            the new color
		 */
		public void setColor(Color color) {
			this.color = color;
			repaint();
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected JButton createEditor() {
		final JButton button = new JButton(Messages.getString("ColorProperty.ButtonSelectColor")); //$NON-NLS-1$
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				Color c = JColorChooser.showDialog(
						SwingUtilities.getWindowAncestor(button),
						Messages.getString("ColorProperty.SelectColorDialogTitle"), getValue()); //$NON-NLS-1$
				if (c != null) {
					setValue(c);
				}
			}
		});
		return button;
	}

	@Override
	protected ColorViewer createViewer() {
		return new ColorViewer();
	}

	@Override
	protected void updateEditor(JButton editor) {
		editor.setEnabled(isEnabled());
		editor.setIcon(getIcon(editor.getPreferredSize().height * 2 / 3));
	}

	@Override
	protected void updateViewer(ColorViewer viewer) {
		viewer.setColor(getValue());
	}

	/**
	 * Gets an image icon displaying the {@linkplain #getValue() currently
	 * selected} color, or a slashed circle if the value is {@code null}.
	 * 
	 * @param circleSize
	 *            the size of the icon (will be square)
	 * @return the icon
	 */
	private ImageIcon getIcon(int circleSize) {
		BufferedImage bi = new BufferedImage(circleSize, circleSize,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) bi.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Ellipse2D e2d = new Ellipse2D.Double(0, 0, circleSize - 1, circleSize - 1);
		if (getValue() == null) {
			double offsetFromCenter = (circleSize * Math.sqrt(2) / 4);
			g2d.setColor(Color.BLACK);
			final int small = (int) (circleSize / 2 - offsetFromCenter) + 1;
			final int large = (int) Math.ceil(circleSize / 2 + offsetFromCenter) - 1;
			g2d.drawLine(small, large, large, small);
		} else {
			g2d.setColor(getValue());
			g2d.fill(e2d);
		}
		g2d.setColor(Color.BLACK);
		g2d.draw(e2d);
		return new ImageIcon(bi);
	}

	/**
	 * Creates the property with a blank name and black color.
	 */
	public ColorProperty() {
		this(new String(), Color.BLACK);
	}

	/**
	 * Creates the property with the given name and value (color).
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public ColorProperty(String name, Color value) {
		super(name, value);
	}

	/**
	 * Sets the color to the given value, or {@link Color#BLACK} if the given
	 * value is {@code null}.
	 * 
	 * @param value
	 *            the new value
	 * @see tools.customizable.AbstractSwingProperty#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Color value) {
		super.setValue(value == null ? Color.BLACK : value);
	}

}