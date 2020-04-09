package com.dkit.maguireJack.Client;

public class Request
{
    public String packetType;

    public Request(String packetType)
    {
        this.packetType = packetType;

    }

    @Override
    public String toString() {
        return "{" + "\"packetType\"" + ":" + "\"" + packetType + "\"" + '}';
    }
}
