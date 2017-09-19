package net.netau.vasyoid;

/**
 * Main class with simple tests.
 */
public class Main {

    /**
     * Some simple tests.
     * Just to make sure it works sometimes.
     *
     * @param args not used.
     */
    public static void main(String[] args) throws AssertionError {
        HashTable table = new HashTable();
        boolean success =  table.put("Mama", "Papa") == null &&
            table.put("Mama", "Mama").equals("Papa") &&
            table.contains("Mama") &&
            table.get("Mama").equals("Mama") &&
            table.remove("Mama").equals("Mama") &&
            table.remove("Mama") == null &&
            !table.contains("Mama");
        for (int i = 0; i < 20; i++) {
            table.put(String.valueOf(i), String.valueOf(i * 100));
            success &= table.size() == i + 1;
        }
        System.out.println(success ? "tests passed" : "tests failed");
        table.clear();
    }
}
