package com.roamengine;

public class RouterCostTuple implements Comparable<RouterCostTuple>
{
    public String ssid;
    public double cost;

    public RouterCostTuple(String ssid, double cost)
    {
        this.ssid = ssid;
        this.cost = cost;
    }

    @Override
    public int compareTo(RouterCostTuple other)
    {
        return Double.compare(this.cost, other.cost);
    }
}