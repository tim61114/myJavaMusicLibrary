import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class DataFetcher {

    private static final String theAudioDBapiKey = "523532"; // Insert Thanos here: A small price to pay for salvation

    /* for AudioDB*/

    /**
     * @param artistName is the name of the artist
     * @param songName is the name of the song
     * @return the json data of the specific query
     */
    public String fetchJSONStringFromArtistAndSong(String artistName, String songName){
        String requestURL = "https://www.theaudiodb.com/api/v1/json/"+theAudioDBapiKey+"/searchtrack.php?";
        URL u;
        try {
            u = new URL(requestURL +"s="+artistName +"&t="+songName);
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL");
            return null;
        }
        try {
            return fetchFromURL(u);
        } catch (IOException e) {
            System.out.println("Error reading response.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param artistName is the name of the artist
     * @return the json data of the artist
     */
    public String fetchJSONStringFromArtist(String artistName){
        String requestURL = "https://www.theaudiodb.com/api/v1/json/"+theAudioDBapiKey+"/search.php?";
        URL u;
        try {
            u = new URL(requestURL +"s="+artistName);
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL");
            return null;
        }
        try {
            return fetchFromURL(u);
        } catch (IOException e) {
            System.out.println("Error reading response.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param artistName is the name of the artist
     * @param albumName is the name of the album
     * @return the json data of the album performed by the artist
     */
    public String fetchJSONStringFromArtistAndAlbum(String artistName, String albumName){
        String requestURL = "https://www.theaudiodb.com/api/v1/json/"+theAudioDBapiKey+"/searchalbum.php?";
        URL u;
        try {
            u = new URL(requestURL +"s="+artistName +"&a="+albumName);
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL");
            return null;
        }
        try {
            return fetchFromURL(u);
        } catch (IOException e) {
            System.out.println("Error reading response.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param artistName is the name of the artist
     * @param albumName is the name of the album
     * @return an array of strings containing the id, name of the artist and name of the album.
     * This is done by using the "indexed search with advanced syntax" provided by musicbrainz
     */
    /* for Musicbrainz */
    public String[] fetchAlbumID(String artistName, String albumName){
        String queryResult;
        StringBuilder longArtistName = new StringBuilder();
        String requestURL = ("https://musicbrainz.org/ws/2/release?query="+albumName+" AND "+artistName+"&fmt=json").replaceAll(" ","%20");
        queryResult = getFromURL(requestURL);
        try {
            String[] albumData = new String[3];
            JSONParser p = new JSONParser();
            Object obj = p.parse(queryResult);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray arrayData = (JSONArray)jsonObject.get("releases");
            JSONObject data = (JSONObject) arrayData.get(0);
            JSONArray artist = (JSONArray) data.get("artist-credit");
            JSONObject artistData = (JSONObject) artist.get(0);
            longArtistName.append(artistData.get("name"));
            if(artist.size() > 1){
                //System.out.println("a lot of artists");
                for(int i = 1;i < artist.size();i++){
                    JSONObject moreArtistData = (JSONObject) artist.get(i);
                    longArtistName.append(", " + moreArtistData.get("name"));
                }
            }
            System.out.println("Do you mean Album '"+data.get("title")+"' by '"+longArtistName+"'? (Y/N)");
            String id = (String)data.get("id");
            System.out.println("https://musicbrainz.org/release/"+id);
            albumData[0] = id;
            albumData[1] = (String) data.get("title");
            albumData[2] = longArtistName.toString();
            return albumData;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param requestURL is the URL of the query
     * @return the whole data read from the url
     */
    protected String getFromURL(String requestURL) {
        String result = null;
        URL u = null;
        try{
            u = new URL(requestURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            result = fetchFromURL(u);
        }catch (IOException e){
            System.out.println("Error reading response.");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param id is the id of the musicbrainz data
     * @return the album data in json
     */
    public String fetchJSONAlbumFromID(String id){
        String queryResult = null;
        String requestURL = "https://musicbrainz.org/ws/2/release/"+id+"?inc=artist-credits%2Brecordings&fmt=json";
        queryResult = getFromURL(requestURL);
        return queryResult;
    }

    public String fetchFromURL(URL u) throws IOException{
        StringBuilder response = new StringBuilder();
        URLConnection connection = u.openConnection();
        HttpURLConnection httpConnection = (HttpURLConnection) connection;
        int code = httpConnection.getResponseCode();

        //String message = httpConnection.getResponseMessage();
        //System.out.println(code + " " + message);
        if (code != HttpURLConnection.HTTP_OK) {
            System.out.println("HTTP Connection error");
            return null;
        }
        InputStream instream = connection.getInputStream();
        Scanner in = new Scanner(instream);
        while (in.hasNextLine()) {
            response.append(in.nextLine());
        }
        return response.toString();
    }



    public static void main(String[] args) {
        DataFetcher df = new DataFetcher();
        //System.out.println(df.fetchJSONStringFromArtistAndSong("coldplay","yellow"));
        //System.out.println(df.fetchJSONStringFromArtist("adele"));
        //System.out.println(df.fetchJSONStringFromArtistAndAlbum("adele","25"));
        //System.out.println(df.fetchJSONAlbumFromID(df.fetchAlbumID("adele","25")));
    }

}
