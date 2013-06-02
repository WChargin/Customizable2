package tools.customizable;


import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * An amount of time.
 * 
 * @author William Chargin
 * 
 */
public class Time implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The number of hours in this {@code Time} object.
	 */
	public final int hours;

	/**
	 * The number of minutes in this {@code Time} object.
	 */
	public final int minutes;

	/**
	 * The number of seconds in this {@code Time} object.
	 */
	public final int seconds;

	/**
	 * Gets the total number of seconds in this {@code Time} object (e.g., a
	 * {@code Time} object with 1 hour, 2 minutes, and 30 seconds would return
	 * {@code 3750}).
	 * 
	 * @return the total number of seconds
	 */
	public int getTotalSeconds() {
		return seconds + (minutes * 60) + (hours * 60 * 60);
	}

	/**
	 * Creates a new {@code Time} object, given the total number of seconds
	 * elapsed. This is the inverse function of {@link #getTotalSeconds()}.
	 * 
	 * @param totalSeconds
	 *            the total number of seconds
	 * @return a {@code Time} object whose {@link #getTotalSeconds()} returns
	 *         the same number as was passed to this function
	 */
	public static Time fromSeconds(int totalSeconds) {
		int seconds = totalSeconds % 60;
		int totalMinutes = (totalSeconds - seconds) / 60;
		int minutes = totalMinutes % 60;
		int hours = totalMinutes / 60;
		return new Time(hours, minutes, seconds);
	}

	/**
	 * Creates the {@code Time} object with all required information.
	 * 
	 * @param hours
	 *            the number of hours
	 * @param minutes
	 *            the number of minutes
	 * @param seconds
	 *            the number of seconds
	 */
	public Time(int hours, int minutes, int seconds) {
		super();
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}

	@Override
	public String toString() {
		DecimalFormat twoPlaces = new DecimalFormat("00"); //$NON-NLS-1$
		return (hours < 10 ? "0" : "") + (hours) + ":" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ twoPlaces.format(minutes) + ":" + twoPlaces.format(seconds); //$NON-NLS-1$
	}

	@Override
	public int hashCode() {
		return getTotalSeconds();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Time)) {
			return false;
		}
		Time other = (Time) obj;
		if (hours != other.hours) {
			return false;
		}
		if (minutes != other.minutes) {
			return false;
		}
		if (seconds != other.seconds) {
			return false;
		}
		return true;
	}

}
