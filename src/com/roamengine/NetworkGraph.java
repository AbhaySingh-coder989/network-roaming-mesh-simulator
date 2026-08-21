package com.roamengine;
import java.util.*;

public class NetworkGraph
{
    private Map<String, List<String>> adjacencyList;
    private String gatewayId;

    public NetworkGraph(String gatewayId)
    {
        this.adjacencyList = new HashMap<>();
        this.gatewayId = gatewayId;
    }

    public void addNode(String ssid)
    {
        if(!adjacencyList.containsKey(ssid))
        {
            adjacencyList.put(ssid, new ArrayList<>());
        }
    }

    public void connect(String ssid1, String ssid2)
    {
        if(adjacencyList.containsKey(ssid1) && adjacencyList.containsKey(ssid2))
        {
            adjacencyList.get(ssid1).add(ssid2);
            adjacencyList.get(ssid2).add(ssid1);
        }
    }

    public int getHopCountToGateway(String startSsid)
    {
        if(startSsid.equals(gatewayId))
        {
            return 0;
        }

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, Integer> distances = new HashMap<>();
        queue.add(startSsid);
        visited.add(startSsid);
        distances.put(startSsid, 0);

        while(!queue.isEmpty())
        {
            String current = queue.poll();
            int currentDist = distances.get(current);
            List<String> neighbors = adjacencyList.get(current);

            if(neighbors != null)
            {
                for(String neighbor : neighbors)
                {
                    if(neighbor.equals(gatewayId))
                    {
                        return currentDist + 1;
                    }

                    if(!visited.contains(neighbor))
                    {
                        visited.add(neighbor);
                        distances.put(neighbor, currentDist + 1);
                        queue.add(neighbor);
                    }
                }
            }
        }
        return 9999; //pq will ignore
    }
}