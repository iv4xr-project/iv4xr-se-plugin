/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package communication.system;

/**
 * Keep this enum synced with Unity!
 */

public enum RequestType
{
    DISCONNECT,
    PAUSE,
    START,
    INIT,
    UPDATE_ENVIRONMENT,
    AGENTCOMMAND,  // TODO(PP): Should probably be AGENT_COMMAND
    SESSION  // TODO(PP): Added for Space Engineers, consider making a separate RequestType for SE
}

