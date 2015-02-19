import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import models.Comment;
import models.Post;
import models.User;
import org.junit.*;
import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import static org.junit.Assert.*;
import play.db.ebean.Model;

/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ApplicationTest extends YABETestBase {

    @Test
    public void createAndRetrieveUser() {
        // Create a new user and save it
        new User("bob@gmail.com", "secret", "Bob").save();

        // Retrieve the user with e-mail address bob@gmail.com
        User bob = User.FINDER.where().eq("email","bob@gmail.com").findUnique();

        // Test
        assertNotNull(bob);
        assertEquals("Bob", bob.fullname);
    }

    @Test
    public void tryConnectAsUser() {
        // Create a new user and save it
        new User("bob@gmail.com", "secret", "Bob").save();

        // Test
        assertNotNull(User.connect("bob@gmail.com", "secret"));
        assertNull(User.connect("bob@gmail.com", "badpassword"));
        assertNull(User.connect("tom@gmail.com", "secret"));
    }

    @Test
    public void createPost() {
        // Create a new user and save it
        User bob = new User("bob@gmail.com", "secret", "Bob");
        bob.save();

        // Create a new post
        new Post(bob, "My first post", "Hello world").save();

        // Test that the post has been created
        assertEquals(1, Post.FINDER.findRowCount());

        // Retrieve all posts created by Bob
        List<Post> bobPosts = Post.FINDER.where().eq("author", bob).findList();

        // Tests
        assertEquals(1, bobPosts.size());
        Post firstPost = bobPosts.get(0);
        assertNotNull(firstPost);
        assertEquals(bob, firstPost.author);
        assertEquals("My first post", firstPost.title);
        assertEquals("Hello world", firstPost.getContent());
        assertNotNull(firstPost.postedAt);
    }

    @Test
    public void postComments() {
        // Create a new user and save it
        User bob = new User("bob@gmail.com", "secret", "Bob");
        bob.save();

        // Create a new post
        Post bobPost = new Post(bob, "My first post", "Hello world");
        bobPost.save();

        // Post a first comment
        new Comment(bobPost, "Jeff", "Nice post").save();
        new Comment(bobPost, "Tom", "I knew that !").save();

        // Retrieve all comments
        List<Comment> bobPostComments = Comment.FINDER.where().eq("post", bobPost).findList();

        // Tests
        assertEquals(2, bobPostComments.size());

        Comment firstComment = bobPostComments.get(0);
        assertNotNull(firstComment);
        assertEquals("Jeff", firstComment.author);
        assertEquals("Nice post", firstComment.content);
        assertNotNull(firstComment.postedAt);

        Comment secondComment = bobPostComments.get(1);
        assertNotNull(secondComment);
        assertEquals("Tom", secondComment.author);
        assertEquals("I knew that !", secondComment.content);
        assertNotNull(secondComment.postedAt);
    }

    @Test
    public void useTheCommentsRelation() {
        // Create a new user and save it
        User bob = new User("bob@gmail.com", "secret", "Bob");
        bob.save();

        // Create a new post
        Post bobPost = new Post(bob, "My first post", "Hello world");
        bobPost.save();

        // Post a first comment
        bobPost.addComment("Jeff", "Nice post");
        bobPost.addComment("Tom", "I knew that !");

        // Count things
        assertEquals(1, User.FINDER.findRowCount());
        assertEquals(1, Post.FINDER.findRowCount());
        assertEquals(2, Comment.FINDER.findRowCount());

        // Retrieve Bob's post
        bobPost = Post.FINDER.where().eq("author", bob).findUnique();
        assertNotNull(bobPost);

        // Navigate to comments
        assertEquals(2, bobPost.comments.size());
        assertEquals("Jeff", bobPost.comments.get(0).author);

        // Delete the post
        bobPost.delete();

        // Check that all comments have been deleted
        assertEquals(1, User.FINDER.findRowCount());
        assertEquals(0, Post.FINDER.findRowCount());
        assertEquals(0, Comment.FINDER.findRowCount());
    }

    @Test
    public void renderTemplate() {
        Content html = views.html.index.render("Your new application is ready.");
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("Your new application is ready.");
    }


}
