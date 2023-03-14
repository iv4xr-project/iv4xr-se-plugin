package spaceEngineers.iv4xr

import com.google.gson.Gson
import org.junit.jupiter.api.Disabled
import testhelp.MockOrRealGameTest
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.roundToInt
import kotlin.test.Test
import kotlin.test.assertEquals

class NavigateEntityTest : MockOrRealGameTest(
        scenarioId = "simple-place-grind-torch",
        forceRealGame = true,
        loadScenario = true
) {

    @Disabled("This test required a game instance running, enable manually by uncommenting.")
    @Test
    fun test_observed_grids() = testContext {
        // I expect to obtain the existing grids in each executed observation
        // But it seems that every observation is returning current grid + last observed grid
        // FIX: Source\Ivxr.SePlugin\Control\LowLevelObserver.cs - EnumerateSurroundingEntities

        val firstObservedGrids = observer.observeBlocks().grids
        println("firstObservedGrids.size: " + firstObservedGrids.size)
        val secondObservedGrids = observer.observeBlocks().grids
        println("secondObservedGrids.size: " + secondObservedGrids.size)
        val thirdObservedGrids = observer.observeBlocks().grids
        println("thirdObservedGrids.size: " + thirdObservedGrids.size)

        assertEquals(firstObservedGrids.size, 1)
        assertEquals(secondObservedGrids.size, 1)
        assertEquals(thirdObservedGrids.size, 1)
    }

    @Disabled("This test required a game instance running, enable manually by uncommenting.")
    @Test
    fun test_observation_range() = testContext {
        val observationRadius = obtainObservationRadius()
        println("ObservationRadius: $observationRadius")

        val observedBlocks = observer.observeBlocks().grids[0].blocks
        println("observedBlocks.size: " + observedBlocks.size)

        // These are the range of expected observed blocks base on the observation range
        // Note that the ivxr-plugin.config file must be edited and SE restarted
        if(observationRadius <= 2){
            assert(observedBlocks.size in 1..10)
        } else if (observationRadius in 2..5) {
            assert(observedBlocks.size in 5..25)
        } else if (observationRadius in 5..10) {
            assert(observedBlocks.size in 20..75)
        }
    }

    @Throws(IOException::class)
    private fun obtainObservationRadius(): Int {
        val gson = Gson()
        val reader = Files.newBufferedReader(Paths.get(System.getProperty("user.home") + File.separator + "AppData/Roaming/SpaceEngineers/ivxr-plugin.config"))
        val map = gson.fromJson(reader, Map::class.java)
        val observationRadius = map["ObservationRadius"].toString().toDouble().roundToInt()
        reader.close()
        return observationRadius;
    }

}