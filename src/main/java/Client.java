import java.io.File;
import java.io.IOException;
import java.util.*;

public class Client {

    public static final String dataPath = "data/";

    int currentNumberOfUsers;
    static UserDB userDB = new UserDB(dataPath+"user.db");
    static String currentUser;

    public Client(){
        currentNumberOfUsers = userDB.getNumUsers();
    }

    public void createUser(String username, String password){
        userDB.createNewUser(username, password);
        File dir = new File(dataPath+username);
        dir.mkdirs();
        File file = new File(dataPath+username+"/"+username+".db");
        try {
            file.createNewFile();
            MusicDB mdb = new MusicDB(file.getPath(),true);
            mdb.init();
        } catch (IOException e) {
            System.out.println("Error: Unable to create user database.");
        }
    }

    public boolean login(String username, String password){
        return password.equals(userDB.matchPassword(username));
    }

    public Library loadUserMusicDB(String username){
        MusicDB mDB = new MusicDB(dataPath+username+"/"+username+".db");
        return new Library(mDB);
    }

    public void adminMenu(){
        int input = 0;
        String inputString;
        Scanner scanner = new Scanner(System.in);
        while(input != 3){
            System.out.println("Admin Menu:");
            System.out.println("1. Retrieve password for user");
            System.out.println("2. Remove User");
            System.out.println("3. Logout");
            input = scanner.nextInt();
            switch(input){
                case 1:
                    System.out.println("Please enter the username");
                    String username = scanner.next();
                    if(userDB.checkUsernameExists(username)){
                        System.out.println("Retrieve password for user '"+username+"'.");
                        System.out.println("Enter password");
                        inputString = scanner.next();
                        if(inputString.equals(userDB.matchPassword("admin"))){
                            System.out.println("Password for user '"+username+"': "+userDB.matchPassword(username)+"\n");
                        }
                    } else {
                        System.out.println("This user does not exist.\n");
                    }

                    break;

                case 2:
                    System.out.println("Please enter the username");
                    String deleteUsername = scanner.next();
                    System.out.println("Delete user '"+deleteUsername+"'");
                    System.out.println("Enter password");
                    inputString = scanner.next();
                    if(inputString.equals(userDB.matchPassword("admin"))){
                        if(userDB.removeUser(deleteUsername)){
                            System.out.println("Deletion Success.");
                        } else {
                            System.out.println("Deletion Failed.");
                        }
                    }
                    break;
            }
        }
    }

    public static void showLib(Library lib){
        int input = 0;
        Scanner scanner = new Scanner(System.in);
        while(input != 5) {
            System.out.println(currentUser + "'s Library: ");
            System.out.println("1. Songs");
            System.out.println("2. Albums");
            System.out.println("3. Artists");
            System.out.println("4. Playlists");
            System.out.println("5. Back to Menu");

            input = scanner.nextInt();
            switch (input){
                case 1:
                    lib.showSongsWithIndex();
                    System.out.println("1. Play");
                    System.out.println("2. Back");
                    input = scanner.nextInt();
                    if(input == 1){
                        System.out.println("Please input the Song index:");
                        input = scanner.nextInt();
                        lib.playSong(input);
                        lib.nowPlaying = lib.nowPlayingList.get(lib.nowPlayingIndex);
                    }
                    break;
                case 2:
                    lib.showAlbums();
                    break;
                case 3:
                    lib.showArtists();
                    break;
                case 4:
                    lib.showPlaylists();
                    break;
            }
        }
    }

    public static void mainMenu(Library lib){
        int input = 0;
        boolean pause = false;
        String stringInput;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello, " + currentUser);
        while(input != 10){
            System.out.println("\nMenu: ");
            System.out.println("1. My library");
            System.out.println("2. Shuffle");
            System.out.println("3. Import songs (from album)");
            System.out.println("4. Create new Playlist");
            System.out.println("5. Export my playlists");
            System.out.println("6. Play next song");
            System.out.println("7. View now playing list");
            System.out.println("8. Play/Pause");
            System.out.println("9. What am I listening?");
            System.out.println("10. Logout");
            System.out.println();
            if(lib.nowPlaying != null){
                lib.nowPlaying.play(pause);
                /*if(pause){
                    System.out.println("Now playing "+ lib.nowPlaying.showSong() + " (Paused)");
                } else {
                    System.out.println("Now playing "+ lib.nowPlaying.showSong());
                }*/
            }

            input = scanner.nextInt();
            try{
                switch(input){
                    case 1:
                        showLib(lib);
                        break;
                    case 2:
                        if(lib.Shuffle() == null){
                            System.out.println("I see no songs right here hmm...");
                            break;
                        }
                        lib.nowPlaying = lib.nowPlayingList.get(lib.nowPlayingIndex);
                        break;
                    case 3:
                        try{
                            System.out.println("This helper will let you import songs given the name of the artist and the name of the album.");
                            System.out.println("Please enter the name of the Artist");
                            //String a = scanner.nextLine();
                            scanner.nextLine();
                            String artistName = scanner.nextLine();
                            System.out.println("Please enter the name of the Album");
                            String albumName = scanner.nextLine();
                            DataFetcher df = new DataFetcher();
                            Parser p = new Parser();
                            String[] data = df.fetchAlbumID(artistName,albumName);
                            String yesOrNo = scanner.next();
                            if(yesOrNo.equalsIgnoreCase("y")){
                                List<Song> newAlbum = p.getFullAlbum(data);
                                System.out.println("Here's what I got for you.");
                                newAlbum.forEach(song -> System.out.println(song.showSong()));
                                System.out.println("Updating database...");
                                lib.addNewAlbumToDB(newAlbum,data[2],data[1]);
                                System.out.println("Done.");
                            }
                        } catch (IndexOutOfBoundsException e){
                            System.out.println("Sorry, I can't find this album.");
                        }
                        break;
                    case 4:
                        if(!lib.songs.isEmpty()){
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
                                if(stringInput.matches("[a-zA-Z1-9']+")){
                                    Playlist temp = lib.createNewPlaylist(stringInput,indexes);
                                    temp.show();
                                } else{
                                    System.out.println("No symbols naughty boy");
                                }
                            }
                        } else {
                            System.out.println("You gotta add some songs first!");
                        }

                        break;
                    case 5:
                        if(!lib.playlists.isEmpty()) lib.exportPlaylists(currentUser);
                        else System.out.println("Ok I exported nothing. You have no playlists!");
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
                        break;
                    case 8:
                        if(lib.nowPlaying == null){
                            System.out.println("Play something first!");
                        } else {
                            pause = !pause;
                        }
                        break;
                    case 9:
                        lib.showBio();
                        break;
                }
            }catch(NumberFormatException e){
                System.out.println("As you wish.");
            }catch(InputMismatchException e){
                System.out.println("Input numbers not letters!");
            }
        }
    }

    public static void loginMenu(){
        Client client = new Client();
        int input = 0;
        while(input != 3){
            try{
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
                    if(username.equals("admin")){
                        client.adminMenu();
                    } else {
                        currentUser = username;
                        mainMenu(client.loadUserMusicDB(username));
                    }
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
                    System.out.println("Please login.\n");
                }
            }catch (NullPointerException e){
                userDB = new UserDB("../"+dataPath+"user.db");
            }
        }
    }
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        loginMenu();
    }
}
