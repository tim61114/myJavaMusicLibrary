import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Library {

    MusicDB database;
    List<Song> songs;
    List<Artist> artists;
    List<Album> albums;
    List<Playlist> playlists = new ArrayList<>();
    Song nowPlaying = null;
    List<Song> nowPlayingList = new ArrayList<>();
    int nowPlayingIndex = -1;

    public Library(MusicDB mDB){
        database = mDB;
        songs = mDB.readSongs();
        artists = mDB.readArtists();
        albums = mDB.readAlbums();
        List<String> playlistNames = mDB.readPlaylistNames();
        List<List<Integer>> playlistData = mDB.readPlaylist();
        for(int i = 0;i < playlistNames.size();i++){
            Playlist temp = new Playlist(mDB.readPlaylistNames().get(i));
            playlistData.get(i).forEach(index -> temp.addSong(songs.get(index)));
            playlists.add(temp);
        }
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

    public List<Song> Shuffle(){
        Random random = new Random();
        if(songs.isEmpty()) return null;
        List<Song> songcopy = new ArrayList<>(songs);
        for(int i = songcopy.size() - 1;i >= 0;i--){
            int randomIndex = random.nextInt(songcopy.size()) ;
            nowPlayingList.add(songcopy.get(randomIndex));
            songcopy.remove(randomIndex);
        }
        nowPlayingIndex = 0;
        return nowPlayingList;
    }

    public List<Playlist> getPlaylists(){
        return playlists;
    }

    public void importSongs(List<Song> newSongs, List<Artist> newArtists, List<Album> newAlbums ){
        database.writeSongsToDB(newSongs);
        database.writeArtistsToDB(newArtists);
        database.writeAlbumsToDB(newAlbums);
    }

}
