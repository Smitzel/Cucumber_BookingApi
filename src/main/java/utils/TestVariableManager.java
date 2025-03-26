package utils;

import java.util.HashMap;
import java.util.Map;

public class TestVariableManager {

    private static final Map<TestVariables, String> variableMap = new HashMap<>();

    // Set a variable
    public static void SetVariable(TestVariables key, String value) {
        variableMap.put(key, value);
        // Log the stored variable
        System.out.println("SetVariable: " + key + ": " + value);
    }

    // Get a variable
    public static String GetVariable(TestVariables key) {
        return variableMap.getOrDefault(key, null); // Return null when the variable is not vaild
    }
}
