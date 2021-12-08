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
        linkData();
    }

    public void linkData(){
        for(Album a: albums){
            for(Artist ar : artists){
                if (a.getArtistName().equals(ar.getName())) {
                    a.setArtist(ar);
                    ar.addToAlbums(a);
                }
            }
        }
    }

    //Show functions

    public void showSongs(){
        System.out.println();
        if(songs.isEmpty()) System.out.println("Your library is empty! Consider adding some songs?");
        else{
            songs.forEach(song -> System.out.println(song.showSong()));
            System.out.println();
        }
    }

    public void showSongsWithIndex(){
        System.out.println();
        if(songs.isEmpty()) System.out.println("Your library is empty! Consider adding some songs?");
        else{
            songs.forEach(song -> System.out.println((songs.indexOf(song) +1) +" "+song.showSong()));
            System.out.println();
        }
    }

    public void showAlbums(){
        System.out.println();
        if(albums.isEmpty()){
            System.out.println("There's nothing here...");
        } else {
            albums.forEach(album -> System.out.println(album.getName()));
            System.out.println();
        }
    }

    public void showArtists(){
        System.out.println();
        if(artists.isEmpty()){
            System.out.println("It's quiet in here...");
        } else {
            artists.forEach(artist -> System.out.println(artist.getName()));
            System.out.println();
        }
    }

    public void showPlaylists(){
        System.out.println();
        if(playlists.size() == 0){
            System.out.println("You have no playlists! Create one!");
        } else{
            playlists.forEach(playlist -> {
                System.out.println(playlist.getName());
                playlist.show();
                System.out.println();
            });
            System.out.println();
        }
    }


    //Create new playlist

    public Playlist createNewPlaylist(String name,List<Integer> indexes){
        Playlist temp = new Playlist(name);
        for(Integer i : indexes){
            temp.addSong(songs.get(i-1));
        }
        playlists.add(temp);
        database.writeNewPlaylist(temp);
        return temp;
    }

    //Create a new random list

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

    public void exportPlaylists(String currentUser){
        playlists.forEach(playlist -> playlist.export(currentUser));
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
