package org.springdoc.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.util.Assert;

import java.util.*;

@NoRepositoryBean
public abstract class HashMapRepository<T, ID> implements CrudRepository<T, ID> {

    Map<ID, T> entities = new HashMap<>();

    abstract <S extends T> ID getEntityId(S entity);

    @Override
    public <S extends T> S save(S entity) {
        Assert.notNull(entity, "entity cannot be null");
        Assert.notNull(getEntityId(entity), "entity ID cannot be null");
        entities.put(getEntityId(entity), entity);
        return entity;
    }

    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        Assert.notNull(entities, "entities cannot be null");
        List<S> result = new ArrayList<>();
        entities.forEach(entity -> result.add(save(entity)));
        return result;
    }

    @Override
    public Collection<T> findAll() {
        return entities.values();
    }

    @Override
    public long count() {
        return entities.keySet().size();
    }

    @Override
    public void delete(T entity) {
        Assert.notNull(entity, "entity cannot be null");
        deleteById(getEntityId(entity));
    }

    @Override
    public void deleteAll(Iterable<? extends T> entitiesToDelete) {
        Assert.notNull(entitiesToDelete, "entities cannot be null");
        entitiesToDelete.forEach(entity -> entities.remove(getEntityId(entity)));
    }

    @Override
    public void deleteAll() {
        entities.clear();
    }

    @Override
    public void deleteById(ID id) {
        Assert.notNull(id, "Id cannot be null");
        entities.remove(id);
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        Assert.notNull(ids, "Ids cannot be null");
        List<T> result = new ArrayList<>();
        ids.forEach(id -> findById(id).ifPresent(result::add));
        return result;
    }

    @Override
    public boolean existsById(ID id) {
        Assert.notNull(id, "Id cannot be null");
        return entities.keySet().contains(id);
    }

    public T findOne(ID id) {
        Assert.notNull(id, "Id cannot be null");
        return entities.get(id);
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(findOne(id));
    }
}
