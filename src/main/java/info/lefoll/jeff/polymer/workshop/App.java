package info.lefoll.jeff.polymer.workshop;


import com.mongodb.WriteResult;
import io.vavr.collection.List;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.jooby.Jooby;
import org.jooby.Results;
import org.jooby.json.Jackson;
import org.jooby.mongodb.JongoFactory;
import org.jooby.mongodb.Jongoby;
import org.jooby.mongodb.Mongodb;

import static org.jongo.Oid.withOid;


public class App extends Jooby {

    {
        use(new Jackson());
        use(new Mongodb());
        use(new Jongoby());

        use("/api/blogpost")
                .get("/", req -> {
                    Jongo jongo = require(JongoFactory.class).get("blog-db");
                    MongoCollection blogPosts = jongo.getCollection("blogposts");

                    MongoCursor<BlogPost> result = blogPosts.find().as(BlogPost.class);

                    return List.ofAll(() -> result.iterator()).asJava();
                })
                .get("/:id", req -> {
                    Jongo jongo = require(JongoFactory.class).get("blog-db");
                    MongoCollection blogPosts = jongo.getCollection("blogposts");

                    ObjectId query = new ObjectId(req.param("id").value());
                    BlogPost result = blogPosts.findOne(query).as(BlogPost.class);

                    return result;
                })
                .post(req -> {
                    Jongo jongo = require(JongoFactory.class).get("blog-db");
                    MongoCollection blogPosts = jongo.getCollection("blogposts");

                    BlogPost blogPost = req.body(BlogPost.class);

                    blogPosts.save(blogPost);

                    return blogPost;
                })
                .put(req -> {
                    Jongo jongo = require(JongoFactory.class).get("blog-db");
                    MongoCollection blogPosts = jongo.getCollection("blogposts");

                    BlogPost blogPost = req.body(BlogPost.class);

                    blogPosts.update(withOid(blogPost.get_id())).with(blogPost);

                    return blogPost;
                })
                .delete("/:id", req -> {
                    Jongo jongo = require(JongoFactory.class).get("blog-db");
                    MongoCollection blogPosts = jongo.getCollection("blogposts");

                    ObjectId query = new ObjectId(req.param("id").value());
                    blogPosts.remove(query);

                    return Results.ok();
                });
    }

    public static void main(final String[] args) {
        run(App::new, args);
    }
}
