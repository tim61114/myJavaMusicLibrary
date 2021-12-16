Music Library ver. 1.0

This is a Music Library with user system, where each user has their own database and data directory.
We can create new users or use the example user provided. (Which is user "test" with password "test".)
There is also an admin user that can delete users. (Which is user "admin" with password "admin".)

The Login Menu looks like 

Welcome
1. User Login.
2. Create new account.
3. Exit the program.

Users can choose to log in, create a new account or exit the program by inputting the index.

After logging in, the main menu will be shown as below

Menu:
1. My library
2. Shuffle
3. Import songs (from album)
4. Create new Playlist
5. Export my playlists
6. Play next song
7. View now playing list
8. Play/Pause
9. What am I listening?
10. Logout

The Library is where users can see their songs, artists and albums (if it's not an empty library)

Shuffle is to play all songs in random order.

Import songs is to fetch data from musicbrainz given the name of the artist and the album. 

Create new playlists is to create a new customized playlist where the user can choose what to be in the playlist.

Export my playlists is to export the users' all playlists to xml files.

Play next song is to switch to next song in the song list.

View now playing list shows the current list you are playing. (Like playing the shuffle list or playing songs starting from the middle.)

What am I listening is to fetch the song, album artist description from theAudioDB (if is provided). Normally it does NOT work on classical music, please try it on pop songs.

Logout is to log out of the current user, brings the user back to the Login menu.




This Music Library is mostly encapsulated, which the classes can be roughly separated into 5 categories.

1. Song-data related - Song, Artist, Album, Playlist
2. Database related - Database, MusicDB, UserDB
3. The Library
4. The Client
5. The RestAPI related - DataFetcher, Parser

The song-data related classes: 
    Mostly getters and setters, or simple methods to turn stored data into desired form.

The Database related classes: 
    MusicDB and UserDB extends the Database class, where the Database class implements the lowest level of SQL, where MusicDB and UserDB 
    has upper level methods and lower level SQL methods.

The Library: 
    The Library handles the connection between database and java, and is the main core. The library calls the methods from musicDB classes
    and also has classes to check the stored data. After a user login, a library of the user is created, where the data of the user is 
    read into the user's library.

The Client:
    The Client is where the menu are stored, and also has methods to create new users, and also load the user's database and by reading the
    db, creating a new library of the user.

The RestAPI related classes:
    The DataFetcher is the lower level class which fetches the Strings of json data from either musicbrainz or theAudioDB and passes to 
    Parser, and is then parsed into data such as artist name, song name etc.


This Library is a clean project, so it did not contain the classes used in previous HWs, because I was trying to see how far can I go 
designing the whole structure on my own. Turns out the structure is acceptable, and there are plenty of implementations and functions 
that can be extended afterwards. Sadly I did not have the time or the strength to finish the GUI, so that's probably the thing I'll do 
this winter. It was a great fun designing the structure and also thinking about the possibilities this project can create.