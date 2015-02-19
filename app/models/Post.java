package models;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.Model;

@Entity
public class Post extends Model {

    public static Model.Finder<String,Post> FINDER = new Model.Finder<String, Post>(String.class, Post.class);

    @Id
    public String title;
    public Date postedAt;

    @OneToMany(mappedBy="post", cascade=CascadeType.ALL)
    public List<Comment> comments;

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
        this.comments = new ArrayList<>();
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public Post addComment(String author, String content) {
        Comment newComment = new Comment(this, author, content);
        newComment.save();
        this.comments.add(newComment);
        this.save();
        return this;
    }



}