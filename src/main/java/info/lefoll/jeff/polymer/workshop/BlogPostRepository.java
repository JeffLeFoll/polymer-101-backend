package info.lefoll.jeff.polymer.workshop;

import io.vavr.collection.List;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.jooby.mongodb.JongoFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

import static org.jongo.Oid.withOid;

@Singleton
public class BlogPostRepository {

    private Jongo jongo;

    @Inject
    public BlogPostRepository(JongoFactory jongoFactory) {
        jongo = jongoFactory.get("blog-db");
    }

    private MongoCollection getCollection() {
        return  jongo.getCollection("blogposts");
    }

    List<BlogPost> findAll() {
        MongoCursor<BlogPost> result = getCollection().find().as(BlogPost.class);

        return List.ofAll(() -> result.iterator());
    }


    BlogPost findById(String id) {
        ObjectId query = new ObjectId(id);

        return getCollection().findOne(query).as(BlogPost.class);
    }

    BlogPost create(BlogPost entity) {
        getCollection().save(entity);

        return entity;
    }

    BlogPost update(BlogPost entity) {

        getCollection().update(withOid(entity.get_id())).with(entity);

        return entity;
    }

    void removeById(String id) {
        ObjectId query = new ObjectId(id);

        getCollection().remove(query);
    }
}
