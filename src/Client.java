import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Client {

    public static final String dataPath = "data/";

    int currentNumberOfUsers;
    Database userDB = new Database(dataPath+"user.db");

    public Client(){
        currentNumberOfUsers = userDB.countRows("users") - 1;
    }

    public boolean checkUsernameExists(String username){
        if(userDB.singleQuery("select username from users where username = '" + username + "'").size() == 0) {
            return false;
        }
        else return true;
    }

    public void createUser(String username, String password){
        userDB.insert("insert into users values(" + (currentNumberOfUsers + 1) + ",'"+username+"','"+password+"','"+username+".db',false)");
        File userMusicDB = new File(dataPath+username+".db");
        try {
            userMusicDB.createNewFile();
        } catch (IOException e) {
            System.out.println("Error: Unable to create user database.");
        }
    }

    public boolean login(String username, String password){
        return password.equals(userDB.singleQuery("select password from users where username = '" + username + "'").get(0));
    }

    public void loadUserDB(String username){

    }

    public static void main(String[] args) {

        Client client = new Client();
        System.out.println("Welcome");
        System.out.println("1. User Login");
        System.out.println("2. Create new account");
        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();
        String username,pwd, pwdCheck;
        if(input == 1){
            do{
                System.out.println("Please enter username");
                username = scanner.next();
                if(!client.checkUsernameExists(username)) System.out.println("User does not exist.");
            }while(!client.checkUsernameExists(username));
            do{
                System.out.println("Please enter password");
                pwd = scanner.next();
                if(!client.login(username,pwd)) System.out.println("Wrong password");
            }while(!client.login(username,pwd));


        } else if (input == 2){
            do{
                System.out.println("Please enter username");
                username = scanner.next();

            }while(client.checkUsernameExists(username));
            do {
                System.out.println("PLease enter your password");
                pwd = scanner.next();
                System.out.println("Enter password again");
                pwdCheck = scanner.next();
                if(!pwd.equals(pwdCheck)) System.out.println("password does not match, try again");
            }while(!pwd.equals(pwdCheck));
            client.createUser(username,pwd);

        }


    }
}
