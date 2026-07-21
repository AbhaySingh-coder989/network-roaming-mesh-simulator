# network-roaming-mesh-simulator

A Java simulation for the "sticky client" WiFi issue: where devices persist on a far, weak router when a close, strong router is available.

## Problem formulation

Devices don't check if a better connection is available often enough. A mobile device will continue to connect to a far-off, weak router, when a close, strong router is nearby because most devices only check for alternate connections once their connection is already bad. A good signal strength on a connection doesn't necessarily imply a good one if that router connects to the internet via other routers.

## Functional description

This model simulates a group of routers and one moving device. Currently, the signal quality for one router is determined based solely on the distance of the device to the router. Features to be implemented include: rating routers based on name, ordering candidate routers based on signal strength, and a way to judge signal quality based on hop-count and signal (requires a router graph).

## How it's built

- `Router`: Creates a 0-100 score for signal strength based on distance from the device.
- `HashMap<String, Router>`: Store routers by name to access them (in progress).
- `PriorityQueue`: To order routers based on their score to determine which is best (in progress).
- Adjacency list (`HashMap<String, List<String>>`) + BFS, to estimate hops back to the internet/modem (in progress)

## What it is (and isn't)

It's a simulation. It is not real device firmware, and doesn't interact with Wi-Fi hardware, or the 802.11 standard itself. This code is intended to illustrate the decision making of the fix, not to replace real-time network protocols.
