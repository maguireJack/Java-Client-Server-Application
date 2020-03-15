package com.dkit.maguireJack.Daos;

import com.dkit.maguireJack.Exceptions.DaoException;
import com.dkit.maguireJack.Toll.TollEvent;


import java.io.DataOutput;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MySqlTollEventDao extends MySqlDao
{

    public void batchToDatabase(HashMap<String, ArrayList<TollEvent>> table) throws DaoException
    {
        Connection con = null;
        Statement ps = null;
        ResultSet rs = null;
        try
        {
            con = this.getConnection();
            String query;
            int i = 0;

            for (String key : table.keySet())
            {
                    ps = (Statement) con.createStatement();
                    query = "INSERT INTO TollEvents Values ('" + key + "'," + table.get(key).get(i).getCarID() + ",'"+ table.get(key).get(i).getTime() +"')";
                    ps.executeUpdate(query);
                    i++;
            }
        }
        catch (SQLException e)
        {
            throw new DaoException(e.getMessage());
        }finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            } catch (SQLException e)
            {
                throw new DaoException(e.getMessage());
            }
        }
    }

    public HashMap<String, ArrayList<TollEvent>> loadTollEventsTable() throws DaoException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        HashMap<String, ArrayList<TollEvent>> TollEventsByReg = new HashMap<String, ArrayList<TollEvent>>();
        ArrayList<TollEvent> TollEvents = new ArrayList<TollEvent>();

        try
        {
            con = this.getConnection();

            String query = "select * from TollEvents";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next())
            {
                String VehicleReg = rs.getString("numPlate");
                long imageID = rs.getInt("image");
                Instant time = Instant.parse(rs.getString("date"));
                TollEvent tollEvent = new TollEvent(VehicleReg, imageID, time);
                TollEvents.add(tollEvent);
                TollEventsByReg.put(VehicleReg, TollEvents);
            }
        } catch (SQLException e)
        {
            throw new DaoException(e.getMessage());
        } finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            } catch (SQLException e)
            {
                throw new DaoException(e.getMessage());
            }
        }
        return TollEventsByReg;
    }

    public HashMap<String, ArrayList<TollEvent>> loadTollEventsByRegistration(String vehicleReg) throws DaoException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        HashMap<String, ArrayList<TollEvent>> TollEventsByReg = new HashMap<String, ArrayList<TollEvent>>();
        ArrayList<TollEvent> TollEvents = new ArrayList<TollEvent>();

        try
        {
            con = this.getConnection();

            String query = "select * from TollEvents where numPlate=\""+ vehicleReg +" \"";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next())
            {
                String VehicleReg = rs.getString("numPlate");
                long imageID = rs.getInt("image");
                Instant time = Instant.parse(rs.getString("date"));
                TollEvent tollEvent = new TollEvent(VehicleReg, imageID, time);
                TollEvents.add(tollEvent);
                TollEventsByReg.put(VehicleReg, TollEvents);
            }
            System.out.println(TollEventsByReg.toString());
        } catch (SQLException e)
        {
            throw new DaoException(e.getMessage());
        } finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            } catch (SQLException e)
            {
                throw new DaoException(e.getMessage());
            }
        }
        return TollEventsByReg;
    }

    public HashMap<String, ArrayList<TollEvent>> loadTollEventsByDate(String dt) throws DaoException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        HashMap<String, ArrayList<TollEvent>> TollEventsByReg = new HashMap<String, ArrayList<TollEvent>>();
        ArrayList<TollEvent> TollEvents = new ArrayList<TollEvent>();

        try
        {
            con = this.getConnection();

            String query = "select * from TollEvents where date > " + dt;
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next())
            {
                String VehicleReg = rs.getString("numPlate");
                long imageID = rs.getInt("image");
                Instant time = Instant.parse(rs.getString("date"));
                TollEvent tollEvent = new TollEvent(VehicleReg, imageID, time);
                TollEvents.add(tollEvent);
                TollEventsByReg.put(VehicleReg, TollEvents);
            }
            System.out.println(TollEventsByReg.toString());
        } catch (SQLException e)
        {
            throw new DaoException(e.getMessage());
        } finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            } catch (SQLException e)
            {
                throw new DaoException(e.getMessage());
            }
        }
        return TollEventsByReg;
    }
    public HashMap<String, ArrayList<TollEvent>> loadTollEventsByDateToDate(String dt, String ft) throws DaoException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        HashMap<String, ArrayList<TollEvent>> TollEventsByReg = new HashMap<String, ArrayList<TollEvent>>();
        ArrayList<TollEvent> TollEvents = new ArrayList<TollEvent>();

        try
        {
            con = this.getConnection();

            String query = "select * from TollEvents where date between " + dt + "and" + ft;
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next())
            {
                String VehicleReg = rs.getString("numPlate");
                long imageID = rs.getInt("image");
                Instant time = Instant.parse(rs.getString("date"));
                TollEvent tollEvent = new TollEvent(VehicleReg, imageID, time);
                TollEvents.add(tollEvent);
                TollEventsByReg.put(VehicleReg, TollEvents);
            }
            System.out.println(TollEventsByReg.toString());
        } catch (SQLException e)
        {
            throw new DaoException(e.getMessage());
        } finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            } catch (SQLException e)
            {
                throw new DaoException(e.getMessage());
            }
        }
        return TollEventsByReg;
    }

    public HashSet<String> getAllUniqueReg() throws DaoException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        HashSet<String> HashSetReg;
        try
        {
            con = this.getConnection();

            String query = "select * from TollEvents";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            HashSetReg = new HashSet<String>();
            while (rs.next())
            {
                String VehicleReg = rs.getString("numPlate");
                long imageID = rs.getInt("image");
                Instant time = Instant.parse(rs.getString("date"));
                TollEvent tollEvent = new TollEvent(VehicleReg, imageID, time);
                HashSetReg.add(VehicleReg);

            }
            System.out.println(HashSetReg.toString());
        } catch (SQLException e)
        {
            throw new DaoException(e.getMessage());
        } finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            } catch (SQLException e)
            {
                throw new DaoException(e.getMessage());
            }
        }
        return HashSetReg;
    }
}
