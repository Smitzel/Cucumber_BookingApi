package steps;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.RestAssured;
import io.restassured.config.DecoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.LogConfig;
import io.restassured.specification.RequestSpecification;
import utils.TestData;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class Hooks {

    private static WireMockServer wireMockServer;
    public static RequestSpecification wireMockSpec;
    public static RequestSpecification bookingApiSpec;

    @Before
    public void setup() {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        configureFor("localhost", 8080);
        System.out.println("✅ WireMock started on http://localhost:8080");

        // stub for first_name
        stubFor(get(urlEqualTo("/first_name"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"first_name\": \"" + TestData.getFirstName() + "\"}")));

        // stub for last_name
        stubFor(get(urlEqualTo("/last_name"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"last_name\": \"" + TestData.getLastName() + "\"}")));

        // stub for total_price
        stubFor(get(urlEqualTo("/total_price"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                      .withBody("{\"total_price\": \"" + TestData.getPrice(1,9999) + "\"}")));

        // stub for invalid_token
        stubFor(get(urlEqualTo("/invalid_token"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"invalid_token\": \"" + TestData.getInvalidToken() + "\"}")));

        //requestSpecification Wiremock
        wireMockSpec = RestAssured.given().baseUri("http://localhost:8080");

        //requestSpecification Booking API
        bookingApiSpec = RestAssured
                .given()
                .baseUri("https://restful-booker.herokuapp.com/")
                .header("Content-Type", "application/json");

        RestAssured.config = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", 15000)
                        .setParam("http.socket.timeout", 15000))
                .logConfig(LogConfig.logConfig()
                        .enableLoggingOfRequestAndResponseIfValidationFails())
                .decoderConfig(DecoderConfig.decoderConfig()
                        .defaultContentCharset("UTF-8"));
    }


    @After
    public void stopWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
            System.out.println("🛑 WireMock stopped");
        }
    }
}
