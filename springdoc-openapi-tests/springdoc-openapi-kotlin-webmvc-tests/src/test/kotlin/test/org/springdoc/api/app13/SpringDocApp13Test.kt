/*
 *
 *  * Copyright 2019-2020 the original author or authors.
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

package test.org.springdoc.api.app13

import org.springdoc.core.properties.SpringDocConfigProperties
import org.springdoc.core.properties.SpringDocConfigProperties.ApiDocs.OpenApiVersion
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import test.org.springdoc.api.AbstractKotlinSpringDocMVCTest


@SpringBootTest//(classes = [Config::class])
class SpringDocApp13Test : AbstractKotlinSpringDocMVCTest() {


    @Configuration
    class Config{
        @Bean
        fun springDocConfigProperties():SpringDocConfigProperties{
            val x= SpringDocConfigProperties()
            x.apiDocs.version = OpenApiVersion.OPENAPI_3_1
            return x
        }

    }


    @SpringBootApplication
    class DemoApplication

}
