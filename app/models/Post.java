package models;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.Model;

@Entity
public class Post extends Model {

    public static Model.Finder<String,Post> FINDER = new Model.Finder<String, Post>(String.class, Post.class);

    public String title;
    public Date postedAt;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    public String content;

    @ManyToOne
    public User author;

    public Post(User author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.postedAt = new Date();
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}