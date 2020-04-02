package com.dkit.maguireJack.Server;

import com.dkit.maguireJack.Client.Request;
import com.dkit.maguireJack.Core.TollBoothServiceDetails;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;
import com.dkit.maguireJack.Toll.TollEventHandler;
import org.w3c.dom.ls.LSOutput;

public class TollBoothServiceThread extends Thread
{
    private Socket dataSocket;
    private Scanner input;
    private PrintWriter output;
    private int number;

    public TollBoothServiceThread(ThreadGroup group, String name, Socket dataSocket, int number)
    {
        super(group, name);
        try
        {
            this.dataSocket = dataSocket;
            this.number = number;
            input = new Scanner(new InputStreamReader(this.dataSocket.getInputStream()));
            output = new PrintWriter(this.dataSocket.getOutputStream());
        }
        catch(IOException ioe)
        {
            System.out.println(ioe.getMessage());
        }
    }

    @Override
    public void run()
    {
        //Loop
        //Wait for message
        //Process message
        //Send response
        //TODO Homework part one write the rest of this method
        //TODO Homework part two pair up with someone from the class. Set up port forwarding on both of your routers. One runs the server and the other connected and then switch.
        //Will write the solution on Thursday
        //Set up variables for communication with the client
        String incomingMessage = "";
        String response;
        TollEventHandler te = new TollEventHandler();

        try
        {
            //while the client does not want to end the session
            while(!incomingMessage.equals(TollBoothServiceDetails.END_SESSION))
            {
                response = null;

                //Take input from the client
                incomingMessage = input.nextLine();
                System.out.println("Received message: " + incomingMessage);

                //Break the input up into components
                String[] components = incomingMessage.split(TollBoothServiceDetails.COMMAND_SEPARATOR);

                //Process the information supplied by the client
                if(components[0].equals(TollBoothServiceDetails.ECHO))
                {
                    StringBuffer echoMessage = new StringBuffer("");

                    if(components.length > 1)
                    {
                        echoMessage.append(components[1]);
                        //What if the user had %% in their message
                        for(int i=2; i < components.length; ++i)
                        {
                            echoMessage.append(TollBoothServiceDetails.COMMAND_SEPARATOR);
                            echoMessage.append(components[i]);
                        }
                    }
                    response = echoMessage.toString();
                }
                else if(components[0].equals(new Request(TollBoothServiceDetails.GET_REGISTERED_VEHICLES)))
                {
                    System.out.println("Found");
                    response = "Found";
                }
                else if(components[0].equals(TollBoothServiceDetails.END_SESSION))
                {
                    response = TollBoothServiceDetails.SESSION_TERMINATED;
                }
                else
                {
                    response = TollBoothServiceDetails.UNRECOGNISED;
                }

                //Send back the response
                output.println(response);
                output.flush();
            }
        }
        catch(NoSuchElementException nse)
        {
            System.out.println(nse.getMessage());
        }
        finally
        {
            try
            {
                System.out.println("\n Closing connection with client #" + number + "...");
                dataSocket.close();
            }
            catch(IOException ioe)
            {
                System.out.println("Unable to disconnect: " + ioe.getMessage());
                System.exit(1);
            }
        }
    }
}
