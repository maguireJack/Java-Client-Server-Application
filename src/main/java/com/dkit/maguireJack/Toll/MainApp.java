package com.dkit.maguireJack.Toll;

import com.dkit.maguireJack.Daos.MySqlTollEventDao;
import com.dkit.maguireJack.Daos.MySqlVehicleDao;
import com.dkit.maguireJack.Exceptions.DaoException;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MainApp
{
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";

    public static void main(String[] args)
    {
        TollEventHandler tollEventHandler = new TollEventHandler();
        tollEventHandler.initialize();

        String input;
        Scanner kb = new Scanner(System.in);


        int i = 0;

        while (i != 1)
        {
            System.out.println(ANSI_RED + "These must be executed in order, or there will be no output");
            System.out.println(ANSI_RESET + "Load Registration Numbers (Type:Load)");
            System.out.println("Check Valid Registrations Numbers (Type:Validation)");
            System.out.println("Write Valid events to the Database (Type:Write)");

            input = kb.nextLine();
            switch (input)
            {
                case "Load":
                        tollEventHandler.loadDefaults();
                    break;

                case "Validate":
                    tollEventHandler.checkForValidRegistration();
                    break;

                case "Exit":
                    i = 1;
                    break;

                case "Write":

                    tollEventHandler.writeToDatabase();
                    String choice;
                    System.out.println(ANSI_RESET);
                    System.out.println("DAO Questions");
                    System.out.println("Search by Registration (Type: Registration)");
                    System.out.println("Search by Start Date (Type: From)");
                    System.out.println("Search from Start to End Date (Type: To)");
                    System.out.println("Get All Registrations");
                    System.out.println("Return Details as a Map");
                    choice = kb.nextLine();
                    switch (choice)
                    {
                        case "Registration":
                            System.out.println("Please enter a Vehicle Registration (Case Sensitive)");
                            String reg = kb.nextLine();
                            tollEventHandler.getEventsByRegistration(reg);
                            break;
                        case "From":
                            System.out.println("Please Enter the Day 01-31DD");
                            String day = kb.nextLine();
                            if (day.length() != 2)
                            {
                                System.out.println("Incorrect Format, Please Enter the Day 01-31DD");
                                 day = kb.nextLine();
                            }
                            System.out.println("Please enter the Month 01-12 MM");
                            String month = kb.nextLine();
                            if (month.length() != 2)
                            {
                                System.out.println("Incorrect Format Please enter the Month 01-12 MM");
                                month = kb.nextLine();
                            }
                            System.out.println("Please enter the Year YYYY");
                            String year = kb.nextLine();
                            if (year.length() != 4)
                            {
                                System.out.println("Incorrect Format Please enter the Year YYYY");
                                year = kb.nextLine();
                            }
                            tollEventHandler.getEventsFromDt(day,month,year);
                            break;

                        case "To":
                            System.out.println("Please Enter the Starting Day 01-31DD");
                            day = kb.nextLine();
                            if (day.length() != 2)
                            {
                                System.out.println("Incorrect Format, Please Enter the Day 01-31DD");
                                day = kb.nextLine();
                            }
                            System.out.println("Please enter the Starting Month 01-12 MM");
                            month = kb.nextLine();
                            if (month.length() != 2)
                            {
                                System.out.println("Incorrect Format Please enter the Month 01-12 MM");
                                month = kb.nextLine();
                            }
                            System.out.println("Please enter the Starting Year YYYY");
                            year = kb.nextLine();
                            if (year.length() != 4)
                            {
                                System.out.println("Incorrect Format Please enter the Year YYYY");
                                year = kb.nextLine();
                            }

                            System.out.println("Please Enter the Ending Day 01-31DD");
                            String day1 = kb.nextLine();
                            if (day1.length() != 2)
                            {
                                System.out.println("Incorrect Format, Please Enter the Day 01-31DD");
                                day1 = kb.nextLine();
                            }
                            System.out.println("Please enter the Ending Month 01-12 MM");
                            String month1 = kb.nextLine();
                            if (month1.length() != 2)
                            {
                                System.out.println("Incorrect Format Please enter the Month 01-12 MM");
                                month1 = kb.nextLine();
                            }
                            System.out.println("Please enter the Ending Year YYYY");
                            String year1 = kb.nextLine();
                            if (year1.length() != 4)
                            {
                                System.out.println("Incorrect Format Please enter the Year YYYY");
                                year1 = kb.nextLine();
                            }
                            tollEventHandler.getEventsBetweenDtFt(day, month, year, day1, month1, year1);
                            break;
                    }
                    break;

                default:
                    System.out.println(ANSI_RED + "Unaccepted input, please try keywords such as \"Load\", \"Validate\"");
                    System.out.println(ANSI_RESET + "Load Registration Numbers (Type:Load)");
                    System.out.println("Check Valid Registrations Numbers (Type:Validation)");
                    System.out.println("Write Valid events to the Database (Type:Write)");

                    input = kb.nextLine();

                    break;

            }
        }
    }
}
