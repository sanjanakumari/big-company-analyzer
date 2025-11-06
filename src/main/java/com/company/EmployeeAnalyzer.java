package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeAnalyzer {

    private Map<Integer, Employee> employees = new HashMap<>();
    private Map<Integer, List<Integer>> subordinates = new HashMap<>();
    private Employee ceo;

    public void loadData(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0].trim());
                String firstName = parts[1].trim();
                String lastName = parts[2].trim();
                double salary = Double.parseDouble(parts[3].trim());
                Integer managerId = (parts.length > 4 && !parts[4].trim().isEmpty())
                        ? Integer.parseInt(parts[4].trim())
                        : null;

                Employee emp = new Employee(id, firstName, lastName, salary, managerId);
                employees.put(id, emp);

                if (managerId != null) {
                    subordinates.computeIfAbsent(managerId, k -> new ArrayList<>()).add(id);
                } else {
                    ceo = emp;
                }
            }
        }
    }

    public void analyze() {
        System.out.println("===== SALARY ANALYSIS =====");
        checkSalaries();
        System.out.println();
        System.out.println("===== REPORTING LINE CHECK =====");
        checkReportingDepth();
    }

    private void checkSalaries() {
        for (Integer managerId : subordinates.keySet()) {
            Employee manager = employees.get(managerId);
            List<Integer> subList = subordinates.get(managerId);

            double total = 0;
            for (int subId : subList) {
                total += employees.get(subId).getSalary();
            }
            double avg = total / subList.size();

            double min = avg * 1.2;
            double max = avg * 1.5;

            if (manager.getSalary() < min) {
                double diff = min - manager.getSalary();
                System.out.printf("%s earns %.2f but should earn at least %.2f (%.2f less)%n",
                        manager.getFullName(), manager.getSalary(), min, diff);
            } else if (manager.getSalary() > max) {
                double diff = manager.getSalary() - max;
                System.out.printf("%s earns %.2f but should earn no more than %.2f (%.2f more)%n",
                        manager.getFullName(), manager.getSalary(), max, diff);
            }
        }
    }

    private void checkReportingDepth() {
        for (Employee e : employees.values()) {
            int depth = 0;
            Integer mgr = e.getManagerId();

            while (mgr != null) {
                depth++;
                Employee manager = employees.get(mgr);
                mgr = manager.getManagerId();
            }

            if (depth > 5) {
                System.out.printf("%s has %d managers between them and the CEO (%d too many)%n",
                        e.getFullName(), depth - 1, depth - 5);
            }
        }
    }
}
