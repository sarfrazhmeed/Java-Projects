/*import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private Map<String, User> users = new HashMap<>();

    public boolean registerUser(String username, String password, User.Role role) {
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, new User(username, password, role));
        return true;
    }

    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    public void printAllUsers() {
        users.values().forEach(System.out::println);
    }
}
*/