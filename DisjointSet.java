import java.util.*;

public class DisjointSet {
    private final Map<String, String> parent = new HashMap<>();

    public void makeSet(Collection<String> nodes) {
        for (String node : nodes)
            parent.put(node, node);
    }

    public String find(String node) {
        if (!parent.get(node).equals(node))
            parent.put(node, find(parent.get(node)));
        return parent.get(node);
    }

    public void union(String a, String b) {
        String rootA = find(a);
        String rootB = find(b);
        if (!rootA.equals(rootB))
            parent.put(rootB, rootA);
    }
}