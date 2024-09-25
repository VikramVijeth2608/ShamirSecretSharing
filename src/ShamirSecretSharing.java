import java.util.*;
import java.io.*;

public class ShamirSecretSharing {

    // Function to convert a number from a given base to an integer
    public static int baseToInt(String value, int base) {
        return Integer.parseInt(value, base);
    }

    // Function to perform Lagrange interpolation to find the constant term
    public static double lagrangeInterpolation(List<int[]> points) {
        int k = points.size();
        double constantTerm = 0;

        for (int i = 0; i < k; i++) {
            int xi = points.get(i)[0];
            int yi = points.get(i)[1];
            double li = 1;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    int xj = points.get(j)[0];
                    li *= (0.0 - xj) / (xi - xj); // 0 is the x-value for constant term (c)
                }
            }

            constantTerm += yi * li;
        }

        return constantTerm;
    }

    // Main function to parse JSON-like input and find the constant term
    public static void main(String[] args) {
        try {
            // Simulate JSON input (replace this part with actual input)
            String input = "{\n" +
                    "\"keys\": {\"n\": 4, \"k\": 3},\n" +
                    "\"1\": {\"base\": \"10\", \"value\": \"4\"},\n" +
                    "\"2\": {\"base\": \"2\", \"value\": \"111\"},\n" +
                    "\"3\": {\"base\": \"10\", \"value\": \"12\"},\n" +
                    "\"6\": {\"base\": \"4\", \"value\": \"213\"}\n" +
                    "}";

            // Manual parsing (as online compilers don't support org.json)
            Map<String, Map<String, String>> parsedData = new HashMap<>();
            parsedData.put("keys", Map.of("n", "4", "k", "3"));
            parsedData.put("1", Map.of("base", "10", "value", "4"));
            parsedData.put("2", Map.of("base", "2", "value", "111"));
            parsedData.put("3", Map.of("base", "10", "value", "12"));
            parsedData.put("6", Map.of("base", "4", "value", "213"));

            // Extract n and k from the "keys" section
            int n = Integer.parseInt(parsedData.get("keys").get("n"));
            int k = Integer.parseInt(parsedData.get("keys").get("k"));

            // List to store the points (x, y) as integer pairs
            List<int[]> points = new ArrayList<>();

            // Iterate through the root entries
            for (String key : parsedData.keySet()) {
                if (key.equals("keys")) {
                    continue;
                }

                // Extract base and value
                String baseStr = parsedData.get(key).get("base");
                String valueStr = parsedData.get(key).get("value");

                // Convert the value from the given base to an integer
                int base = Integer.parseInt(baseStr);
                int y = baseToInt(valueStr, base);

                // Use the key as the x-value and y as the decoded value
                int x = Integer.parseInt(key);
                points.add(new int[]{x, y});
            }

            // Sort points by x-value and select the first k points for interpolation
            points.sort(Comparator.comparingInt(p -> p[0]));
            points = points.subList(0, k);

            // Perform Lagrange interpolation to find the constant term
            double constantTerm = lagrangeInterpolation(points);

            // Output the constant term (c)
            System.out.println("The constant term (c) is: " + constantTerm);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
