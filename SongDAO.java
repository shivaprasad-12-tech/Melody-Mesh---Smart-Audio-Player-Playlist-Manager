package MelodyMesh;

import java.sql.*;

public class SongDAO {


    public void addSong(Song song) {

        String sql = "INSERT INTO songs" + "(title, artist, album, genre, file_path)" + " VALUES(?,?,?,?,?)";


        try(Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setString(1, song.getTitle());
            ps.setString(2, song.getArtist());
            ps.setString(3, song.getAlbum());
            ps.setString(4, song.getGenre());
            ps.setString(5, song.getFilePath());
            ps.executeUpdate();
            System.out.println("Song added successfully.");
        } catch(SQLException e) {
            System.out.println("Error adding song: " + e.getMessage());
        }
    }

    public void displaySongs() {
        String sql="SELECT * FROM songs " + "ORDER BY song_id";

        try(Connection con=DBConnection.getConnection();
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery(sql)) {
            System.out.println("\n========== SONG LIBRARY ==========");
            boolean found = false;

            while(rs.next()) {
                found = true;
                System.out.printf("%d | %-25s | %-20s | %-15s | %-12s%n",
                        rs.getInt("song_id"),
                        rs.getString("title"),
                        rs.getString("artist"),
                        rs.getString("album"),
                        rs.getString("genre"));
            }

            if(!found) {
                System.out.println("No songs available.");
            }

        } catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    public Song getSongById(int id) {
        String sql="SELECT * FROM songs " + "WHERE song_id=?";

        try(Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs=ps.executeQuery();

            if(rs.next()) {
                return new Song(
                        rs.getInt("song_id"),
                        rs.getString("title"),
                        rs.getString("artist"),
                        rs.getString("album"),
                        rs.getString("genre"),
                        rs.getString("file_path"),
                        rs.getInt("duration"));
            }
        } catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }


    public void searchSong(
            String keyword) {
        String sql="SELECT * FROM songs " + "WHERE title LIKE ? " + "OR artist LIKE ? " + "OR album LIKE ? " + "OR genre LIKE ? " + "ORDER BY title";
        try(Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            String value= "%" + keyword + "%";

            ps.setString(1, value);
            ps.setString(2, value);
            ps.setString(3, value);
            ps.setString(4, value);
            ResultSet rs=ps.executeQuery();
            boolean found = false;
            System.out.println("\n========== SEARCH RESULTS ==========");

            while(rs.next()) {
                found = true;
                System.out.println(rs.getInt("song_id") + " - " + rs.getString("title") + " - " + rs.getString("artist") + " - " + rs.getString("genre"));
            }
            if(!found) {
                System.out.println("No matching songs.");
            }
        } catch(SQLException e) {
            System.out.println("Search error: " + e.getMessage());
        }
    }

    public boolean deleteSong(int id) {
        String sql="DELETE FROM songs " + "WHERE song_id=?";
        try(Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows=ps.executeUpdate();
            if(rows > 0) {
                System.out.println("Song deleted successfully.");
                return true;
            }

            System.out.println("Song not found.");

        } catch(SQLException e) {
            System.out.println("Delete error: " + e.getMessage());
        }
        return false;
    }
}
