package MelodyMesh;

public class Song {

    private int songId;
    private String title;
    private String artist;
    private String album;
    private String genre;
    private String filePath;
    private int duration;

    public Song() {
    }


    public Song(int songId, String title, String artist, String album, String genre, String filePath, int duration) {
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.filePath = filePath;
        this.duration = duration;
    }


    public Song(String title, String artist, String album, String genre, String filePath) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.filePath = filePath;
    }

    public int getSongId() {
        return songId;
    }


    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getGenre() {
        return genre;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return songId + " - " + title + " - " + artist;
    }
}
