package com.roamengine;
import java.util.*;

public class NetworkMap
{
    private Map<String, Router> routerRegistry;
    private Map<String, List<String>> adjacencyList;
    private String gatewayId;

    public NetworkMap(String gatewayId)
    {
        this.routerRegistry = new HashMap<>();
        this.adjacencyList = new HashMap<>();
        this.gatewayId = gatewayId;
    }

    public void addRouter(Router router)
    {
        routerRegistry.put(router.getSsid(), router);
        adjacencyList.putIfAbsent(router.getSsid(), new ArrayList<>());
    }

    public void connectRouters(String ssid1, String ssid2)
    {
        if(adjacencyList.containsKey(ssid1) && adjacencyList.containsKey(ssid2))
        {
            adjacencyList.get(ssid1).add(ssid2);
            adjacencyList.get(ssid2).add(ssid1);
        }
    }

    public Router getRouter(String ssid)
    {
        return routerRegistry.get(ssid);
    }

    public Collection<Router> getAllRouters()
    {
        return routerRegistry.values();
    }

    public int getHopCountToGateway(String startSsid)
    {
        if(startSsid.equals(gatewayId))
            return 0;

        Queue<String> queue = new LinkedList<>();
        Map<String, Integer> distanceMap = new HashMap<>();

        queue.add(startSsid);
        distanceMap.put(startSsid, 0);

        while(!queue.isEmpty())
        {
            String current = queue.poll();
            int currentHops = distanceMap.get(current);
            for(String neighbor : adjacencyList.getOrDefault(current, new ArrayList<>()))
            {
                if(neighbor.equals(gatewayId))
                {
                    return currentHops + 1;
                }

                if(!distanceMap.containsKey(neighbor))
                {
                    distanceMap.put(neighbor, currentHops + 1);
                    queue.add(neighbor);
                }
            }
        }
        return Integer.MAX_VALUE;
    }
}