package com.roamengine;
import java.util.*;

public class SimulationEngine
{
    private static final double ROAMING_THRESHOLD = 15.0;

    public static void main(String[] args)
    {
        NetworkMap meshMap = new NetworkMap("Main_Modem");

        meshMap.addRouter(new Router("Main_Modem", 0.0, 0.0, 40.0));
        meshMap.addRouter(new Router("AP_Hallway", 25.0, 0.0, 40.0));
        meshMap.addRouter(new Router("AP_Bedroom", 60.0, 0.0, 50.0));

        meshMap.connectRouters("Main_Modem", "AP_Hallway");
        meshMap.connectRouters("AP_Hallway", "AP_Bedroom");

        String currentConnectedSsid = "Main_Modem";
        System.out.println("Starting Wi-Fi Handoff Simulation...\n");

        for(double deviceX=0; deviceX<=60; deviceX+= 12)
        {
            System.out.println("Walking to X = " + deviceX);

            PriorityQueue<RouterCostTuple> pathHeap = new PriorityQueue<>();

            for(Router router : meshMap.getAllRouters())
            {
                double signal=router.calculateSignalStrength(deviceX, 0.0);

                if(signal<=0)
                {
                    continue;
                }

                double signalLossCost = 100.0 - signal;
                int hopCount = meshMap.getHopCountToGateway(router.getSsid());
                double hopPenaltyCost = hopCount * 20.0;
                double totalPathCost = signalLossCost + hopPenaltyCost;
                pathHeap.add(new RouterCostTuple(router.getSsid(), totalPathCost));
            }

            RouterCostTuple optimalCandidate = pathHeap.peek();
            if(optimalCandidate == null)
            {
                System.out.println("\tNo signal found here.");
                continue;
            }

            Router currentRouter = meshMap.getRouter(currentConnectedSsid);
            double currentSignal = currentRouter.calculateSignalStrength(deviceX, 0.0);
            int currentHops = meshMap.getHopCountToGateway(currentConnectedSsid);
            double currentTotalCost = (100.0 - currentSignal) + (currentHops * 20.0);

            System.out.println("\tCurrent Router: " + currentConnectedSsid + " (Cost: " + currentTotalCost + ")");
            System.out.println("\tBest Available: " + optimalCandidate.ssid + " (Cost: " + optimalCandidate.cost + ")");

            if(currentTotalCost - optimalCandidate.cost > ROAMING_THRESHOLD)
            {
                System.out.println("\t Better signal found! Switching to: " + optimalCandidate.ssid);
                currentConnectedSsid = optimalCandidate.ssid;
            }
            else
            {
                System.out.println("\t Signal is fine. Staying connected to: " + currentConnectedSsid);
            }
            System.out.println();
        }
    }
}