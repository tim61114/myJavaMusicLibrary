import java.util.ArrayList;
import java.util.List;

public class Artist {

    protected int ArtistID;
    protected String name;
    //protected String bio;
    protected List<Album> albums;

    public Artist(int id, String name){
        ArtistID = id;
        this.name = name;
        albums = new ArrayList<>();
    }

    /**
     * this constructor is used when importing data from musicbrainz and is then given an ID afterwards.
     */
    public Artist(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
    public int getArtistID() { return ArtistID; }
    public List<Album> getAlbums() { return albums;}
    public void addToAlbums(Album a){ albums.add(a);}

    @Override
    public String toString() {
        return this.name;
    }
}
