package info.lefoll.jeff.polymer.workshop.blogpost;

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

import static org.jongo.Oid.withOid;

@Singleton
public class BlogPostRepository {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private Jongo jongo;

    @Inject
    public BlogPostRepository(@Named("dbName") String dbName, MongoClient mongoClient) {
        DB mongoDb = mongoClient.getDB(dbName);

        jongo = new Jongo(mongoDb,
                new JacksonMapper.Builder()
                        .registerModule(new Jdk8Module())
                        .registerModule(new JavaTimeModule())
                        .build());
    }

    private MongoCollection getCollection() {
        return  jongo.getCollection("blogposts");
    }

    public List<BlogPost> findAll() {
        MongoCursor<BlogPost> result = getCollection().find().as(BlogPost.class);

        return List.ofAll(result);
    }


    public BlogPost findById(String id) {
        ObjectId query = new ObjectId(id);

        return getCollection().findOne(query).as(BlogPost.class);
    }

    public BlogPost create(BlogPost entity) {
        getCollection().save(entity);

        return entity;
    }

    public BlogPost update(BlogPost entity) {

        getCollection().update(withOid(entity.get_id())).with(entity);

        return entity;
    }

    public void removeById(String id) {
        ObjectId query = new ObjectId(id);

        getCollection().remove(query);
    }

    public void removeAll(){
        getCollection().drop();
    }

    public List<BlogPost> find(String field, String param) {
        MongoCursor<BlogPost> result = getCollection().find("{" + field +": #}", param).as(BlogPost.class);

        return List.ofAll(result);
    }
}
