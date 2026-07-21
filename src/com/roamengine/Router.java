package com.roamengine;

public class Router
{
    private String ssid;
    private double x, y;
    private double maxRange;

    public Router(String ssid, double x, double y, double maxRange)
    {
        this.ssid = ssid;
        this.x = x;
        this.y = y;
        this.maxRange = maxRange;
    }

    public String getSsid()
    {
        return ssid;
    }

    public double calculateSignalStrength(double deviceX, double deviceY)
    {
        double distance=Math.sqrt(Math.pow(this.x - deviceX, 2) + Math.pow(this.y - deviceY, 2));

        if (distance>=maxRange)
        {
            return 0.0;
        }

        return 100.0 * (1.0 - (distance / maxRange));
    }
}