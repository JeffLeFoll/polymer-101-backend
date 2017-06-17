package info.lefoll.jeff.polymer.workshop;

import lombok.Data;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import java.time.Instant;


@Data
public class BlogPost {

    @MongoObjectId
    private String _id;

    private String titre;
    private String message;
    private Instant dateCreation;
    private Instant dateMaj;

    public BlogPost() {}
}
