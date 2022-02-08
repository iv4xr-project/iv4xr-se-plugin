Feature: C197574 Character can crouch / stand back up

  Background:
    Given Dedicated server is at "127.0.0.1", plugin running at port "3333".
    #And Server loads scenario "simple-place-grind-torch-with-tools".
    And Client "A" is at "127.0.0.1", plugin running at port "3334".
    And Client "A" connects to the server.
    And Test waits 40 seconds.
    #And Client "B" is at "127.0.0.1", plugin running at port "3333".

  Scenario: C197574 Character can crouch / stand back up
    And Character "A" is standing as seen by itself.
    And Character "A" is standing as seen by server.
    #And Character "A" is standing as seen by client "B".
    When Character "A" crouches.
    Then Character "A" is crouching as seen by itself.
    Then Character "A" is crouching as seen by server.
    #Then Character "A" is crouching as seen by client "B".


