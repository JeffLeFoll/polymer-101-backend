package info.lefoll.jeff.polymer.workshop.comment;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import io.vavr.collection.List;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.jongo.marshall.jackson.JacksonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class CommentRepository {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private Jongo jongo;

    @Inject
    public CommentRepository(@Named("dbName") String dbName, MongoClient mongoClient) {
        DB mongoDb = mongoClient.getDB(dbName);

        jongo = new Jongo(mongoDb,
                new JacksonMapper.Builder()
                        .registerModule(new Jdk8Module())
                        .registerModule(new JavaTimeModule())
                        .build());
    }

    private MongoCollection getCollection() {
        return  jongo.getCollection("comments");
    }

    public List<Comment> findAll(String blogId) {
        MongoCursor<Comment> result = getCollection().find("{blogId: #}", blogId).as(Comment.class);

        return List.ofAll(result::iterator);
    }

    public Comment findById(String id) {
        ObjectId query = new ObjectId(id);

        return getCollection().findOne(query).as(Comment.class);
    }

    public Comment create(Comment entity) {
        getCollection().save(entity);

        return entity;
    }

    public void removeById(String id) {
        ObjectId query = new ObjectId(id);

        getCollection().remove(query);
    }
}
