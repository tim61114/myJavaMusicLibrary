public class Album {
    protected int AlbumID;
    protected String name;
    protected String artistName;
    protected Artist artist;

    public Album(String name){
        this.name = name;
    }
    public Album(int id, String name){
        AlbumID = id;
        this.name = name;
        artistName = "";
        artist = new Artist("");
    }

    public String getName(){
        return this.name;
    }
    public String toString(){
        return this.name;
    }
    public void setArtistName(String aName) {artistName = aName;}
    public void setArtist(Artist a){ artist = a;}
    public int getAlbumID() { return AlbumID; }
    public String getArtistName(){ return artistName;}
}
