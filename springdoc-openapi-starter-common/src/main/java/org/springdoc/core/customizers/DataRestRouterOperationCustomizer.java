package org.springdoc.core.customizers;

import org.springdoc.core.fn.RouterOperation;

/**
 * The interface Data rest router operation customizer.
 *
 * @author bnasslahsen
 */
@FunctionalInterface
public interface DataRestRouterOperationCustomizer {

	/**
	 * Customize router operation.
	 *
	 * @param routerOperation input operation
	 * @return customized router operation
	 */
	RouterOperation customize(RouterOperation routerOperation);

}
