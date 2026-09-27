package MelodyMesh;

import java.sql.*;

public class RecentDAO {
    public void addRecentlyPlayed(int userId,int songId) {
        String sql="INSERT INTO recently_played" + "(user_id, song_id)" + " VALUES(?,?)";
        try(Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(1,userId);
            ps.setInt(2, songId);
            ps.executeUpdate();
        } catch(SQLException e) {
            System.out.println("History error: " + e.getMessage());
        }
    }

    public void displayRecentlyPlayed(int userId) {
        String sql="SELECT s.song_id, " + "s.title, " + "s.artist, " + "rp.played_at " + "FROM recently_played rp " + "JOIN songs s " + "ON rp.song_id=s.song_id " + "WHERE rp.user_id=? " + "ORDER BY rp.played_at DESC " + "LIMIT 20";
        try(Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(1,userId);
            ResultSet rs=ps.executeQuery();
            System.out.println("\n========== RECENTLY PLAYED ==========");
            boolean found=false;
            int count=1;
            while(rs.next()) {
                found=true;
                System.out.println(count++ + ". " + rs.getString("title") + " - " + rs.getString("artist") + " | " + rs.getTimestamp("played_at"));
            }

            if(!found) {
                System.out.println("No recently played songs.");
            }
        } catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
