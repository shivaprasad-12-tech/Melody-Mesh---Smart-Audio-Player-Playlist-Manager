package MelodyMesh;

import java.sql.*;

public class UserDAO {
    public boolean register(User user) {

        String sql="INSERT INTO users(username, password) " + "VALUES(?, ?)";

        try(Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.executeUpdate();
            return true;
        } catch(SQLIntegrityConstraintViolationException e) {
            System.out.println("Username already exists.");
        } catch(SQLException e) {
            System.out.println("Registration error: " + e.getMessage());
        }
        return false;
    }

    public User login(
            String username,
            String password) {

        String sql="SELECT * FROM users " + "WHERE username=? AND password=?";

        try(Connection con=DBConnection.getConnection();

            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs=ps.executeQuery();

            if(rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"));
            }
        } catch(SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return null;
    }
}
