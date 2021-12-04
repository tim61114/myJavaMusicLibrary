public class Album {
    int AlbumID;
    String name;

    public Album(String name){
        this.name = name;
    }
    public Album(int id, String name){
        AlbumID = id;
        this.name = name;
    }

    public String toString(){
        return this.name;
    }
}
