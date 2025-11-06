package com.company;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeAnalyzerTest {

    @Test
    public void testFileLoadsSuccessfully() throws Exception {
        EmployeeAnalyzer analyzer = new EmployeeAnalyzer();
        analyzer.loadData("employees.csv");
        assertNotNull(analyzer);
    }

    @Test
    public void testAnalyzeDoesNotThrow() {
        EmployeeAnalyzer analyzer = new EmployeeAnalyzer();
        try {
            analyzer.loadData("employees.csv");
            analyzer.analyze();
        } catch (Exception e) {
            fail("Analyze method threw an exception: " + e.getMessage());
        }
    }
}
