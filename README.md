# network-roaming-mesh-simulator

A Java simulation for the "sticky client" WiFi issue: where devices persist on a far, weak router when a close, strong router is available.

## Problem formulation
Devices don't check if a better connection is available often enough. A mobile device will continue to connect to a far-off, weak router, when a close, strong router is nearby because most devices only check for alternate connections once their connection is already bad. A good signal strength on a connection doesn't necessarily imply a good connection to the internet, because that router might reach the internet only by going through other routers.

## Functional description
The model implements a number of routers and a device moving between them. It evaluates each router's signal strength using distance, looks up routers by name, sorts candidate routers by a combined cost of signal strength and hop count back to the gateway (using a graph implementation), and decides when to switch connections based on that comparison.

## How it's built
- `Router`, turns distance into a 0-100 signal score
- `RouterRegistry` (`HashMap<String, Router>`), fast lookup by router name
- `PriorityQueue` of `RouterCostTuple`, ranks candidate routers by combined signal + hop cost
- `NetworkGraph`, an adjacency list (`HashMap<String, List<String>>`) + BFS, counts hops back to the modem
- `SimulationEngine`, ties everything together and decides when to hand off between routers

## What it is (and isn't)
This is just a simulation, not real device firmware. It doesn't interact with Wi-Fi hardware or the 802.11 standard itself. This code is intended to illustrate the decision making of the fix, not to replace real-time network protocols.

## Running it
From the project root, with the five classes under `src/com/roamengine/`:

```bash
javac -d out src/com/roamengine/*.java
java -cp out com.roamengine.SimulationEngine
```

Or open the project in IntelliJ and run `SimulationEngine` directly.

Example output:
```
Position X=0.0 | Maintained connection to Gateway
Position X=12.0 | Maintained connection to Gateway
Position X=24.0 | Maintained connection to Gateway
Position X=36.0 | Maintained connection to Gateway
Position X=48.0 | Maintained connection to Gateway
Position X=60.0 | Handoff Triggered! Roamed from Gateway to Node_B
```

## Why I built this
I'd have to notice the connection was dead, whether it be during a game or a download, then manually disconnect and reconnect just to get back online. Once I found out this was an actual, recognized problem called the "sticky client", I wanted to see if I could find a way to fix it myself. Finishing it is about more than solving that one annoyance, though; it's a way to test whether what I've learned in computer science actually holds up against a real problem, and to prove to myself I can find a solution to something I noticed on my own, not just something I was assigned.