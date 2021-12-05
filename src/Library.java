import java.util.ArrayList;
import java.util.List;

public class Library {

    MusicDB database;
    List<Song> songs = new ArrayList<>();
    List<Artist> artists = new ArrayList<>();
    List<Album> albums = new ArrayList<>();
    List<Playlist> playlists = new ArrayList<>();

    public Library(MusicDB mDB){
        database = mDB;
        songs = mDB.readSongs();
        artists = mDB.readArtists();
        albums = mDB.readAlbums();
    }

    public void showSongs(){
        if(songs.isEmpty()) System.out.println("Your library is empty! Consider adding some songs?");
        else{
            songs.forEach(song -> System.out.println(song.showSong()));
        }
    }

    public void showSongsWithIndex(){
        if(songs.isEmpty()) System.out.println("Your library is empty! Consider adding some songs?");
        else{
            songs.forEach(song -> System.out.println((songs.indexOf(song) +1) +" "+song.showSong()));
        }
    }

    public Playlist createNewPlaylist(String name,List<Integer> indexes){
        Playlist temp = new Playlist(name);
        for(Integer i : indexes){
            temp.addSong(songs.get(i-1));
        }
        playlists.add(temp);
        database.writeNewPlaylist(temp);
        return temp;
    }

    public void importSongs(List<Song> newSongs, List<Artist> newArtists, List<Album> newAlbums ){
        database.writeSongsToDB(newSongs);
        database.writeArtistsToDB(newArtists);
        database.writeAlbumsToDB(newAlbums);
    }

}
