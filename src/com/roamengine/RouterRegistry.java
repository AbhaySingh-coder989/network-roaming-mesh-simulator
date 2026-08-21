package com.roamengine;
import java.util.*;

public class RouterRegistry
{
    private Map<String, Router> routers;

    public RouterRegistry()
    {
        this.routers = new HashMap<>();
    }

    public void addRouter(Router router)
    {
        this.routers.put(router.getSsid(), router);
    }

    public Router getRouter(String ssid)
    {
        return this.routers.get(ssid);
    }

    public Collection<Router> getAllRouters()
    {
        return this.routers.values();
    }
}