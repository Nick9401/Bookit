package com.bookit.step_definitions;

import com.bookit.utilities.BookitUtils;
import com.bookit.utilities.ConfigurationReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;

import static io.restassured.RestAssured.given;

public class ApiStepDefs {
String token;
Response response;
    @Given("I logged Bookit api as a {string}")
    public void i_logged_bookit_api_as_a(String role) {
         token = BookitUtils.generateTokenByRole(role);
        System.out.println("token = " + token);
    }
    @When("I sent get request to {string} endpoint")
    public void i_sent_get_request_to_endpoint(String endpoint) {
      response = given().accept(ContentType.JSON)
              .header("Authorization",token)
              .when().get(ConfigurationReader.getProperty("base_url")+endpoint);
    }
    @Then("status code should be {int}")
    public void status_code_should_be(int expectedStatusCode) {
        System.out.println("response.statusCode() = " + response.statusCode());
        Assert.assertEquals(expectedStatusCode,response.statusCode());
    }
    @Then("content type is {string}")
    public void content_type_is(String expectedContentType) {
        System.out.println("response.contentType() = " + response.contentType());
        Assert.assertEquals(expectedContentType,response.contentType());
    }
    @Then("role is {string}")
    public void role_is(String expectedRole) {
        System.out.println("response.path(\"rale\") = " + response.path("rale"));
        System.out.println("response.jsonPath().getString(\"path\") = " + response.jsonPath().getString("path"));

        Assert.assertEquals(expectedRole,response.path("role"));

    }

}
