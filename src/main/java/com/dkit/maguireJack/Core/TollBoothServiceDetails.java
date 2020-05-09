package com.dkit.maguireJack.Core;

public class TollBoothServiceDetails
{
    public static final int LISTENING_PORT = 50001;

    //Breaking characters
    public static final String COMMAND_SEPARATOR = "%%";

    //Command strings
    public static final String END_SESSION = "QUIT";
    public static final String ECHO = "ECHO";
    public static final String GET_REGISTERED_VEHICLES = "GetRegisteredVehicles";
    public static final String REGISTER_VEHICLE = "RegisterVehicle";
    public static final String HEARTBEAT = "Heartbeat";
    public static final String BATCH = "Batch";

    //Response strings
    public static final String UNRECOGNISED = "UNKNOWN_COMMAND";
    public static final String SESSION_TERMINATED = "GOODBYE";
}
