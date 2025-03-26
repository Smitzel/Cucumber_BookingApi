@regression

Feature: Airline tests

  Background:
    Given the booking API is available


  @booking
  Scenario: 01. As a customer I want to add a booking

    When I add a booking to the api
    Then the POSTED booking is stored correctly in the booking database


  @booking
  Scenario: 02. As a customer I want to add a booking with fixed data

    When I add a booking to the api with these values
      | firstname | lastname | totalprice | depositpaid | checkin    | checkout   | additionalneeds |
      | John      | Doe      | 100        | true        | 2018-01-01 | 2018-01-02 | fries           |


  @booking
  Scenario Outline: 03. As a customer I want to add multiple bookings with different data

    When I add a booking to the api with these values
      | firstname   | lastname   | totalprice   | depositpaid   | checkin   | checkout   | additionalneeds   |
      | <firstname> | <lastname> | <totalprice> | <depositpaid> | <checkin> | <checkout> | <additionalneeds> |

    Examples:
      | firstname | lastname | totalprice | depositpaid | checkin    | checkout   | additionalneeds |
      | John      | Doe      | 100        | true        | 2018-01-01 | 2018-01-02 | fries           |
      | Alice     | Brown    | 250        | false       | 2019-05-10 | 2019-05-15 | breakfast       |
      | Bob       | Smith    | 500        | true        | 2020-07-20 | 2020-07-25 | extra pillows   |


  @booking
  Scenario: 04. As a customer I want to partly update a booking

    Given I get a valid authentication token

    When I add a booking to the api
    Then the POSTED booking is stored correctly in the booking database
    And I update one value of the booking
      | firstname | Kees |
    Then the PATCHED booking is stored correctly in the booking database


  @booking
  Scenario: 05. As a customer I want to completely update a booking

    Given I get a valid authentication token

    When I add a booking to the api
    Then the POSTED booking is stored correctly in the booking database

    When I update all details of the booking
      | firstname | lastname | totalprice | depositpaid | checkin    | checkout   | additionalneeds |
      | John      | Doe      | 100        | true        | 2018-01-01 | 2018-01-02 | fries           |
    Then the FULLY UPDATED booking is stored correctly in the booking database


  @booking
  Scenario: 06. As a customer I want to do a booking with data from the Mocked services

    Given I will get a random first name
    And I will get a random last name
    And I will get a random total price
    And I add a booking to the api with the random data
    Then the MOCKED booking is stored correctly in the booking database


  @booking
  Scenario: 07. Delete a random existing booking

    Given I get a random booking from the database
    And I get a valid authentication token
    And I delete the booking
    Then the booking should no longer be retrievable


    @booking
  Scenario: 07. As a customer I want to create, read, update and delete a booking

      Given I get a valid authentication token

      When I add a booking to the api
      Then the POSTED booking is stored correctly in the booking database

      When I update all details of the booking
        | firstname | lastname | totalprice | depositpaid | checkin    | checkout   | additionalneeds |
        | John      | Doe      | 100        | true        | 2018-01-01 | 2018-01-02 | fries           |
      Then the FULLY UPDATED booking is stored correctly in the booking database
      And I delete the booking
      Then the booking should no longer be retrievable

  @booking
  Scenario: 08. Attempt to retrieve a non-existent booking

    Given I have these variables
      | INVALIDBOOKINGID | 999999999 |

    Then no booking exists with invalid ID

  @booking
  Scenario: 09. Attempt to authenticate with invalid credentials
    Given I have invalid token

    When I add a booking to the api
    Then the POSTED booking is stored correctly in the booking database

    When I do a request with the invalid token the response status should be 403