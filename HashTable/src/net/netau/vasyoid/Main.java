package net.netau.vasyoid;

public class Main {

    private static void print(String value) {
        if (value == null) {
            System.out.println("null");
        } else {
            System.out.println(value);
        }
    }

    public static void main(String[] args) {
        HashTable table = new HashTable();
        print(table.put("Mama", "Papa"));
        print(table.put("Mama", "Mama"));
        print(table.get("Mama"));
        print(table.contains("Mama") ? "contains" : "doesn't contain");
        print(table.remove("Mama"));
        print(table.remove("Mama"));
        print(table.contains("Mama") ? "contains" : "doesn't contain");
        for (int i = 0; i < 20; i++) {
            table.put(String.valueOf(i), String.valueOf(i * 100));
            print(String.valueOf(table.size()));
        }
        table.clear();
    }
}
