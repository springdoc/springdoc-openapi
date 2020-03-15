package test.org.springdoc.api.app100;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.StringPath;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Mar, 2020
 **/
public class QDummyEntity extends EntityPathBase<DummyEntity> {

    public static final QDummyEntity dummyEntity = new QDummyEntity("dummyEntity");
    private static final long serialVersionUID = -1184258693L;
    public final StringPath code = createString("code");
    public final EnumPath<SpringDocApp100Test.Status> status = createEnum("status", SpringDocApp100Test.Status.class);

    public final StringPath name = createString("name");

    public final StringPath shortName = createString("shortName");

    public QDummyEntity(String variable) {
        super(DummyEntity.class, forVariable(variable));
    }

    public QDummyEntity(Path<? extends DummyEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDummyEntity(PathMetadata metadata) {
        super(DummyEntity.class, metadata);
    }
}
