package org.arsen.cee.hard;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Thor on 20.08.2015.
 */
public class ABusNetwork {

    public static final String OVERALL_REGEX = "([0-9]+,[0-9]+)\\);\\x20(.*)";
    public static final String ROUTE_REGEX = "R[0-9]+=\\[([0-9,]+)\\]";

    public static void main(String[] args) throws IOException {

        File file = new File(args[0]);
        String line;

        Pattern pattern = Pattern.compile(OVERALL_REGEX);

        if (file.isFile()) {
            InputStream is = new FileInputStream(file);

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                System.out.println(findOptimalRoute(line, pattern));
            }
        }
    }

    public static List<Node> initNodeCandidates(List<Integer> candidates, Integer routeNumber) {
        List<Node> result = new ArrayList<Node>();

        for (Integer i : candidates) {
            result.add(new Node(i, routeNumber));
        }

        return result;
    }

    public static void addToBusNetwork(List<Node> candidates, List<Node> busNetwork) {
        c:
        for (Node node : candidates) {
            for (Node station : busNetwork) {
                if (station.getNumber() == node.getNumber()) {
                    station.getRoute().add(node.getRoute().get(0));
                    //addNeighbours(candidates, station);
                    continue c;
                }
            }

            //addNeighbours(candidates, node);

            busNetwork.add(node);
        }
    }

    private static void addNeighbours(List<Node> candidates, Node node) {
        Node targetNode = null;

        for (Node candidateNode : candidates) {
            if (candidateNode.getNumber() == node.getNumber()) {
                targetNode = candidateNode;
                break;
            }
        }

        if (candidates.indexOf(targetNode) != candidates.size() - 1) {
            node.getNeighbors().add(candidates.get(candidates.indexOf(targetNode) + 1));
        }

        if (candidates.indexOf(targetNode) != 0) {
            node.getNeighbors().add(candidates.get(candidates.indexOf(targetNode) - 1));
        }
    }

    private static Integer findOptimalRoute(String line, Pattern pattern) {
        Matcher matcher = pattern.matcher(line);
        matcher.find();

        String points = matcher.group(1);
        String routes = matcher.group(2);

        int[] dests = splitDests(points);

        List<Node> busNetwork = fillBusNetwork(prepareRoutesList(routes));

        return findOptimalRoute(busNetwork, dests);
    }

    private static Integer findOptimalRoute(List<Node> busNetwork, int[] dests) {

        Iterator iterator = busNetwork.iterator();
        Node firstNode = null;
        Node destNode = null;

        while (iterator.hasNext()) {
            Node node = (Node) iterator.next();

            if (node.getNumber() == dests[0]) {
                firstNode = node;

                if (destNode != null) {
                    break;
                } else {
                    continue;
                }
            }

            if (node.getNumber() == dests[1]) {
                destNode = node;

                if (firstNode != null) {
                    break;
                }
            }
        }

        initLengths(firstNode, destNode);

        return destNode.getLength();

    }

    private static void initLengths(Node firstNode, Node destNode) {
        firstNode.setLength(0);
        firstNode.setVisited(true);
        Node targetNode = firstNode;
        Node prev = null;
        Integer currentRoute;

        if (firstNode.getRoute().size() == 1) {
            currentRoute = firstNode.getRoute().get(0);
        } else {
            currentRoute = Integer.MAX_VALUE;
        }

        while (destNode.getLength() == Integer.MAX_VALUE) {
            for (Node node : targetNode.getNeighbors()) {
                if (node.getLength() == Integer.MAX_VALUE) {
                    boolean isOnSameRoute = false;

                    breakpoint:
                    for (Integer route : node.getRoute()) {
                        for (Integer targetRoute : targetNode.getRoute()) {
                            if (route.equals(targetRoute) && route.equals(currentRoute)) {
                                isOnSameRoute = true;
                                break breakpoint;
                            }
                        }
                    }

                    if (isOnSameRoute) {
                        node.setLength(targetNode.getLength() + TIME.ROUTE_TIME.getTime());
                    } else {
                        node.setLength(targetNode.getLength() + TIME.ROUTE_TIME.getTime() + TIME.TRANSFER_TIME.getTime());
                    }
                }
            }

            boolean flag = false;
            int counter = 0;
            Node oldTargetNode = targetNode;


            for (Node node : targetNode.getNeighbors()) {
                if (!node.isVisited()) {
                    counter++;
                }
            }

            if (counter >= 2) {
                for (Node node : targetNode.getNeighbors()) {
                    if (!node.isVisited() && !flag) {
                        targetNode = node;
                        targetNode.setVisited(true);
                        flag = true;
                        continue;
                    } else if (flag) {
                        prev = node;
                    }
                }
            } else {
                for (Node node : targetNode.getNeighbors()) {
                    if (!node.isVisited()) {
                        targetNode = node;
                        targetNode.setVisited(true);
                    }
                }
            }

            if (oldTargetNode == targetNode) {
                targetNode = prev;
            }
        }
    }

    private static int[] splitDests(String points) {
        String[] strings = points.split(",");
        int[] dests = new int[strings.length];

        for (int i = 0; i < strings.length; i++) {
            dests[i] = Integer.parseInt(strings[i]);
        }

        return dests;
    }

    private static List<List<Integer>> prepareRoutesList(String routes) {
        Pattern route_pattern = Pattern.compile(ROUTE_REGEX);
        Matcher route_matcher = route_pattern.matcher(routes);

        List<List<Integer>> routesList = new ArrayList<List<Integer>>();

        while (route_matcher.find()) {
            String routeStations = route_matcher.group(1);
            List<Integer> route = new ArrayList<Integer>();

            for (String s : routeStations.split(",")) {
                route.add(Integer.parseInt(s));
            }

            routesList.add(route);
        }

        return routesList;
    }

    private static List<Node> fillBusNetwork(List<List<Integer>> routesList) {
        List<Node> busNetwork = new ArrayList<Node>();
        Integer routeNumber = 1;

        for (List<Integer> route : routesList) {
            List<Node> nodeCandidates = initNodeCandidates(route, routeNumber++);
            addToBusNetwork(nodeCandidates, busNetwork);
        }

        fillNeighbors(busNetwork, routesList);

        return busNetwork;
    }

    private static void fillNeighbors(List<Node> busNetwork, List<List<Integer>> routesList) {
        for (Node node : busNetwork) {
            breakpoint3:
            for (List<Integer> route : routesList) {

                if (route.contains(node.getNumber())) {

                        if (route.indexOf(node.getNumber()) != route.size() - 1) {
                            for (Node neighbor : busNetwork) {
                                if (neighbor.getNumber().equals(route.get(route.indexOf(node.getNumber()) + 1))) {
                                    node.getNeighbors().add(neighbor);

                                }
                            }
                        }

                        if (route.indexOf(node.getNumber()) != 0) {
                            for (Node neighbor : busNetwork) {
                                if (neighbor.getNumber().equals(route.get(route.indexOf(node.getNumber()) - 1))) {
                                    node.getNeighbors().add(neighbor);

                                }
                            }
                        }
                }
            }
        }
    }

}

class Node {

    private List<Integer> route;
    private Integer number;
    private Integer length = Integer.MAX_VALUE;
    private boolean visited;

    Node(int number, int route) {
        this.number = number;
        this.setRoute(new ArrayList<Integer>());
        this.getRoute().add(route);
    }

    private List<Node> neighbors = new LinkedList<Node>();

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public List<Node> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<Node> neighbors) {
        this.neighbors = neighbors;
    }

    public List<Integer> getRoute() {
        return route;
    }

    public void setRoute(List<Integer> route) {
        this.route = route;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}

enum TIME {
    ROUTE_TIME(7), TRANSFER_TIME(12);

    private int time;

    TIME(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }
}
