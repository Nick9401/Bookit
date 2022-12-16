package com.bookit.step_definitions;

import com.bookit.utilities.BookitUtils;
import com.bookit.utilities.ConfigurationReader;
import com.bookit.utilities.DB_Util;
import com.bookit.utilities.Environment;
import io.cucumber.java.bs.A;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.Map;

import static com.github.dockerjava.api.model.LogConfig.LoggingType.DB;
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
                .header("Authorization", token)
                .when().get(Environment.BASE_URL + endpoint);

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

        System.out.println("response.path(\"role\") = " + response.path("role"));
        System.out.println("response.jsonPath().getString(\"role\") = " + response.jsonPath().getString("role"));

        Assert.assertEquals(expectedRole,response.path("role"));
    }

    @Then("the information about current user from api and database should match")
    public void the_information_about_current_user_from_api_and_database_should_match() {

        JsonPath jsonPath = response.jsonPath();
        String actualLastName = jsonPath.getString("lastName");
        String actualFirstName = jsonPath.getString("firstName");
        String actualRole = jsonPath.getString("role");

        String query = "select firstname,lastname,role from users\n" +
                "where email = 'lfinnisz@yolasite.com'";

        DB_Util.runQuery(query);
        Map<String,String>dbMap = DB_Util.getRowMap(1);
        System.out.println(dbMap);

        String firstName = dbMap.get("firstName");
        String lastName = dbMap.get("lastName");
        String Role = dbMap.get("role");


        Assert.assertEquals(firstName,actualFirstName);
        Assert.assertEquals(lastName,actualLastName);
        Assert.assertEquals(Role,actualRole);


    }

}
