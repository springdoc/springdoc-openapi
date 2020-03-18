package test.org.springdoc.api.app5;

import javax.annotation.Generated;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import test.org.springdoc.api.app5.Country.Status;

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

