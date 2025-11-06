package com.company;

public class Main {
    public static void main(String[] args) {
        String fileName = args.length > 0 ? args[0] : "employees.csv";
        EmployeeAnalyzer analyzer = new EmployeeAnalyzer();

        try {
            analyzer.loadData(fileName);
            analyzer.analyze();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
