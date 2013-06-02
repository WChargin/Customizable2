package tools.customizable;

import java.util.ArrayList;
import java.util.Collection;

public class PropertySet extends ArrayList<AbstractProperty<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates an empty property set.
	 */
	public PropertySet() {
		super();
	}

	/**Creates a property set with the given contents.
	 * @param properties the contents
	 */
	public PropertySet(Collection<? extends AbstractProperty<?>> properties) {
		super(properties);
	}

	/**Creates a property set with the given property.
	 * @param property the property
	 */
	public PropertySet(AbstractProperty<?> property) {
		this();
		add(property);
	}

	
}
