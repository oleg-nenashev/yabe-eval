package models;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.Model;

@Entity
public class Comment extends Model {

    public String author;
    public Date postedAt;

    public static Model.Finder<String,Comment> FINDER = new Model.Finder<String, Comment>(String.class, Comment.class);

    @Lob
    @Basic(fetch = FetchType.EAGER)
    public String content;

    @ManyToOne
    public Post post;

    public Comment(Post post, String author, String content) {
        this.post = post;
        this.author = author;
        this.content = content;
        this.postedAt = new Date();
    }

}