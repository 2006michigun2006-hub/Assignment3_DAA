import java.util.*;
public class MSTAlgorithms {
    public static int operationCount = 0;
    public static List<Edge> primMST(List<String> nodes, List<Edge> edges) {
        Map<String, List<Edge>> adj = new HashMap<>();
        for (String n : nodes) adj.put(n, new ArrayList<>());
        for (Edge e : edges) {
            adj.get(e.from).add(e);
            adj.get(e.to).add(new Edge(e.to, e.from, e.weight));
        }
        List<Edge> mst = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
        String start = nodes.get(0);
        visited.add(start);
        pq.addAll(adj.get(start));
        while (!pq.isEmpty() && mst.size() < nodes.size() - 1) {
            operationCount++;
            Edge edge = pq.poll();
            if (visited.contains(edge.to)) continue;
            visited.add(edge.to);
            mst.add(edge);
            for (Edge next : adj.get(edge.to))
                if (!visited.contains(next.to)) pq.add(next);
        }
        return mst;
    }
    public static List<Edge> kruskalMST(List<String> nodes, List<Edge> edges) {
        List<Edge> sorted = new ArrayList<>(edges);
        sorted.sort(Comparator.comparingInt(e -> e.weight));
        DisjointSet ds = new DisjointSet();
        ds.makeSet(nodes);
        List<Edge> mst = new ArrayList<>();
        for (Edge e : sorted) {
            operationCount++;
            String rootA = ds.find(e.from);
            String rootB = ds.find(e.to);
            if (!rootA.equals(rootB)) {
                mst.add(e);
                ds.union(rootA, rootB);
            }
        }
        return mst;
    }
}