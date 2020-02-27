package com.dkit.maguireJack.Daos;

import com.dkit.maguireJack.Exceptions.DaoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
    MySqlDao
    implements functionality that is common to all MySQL DAOs
    - getConnection() and freeConnection()
    All MySQL DAOs will extend this class in order to gain the
    connection functionality. This avoid repetition of this code
 */
public class MySqlDao
{
    public Connection getConnection() throws DaoException
    {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/gd2_database";
        String username = "root";
        String password = "";
        Connection con = null;

        try
        {
            Class.forName(driver);
            con = DriverManager.getConnection(url, username, password);
        }
        catch(ClassNotFoundException e)
        {
            System.out.println("Failed to find driver class" + e.getMessage());
            System.exit(1);
        }
        catch(SQLException e)
        {
            System.out.println("Connection failed" + e.getMessage());
            System.exit(2);
        }
        System.out.println("Connected successfully");
        return con;
    }

    public void freeConnection(Connection con) throws DaoException
    {
        try
        {
            if(con != null)
            {
                con.close();
                con = null;
            }
        }
        catch(SQLException e)
        {
            System.out.println("Failed to free the connection" + e.getMessage());
            System.exit(1);
        }
    }
}
