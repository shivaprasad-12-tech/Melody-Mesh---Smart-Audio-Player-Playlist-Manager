package MelodyMesh;

import java.sql.*;

public class FavoriteDAO {
    public boolean addFavorite(int userId,int songId) {
        String sql="INSERT INTO favorites" + "(user_id, song_id)" + " VALUES(?,?)";
        try(Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, songId);
            ps.executeUpdate();
            return true;
        } catch(SQLIntegrityConstraintViolationException e) {
            System.out.println("Song is already in favorites.");
        } catch(SQLException e) {
            System.out.println("Favorite error: " + e.getMessage());
        }
        return false;
    }

    public boolean removeFavorite(int userId, int songId) {
        String sql="DELETE FROM favorites " + "WHERE user_id=? " + "AND song_id=?";
        try(Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, songId);
            return ps.executeUpdate() > 0;
        } catch(SQLException e) {
            System.out.println("Favorite error: " + e.getMessage());
        }
        return false;
    }


    public void displayFavorites(int userId) {
        String sql="SELECT s.* " + "FROM songs s " + "JOIN favorites f " + "ON s.song_id=f.song_id " + "WHERE f.user_id=? " + "ORDER BY f.added_at DESC";
        try(Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs=ps.executeQuery();
            System.out.println("\n========== FAVORITES ==========");
            boolean found=false;
            while(rs.next()) {
                found=true;
                System.out.println(rs.getInt("song_id") + " - " + rs.getString("title") + " - " + rs.getString("artist"));
            }
            if(!found) {
                System.out.println("No favorite songs.");
            }
        } catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
