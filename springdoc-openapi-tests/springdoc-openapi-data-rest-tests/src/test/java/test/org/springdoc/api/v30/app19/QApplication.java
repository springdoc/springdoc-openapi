/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
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

package test.org.springdoc.api.v30.app19;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.StringPath;
import jakarta.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QApplication is a Querydsl query type for Application
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QApplication extends EntityPathBase<Application> {

	public static final QApplication application = new QApplication("application");

	private static final long serialVersionUID = 2120388982L;

	public final EnumPath<Application.AuditStatus> auditStatus = createEnum("auditStatus", Application.AuditStatus.class);

	public final DateTimePath<java.time.LocalDateTime> auditTime = createDateTime("auditTime", java.time.LocalDateTime.class);

	public final DateTimePath<java.time.LocalDateTime> createTime = createDateTime("createTime", java.time.LocalDateTime.class);

	public final StringPath description = createString("description");

	public final StringPath icon = createString("icon");

	public final StringPath id = createString("id");

	public final StringPath name = createString("name");

	public final StringPath publicKey = createString("publicKey");

	public final EnumPath<Application.AppType> type = createEnum("type", Application.AppType.class);

	public QApplication(String variable) {
		super(Application.class, forVariable(variable));
	}

	public QApplication(Path<? extends Application> path) {
		super(path.getType(), path.getMetadata());
	}

	public QApplication(PathMetadata metadata) {
		super(Application.class, metadata);
	}

}

