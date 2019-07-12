package org.springdoc.core;

import java.util.Iterator;
import java.util.List;

public class OpenAPIExtensions {

	private static List<OpenAPIExtension> extensions = null;

	public static List<OpenAPIExtension> getExtensions() {
		return extensions;
	}

	public static void setExtensions(List<OpenAPIExtension> ext) {
		extensions = ext;
	}
	public static Iterator<OpenAPIExtension> chain() {
		return extensions.iterator();
	}


}
