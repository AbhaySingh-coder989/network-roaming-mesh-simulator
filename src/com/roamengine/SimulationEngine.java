package com.roamengine;

import java.util.*;

public class SimulationEngine
{
    private static final double HANDOFF_THRESHOLD = 15.0; //not 0 bc thrashing

    public static void main(String[] args)
    {
        RouterRegistry registry = new RouterRegistry();
        registry.addRouter(new Router("Gateway", 0.0, 0.0, 100.0));
        registry.addRouter(new Router("Node_A", 30.0, 0.0, 100.0));
        registry.addRouter(new Router("Node_B", 60.0, 0.0, 100.0));

        NetworkGraph graph = new NetworkGraph("Gateway");
        graph.addNode("Gateway");
        graph.addNode("Node_A");
        graph.addNode("Node_B");
        graph.connect("Gateway", "Node_A");
        graph.connect("Node_A", "Node_B");
        String currentSsid = "Gateway";

        double deviceY = 0.0;
        for(double deviceX = 0; deviceX <= 60; deviceX += 12)
        {
            String bestSsid = findBestRouter(registry, graph, deviceX, deviceY);
            if(bestSsid == null)
            {
                System.out.println("Position X=" + deviceX + " | Out of range of all routers.");
                continue;
            }
            Router currentRouter = registry.getRouter(currentSsid);
            double currentCost = calculateCost(currentRouter, deviceX, deviceY, graph);
            Router bestRouter = registry.getRouter(bestSsid);
            double bestCost = calculateCost(bestRouter, deviceX, deviceY, graph);
            boolean switched = false;
            String previousSsid = currentSsid;

            if(!currentSsid.equals(bestSsid) && (currentCost - bestCost) > HANDOFF_THRESHOLD)
            {
                currentSsid = bestSsid;
                switched = true;
            }

            if(switched)
            {
                System.out.println("Position X=" + deviceX + " | Handoff Triggered! Roamed from " + previousSsid + " to " + currentSsid);
            }

            else
            {
                System.out.println("Position X=" + deviceX + " | Maintained connection to " + currentSsid);
            }
        }
    }

    private static double calculateCost(Router router, double deviceX, double deviceY, NetworkGraph graph)
    {
        double signal = router.calculateSignalStrength(deviceX, deviceY);
        int hops = graph.getHopCountToGateway(router.getSsid());
        double signalPenalty = 100.0 - signal;
        double hopPenalty = hops * 20.0;
        return signalPenalty + hopPenalty;
    }

    private static String findBestRouter(RouterRegistry registry, NetworkGraph graph, double deviceX, double deviceY)
    {
        PriorityQueue<RouterCostTuple> pq = new PriorityQueue<>();
        for(Router router : registry.getAllRouters())
        {
            double signal = router.calculateSignalStrength(deviceX, deviceY);

            if(signal > 0)
            {
                double cost = calculateCost(router, deviceX, deviceY, graph);
                pq.add(new RouterCostTuple(router.getSsid(), cost));
            }
        }

        if(pq.isEmpty())
        {
            return null;
        }

        return pq.poll().ssid;
    }
}