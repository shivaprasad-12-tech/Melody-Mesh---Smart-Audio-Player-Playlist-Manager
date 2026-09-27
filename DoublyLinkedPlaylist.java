package MelodyMesh;

public class DoublyLinkedPlaylist {

    private SongNode head;
    private SongNode tail;
    private SongNode current;
    private int size;

    public void clear() {
        head=null;
        tail=null;
        current=null;
        size=0;
    }


    public void addSong(Song song) {
        SongNode newNode=new SongNode(song);
        if(head==null) {
            head=tail=current=newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrevious(tail);
            tail = newNode;
        }
        size++;
    }


    public boolean removeSong(int songId) {
        SongNode node=findNode(songId);
        if(node==null) {
            return false;
        }
        if(node==head) {
            head=node.getNext();
        }
        if(node==tail) {
            tail=node.getPrevious();
        }
        if(node.getPrevious()!=null) {
            node.getPrevious().setNext(node.getNext());
        }
        if(node.getNext()!=null) {
            node.getNext().setPrevious(node.getPrevious());
        }
        if(current==node) {
            if(node.getNext()!=null) {
                current=node.getNext();
            } else {
                current=node.getPrevious();
            }
        }
        size--;
        if(size==0) {
            head=tail=current=null;
        }
        return true;
    }

    public SongNode findNode(
            int songId) {
        SongNode temp=head;
        while(temp!=null) {
            if(temp.getSong().getSongId()== songId) {
                return temp;
            }
            temp=temp.getNext();
        }
        return null;
    }


    public void setCurrentBySongId(int songId) {
        SongNode node=findNode(songId);
        if(node!=null) {
            current=node;
        }
    }


    public Song getCurrentSong() {
        if(current==null) {
            return null;
        }
        return current.getSong();
    }


    public Song getNextSong() {
        if(current!=null && current.getNext()!=null) {
            current=current.getNext();
            return current.getSong();
        }
        return null;
    }


    public Song getPreviousSong() {
        if(current!=null && current.getPrevious()!=null) {
            current=current.getPrevious();
            return current.getSong();
        }
        return null;
    }


    public boolean isEmpty() {
        return head==null;
    }
    public int size() {
        return size;
    }

    public void display() {
        if(head==null) {
            System.out.println("Playlist is empty.");
            return;
        }
        SongNode temp=head;
        int number=1;
        System.out.println("\n========== PLAYLIST ==========");

        while(temp!=null) {
            String marker=temp == current ? "  <-- CURRENT" : "";
            System.out.println(number++ + ". " + temp.getSong().getTitle() + " - " + temp.getSong().getArtist() + marker);
            temp=temp.getNext();
        }
    }
}
