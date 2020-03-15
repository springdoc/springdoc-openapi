package test.org.springdoc.api.app100;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Mar, 2020
 **/
public class QChildEntity extends EntityPathBase<ChildEntity> {

    public static final QChildEntity childEntity = new QChildEntity("childEntity");
    private static final long serialVersionUID = -1184258693L;
    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QChildEntity(String variable) {
        super(ChildEntity.class, forVariable(variable));
    }

    public QChildEntity(Path<? extends ChildEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChildEntity(PathMetadata metadata) {
        super(ChildEntity.class, metadata);
    }
}
