import java.util.ArrayList;
import java.util.List;

public class Library {

    List<Song> songs = new ArrayList<>();
    List<Artist> artists = new ArrayList<>();
    List<Album> albums = new ArrayList<>();

    public Library(MusicDB mDB){
        songs = mDB.readSongs();
        artists = mDB.readArtists();
        albums = mDB.readAlbums();
    }


}
