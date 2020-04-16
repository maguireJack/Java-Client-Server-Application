package com.dkit.maguireJack.Client;

import com.dkit.maguireJack.Core.TollBoothServiceDetails;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.dkit.maguireJack.Toll.TollEventHandler;
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
            Socket dataSocket = new Socket("109.76.42.206", TollBoothServiceDetails.LISTENING_PORT);
            TollEventHandler te = new TollEventHandler();

            //Step 2: Build the output and input streams
            OutputStream out = dataSocket.getOutputStream();
            PrintWriter output = new PrintWriter((new OutputStreamWriter(out)));

            InputStream in = dataSocket.getInputStream();
            Scanner input = new Scanner(new InputStreamReader(in));

            Scanner keyboard = new Scanner(System.in);
            ObjectMapper objectMapper = new ObjectMapper();
            String message = "";
            Request req;
            Scanner kb = new Scanner(System.in);


            while(!message.equals(TollBoothServiceDetails.END_SESSION))
            {
                displayMenu();
                int choice = getNumber(keyboard);
                String response = "";

                if(choice >=0 && choice < 5)
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
                            req = new Request(message);
                            output.println(req.toString());
//                            objectMapper.writeValue(out, req);
                            output.flush();

                            while(input.hasNext())
                            {
                                response = input.nextLine();
                                System.out.println(response);
                            }
                            //Get response

                            System.out.println("Registered Vehicles : " + response);
                            break;
                        case 3:
                            message = TollBoothServiceDetails.REGISTER_VEHICLE;
                            Instant time = Instant.now();
                            req = new Request(message);
                            output.println(req.toString());
                            output.flush();
                            System.out.println("Please enter the following details in the format");
                            System.out.println("TollBoothId" + ',' + "VehicleRegistration" + ',' + "VehicleImageID" + ',');
                            String ValidEvent;
                            ValidEvent = kb.nextLine();
                            String tollEvent = objectMapper.writeValueAsString(te.loadDefaultsByString(ValidEvent));
                            output.println(tollEvent);
                            break;
                        case 4:
                            message = TollBoothServiceDetails.HEARTBEAT;
                            req = new Request(message);
                            output.println(req.toString());
                            output.flush();
                            response = input.nextLine();
                            System.out.println("Received response: " + response);

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
        System.out.println("2) to get all registered vehicles");
        System.out.println("3) to register a new vehicle");
        System.out.println("4) to check for heartbeat");
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
