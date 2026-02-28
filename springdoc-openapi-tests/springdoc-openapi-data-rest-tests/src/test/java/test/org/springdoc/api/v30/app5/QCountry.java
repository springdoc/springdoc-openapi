/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v30.app5;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import jakarta.annotation.Generated;
import test.org.springdoc.api.v30.app5.Country.Status;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QCountry is a Querydsl query type for Country
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCountry extends EntityPathBase<Country> {

	public static final QCountry country = new QCountry("country");

	private static final long serialVersionUID = -1184258693L;

	public final StringPath codeISO3166 = createString("codeISO3166");

	public final StringPath dialingCode = createString("dialingCode");

	public final NumberPath<Long> id = createNumber("id", Long.class);

	public final StringPath name = createString("name");

	public final StringPath shortName = createString("shortName");

	public final EnumPath<Status> status = createEnum("status", Status.class);

	public QCountry(String variable) {
		super(Country.class, forVariable(variable));
	}

	public QCountry(Path<? extends Country> path) {
		super(path.getType(), path.getMetadata());
	}

	public QCountry(PathMetadata metadata) {
		super(Country.class, metadata);
	}

}

