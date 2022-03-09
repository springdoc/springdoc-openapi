package org.springdoc.core.customizers;

/**
 * The interface Server Base URL customiser.
 * @author skylar-stark
 */
@FunctionalInterface
public interface ServerBaseUrlCustomizer {

	/**
	 * Customise.
	 * 
	 * @param serverBaseUrl the serverBaseUrl.
	 * @return the customised serverBaseUrl
	 */
	public String customise(String serverBaseUrl);
}
