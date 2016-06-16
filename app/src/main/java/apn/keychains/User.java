package apn.keychains;
import java.util.*;
/**
 * Created by kporika on 6/10/2016.
 */
public class User {

    public String name;

    public User(String name) {
        this.name = name;
    }
    public static ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<User>();
        users.add(new User("Harry"));
        users.add(new User("Marla"));
        users.add(new User("Sarah"));
        return users;
    }
}