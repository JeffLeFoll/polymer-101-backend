package info.lefoll.jeff.polymer.workshop;


import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.vavr.collection.List;
import org.jooby.Jooby;
import org.jooby.Results;
import org.jooby.handlers.CorsHandler;
import org.jooby.json.Jackson;
import org.jooby.mongodb.Jongoby;
import org.jooby.mongodb.Mongodb;

import java.time.LocalDateTime;


public class App extends Jooby {

    {
        use(new Jackson().doWith(mapper ->
            mapper.registerModule(new ParameterNamesModule())
                    .registerModule(new Jdk8Module())
                    .registerModule(new JavaTimeModule())
        ));
        use(new Mongodb());

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
                    blogPost.setDateCreation(LocalDateTime.now());

                    return require(BlogPostRepository.class).create(blogPost);
                })

                .put(req -> {
                    BlogPost blogPost = req.body(BlogPost.class);
                    blogPost.setDateMaj(LocalDateTime.now());

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
