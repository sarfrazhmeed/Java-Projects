public class User {
    private String username;
    private String password;


    public enum Role {
        SENDER,
        RECEIVER
    }
    private Role role;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public void setRole(Role newRole) {
        this.role = newRole;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", role=" + role +
                '}';
    }
}
