import java.util.List;

public class Artist {

    protected int ArtistID;
    protected String name;
    //protected String bio;
    protected List<Album> albums;

    public Artist(int id, String name){
        ArtistID = id;
        this.name = name;
    }

    public Artist(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
