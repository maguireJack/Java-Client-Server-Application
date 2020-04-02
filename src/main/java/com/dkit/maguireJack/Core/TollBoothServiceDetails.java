package com.dkit.maguireJack.Core;

public class TollBoothServiceDetails
{
    public static final int LISTENING_PORT = 50000;

    //Breaking characters
    public static final String COMMAND_SEPARATOR = "%%";

    //Command strings
    public static final String END_SESSION = "QUIT";
    public static final String ECHO = "ECHO";
    public static final String GET_REGISTERED_VEHICLES = "GetRegisteredVehicles";

    //Response strings
    public static final String UNRECOGNISED = "UNKNOWN_COMMAND";
    public static final String SESSION_TERMINATED = "GOODBYE";
}
