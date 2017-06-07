package info.lefoll.jeff.polymer.workshop;

import io.vavr.collection.List;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.jooby.Jooby;
import org.jooby.json.Jackson;
import org.jooby.mongodb.JongoFactory;
import org.jooby.mongodb.Jongoby;
import org.jooby.mongodb.Mongodb;


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

                    return List.ofAll(result);
                })
                .get("/:id", req -> {
                    Jongo jongo = require(JongoFactory.class).get("blog-db");
                    MongoCollection blogPosts = jongo.getCollection("blogposts");

                    String query = String.format("{name: '%s'}", req.param("id").toString());
                    BlogPost result = blogPosts.findOne(query).as(BlogPost.class);

                    return result;
                })
                .post(req -> {
                    Jongo jongo = require(JongoFactory.class).get("blog-db");
                    MongoCollection blogPosts = jongo.getCollection("blogposts");

                    BlogPost blogPost = req.body(BlogPost.class);

                    blogPosts.save(blogPost);

                    return blogPost;
                });
    }

    public static void main(final String[] args) {
        run(App::new, args);
    }
}
