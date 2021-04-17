Implementation of a Group chat application using MulticastSocket.
A MulticastSocket is a (UDP) DatagramSocket, with additional capabilities for joining “groups” of other multicast hosts on the internet.
The chat application has the capability of sending/receiving messages as well as sharing images between multiple client.
Created two threads in this application: One for accepting the user input (using the java.util.Scanner class) and the other for reading the messages sent from other clients.
