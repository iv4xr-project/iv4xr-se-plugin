package spaceEngineers.mock;

import org.junit.jupiter.api.Test;
import spaceEngineers.commands.InteractionArgs;
import spaceEngineers.commands.InteractionType;
import spaceEngineers.controller.ProprietaryJsonTcpCharacterController;
import spaceEngineers.model.Observation;
import testhelp.TestUtilKt;

import static testhelp.AssertKt.checkMockObservation;

public class JavaInteractionTest {

    @Test
    public void equipToolbarItemTest() {
        ProprietaryJsonTcpCharacterController controller = ProprietaryJsonTcpCharacterController.Companion.mock(
                TestUtilKt.TEST_AGENT,
                TestUtilKt.getTEST_MOCK_RESPONSE_LINE()
        );
        Observation observation = controller.interact(new InteractionArgs(InteractionType.EQUIP, 4, -1, true));
        checkMockObservation(observation);
    }
}
