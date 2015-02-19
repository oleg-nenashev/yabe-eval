package models;

import play.db.ebean.Model;

import java.util.*;
import javax.persistence.*;


@Entity
public class User extends Model {

    @Id
    public String email;
    public String password;
    public String fullname;
    public boolean isAdmin;

    public static Model.Finder<String,User> FINDER = new Model.Finder<String, User>(String.class, User.class);

    public User(String email, String password, String fullname) {
        this.email = email;
        this.password = password;
        this.fullname = fullname;
    }

    public static User connect(String email, String password) {
        return FINDER.where().eq("email",email).eq("password",password).findUnique();
    }

}