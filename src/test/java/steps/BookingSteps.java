package steps;

import hooks.Hooks;
import pojo.Booking;
import pojo.BookingDates;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;
import utils.TestVariables;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static utils.TestVariableManager.*;
import static utils.TestVariableManager.SetVariable;
import static utils.TestVariables.*;

public class BookingSteps {

    @When("I add a booking to the api")
    public void iAddABookingToTheApi() {
        Booking booking = new Booking();
        Response response = Hooks.bookingApiSpec
                .when()
                .body(booking)
                .when()
                .post("booking");
        response.then().statusCode(200);

        Map<TestVariables, String> variableMappings = Map.of(
                POST_ADDITIONALNEEDS, "booking.additionalneeds",
                POST_BOOKINGID, "bookingid",
                POST_CHECKIN, "booking.bookingdates.checkin",
                POST_CHECKOUT, "booking.bookingdates.checkout",
                POST_DEPOSITPAID, "booking.depositpaid",
                POST_FIRSTNAME, "booking.firstname",
                POST_LASTNAME, "booking.lastname",
                POST_TOTALPRICE, "booking.totalprice"
        );
        variableMappings.forEach((key, path) -> SetVariable(key, response.jsonPath().getString(path)));
    }

    @Then("^the (POSTED|FULLY UPDATED|PATCHED|MOCKED) booking is stored correctly in the booking database$")
    public void verifyBookingStoredCorrectly(String status) {
        switch (status) {
            case "POSTED":
                verifyPostedBooking();
                break;
            case "FULLY UPDATED":
                verifyFullyUpdatedBooking();
                break;
            case "PATCHED":
                verifyPatchedBooking();
                break;
            case "MOCKED":
                verifyMockeddBooking();
                break;
            default:
                throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    private void verifyMockeddBooking() {
        Response response = Hooks.bookingApiSpec
                .when()
                .get("booking/{id}", GetVariable(POST_BOOKINGID));

        response.then().statusCode(200);

        Map<TestVariables, String> variableMappings = Map.of(
                GET_ADDITIONALNEEDS, "additionalneeds",
                GET_CHECKIN, "bookingdates.checkin",
                GET_CHECKOUT, "bookingdates.checkout",
                GET_DEPOSITPAID, "depositpaid",
                GET_FIRSTNAME, "firstname",
                GET_LASTNAME, "lastname",
                GET_TOTALPRICE, "totalprice"
        );
        variableMappings.forEach((key, path) -> SetVariable(key, response.jsonPath().getString(path)));

        assertEquals(GetVariable(MOCK_FIRSTNAME), GetVariable(GET_FIRSTNAME));
        assertEquals(GetVariable(MOCK_LASTNAME), GetVariable(GET_LASTNAME));
        assertEquals(GetVariable(MOCK_TOTALPRICE), GetVariable(GET_TOTALPRICE));
    }

    @When("I add a booking to the api with these values")
    public void i_add_a_booking_to_the_api_with_these_values(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);

        // Create BookingDates object
        BookingDates bookingDates = new BookingDates(
                data.get("checkin"),
                data.get("checkout"));

        // Create Booking object
        Booking booking = new Booking(
                data.get("firstname"),
                data.get("lastname"),
                Integer.parseInt(data.get("totalprice")),
                Boolean.parseBoolean(data.get("depositpaid")),
                bookingDates,
                data.get("additionalneeds"));

        Response response = Hooks.bookingApiSpec
                .when()
                .body(booking)
                .when()
                .post("booking");

        response.then().statusCode(200);
        String bookingId = response.jsonPath().getString("bookingid");
        System.out.println("BookingID: " + bookingId);
    }


    private void verifyPostedBooking() {
        Response response = Hooks.bookingApiSpec
                .when()
                .get("booking/{id}", GetVariable(POST_BOOKINGID));

        response.then().statusCode(200);

        Map<TestVariables, String> variableMappings = Map.of(
                GET_ADDITIONALNEEDS, "additionalneeds",
                GET_CHECKIN, "bookingdates.checkin",
                GET_CHECKOUT, "bookingdates.checkout",
                GET_DEPOSITPAID, "depositpaid",
                GET_FIRSTNAME, "firstname",
                GET_LASTNAME, "lastname",
                GET_TOTALPRICE, "totalprice"
        );
        variableMappings.forEach((key, path) -> SetVariable(key, response.jsonPath().getString(path)));
    }

    private void verifyPatchedBooking() {
        Response response = Hooks.bookingApiSpec
                .when()
                .get("booking/{id}", GetVariable(POST_BOOKINGID));

        response.then()
                .statusCode(200)
                .body(GetVariable(PATCH_KEY), equalTo(GetVariable(PATCH_VALUE)));
    }


    private void verifyFullyUpdatedBooking() {

        Response response = Hooks.bookingApiSpec
                .when()
                .get("booking/{id}", GetVariable(POST_BOOKINGID));
        response.then().statusCode(200);

        Map<TestVariables, String> variableMappings = Map.of(
                GET_ADDITIONALNEEDS, "additionalneeds",
                GET_CHECKIN, "bookingdates.checkin",
                GET_CHECKOUT, "bookingdates.checkout",
                GET_DEPOSITPAID, "depositpaid",
                GET_FIRSTNAME, "firstname",
                GET_LASTNAME, "lastname",
                GET_TOTALPRICE, "totalprice"
        );
        variableMappings.forEach((key, path) -> SetVariable(key, response.jsonPath().getString(path)));

        assertEquals(GetVariable(PUT_FIRSTNAME), GetVariable(GET_FIRSTNAME));
        assertEquals(GetVariable(PUT_LASTNAME), GetVariable(GET_LASTNAME));
        assertEquals(GetVariable(PUT_TOTALPRICE), GetVariable(GET_TOTALPRICE));
        assertEquals(GetVariable(PUT_DEPOSITPAID), GetVariable(GET_DEPOSITPAID));
        assertEquals(GetVariable(PUT_CHECKIN), GetVariable(GET_CHECKIN));
        assertEquals(GetVariable(PUT_CHECKOUT), GetVariable(GET_CHECKOUT));
        assertEquals(GetVariable(PUT_ADDITIONALNEEDS), GetVariable(GET_ADDITIONALNEEDS));
    }


    @Given("the booking API is available")
    public void theBookingAPIIsAvailable() {
        Response response = Hooks.bookingApiSpec
                .when()
                .get("ping");
        response.then().statusCode(201);
    }

    @And("I update one value of the booking")
    public void iUpdateOneValueOfTheBooking(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);

        String key = data.keySet().iterator().next();
        String value = data.get(key);

        SetVariable(PATCH_KEY, key);
        SetVariable(PATCH_VALUE, value);

        JSONObject requestParams = new JSONObject();
        requestParams.put(key, value);

        Response response = Hooks.bookingApiSpec
                .body(requestParams.toString())
                .cookie("token", GetVariable(AUTH_TOKEN))
                .when()
                .patch("booking/" + GetVariable(POST_BOOKINGID));
        response.then().statusCode(200);
    }

    @When("I update all details of the booking")
    public void iUpdateAllDetailsOfTheBooking(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);

        // Create BookingDates object
        BookingDates bookingDates = new BookingDates(
                data.get("checkin"),
                data.get("checkout"));

        SetVariable(PUT_CHECKIN, data.get("checkin"));
        SetVariable(PUT_CHECKOUT, data.get("checkout"));

        // Create Booking object
        Booking booking = new Booking(
                data.get("firstname"),
                data.get("lastname"),
                Integer.parseInt(data.get("totalprice")),
                Boolean.parseBoolean(data.get("depositpaid")),
                bookingDates,
                data.get("additionalneeds"));

        SetVariable(PUT_FIRSTNAME, data.get("firstname"));
        SetVariable(PUT_LASTNAME, data.get("lastname"));
        SetVariable(PUT_TOTALPRICE, data.get("totalprice"));
        SetVariable(PUT_DEPOSITPAID, data.get("depositpaid"));
        SetVariable(PUT_ADDITIONALNEEDS, data.get("additionalneeds"));

        System.out.println(booking);

        Response response = Hooks.bookingApiSpec
                .when()
                .body(booking)
                .cookie("token", GetVariable(AUTH_TOKEN))
                .put("booking/" + GetVariable(POST_BOOKINGID));

        response.then().statusCode(200);
    }

    @And("I add a booking to the api with the random data")
    public void iAddABookingToTheApiWithTheRandomData() {
        Booking booking = new Booking();
        booking.setFirstname(GetVariable(MOCK_FIRSTNAME));
        booking.setLastname(GetVariable(MOCK_LASTNAME));
        booking.setTotalprice(Integer.parseInt(GetVariable(MOCK_TOTALPRICE)));

        Response response = Hooks.bookingApiSpec
                .when()
                .body(booking)
                .when()
                .post("booking");

        SetVariable(POST_BOOKINGID, response.jsonPath().getString("bookingid"));

        response.then().statusCode(200);
    }

    @Given("I get a random booking from the database")
    public void iGetARandomBookingFromTheDatabase() {
        Response response = Hooks.bookingApiSpec
                .when()
                .get("booking")
                .then()
                .statusCode(200)
                .extract()
                .response();

        List<Integer> bookingIds = response.jsonPath().getList("bookingid", Integer.class);

        Random random = new Random();
        int randomIndex = random.nextInt(bookingIds.size());
        int randomBookingId = bookingIds.get(randomIndex);

        System.out.println("Random Booking ID: " + randomBookingId);
        SetVariable(POST_BOOKINGID, String.valueOf(randomBookingId));

    }

    @When("I delete the booking")
    public void iDeleteTheBooking() {
        Hooks.bookingApiSpec
                .when()
                .cookie("token", GetVariable(AUTH_TOKEN))
                .delete("booking/" + GetVariable(POST_BOOKINGID))
                .then()
                .statusCode(201);
    }

    @Then("the booking should no longer be retrievable")
    public void the_booking_should_no_longer_be_retrievable() {
        Response response = Hooks.bookingApiSpec
                .when()
                .get("booking/{id}", GetVariable(POST_BOOKINGID));
        response.then().statusCode(404);
        System.out.println("Status 404: Booking not found");
    }

    @Given("no booking exists with invalid ID")
    public void noBookingExistsWithID() {
        Response response = Hooks.bookingApiSpec
                .when()
                .get("booking/{id}", GetVariable(INVALIDBOOKINGID));
        response.then().statusCode(404);
    }

    @When("I do a request with the invalid token the response status should be {int}")
    public void iDoARequestWithTheInvalidTokenTheResponseStatusShouldBe(int statuscode) {
        Hooks.bookingApiSpec
                .when()
                .cookie("token", GetVariable(INVALID_TOKEN))
                .delete("booking/" + GetVariable(POST_BOOKINGID))
                .then()
                .statusCode(statuscode);
    }
}

