package tools.customizable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.JComponent;

/**
 * A {@linkplain AbstractProperty property} with support for Swing GUI editors
 * and viewers.
 * 
 * @author William Chargin
 * 
 * @param <T>
 *            the type of value stored in the property
 * @param <E>
 *            the type of editor used (must be a {@code JComponent})
 * @param <V>
 *            the type of viewer used (must be a {@code JComponent})
 */
public abstract class AbstractSwingProperty<T, E extends JComponent, V extends JComponent>
		extends AbstractProperty<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The editors for this property.
	 */
	private transient ArrayList<E> editors = new ArrayList<E>();

	/**
	 * The viewers for this property.
	 */
	private transient ArrayList<V> viewers = new ArrayList<V>();

	/**
	 * The action invoked if the edit action is {@link EditAction#ACTION}.
	 */
	private transient Action action;

	/**
	 * The current edit action.
	 */
	private EditAction editAction = EditAction.EDIT;

	/**
	 * The current enabled status of this property.
	 */
	private boolean enabled = true;

	/**
	 * Creates the property with a blank name (with {@link String#String()}) and
	 * a {@code null} value
	 */
	public AbstractSwingProperty() {
		super();
	}

	/**
	 * Creates the property with the given name and value.
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public AbstractSwingProperty(String name, T value) {
		super(name, value);
	}

	/**
	 * Creates and returns a new editor. Before being delivered to the user, the
	 * {@link #updateEditor(JComponent)} method will be invoked on it.
	 * 
	 * @return the new editor
	 */
	protected abstract E createEditor();

	/**
	 * Creates and returns a new viewer. Before being delivered to the user, the
	 * {@link #updateViewer(JComponent)} method will be invoked on it.
	 * 
	 * @return the new viewer
	 */
	protected abstract V createViewer();

	/**
	 * Gets the action.
	 * 
	 * @return the new action
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * Gets the current edit action.
	 * 
	 * @return the edit action
	 */
	public EditAction getEditAction() {
		return editAction;
	}

	/**
	 * Creates, sets up, and returns a new editor for this property.
	 * 
	 * @return the new editor
	 */
	public E getEditor() {
		E e = createEditor();
		updateEditor(e);
		editors.add(e);
		return e;
	}

	/**
	 * Creates, sets up, and returns a new viewer for this property.
	 * 
	 * @return the new viewer
	 */
	public V getViewer() {
		V v = createViewer();
		updateViewer(v);
		viewers.add(v);
		return v;
	}

	/**
	 * Determines whether the user may edit/action the property.
	 * 
	 * @return whether the property is enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets the action.
	 * 
	 * @param action
	 *            the new action
	 */
	public void setAction(Action action) {
		this.action = action;
		fireChangeEvent();
	}

	/**
	 * Sets the new edit action.
	 * 
	 * @param action
	 *            the new edit action
	 */
	public void setEditability(EditAction action) {
		this.editAction = action;
		fireChangeEvent();
	}

	/**
	 * Sets whether the user may edit/action the property.
	 * 
	 * @param enabled
	 *            the new enabled status
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		updateEditors();
		fireChangeEvent();
	}

	@Override
	public void setValue(T value) {
		this.value = value;
		updateEditors();
		updateViewers();
		fireChangeEvent();
	}

	/**
	 * Updates the given editor to match the new value of the property.
	 * 
	 * @param editor
	 *            the editor to update
	 */
	protected abstract void updateEditor(E editor);

	/**
	 * Updates all editors with the {@link #updateEditor(JComponent)} method.
	 */
	protected void updateEditors() {
		for (E editor : editors) {
			updateEditor(editor);
		}
	}

	/**
	 * Updates the given viewer to match the new value of the property.
	 * 
	 * @param viewer
	 *            the viewer to update
	 */
	protected abstract void updateViewer(V viewer);

	/**
	 * Updates all viewers with the {@link #updateViewer(JComponent)} method.
	 */
	protected void updateViewers() {
		for (V viewer : viewers) {
			updateViewer(viewer);
		}
	}

	private synchronized void writeObject(ObjectOutputStream oos)
			throws IOException {
		ArrayList<E> oldEditors = new ArrayList<E>(editors);
		ArrayList<V> oldViewers = new ArrayList<V>(viewers);
		editors.clear();
		viewers.clear();
		oos.defaultWriteObject();
		editors.addAll(oldEditors);
		viewers.addAll(oldViewers);
	}

	private synchronized void readObject(ObjectInputStream oos)
			throws IOException, ClassNotFoundException {
		oos.defaultReadObject();

		editors = new ArrayList<E>();
		viewers = new ArrayList<V>();
	}
}
