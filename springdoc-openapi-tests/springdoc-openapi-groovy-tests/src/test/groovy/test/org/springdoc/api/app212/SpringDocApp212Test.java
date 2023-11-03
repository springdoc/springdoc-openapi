/*
 *
 *  * Copyright 2019-2023 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package test.org.springdoc.api.app212;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ActiveProfiles;
import test.org.springdoc.api.AbstractSpringDocTest;

/**
 * The type Spring doc app 192 test.
 * <p>
 * A test for {@link org.springdoc.core.customizers.SpecificationStringPropertiesCustomizer}
 */
@ActiveProfiles("212")
public class SpringDocApp212Test extends AbstractSpringDocTest {

    /**
     * The type Spring doc test app.
     */
    @SpringBootApplication
    static class SpringDocTestApp {
    }

}
