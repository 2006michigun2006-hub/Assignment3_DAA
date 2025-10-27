import java.util.*;
public class Main {
    public static void main(String[] args) throws Exception {
        String inputPath = "input_real.json";
        String outputPath = "output.json";
        List<JSONParser.GraphData> graphs = JSONParser.parse(inputPath);
        List<Map<String, Object>> results = new ArrayList<>();
        for (JSONParser.GraphData g : graphs) {
            System.out.println("\n========== GRAPH " + g.id + " ==========");
            long start = System.nanoTime();
            List<Edge> prim = MSTAlgorithms.primMST(g.nodes, g.edges);
            long end = System.nanoTime();
            results.add(saveResult(g.id, "Prim", prim, end - start));
            start = System.nanoTime();
            List<Edge> kruskal = MSTAlgorithms.kruskalMST(g.nodes, g.edges);
            end = System.nanoTime();
            results.add(saveResult(g.id, "Kruskal", kruskal, end - start));
        }
        JSONParser.writeResults(outputPath, results);
        System.out.println("\n✅ Results saved to " + outputPath);
    }
    static Map<String, Object> saveResult(int id, String algo, List<Edge> mst, long timeNs) {
        int total = 0;
        for (Edge e : mst) total += e.weight;
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("id", id);
        res.put("algorithm", algo);
        res.put("edges", mst);
        res.put("totalCost", total);
        res.put("operations", MSTAlgorithms.operationCount);
        res.put("timeMs", timeNs / 1_000_000.0);
        MSTAlgorithms.operationCount = 0;
        return res;
    }
}