package steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import utils.TestVariableManager;
import utils.TestVariables;

import java.util.Map;

public class varSteps {
    @Given("I have these variables")
    public void iHaveTheseVariables(DataTable table) {
        Map<String, String> variables = table.asMap(String.class, String.class);

        for (Map.Entry<String, String> entry : variables.entrySet()) {
            TestVariables key = TestVariables.valueOf(entry.getKey().toUpperCase().replace(" ", "_")); // Convert to ENUM-format
            String value = entry.getValue();
            TestVariableManager.SetVariable(key, value);
        }
    }
}