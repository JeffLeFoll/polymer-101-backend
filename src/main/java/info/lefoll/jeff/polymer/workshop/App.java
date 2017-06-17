package info.lefoll.jeff.polymer.workshop;


import io.vavr.collection.List;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.jooby.Jooby;
import org.jooby.Results;
import org.jooby.handlers.CorsHandler;
import org.jooby.json.Jackson;
import org.jooby.mongodb.JongoFactory;
import org.jooby.mongodb.Jongoby;
import org.jooby.mongodb.Mongodb;

import java.time.Instant;

import static org.jongo.Oid.withOid;


public class App extends Jooby {

    {

        on("dev", () -> {
            System.out.println("### DEV MODE ###");
        });

        on("prod", () -> {
            System.out.println("### PROD MODE ###");
        });


        use(new Jackson());
        use(new Mongodb());
        use(new Jongoby());

        use("*", new CorsHandler());

        get("/", () -> "I'm alive !!");

        use("/api/blogpost")
                .get("/", req -> {
                    List<BlogPost> result = require(BlogPostRepository.class).findAll();

                    return result.asJava();
                })

                .get("/:id", req -> {
                    return require(BlogPostRepository.class).findById(req.param("id").value());
                })

                .post(req -> {
                    BlogPost blogPost = req.body(BlogPost.class);
                    blogPost.setDateCreation(Instant.now());

                    return require(BlogPostRepository.class).create(blogPost);
                })

                .put(req -> {
                    BlogPost blogPost = req.body(BlogPost.class);
                    blogPost.setDateMaj(Instant.now());

                    return require(BlogPostRepository.class).update(blogPost);
                })

                .delete("/:id", req -> {
                    require(BlogPostRepository.class).removeById(req.param("id").value());

                    return Results.ok();
                });
    }

    public static void main(final String[] args) {
        run(App::new, args);
    }
}
