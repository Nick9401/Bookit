
Feature: User Verification

@wop
  Scenario: verify information about logged user
    Given I logged Bookit api using "lfinnisz@yolasite.com" and "lissiefinnis"
    When I sent get request to "/api/users/me" endpoint
    Then status code should be 200
    And content type is "application/json"
    And role is "student-team-leader"

