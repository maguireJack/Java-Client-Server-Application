package com.dkit.maguireJack.Server;


import com.dkit.maguireJack.Core.TollBoothServiceDetails;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ComboStreamServer
{
    public static void main(String[] args)
    {
        try
        {
            //Set up the listening socket
            ServerSocket listeningSocket = new ServerSocket(TollBoothServiceDetails.LISTENING_PORT);

            //Set up a ThreadGroup to manage all of the client threads
            ThreadGroup clientThreadGroup = new ThreadGroup("Client threads");
            //Place more emphasis on accepting clients than processing them
            //by setting their priority to be one less than the main thread
            clientThreadGroup.setMaxPriority(Thread.currentThread().getPriority()-1);

            //Do the main logic of the server
            boolean continueRunning = true;
            int threadCount = 0;

            while(continueRunning)
            {
                //Wait for incoming connection and build communication link
                Socket dataSocket = listeningSocket.accept();

                threadCount++;
                System.out.println("The server has now accepted " + threadCount + " clients");

                //Build the thread
                /* Need to give the thread:
                1) The group to be stored in
                2) a name
                3) a socket to communicate through
                4) any other information that should be shared
                 */
                TollBoothServiceThread newClient = new TollBoothServiceThread(clientThreadGroup, dataSocket.getInetAddress()+"", dataSocket, threadCount);
                newClient.start();
            }
            listeningSocket.close();
        }
        catch(IOException ioe)
        {
            System.out.println(ioe.getMessage());
        }
    }
}
