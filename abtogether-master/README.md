# Abalone Game 
This is the read me to our Abalone game. Abalone is a 2 player board game. https://en.wikipedia.org/wiki/Abalone_(board_game)
We followed the model-view-controller architecture.

# Overview
This is a 2 board game which can be run on the TUI or GUI implemented in the view. The controller enforces the rules of Abalone. There is test coverage for this application of over 50%. It runs on a server socket so that you can play with someone using a different computer by connecting to the same port. The protocol for the way the networking was selected is also avalable in this project. This was a university project. 

# To run 
We have built a client-server board game. 
To run our program first run the Server.java and then run multiple instances of Client.java which will connect clients to the server. 
To run muliple clients select run in parrallel option in your IDE.

Here each client can input there nickname.
If you are the first client to connect to the server you can select The game mode you wish to play. 
Other possible requests can also be made in accordance with our groups protocol


# Gui add-on javafx required 
The gui was partly implemented. The base of the view is there and only if a 
legal selection of marbles is made a move can occur.
To move click the button "clear selection" and click on the marbles you wish to move 
Then click the "submit" button and a direction via the keys R,D,C,V,G or T
which are placed arround the F key. 

To run our application GUI you need to have JavaFx.(view)
If you have jdk 1.8 or 9 you do NOT need any extra files 
BUT
If you use Java 11 you DO need to download a seperate file to run 
our javafx GUI.

You can find the jar files here https://gluonhq.com/products/javafx/
Download the JavaFx 11.0.2 SDK assuming you have jdk11 for 
you mac, windows or linux machine. 

Configure JavaFx in your IDE 
 If you use eclipse skipe ahead.It is highly advisable to use IntelliJ.
This link can aid you -- https://openjfx.io/openjfx-docs/#IDE-Intellij 

Follow the Non-modular projects section near the top.
Note that for step 4 "Add VM options" the second approach is more 
reliable in my opinion. 

Note - the steps are the same for JavaFx 11 and 13. 




