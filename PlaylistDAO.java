package MelodyMesh;

import java.sql.*;

public class PlaylistDAO {

    public int createPlaylist(int userId, String name) {
        String sql="INSERT INTO playlists" + "(user_id, playlist_name)" + " VALUES(?, ?)";
        try(Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setString(2, name);
            ps.executeUpdate();
            ResultSet rs=ps.getGeneratedKeys();
            if(rs.next()) {
                int id=rs.getInt(1);
                System.out.println("Playlist created. ID: " + id);
                return id;
            }
        } catch(SQLException e) {
            System.out.println("Error creating playlist: " + e.getMessage());
        }
        return -1;
    }

    public void displayPlaylists(int userId) {
        String sql="SELECT * FROM playlists " + "WHERE user_id=? " + "ORDER BY playlist_id";
        try(Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs=ps.executeQuery();
            System.out.println("\n========== MY PLAYLISTS ==========");
            boolean found=false;
            while(rs.next()) {
                found=true;
                System.out.println(rs.getInt("playlist_id") + " - " + rs.getString("playlist_name"));
            }
            if(!found) {
                System.out.println("No playlists created.");
            }
        } catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    public boolean playlistBelongsToUser(
            int playlistId,
            int userId) {
        String sql="SELECT playlist_id " + "FROM playlists " + "WHERE playlist_id=? " + "AND user_id=?";
        try(Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(1, playlistId);
            ps.setInt(2, userId);
            ResultSet rs=ps.executeQuery();
            return rs.next();
        } catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }


    public boolean addSongToPlaylist(
            int playlistId,
            int songId) {
        String sql="INSERT INTO playlist_songs" + "(playlist_id, song_id)" + " VALUES(?,?)";
        try(Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(1, playlistId);
            ps.setInt(2, songId);
            ps.executeUpdate();
            return true;
        } catch(SQLIntegrityConstraintViolationException e) {
            System.out.println("Song is already in playlist " + "or an ID is invalid.");
        } catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    public boolean removeSongFromPlaylist(
            int playlistId,
            int songId) {

        String sql="DELETE FROM playlist_songs " + "WHERE playlist_id=? " + "AND song_id=?";
        try(Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(1, playlistId);
            ps.setInt(2, songId);
            return ps.executeUpdate() > 0;
        } catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    public DoublyLinkedPlaylist loadPlaylist(
            int playlistId) {
        DoublyLinkedPlaylist playlist=new DoublyLinkedPlaylist();
        String sql="SELECT s.* " + "FROM songs s " + "JOIN playlist_songs ps " + "ON s.song_id=ps.song_id " + "WHERE ps.playlist_id=? " + "ORDER BY ps.added_at, s.song_id";
        try(Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(1, playlistId);
            ResultSet rs=ps.executeQuery();
            while(rs.next()) {
                Song song=new Song(rs.getInt("song_id"),
                                rs.getString("title"),
                                rs.getString("artist"),
                                rs.getString("album"),
                                rs.getString("genre"),
                                rs.getString("file_path"),
                                rs.getInt("duration"));
                playlist.addSong(song);
            }
        } catch(SQLException e) {
            System.out.println("Error loading playlist: " + e.getMessage());
        }
        return playlist;
    }
}
