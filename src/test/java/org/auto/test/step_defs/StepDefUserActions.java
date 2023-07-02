package org.auto.test.step_defs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.auto.core.api_specs.UserAPISpecs;
import org.auto.core.helpers.ConfigReader;
import org.junit.Assert;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class StepDefUserActions {

    UserAPISpecs user_api_specs;
    HashMap<String, String> create_user_data;
    HashMap<String, String> prepare_test_data;
    Response res;
    ConfigReader cr = new ConfigReader();

    @Given("^application is up and reachable$")
    public void application_is_up_and_reachable() {
        given()
                .get(cr.get_base_uri())
                .then()
                .assertThat().statusCode(200);
        user_api_specs = new UserAPISpecs();
    }

    @Given("^setup create_user api$")
    public void setup_create_user_api() {
        create_user_data = new HashMap<>();
        
    }

    @When("^set (.+) as (.+)$")
    public void set_test_data(String key_name, String content) {
        if(content.equals("empty")){
            create_user_data.put(key_name, "");
        }else{
        create_user_data.put(key_name, content);
        }
    }

    @When("^creates user with the details provided$")
    public void creates_user() {
        res = user_api_specs.create_user(create_user_data);
    }

    @Then("^verify the response code is (\\d+)$")
    public void verify_response_code(int response_code) {
user_api_specs.verify_response_code(res, response_code);
    }

    @Then("^verify response time is within configured value$")
    public void verify_response_time() {
        user_api_specs.verify_response_time(res);
    }

    @Then("^verify the user is created successfully$")
    public void verify_the_user_is_created_successfully() {
        Assert.assertEquals("Users' name doesnt match name returned in response", res.then().assertThat().extract().body().jsonPath().get("data['name']").toString(), create_user_data.get("name"));
        Assert.assertEquals("Users' email doesnt match email returned in response", res.then().assertThat().extract().body().jsonPath().get("data['email']").toString(), create_user_data.get("email"));
        Assert.assertEquals("Users' gender doesnt match gender returned in response", res.then().assertThat().extract().body().jsonPath().get("data['gender']").toString(), create_user_data.get("gender"));
        Assert.assertEquals("Users' status doesnt match status returned in response", res.then().assertThat().extract().body().jsonPath().get("data['status']").toString(), create_user_data.get("status"));
    }

    @And("^user is created in application with (.+) (.+) (.+) (.+) details$")
    public void user_is_created_in_application_with_details(String name, String email, String gender, String status){
        HashMap<String, String> test_data = new HashMap<>();
        test_data.put("name", name);
        test_data.put("email", email);
        test_data.put("gender", gender);
        test_data.put("status", status);
        Response res = user_api_specs.create_user(test_data);
        res.then().assertThat().statusCode(201);
    }

    @And("^verify error message (.+) is displayed for field (.+)$")
    public void verify_error_message(String err_message, String field_name){
       Assert.assertEquals("Field-name in error response didnt match",res.then().extract().jsonPath().get("data[0]['field']").toString(),field_name);
       Assert.assertEquals("Error message value didnt match",res.then().extract().jsonPath().get("data[0]['message']").toString(),err_message);
    }

    }