package sample.com.hazelcast.cloud.maploader5.mongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import com.hazelcast.core.HazelcastJsonValue;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.ReplaceOptions;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;


@Slf4j
@AllArgsConstructor
// tag::class[]
public class MongoPersonRepository implements PersonRepository {

    private final String name;

    private final MongoDatabase db;

    @Override
    public void deleteAll() {
        MongoCollection<Document> collection = db.getCollection(this.name);
        collection.deleteMany(new Document());
    }

    @Override
    public void delete(Collection<Integer> ids) {
        MongoCollection<Document> collection = db.getCollection(this.name);
        collection.deleteMany(Filters.in("id", ids));
    }

    @Override
    public List<HazelcastJsonValue> findAll(Collection<Integer> ids) {
        List<HazelcastJsonValue> persons = new ArrayList<>();
        MongoCollection<Document> collection = db.getCollection(this.name);
        try (MongoCursor<Document> cursor = collection.find(Filters.in("id", ids)).iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                persons.add(new HazelcastJsonValue(com.mongodb.util.JSON.serialize(document)));
            }
        }
        return persons;
    }

    @Override
    public Collection<Integer> findAllIds() {
        Set<Integer> ids = new LinkedHashSet<>();
        MongoCollection<Document> collection = db.getCollection(this.name);
        try (MongoCursor<Document> cursor = collection.find().projection(Projections.include("id")).iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                ids.add(document.get("id", Integer.class));
            }
        }
        return ids;
    }

}
// end::class[]
