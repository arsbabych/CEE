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

    static List<Node> busNetwork = new ArrayList<Node>();
    static int routeNumber = 1;

    public static void main(String[] args) throws IOException {

        File file = new File(args[0]);
        String line;

        Pattern pattern = Pattern.compile(OVERALL_REGEX);
        Matcher matcher;

        if (file.isFile()) {
            InputStream is = new FileInputStream(file);

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {

                matcher = pattern.matcher(line);
                matcher.find();

                String points = matcher.group(1);
                String routes = matcher.group(2);

                Pattern pattern2 = Pattern.compile(ROUTE_REGEX);
                Matcher matcher2 = pattern2.matcher(routes);

                List<List<Integer>> routesList = new ArrayList<List<Integer>>();



                while (matcher2.find()) {
                    String routeStations = matcher2.group(1);
                    List<Integer> route = new ArrayList<Integer>();

                    for (String s : routeStations.split(",")) {
                        route.add(Integer.parseInt(s));
                    }

                    routesList.add(route);
                }

                for (List<Integer> route : routesList) {
                    List<Node> nodeCandidates = initNodeCandidates(route);
                    addToBusNetwork(nodeCandidates);
                }
            }
        }
    }

    public static List<Node> initNodeCandidates(List<Integer> candidates) {
        List<Node> result = new ArrayList<Node>();

        for (Integer i : candidates) {
            result.add(new Node(i, routeNumber));
        }

        return result;
    }

    public static void addToBusNetwork(List<Node> candidates) {
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

        ABusNetwork.routeNumber++;
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
