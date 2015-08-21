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
        for (Node node : candidates) {

            if (candidates.indexOf(node) != candidates.size() - 1) {
                node.getNeighbors().put(candidates.get(candidates.indexOf(node) + 1), TIME.ROUTE_TIME);
            }

            if (candidates.indexOf(node) != 0) {
                node.getNeighbors().put(candidates.get(candidates.indexOf(node) - 1), TIME.ROUTE_TIME);
            }

            for (Node station : busNetwork) {
                if (station.getNumber() == node.getNumber()) {
                    station.getNeighbors().put(node, TIME.TRANSFER_TIME);
                    node.getNeighbors().put(station, TIME.TRANSFER_TIME);
                }
            }

            busNetwork.add(node);
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

        List<Node> firstNodeRepresentation = new ArrayList<Node>();
        List<Node> destNodeRepresentation = new ArrayList<Node>();
        Iterator iterator = busNetwork.iterator();

        while (iterator.hasNext()) {
            Node node = (Node) iterator.next();

            if (node.getNumber() == dests[0]) {
                firstNodeRepresentation.add(node);
            }

            if (node.getNumber() == dests[1]) {
                destNodeRepresentation.add(node);
            }
        }



        return null;

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

        return busNetwork;
    }

}

class Node {

    private Integer route;
    private Integer number;
    private Integer order;

    Node(int number, int route) {
        this.number = number;
        this.route = route;
    }

    private Map<Node, TIME> neighbors = new LinkedHashMap<Node, TIME>();

    public Integer getRoute() {
        return route;
    }

    public void setRoute(Integer route) {
        this.route = route;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Map<Node, TIME> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(Map<Node, TIME> neighbors) {
        this.neighbors = neighbors;
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
