package tools.customizable;

import java.io.Serializable;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * A property with a name and a value.
 * 
 * @author William Chargin
 * 
 * @param <T>
 *            the type of the value (e.g., a property storing text would
 *            subclass {@code Property<String>})
 */
public abstract class AbstractProperty<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The description of this property. This should be displayed in a tooltip.
	 */
	protected String description;

	/**
	 * The name of this property.
	 */
	protected String name;

	/**
	 * The current value of this property.
	 */
	protected T value;

	/**
	 * The list of listeners on this property.
	 */
	private transient EventListenerList listenerList = new EventListenerList();

	/**
	 * Creates the property with a blank name (with {@link String#String()}) and
	 * a {@code null} value
	 */
	public AbstractProperty() {
		this(new String(), null);
	}

	/**
	 * Creates the property with the given name and value.
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public AbstractProperty(String name, T value) {
		super();
		this.name = name;
		this.value = value;
	}

	/**
	 * Adds the given listener to the list of listeners. The listener's
	 * {@link ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 * stateChanged} method will be called after the key, value, or editability
	 * changes.
	 * 
	 * @param cl
	 *            the listener to add
	 */
	public void addChangeListener(ChangeListener cl) {
		if (listenerList == null) {
			listenerList = new EventListenerList();
		}
		listenerList.add(ChangeListener.class, cl);
	}

	/**
	 * Fires {@link ChangeEvents} to all registered listeners.
	 */
	protected void fireChangeEvent() {
		if (listenerList == null) {
			listenerList = new EventListenerList();
		}
		if (listenerList.getListenerCount() > 0) {
			for (ChangeListener cl : listenerList
					.getListeners(ChangeListener.class)) {
				cl.stateChanged(new ChangeEvent(this));
			}
		}
	}

	/**
	 * Gets the name of this property.
	 * 
	 * @return the name of this property
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the value of this property.
	 * 
	 * @return the current value
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Removes the given listener from the list of listeners. The listener's
	 * {@link ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 * stateChanged} method will no longer be called when the key, value, or
	 * editability changes.
	 * 
	 * @param cl
	 *            the listener to remove
	 */
	public void removeChangeListener(ChangeListener cl) {
		if (listenerList == null) {
			listenerList = new EventListenerList();
		}
		listenerList.remove(ChangeListener.class, cl);
	}

	/**
	 * Sets the name of this property.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
		fireChangeEvent();
	}

	/**
	 * Sets the value of this property.
	 * 
	 * @param value
	 *            the new value
	 */
	public void setValue(T value) {
		this.value = value;
		fireChangeEvent();
	}

	/**
	 * Gets the description of this property.
	 * 
	 * @return the current description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of this property.
	 * 
	 * @param description
	 *            the new description
	 */
	public void setDescription(String description) {
		this.description = description;
		fireChangeEvent();
	}
}
