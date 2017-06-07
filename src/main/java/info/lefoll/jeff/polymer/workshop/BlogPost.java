package info.lefoll.jeff.polymer.workshop;

import lombok.Data;
import org.jongo.marshall.jackson.oid.MongoObjectId;


@Data
public class BlogPost {

    @MongoObjectId
    private String _id;

    private String titre;
    private String message;


    public BlogPost() {}
}
