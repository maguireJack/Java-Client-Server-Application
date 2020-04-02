package com.dkit.maguireJack.Client;

import com.dkit.maguireJack.Core.TollBoothServiceDetails;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;
import com.fasterxml.jackson.databind.ObjectMapper;


public class TollBoothClient
{
    public static void main(String[] args)
    {
        try
        {
            //Step 1: Establish a connection with server
            //Like a phone call, first thing you do is
            //dial the number you want to talk to
            Socket dataSocket = new Socket("localhost", TollBoothServiceDetails.LISTENING_PORT);

            //Step 2: Build the output and input streams
            OutputStream out = dataSocket.getOutputStream();
            PrintWriter output = new PrintWriter((new OutputStreamWriter(out)));

            InputStream in = dataSocket.getInputStream();
            Scanner input = new Scanner(new InputStreamReader(in));

            Scanner keyboard = new Scanner(System.in);
            ObjectMapper objectMapper = new ObjectMapper();
            String message = "";


            while(!message.equals(TollBoothServiceDetails.END_SESSION))
            {
                displayMenu();
                int choice = getNumber(keyboard);
                String response = "";

                if(choice >=0 && choice < 3)
                {
                    switch(choice)
                    {
                        case 0:
                            message = TollBoothServiceDetails.END_SESSION;

                            //Send message
                            output.println(message);
                            output.flush();

                            response = input.nextLine();
                            if(response.equals(TollBoothServiceDetails.SESSION_TERMINATED))
                            {
                                System.out.println("Session ended");
                            }
                            break;
                        case 1:
                            message = generateEcho(keyboard);

                            //Send message
                            output.println(message);
                            output.flush();

                            //Get response
                            response = input.nextLine();
                            System.out.println("Received response: " + response);
                            break;

                        case 2:
                            message = TollBoothServiceDetails.GET_REGISTERED_VEHICLES;
                            //Send message
                            Request req = new Request(message);
                            objectMapper.writeValue(out, req);

                            output.flush();

                            //Get response
                            response = input.nextLine();
                            System.out.println("Registered Vehicles : " + response);
                            break;
                    }
                    if(response.equals(TollBoothServiceDetails.UNRECOGNISED))
                    {
                        System.out.println("Sorry, that request is not recognised");
                    }
                }
                else
                {
                    System.out.println("Please select an option from the menu");
                }
            }
            System.out.println("Thank you for using the Combo service system");
            dataSocket.close();
        }
        catch(UnknownHostException uhe)
        {
            System.out.println(uhe.getMessage());
        }
        catch(IOException ioe)
        {
            System.out.println(ioe.getMessage());
        }
    }

    public static void displayMenu()
    {
        System.out.println("0) to exit");
        System.out.println("1) to echo a message");
        System.out.println("2) to date and time");
    }

    public static int getNumber(Scanner keyboard)
    {
        boolean numberEntered = false;
        int number = 0;
        while(!numberEntered)
        {
            try
            {
                number = keyboard.nextInt();
                numberEntered = true;
            }
            catch(InputMismatchException e)
            {
                System.out.println("Please enter a number: ");
                keyboard.nextLine();
            }
        }
        keyboard.nextLine();
        return number;
    }

    public static String generateEcho(Scanner keyboard)
    {
        //Set up the command text
        StringBuffer message = new StringBuffer(TollBoothServiceDetails.ECHO);
        message.append(TollBoothServiceDetails.COMMAND_SEPARATOR);
        //Get the message to be echoed
        System.out.println("What would you like to echo: ");
        String echo = keyboard.nextLine();
        message.append(echo);

        return message.toString();
    }
}
