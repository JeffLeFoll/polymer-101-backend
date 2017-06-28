package info.lefoll.jeff.polymer.workshop.comment;


import lombok.Data;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import java.time.LocalDateTime;

@Data
public class Comment {

    @MongoObjectId
    private String _id;

    private String blogId;
    private String message;
    private LocalDateTime creationDate;
    private String autor;
}
