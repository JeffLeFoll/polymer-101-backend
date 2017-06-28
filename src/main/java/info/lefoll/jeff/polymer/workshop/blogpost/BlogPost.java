package info.lefoll.jeff.polymer.workshop.blogpost;

import lombok.Data;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import java.time.LocalDateTime;


@Data
public class BlogPost {

    @MongoObjectId
    private String _id;

    private String title;
    private String article;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private String autor;
}
