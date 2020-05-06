package com.dkit.maguireJack.Toll;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class TollEvent implements Comparable<TollEvent>
{
    String tollBoothId;
    String vehicleReg;
    long carID;
    Instant time;



    public TollEvent(String tollBoothId, String vehicleReg, long carID, Instant time)
    {
        this.tollBoothId = tollBoothId;
        this.vehicleReg = vehicleReg;
        this.carID = carID;
        this.time = time;
    }

//    public TollEvent()
//    {
//        super();
//    }

    @JsonIgnore
    @JsonIgnoreProperties
    public TollEvent(String vehicleReg, long carID)
    {
        this.vehicleReg = vehicleReg;
        this.carID = carID;
        this.time = Instant.now();
    }

    @JsonIgnore
    @JsonIgnoreProperties
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
                "TollBoothID: " + tollBoothId + "\n" +
                "Vehicle Registration: '" + vehicleReg + '\'' + "\n"+
                "Image ID: " + carID + "\n" +
                "Local Date Time: " + time + "\n"
                + "\n";
    }


    @Override
    public int compareTo(TollEvent vehicleReg)
    {
        String o1 = this.vehicleReg.replaceAll("[0-9]", "");
        String o2 = vehicleReg.vehicleReg.replaceAll("[0-9]", "");

        return o1.compareTo(o2);
    }
}
