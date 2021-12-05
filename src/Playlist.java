import java.util.ArrayList;
import java.util.List;

public class Playlist {
    String name;
    List<Song> songs;

    public Playlist(String name){
        this.name = name;
        songs = new ArrayList<>();
    }

    public void addSong(Song s){
        songs.add(s);
    }

    public String getName(){
        return name;
    }

    public void show(){
        songs.forEach(song -> System.out.println(song.showSong()));
    }

    public List<Song> getSongs(){
        return songs;
    }
}
