package steps;

import io.cucumber.java.en.Given;
import io.restassured.response.Response;
import utils.TestVariables;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static steps.Hooks.bookingApiSpec;
import static utils.TestVariableManager.SetVariable;

public class AuthSteps {

    @Given("I get a valid authentication token")
    public void iGetAValidAuthenticationToken() {
        getAuthToken();
    }


    private static final String CONFIG_PATH = "src/test/resources/config.properties";
    private static final Properties properties = new Properties();

    static {
        try {
            FileInputStream fileInputStream = new FileInputStream(CONFIG_PATH);
            properties.load(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException("Properties not loaded!", e);
        }
    }

    public static void getAuthToken() {
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        String jsonBody = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);

        Response response = bookingApiSpec
                .contentType("application/json")
                .body(jsonBody)
                .post("auth");

        SetVariable(TestVariables.AUTH_TOKEN, response.jsonPath().getString("token"));

        if (response.statusCode() != 200) {
            throw new RuntimeException("Authentication failed: " + response.asString());
        }

        response.jsonPath().getString("token");
    }

}
