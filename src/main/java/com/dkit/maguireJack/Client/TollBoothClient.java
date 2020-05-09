package com.dkit.maguireJack.Client;

import com.dkit.maguireJack.Core.TollBoothServiceDetails;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.dkit.maguireJack.Toll.TollEvent;
import com.dkit.maguireJack.Toll.TollEventHandler;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


public class TollBoothClient
{
    public static void main(String[] args)
    {
        ArrayList<TollEvent> localEvents = new ArrayList<TollEvent>();
        try
        {
            Socket dataSocket = new Socket("127.0.0.1", TollBoothServiceDetails.LISTENING_PORT);
            TollEventHandler te = new TollEventHandler();


            OutputStream out = dataSocket.getOutputStream();
            PrintWriter output = new PrintWriter((new OutputStreamWriter(out)));

            InputStream in = dataSocket.getInputStream();
            Scanner input = new Scanner(new InputStreamReader(in));

            Scanner keyboard = new Scanner(System.in);

            /*
            * By default this wrapper doesnt know how to handle Instants, it will write them in epoch time
            * use the Serialization Feature to write them as normal timestamp
            * Credit To:
            * https://stackoverflow.com/questions/45662820/how-to-set-format-of-string-for-java-time-instant-using-objectmapper
            * */
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

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

                            req = new Request(message);
                            output.println(objectMapper.writeValueAsString(req));
                            output.flush();

                            if(input.hasNextLine())
                            {
                                response = input.nextLine();
                                if(response.equals(TollBoothServiceDetails.SESSION_TERMINATED))
                                {
                                    System.out.println("Session ended");
                                }
                            }
                            else
                            {
                                System.out.println("Connection to server not found, please check for heartbeat");
                            }
                            break;
                        case 1:
                            message = generateEcho(keyboard);

                            output.println(message);
                            output.flush();

                            if(input.hasNextLine()) {
                                response = input.nextLine();
                                System.out.println("Received response: " + response);
                                break;
                            }
                            else
                            {
                                System.out.println("Connection to server not found, please check for heartbeat");
                            }

                        case 2:
                            message = TollBoothServiceDetails.GET_REGISTERED_VEHICLES;
                            //Send message

//                            output.println(req.toString());
//                            objectMapper.writeValue(out, req);
                            req = new Request(message);
                            output.println(objectMapper.writeValueAsString(req));
                            output.flush();


                            response = input.nextLine();
                            System.out.println(response);
                            break;
                        case 3:
                            message = TollBoothServiceDetails.REGISTER_VEHICLE;
                            Instant time = Instant.now();
                            boolean esc = false;
                            long imgid = 0;
                            req = new Request(message);
                            output.println(req.toString());
                            output.flush();

                            System.out.println("Please enter the following details in the format");
                            System.out.println("TollBooth ID");
                            String tbid = kb.nextLine().toUpperCase();
                            System.out.println("Vehicle Registration");
                            String vhreg = kb.nextLine().toUpperCase();
                            System.out.println("Image ID");
                            while(!esc)
                            {
                                if(kb.hasNextLong()) {
                                    imgid = kb.nextLong();
                                    esc = true;
                                }
                                else
                                {
                                    System.out.println("Image ID: ");
                                    kb.nextLine();
                                }
                            }

                            TollEvent clientEvent = new TollEvent(tbid, vhreg, imgid, time);
                            localEvents.add(clientEvent);


                            output.println(objectMapper.writeValueAsString(clientEvent));
                            output.flush();
                            if(input.hasNext())
                            {
                                response = input.nextLine();
                                System.out.println(response);
                            }
                            else
                            {
                                System.out.println("Vehicle Saved Locally");
                                System.out.println("Connection to server not found, please check for heartbeat");
                            }

                            if(response.equals(new Request("RegisteredValidTollEvent").toString()))
                            {
                                localEvents.remove(clientEvent);
                            }

                            break;
                        case 4:
                            message = TollBoothServiceDetails.HEARTBEAT;
                            req = new Request(message);
                            output.println(req.toString());
                            output.flush();

                            if(input.hasNextLine()) {
                                response = input.nextLine();
                                System.out.println("Received response: " + response);

                            }
                            else
                            {
                                System.out.println("Connection to server not found");
                            }

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
            System.out.println("Checking for unsent events, please wait");
            if(dataSocket.isConnected())
            {
                if(!localEvents.isEmpty())
                {
                    for(int i = 0; i <= localEvents.size(); i++) {
                        message = TollBoothServiceDetails.REGISTER_VEHICLE;
                        req = new Request(message);
                        output.println(req.toString());
                        output.flush();

                        output.println(objectMapper.writeValueAsString(localEvents.get(i)));
                        output.flush();
                    }
                }
                else
                {
                    System.out.println("Nothing to Batch");
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
