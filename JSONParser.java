import java.io.*;
import java.util.*;
import java.nio.file.*;
public class JSONParser {
    public static class GraphData {
        int id;
        List<String> nodes;
        List<Edge> edges;
        public GraphData(int id, List<String> nodes, List<Edge> edges) {
            this.id = id;
            this.nodes = nodes;
            this.edges = edges;
        }
    }
    public static List<GraphData> parse(String filePath) throws IOException {
        String text = Files.readString(Path.of(filePath))
                .replaceAll("\\s+", "");

        List<GraphData> graphs = new ArrayList<>();
        String[] parts = text.split("\\{\\\"id\\\":");
        for (int i = 1; i < parts.length; i++) {
            String section = parts[i];
            int id = Integer.parseInt(section.substring(0, section.indexOf(",")));

            // nodes
            int startNodes = section.indexOf("[", section.indexOf("\"nodes\""));
            int endNodes = section.indexOf("]", startNodes);
            String[] nodeArr = section.substring(startNodes + 1, endNodes)
                    .replace("\"", "").split(",");
            List<String> nodes = Arrays.asList(nodeArr);

            // edges
            int startEdges = section.indexOf("[", section.indexOf("\"edges\""));
            int endEdges = section.indexOf("]", startEdges);
            String edgesStr = section.substring(startEdges + 1, endEdges);
            String[] edgeItems = edgesStr.split("\\},\\{");

            List<Edge> edges = new ArrayList<>();
            for (String item : edgeItems) {
                String clean = item.replace("{", "").replace("}", "");
                String[] parts2 = clean.split(",");
                String from = parts2[0].split(":")[1].replace("\"", "");
                String to = parts2[1].split(":")[1].replace("\"", "");
                int weight = Integer.parseInt(parts2[2].split(":")[1]);
                edges.add(new Edge(from, to, weight));
            }

            graphs.add(new GraphData(id, nodes, edges));
        }
        return graphs;
    }

    public static void writeResults(String filePath, List<Map<String, Object>> results) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n  \"results\": [\n");
        for (int i = 0; i < results.size(); i++) {
            Map<String, Object> r = results.get(i);
            sb.append("    {\n");
            sb.append("      \"id\": ").append(r.get("id")).append(",\n");
            sb.append("      \"algorithm\": \"").append(r.get("algorithm")).append("\",\n");
            sb.append("      \"totalCost\": ").append(r.get("totalCost")).append(",\n");
            sb.append("      \"operations\": ").append(r.get("operations")).append(",\n");
            sb.append("      \"timeMs\": ").append(String.format("%.3f", r.get("timeMs"))).append(",\n");
            sb.append("      \"edges\": [");
            @SuppressWarnings("unchecked")
            List<Edge> edges = (List<Edge>) r.get("edges");
            for (int j = 0; j < edges.size(); j++) {
                Edge e = edges.get(j);
                sb.append(String.format("{\"from\":\"%s\",\"to\":\"%s\",\"weight\":%d}", e.from, e.to, e.weight));
                if (j < edges.size() - 1) sb.append(",");
            }
            sb.append("]\n    }");
            if (i < results.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("  ]\n}");
        Files.writeString(Path.of(filePath), sb.toString());
    }
}