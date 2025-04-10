package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.restassured.response.Response;

import static steps.Hooks.wireMockSpec;
import static utils.TestVariableManager.SetVariable;
import static utils.TestVariables.*;

public class MockedSteps {

    @Given("I will get a random first name")
    public void iWillGetARandomFirstName() {
        Response response = wireMockSpec
                .when()
                .get("first_name");
        SetVariable(MOCK_FIRSTNAME, response.jsonPath().getString("first_name"));
    }

    @And("I will get a random last name")
    public void iWillGetARandomLastName() {
        Response response = wireMockSpec
                .when()
                .get("last_name");
        SetVariable(MOCK_LASTNAME, response.jsonPath().getString("last_name"));
    }

    @And("I will get a random total price")
    public void iWillGetARandomTotalPrice() {
        Response response = wireMockSpec
                .when()
                .get("total_price");
        SetVariable(MOCK_TOTALPRICE, response.jsonPath().getString("total_price"));
    }

    @Given("I have invalid token")
    public void iHaveInvalidToken() {
        Response response = wireMockSpec
                .when()
                .get("invalid_token ");
        SetVariable(INVALID_TOKEN, response.jsonPath().getString("invalid_token"));

    }
}
