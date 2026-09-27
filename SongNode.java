package MelodyMesh;

public class SongNode {
    private Song song;
    private SongNode previous;
    private SongNode next;

    public SongNode(Song song) {
        this.song = song;
    }

    public Song getSong() {
        return song;
    }

    public SongNode getPrevious() {
        return previous;
    }

    public SongNode getNext() {
        return next;
    }

    public void setPrevious(SongNode previous) {
        this.previous = previous;
    }

    public void setNext(SongNode next) {
        this.next = next;
    }
}
