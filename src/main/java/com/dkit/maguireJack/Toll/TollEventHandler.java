package com.dkit.maguireJack.Toll;

import com.dkit.maguireJack.Daos.MySqlTollEventDao;
import com.dkit.maguireJack.Daos.MySqlVehicleDao;
import com.dkit.maguireJack.Exceptions.DaoException;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

public class TollEventHandler
{
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";

    private HashMap<Integer, String> loadedVehicles;
    ArrayList<TollEvent> tollEventsByReg = new ArrayList<TollEvent>();
    private MySqlTollEventDao tollEventDao = new MySqlTollEventDao();
    private HashMap<String, ArrayList<TollEvent>>loadedEvents = new HashMap<String, ArrayList<TollEvent>>();
    private HashMap<String, ArrayList<TollEvent>>validEvents = new HashMap<String, ArrayList<TollEvent>>();
    private HashMap<Integer, String> invalidVehicles = new HashMap<Integer, String>();

    public void initialize()
        {

            MySqlVehicleDao vehicleDao = new MySqlVehicleDao();


            try
            {
                 loadedVehicles = vehicleDao.loadLookupTable();
//                 loadedEvents = tollEventDao.loadLookupTable();

            } catch (DaoException e)
            {
                System.out.println(ANSI_RED + "Error, Table Not Loaded");
                System.out.println(ANSI_RESET);
            }
        }

        public void loadDefaults()
        {
            File readFile = new File("Toll-Events.csv");
            int line = 0;
            try (Scanner input = new Scanner(readFile))
            {
                if (!input.hasNext())
                {
                    throw new NullPointerException();
                }
                while (input.hasNext())
                {
                    String tollEvent = input.next();

                    String[] vehicleDetails = tollEvent.split(";");
                    String vehicleReg = vehicleDetails[0];
                    int imageID = Integer.parseInt(vehicleDetails[1]);
                    Instant now = Instant.parse(vehicleDetails[2]);
                    TollEvent newTollEvent = new TollEvent(vehicleReg, imageID, now);
                    tollEventsByReg.add(newTollEvent);
                    loadedEvents.put(vehicleReg, tollEventsByReg);
                }
                System.out.println(ANSI_GREEN + "Loaded Vehicles Succesfully");
                System.out.println(ANSI_RESET);
            }
            catch (IOException e)
            {
                System.out.println("Unable to read file " + e.getMessage());
            }

        }

        public void writeToDatabase()
        {
            try
            {
            tollEventDao.batchToDatabase(validEvents);
                System.out.println(ANSI_GREEN + "Database Succesfully Loaded");
            }catch (DaoException e)
            {
                System.out.println(ANSI_RED + "Unable to batch to database");
                System.out.println(e.getMessage());
            }

        }

        public void checkForValidRegistration()
        {

            for(String key : loadedEvents.keySet())
            {
                System.out.println(loadedEvents.toString());
                if (loadedVehicles.values().contains(key))
                {

                    System.out.println(ANSI_GREEN + "Valid");
                    validEvents.put(key, loadedEvents.get(key));

                }
                else
                {
                    int i = 1;
                    System.out.println(ANSI_RED + "Invalid");
                    invalidVehicles.put(i, key);
                    i++;
                }
            }
            System.out.println(validEvents.toString());
            System.out.println(ANSI_RESET);
        }

        public void getEventsByRegistration(String reg)
        {
            try
            {
                tollEventDao.loadTollEventsByRegistration(reg);
            }catch (DaoException e)
            {
                System.out.println(e.getMessage());
            }
        }

        public void getEventsFromDt(String day, String month, String year)
        {
            String dt = year+"-"+month+"-"+day;
            try
            {
                tollEventDao.loadTollEventsByDate(dt);
            }catch (DaoException e)
            {
                System.out.println(e.getMessage());
            }
        }
        public void getEventsBetweenDtFt(String day, String month, String year, String day1, String month1, String year1)
        {
            String dt = year+"-"+month+"-"+day;
            String ft = year1+"-"+month1+"-"+day1;

            try
            {
                tollEventDao.loadTollEventsByDateToDate(dt, ft);
            }
            catch (DaoException e)
            {
                System.out.println(e.getMessage());
            }
        }

        public void printAllUniqueReg()
        {
            try
            {
                HashMap<String, ArrayList<TollEvent>> UniqueReg = tollEventDao.loadTollEventsTable();
                List<ArrayList<TollEvent>> ListOfEvents = new ArrayList<>(UniqueReg.values());

                List<TollEvent> ArrayListOfEvents = new ArrayList<>();
                for (int i = 0; i < ListOfEvents.size(); i++)
                {
                    for (int j = 0; j < ListOfEvents.get(i).size(); j++)
                    {
                        ArrayListOfEvents.add(ListOfEvents.get(i).get(j));
                    }

                }
                Set<TollEvent> removeDuplicates = new TreeSet<>(ArrayListOfEvents);
                ArrayListOfEvents = new ArrayList<>(removeDuplicates);
                //Tried LinkedHastSet couldnt get it to work the way I intended
                Collections.sort(ArrayListOfEvents);
                System.out.println(ArrayListOfEvents);
            }
            catch (DaoException e)
            {
                System.out.println(e.getMessage());
            }
        }

        public HashMap<String, ArrayList<TollEvent>> loadMap()
        {
            try
            {
                HashMap<String, ArrayList<TollEvent>> loadedMap = tollEventDao.loadTollEventsTable();
                System.out.println(ANSI_GREEN + "Returned as HashMap");
                return loadedMap;
            }catch (DaoException e)
            {
                System.out.println(e.getMessage());
            }
            System.out.println(ANSI_RED +"No Maps Loaded, Check for database population");
            System.out.println(ANSI_RESET);
            return null;

        }
}
