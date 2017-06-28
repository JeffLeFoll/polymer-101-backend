package info.lefoll.jeff.polymer.workshop;


import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import info.lefoll.jeff.polymer.workshop.blogpost.BlogPost;
import info.lefoll.jeff.polymer.workshop.blogpost.BlogPostRepository;
import info.lefoll.jeff.polymer.workshop.comment.Comment;
import info.lefoll.jeff.polymer.workshop.comment.CommentsRepository;
import io.vavr.collection.List;
import org.jooby.Jooby;
import org.jooby.Results;
import org.jooby.handlers.CorsHandler;
import org.jooby.json.Jackson;
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

                .get("/:id", req -> {
                    return require(BlogPostRepository.class).findById(req.param("id").value());
                })

                .delete("/:id", req -> {
                    require(BlogPostRepository.class).removeById(req.param("id").value());

                    return Results.ok();
                })

                .get("/auteur/:name", req -> {
                    List<BlogPost> result = require(BlogPostRepository.class).find("auteur", req.param("name").value());

                    return result.asJava();
                })
                .produces("json")
                .consumes("json");

        use("/api/blogpost/:blogId/comments")
                .get("/", req -> {
                    List<Comment> result = require(CommentsRepository.class).findAll(req.param("blogId").value());

                    return result.asJava();
                })

                .post(req -> {
                    Comment comment = req.body(Comment.class);
                    comment.setBlogId(req.param("blogId").value());
                    comment.setDateCreation(LocalDateTime.now());

                    return require(CommentsRepository.class).create(comment);
                })

                .get("/:id", req -> {
                    return require(CommentsRepository.class).findById(req.param("id").value());
                })

                .delete("/:id", req -> {
                    require(CommentsRepository.class).removeById(req.param("id").value());

                    return Results.ok();
                })
                .produces("json")
                .consumes("json");

    }

    public static void main(final String[] args) {
        run(App::new, args);
    }
}
