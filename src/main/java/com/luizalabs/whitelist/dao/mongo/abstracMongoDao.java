package com.luizalabs.whitelist.dao.mongo;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

@Repository
abstract public class  abstracMongoDao<T, ID> {

    @Autowired
    protected MongoOperations mongoOperations;

    public List<T> listAll(Class<T> entity, String collection) {
        Query query = new Query();
        return mongoOperations.find(query,entity, collection.toString());
    }

    public List<T> listAllPageable(Class<T> entity, Integer page, Integer pageSize, String collection) {

        Query query = new Query().with(PageRequest.of(page == null ?  0 : page, pageSize));
        return mongoOperations.find(query, entity, collection);
    }

    public T findById(String id, Class<T> entity, String collection) {
        ObjectId oid = new ObjectId(id);
        return findById(oid, entity, collection);
    }

    public T findById(Object id, Class<T> entity, String collection) {
        return mongoOperations.findById(id, entity, collection);
    }

    public T insert(T objectToSave, String collection) {
        return mongoOperations.insert(objectToSave, collection);
    }

    public UpdateResult updateById(Object id, UpdateDefinition update, String collection) {
        return mongoOperations.upsert(getQueryByKey(id), update, collection);
    }

    public DeleteResult deleteById(Object id, String collection) {
        return mongoOperations.remove(getQueryByKey(id), collection);
    }

    public long count(String collection) {
        return mongoOperations.count(new Query(), collection);
    }

    public List<T> listByRegex(Class<T> entity, String key, String regex, String collection) {
        Query query = new Query();
        query.addCriteria(Criteria.where(key).regex(regex, "i"));
        return mongoOperations.find(query, entity, collection);
    }

    protected Query getQueryByKey(Object id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));

        return query;
    }
}
