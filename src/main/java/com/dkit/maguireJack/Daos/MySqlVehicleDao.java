package com.dkit.maguireJack.Daos;

import com.dkit.maguireJack.Exceptions.DaoException;
import com.dkit.maguireJack.Toll.TollEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MySqlVehicleDao extends MySqlDao
{

    public HashMap<Integer, String> loadLookupTable() throws DaoException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap<Integer, String> Vehicles = new HashMap<Integer, String>();

        try
        {
            con = this.getConnection();

            String query = "select * from ValidVehicle";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            int count = 1;

            while (rs.next())
            {

                String VehicleReg = rs.getString("numPlate");

                Vehicles.put(count, VehicleReg);
//                try (FileWriter lookupTable = new FileWriter("LookupTable.txt"))
//                {
//                    lookupTable.write(VehicleReg + ",");
//                } catch (IOException e)
//                {
//                    e.printStackTrace();
//                }

                count++;
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
        return Vehicles;
    }

    public void populateDatabase()
    {

    }
}
