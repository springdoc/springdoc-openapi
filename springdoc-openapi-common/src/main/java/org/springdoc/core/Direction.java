package org.springdoc.core;

/**
 * @author bnasslahsen
 */

/**
 * The enum Direction.
 * @author bnasslahsen
 */
public enum Direction {
	/**
	 *Asc direction.
	 */
	ASC,
	/**
	 *Desc direction.
	 */
	DESC;

	/**
	 * Is ascending boolean.
	 *
	 * @return the boolean
	 */
	public boolean isAscending() {
		return this.equals(ASC);
	}
}
