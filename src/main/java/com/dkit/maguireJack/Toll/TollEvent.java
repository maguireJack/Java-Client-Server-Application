package com.dkit.maguireJack.Toll;

import java.time.Instant;

public class TollEvent
{
    String vehicleReg;
    long carID;
    Instant time;

    public TollEvent(String vehicleReg, long carID)
    {
        this.vehicleReg = vehicleReg;
        this.carID = carID;
        this.time = Instant.now();
    }
    public TollEvent(String vehicleReg, long carID, Instant time)
    {
        this.vehicleReg = vehicleReg;
        this.carID = carID;
        this.time = time;
    }

    public String getVehicleReg()
    {
        return vehicleReg;
    }

    public long getCarID()
    {
        return carID;
    }

    public Instant getTime()
    {
        return time;
    }

    @Override
    public String toString()
    {
        return "TollEvent" +"\n" +
                "Vehicle Registration: '" + vehicleReg + '\'' + "\n"+
                "Image ID: " + carID + "\n" +
                "time: " + time + "\n"
                + "\n";
    }
}
