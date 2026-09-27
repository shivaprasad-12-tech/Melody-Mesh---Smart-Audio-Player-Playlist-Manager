package MelodyMesh;

import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);

    private static final UserDAO userDAO = new UserDAO();
    private static final SongDAO songDAO = new SongDAO();
    private static final PlaylistDAO playlistDAO = new PlaylistDAO();
    private static final FavoriteDAO favoriteDAO = new FavoriteDAO();
    private static final RecentDAO recentDAO = new RecentDAO();
    private static final AudioPlayer audioPlayer = new AudioPlayer();

    private static User currentUser;
    private static DoublyLinkedPlaylist currentPlaylist;
    private static int currentPlaylistId = -1;

    public static void main(String[] args) {
        while(true) {
            System.out.println("\n========================================");
            System.out.println("             MELODYMESH");
            System.out.println("   SMART AUDIO PLAYER & PLAYLIST MANAGER");
            System.out.println("========================================");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            int choice = readInt("Enter choice: ");

            switch(choice) {
                case 1:
                    register();
                    break;

                case 2:
                    login();
                    break;

                case 3:
                    audioPlayer.stop();
                    System.out.println("Thank you for using MelodyMesh.");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void register() {
        String username = readLine("Enter username: ");
        String password = readLine("Enter password: ");

        if(username.isBlank() || password.isBlank()) {
            System.out.println("Username and password cannot be empty.");
            return;
        }

        if(userDAO.register(new User(username, password))) {
            System.out.println("Registration successful.");
        }
    }

    private static void login() {
        String username = readLine("Enter username: ");
        String password = readLine("Enter password: ");

        currentUser = userDAO.login(username, password);

        if(currentUser == null) {
            System.out.println("Invalid username or password.");
            return;
        }

        System.out.println("Welcome, " + currentUser.getUsername() + "!");
        dashboard();
    }

    private static void dashboard() {
        while(currentUser != null) {
            System.out.println("\n============== DASHBOARD ==============");
            System.out.println("1.  Add Song");
            System.out.println("2.  Display All Songs");
            System.out.println("3.  Search Song");
            System.out.println("4.  Delete Song");
            System.out.println("5.  Create Playlist");
            System.out.println("6.  Display My Playlists");
            System.out.println("7.  Open Playlist");
            System.out.println("8.  Add Song To Playlist");
            System.out.println("9.  Remove Song From Playlist");
            System.out.println("10. Display Current Playlist");
            System.out.println("11. Play Current Song");
            System.out.println("12. Play Song By ID");
            System.out.println("13. Next Song");
            System.out.println("14. Previous Song");
            System.out.println("15. Pause");
            System.out.println("16. Resume");
            System.out.println("17. Stop");
            System.out.println("18. Set Volume");
            System.out.println("19. Add Favorite");
            System.out.println("20. Remove Favorite");
            System.out.println("21. Display Favorites");
            System.out.println("22. Recently Played");
            System.out.println("23. Show Current Song");
            System.out.println("24. Logout");

            int choice = readInt("Enter choice: ");

            switch(choice) {
                case 1:
                    addSong();
                    break;

                case 2:
                    songDAO.displaySongs();
                    break;

                case 3:
                    searchSong();
                    break;

                case 4:
                    deleteSong();
                    break;

                case 5:
                    createPlaylist();
                    break;

                case 6:
                    playlistDAO.displayPlaylists(currentUser.getUserId());
                    break;

                case 7:
                    openPlaylist();
                    break;

                case 8:
                    addSongToPlaylist();
                    break;

                case 9:
                    removeSongFromPlaylist();
                    break;

                case 10:
                    displayCurrentPlaylist();
                    break;

                case 11:
                    playCurrentSong();
                    break;

                case 12:
                    playSongById();
                    break;

                case 13:
                    nextSong();
                    break;

                case 14:
                    previousSong();
                    break;

                case 15:
                    audioPlayer.pause();
                    break;

                case 16:
                    audioPlayer.resume();
                    break;

                case 17:
                    audioPlayer.stop();
                    break;

                case 18:
                    setVolume();
                    break;

                case 19:
                    addFavorite();
                    break;

                case 20:
                    removeFavorite();
                    break;

                case 21:
                    favoriteDAO.displayFavorites(currentUser.getUserId());
                    break;

                case 22:
                    recentDAO.displayRecentlyPlayed(currentUser.getUserId());
                    break;

                case 23:
                    showCurrentSong();
                    break;

                case 24:
                    logout();
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void addSong() {
        String title = readLine("Song title: ");
        String artist = readLine("Artist: ");
        String album = readLine("Album: ");
        String genre = readLine("Genre: ");
        String path = readLine("WAV file path: ");

        Song song = new Song(title, artist, album, genre, path);
        songDAO.addSong(song);
    }

    private static void searchSong() {
        String keyword = readLine("Enter title/artist/album/genre: ");
        songDAO.searchSong(keyword);
    }

    private static void deleteSong() {
        int songId = readInt("Enter song ID: ");
        songDAO.deleteSong(songId);
    }

    private static void createPlaylist() {
        String name = readLine("Enter playlist name: ");

        if(name.isBlank()) {
            System.out.println("Playlist name cannot be empty.");
            return;
        }

        playlistDAO.createPlaylist(currentUser.getUserId(), name);
    }

    private static void openPlaylist() {
        playlistDAO.displayPlaylists(currentUser.getUserId());

        int playlistId = readInt("Enter playlist ID to open: ");

        if(!playlistDAO.playlistBelongsToUser(playlistId, currentUser.getUserId())) {
            System.out.println("Invalid playlist ID.");
            return;
        }

        currentPlaylistId = playlistId;
        currentPlaylist = playlistDAO.loadPlaylist(playlistId);

        if(currentPlaylist.isEmpty()) {
            System.out.println("Playlist opened, but it has no songs.");
        } else {
            System.out.println("Playlist opened. " + currentPlaylist.size() + " song(s) loaded.");
            currentPlaylist.display();
        }
    }

    private static void addSongToPlaylist() {
        playlistDAO.displayPlaylists(currentUser.getUserId());

        int playlistId = readInt("Playlist ID: ");

        if(!playlistDAO.playlistBelongsToUser(playlistId, currentUser.getUserId())) {
            System.out.println("Invalid playlist ID.");
            return;
        }

        songDAO.displaySongs();

        int songId = readInt("Song ID: ");

        if(playlistDAO.addSongToPlaylist(playlistId, songId)) {
            System.out.println("Song added to playlist.");

            if(currentPlaylistId == playlistId) {
                currentPlaylist = playlistDAO.loadPlaylist(playlistId);
            }
        }
    }

    private static void removeSongFromPlaylist() {
        if(!ensurePlaylistLoaded()) {
            return;
        }

        currentPlaylist.display();

        int songId = readInt("Song ID to remove: ");

        if(playlistDAO.removeSongFromPlaylist(currentPlaylistId, songId)) {
            currentPlaylist.removeSong(songId);
            System.out.println("Song removed from playlist.");
        } else {
            System.out.println("Song was not found in playlist.");
        }
    }

    private static void displayCurrentPlaylist() {
        if(!ensurePlaylistLoaded()) {
            return;
        }

        currentPlaylist.display();
    }

    private static void playCurrentSong() {
        if(!ensurePlaylistLoaded()) {
            return;
        }

        Song song = currentPlaylist.getCurrentSong();

        if(song == null) {
            System.out.println("No current song.");
            return;
        }

        playSong(song);
    }

    private static void playSongById() {
        songDAO.displaySongs();

        int songId = readInt("Enter song ID: ");
        Song song = songDAO.getSongById(songId);

        if(song == null) {
            System.out.println("Song not found.");
            return;
        }

        if(currentPlaylist != null && currentPlaylist.findNode(songId) != null) {
            currentPlaylist.setCurrentBySongId(songId);
        }

        playSong(song);
    }

    private static void playSong(Song song) {
        System.out.println("\nNow playing: " + song.getTitle() + " - " + song.getArtist());

        if(audioPlayer.play(song.getFilePath())) {
            recentDAO.addRecentlyPlayed(currentUser.getUserId(), song.getSongId());

            System.out.println("Duration: " + formatTime(audioPlayer.getDurationSeconds()));
        }
    }

    private static void nextSong() {
        if(!ensurePlaylistLoaded()) {
            return;
        }

        Song next = currentPlaylist.getNextSong();

        if(next == null) {
            System.out.println("You are already at the last song.");
            return;
        }

        playSong(next);
    }

    private static void previousSong() {
        if(!ensurePlaylistLoaded()) {
            return;
        }

        Song previous = currentPlaylist.getPreviousSong();

        if(previous == null) {
            System.out.println("You are already at the first song.");
            return;
        }

        playSong(previous);
    }

    private static void setVolume() {
        float volume = readFloat("Enter volume (0-100): ");

        if(volume < 0 || volume > 100) {
            System.out.println("Volume must be between 0 and 100.");
            return;
        }

        audioPlayer.setVolume(volume);
    }

    private static void addFavorite() {
        songDAO.displaySongs();

        int songId = readInt("Enter song ID: ");

        if(favoriteDAO.addFavorite(currentUser.getUserId(), songId)) {
            System.out.println("Added to favorites.");
        }
    }

    private static void removeFavorite() {
        favoriteDAO.displayFavorites(currentUser.getUserId());

        int songId = readInt("Enter song ID to remove: ");

        if(favoriteDAO.removeFavorite(currentUser.getUserId(), songId)) {
            System.out.println("Removed from favorites.");
        } else {
            System.out.println("Song was not in favorites.");
        }
    }

    private static void showCurrentSong() {
        if(currentPlaylist == null) {
            System.out.println("No playlist is currently open.");
            return;
        }

        Song song = currentPlaylist.getCurrentSong();

        if(song == null) {
            System.out.println("No current song.");
            return;
        }

        System.out.println("\n========== CURRENT SONG ==========");
        System.out.println("Title  : " + song.getTitle());
        System.out.println("Artist : " + song.getArtist());
        System.out.println("Album  : " + song.getAlbum());
        System.out.println("Genre  : " + song.getGenre());
        System.out.println("Status : " + (audioPlayer.isPlaying() ? "Playing" : "Stopped/Paused"));
    }

    private static boolean ensurePlaylistLoaded() {
        if(currentPlaylist == null) {
            System.out.println("Open a playlist first using option 7.");
            return false;
        }

        return true;
    }

    private static void logout() {
        audioPlayer.stop();
        currentUser = null;
        currentPlaylist = null;
        currentPlaylistId = -1;

        System.out.println("Logged out successfully.");
    }

    private static int readInt(String message) {
        while(true) {
            try {
                System.out.print(message);
                return Integer.parseInt(sc.nextLine().trim());
            } catch(NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static float readFloat(String message) {
        while(true) {
            try {
                System.out.print(message);
                return Float.parseFloat(sc.nextLine().trim());
            } catch(NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String readLine(String message) {
        System.out.print(message);
        return sc.nextLine().trim();
    }

    private static String formatTime(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;

        return String.format("%02d:%02d", minutes, remainingSeconds);
    }
}
