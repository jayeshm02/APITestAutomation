Feature: EndUser should be able to create, retrieve, update or delete application user.

  @verify_create_user_fails @smoke_test
  Scenario Outline: Verify create user api functionality
    Given application is up and reachable
    And setup create_user api
    When set name as <name>
    And set gender as <gender>
    And set email as <email>
    And set status as <status>
    And creates user with the details provided
    Then verify the response code is <status_code>
    And verify response time is within configured value
    And verify the user is created successfully
    And verify details of user created is persisted in the application
    And cleanup the user data created
    Examples:
      | name             | gender | email                             | status   | status_code | schema_name |
      | Jayesh Mahadik   | male   | jayesh.mahadik05@gmail.com        | active   | 201         | create_user |

    @verify_user_input_parameters @smoke_test
  Scenario Outline: Validate error message displayed when invalid input is given
    Given application is up and reachable
    And setup create_user api
    When set name as <name>
    And set gender as <gender>
    And set email as <email>
    And set status as <status>
    And creates user with the details provided
    Then verify the response code is <status_code>
    And verify response time is within configured value
    And verify error message <err_message> is displayed for field <field_name>
    Examples:
      | name           | gender | email                      | status     | status_code | err_message    | field_name |
      | Jayesh Mahadik |        | jayesh.mahadik05@gmail.com | active     | 422         | can't be blank | gender     |
      |                | male   | jayesh.mahadik05@gmail.com | active     | 422         | can't be blank | name       |
      | Natasha        | female |                            | inactive   | 422         | can't be blank | email      |
      | Jayeshm        | male   | jayesh.mahadik05@gmail.com |            | 422         | can't be blank | status     |
      | Jayeshm        | male   | jayesh.mahadik05@gmail.com | active     | 422         | is invalid     | email      |
      | Jayeshm        | sdfwfe | jayesh.mahadik05@gmail.com | active     | 422         | is invalid     | gender     |
      | Jayeshm        | male   | jayesh.mahadik05@gmail.com | sdffwefwef | 422         | is invalid     | status     |






