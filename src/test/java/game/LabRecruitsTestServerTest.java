/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package game;

import org.junit.Assert;
import org.junit.Test;

public class LabRecruitsTestServerTest {

    // the game is present in the repository
    @Test
    public void binExistsTest() {

        LabRecruitsTestServer testServer = new LabRecruitsTestServer(
                false,
                Platform.PROJECT_BUILD_PATH);

        // should be false because server is not started yet
        Assert.assertTrue(testServer.isRunning());

        testServer.close();
    }

    // the game is not present in the root of the repository and should throw an exception
    @Test(expected = IllegalArgumentException.class)
    public void binNotExistsTest() {

        LabRecruitsTestServer testServer = new LabRecruitsTestServer(
                false,
                System.getProperty("user.dir") + "LabRecruits.exe");

        // should be false because server is not started yet
        Assert.assertFalse(testServer.isRunning());

        testServer.close();
    }
}
