import java.io.File;
import java.io.IOException;
import java.util.*;

public class Client {

    public static final String dataPath = "data/";

    int currentNumberOfUsers;
    UserDB userDB = new UserDB(dataPath+"user.db");
    static String currentUser;

    public Client(){
        currentNumberOfUsers = userDB.getNumUsers();
    }

    public void createUser(String username, String password){
        userDB.createNewUser(username, password);
        File file = new File(dataPath+username+".db");
        try {
            file.createNewFile();
            MusicDB mdb = new MusicDB(file.getPath());
            mdb.init();
        } catch (IOException e) {
            System.out.println("Error: Unable to create user database.");
        }
    }

    public boolean login(String username, String password){
        return password.equals(userDB.singleQuery("select password from users where username = '" + username + "'").get(0));
    }

    public Library loadUserMusicDB(String username){
        MusicDB mDB = new MusicDB(dataPath+username+".db");
        Library userLib = new Library(mDB);
        return userLib;
    }

    public static void libMenu(Library lib){
        int input = 0;
        boolean pause = false;
        String stringInput;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello, " + currentUser);
        while(input != 9){
            System.out.println("\nMenu: ");
            System.out.println("1. Show my song list.");
            System.out.println("2. Play a random song from my library.");
            System.out.println("3. Import songs");
            System.out.println("4. Create new Playlist");
            System.out.println("5. View my playlists ");
            System.out.println("6. Play next song");
            System.out.println("7. View now playing list");
            System.out.println("8. Play/Pause");
            System.out.println("9. Log out");
            System.out.println();
            if(lib.nowPlaying != null){
                if(pause){
                    System.out.println("Now playing "+ lib.nowPlaying.showSong() + " (Paused)");
                } else {
                    System.out.println("Now playing "+ lib.nowPlaying.showSong());
                }
            }

            input = scanner.nextInt();

            switch(input){
                case 1:
                    lib.showSongs();
                    break;
                case 2:
                    lib.Shuffle();
                    lib.nowPlaying = lib.nowPlayingList.get(lib.nowPlayingIndex);
                    break;
                case 4:
                    lib.showSongsWithIndex();
                    System.out.println("Please enter the index of the songs you want to add into the playlist in a line.");
                    scanner.nextLine();
                    stringInput = scanner.nextLine();
                    String[] splited = stringInput.split(" ");
                    List<Integer> indexes = new ArrayList<>();
                    for(String s: splited){
                        indexes.add(Integer.parseInt(s));
                    }
                    Collections.sort(indexes);
                    if(indexes.get(indexes.size() - 1) > lib.songs.size()){
                        System.out.println("Sorry, you do not have more than "+indexes.get(indexes.size() - 1) + " songs.");
                    }
                    else{
                        System.out.println("Please name your playlist");
                        stringInput = scanner.next();
                        Playlist temp = lib.createNewPlaylist(stringInput,indexes);
                        temp.show();
                    }
                    break;
                case 5:
                    if(lib.getPlaylists().size() == 0){
                        System.out.println("You have no playlists! Create one!");
                    } else{
                        for(Playlist p : lib.getPlaylists()){
                            System.out.println(p.getName());
                            p.show();
                            System.out.println();
                        }
                    }
                    break;
                case 6:
                    if(lib.nowPlayingList.isEmpty() || lib.nowPlayingIndex == -1){
                        System.out.println("Play a song!");
                    } else if(lib.nowPlayingIndex + 1  == lib.nowPlayingList.size()){
                        lib.nowPlayingIndex = -1;
                        lib.nowPlaying = null;
                        lib.nowPlayingList = new ArrayList<>();
                        System.out.println("Play a new list!");

                    } else {
                        lib.nowPlayingIndex++;
                        lib.nowPlaying = lib.nowPlayingList.get(lib.nowPlayingIndex);
                    }
                    break;
                case 7:
                    if(lib.nowPlayingList.isEmpty()){
                        System.out.println("I'll talk if you play something first!");
                    } else {
                        System.out.println();
                        System.out.println("Current playing list:");
                        for(Song s:lib.nowPlayingList){
                            System.out.println(s.showSong());
                        }
                    }
                case 8:
                    if(lib.nowPlaying == null){
                        System.out.println("Play something first!");
                    } else {
                        pause = !pause;
                    }
            }
        }
    }

    public static void loginMenu(){
        Client client = new Client();
        int input = 0;
        while(input != 3){
            System.out.println("Welcome");
            System.out.println("1. User Login.");
            System.out.println("2. Create new account.");
            System.out.println("3. Exit the program.");
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextInt();
            String username,pwd, pwdCheck;
            if(input == 1){
                do{
                    System.out.println("Please enter username");
                    username = scanner.next();
                    if(!client.userDB.checkUsernameExists(username)) System.out.println("User does not exist.");
                }while(!client.userDB.checkUsernameExists(username));
                do{
                    System.out.println("Please enter password");
                    pwd = scanner.next();
                    if(!client.login(username,pwd)) System.out.println("Wrong password");
                }while(!client.login(username,pwd));
                currentUser = username;
                libMenu(client.loadUserMusicDB(username));

            } else if (input == 2){
                do{
                    System.out.println("Please enter username");
                    username = scanner.next();

                }while(client.userDB.checkUsernameExists(username));
                do {
                    System.out.println("PLease enter your password");
                    pwd = scanner.next();
                    System.out.println("Enter password again");
                    pwdCheck = scanner.next();
                    if(!pwd.equals(pwdCheck)) System.out.println("password does not match, try again");
                }while(!pwd.equals(pwdCheck));
                client.createUser(username,pwd);
                System.out.println("Please login.");
            }
        }



    }
    public static void main(String[] args) {
        loginMenu();
    }
}
