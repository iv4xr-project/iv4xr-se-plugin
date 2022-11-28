package bdd

import io.cucumber.junit.Cucumber
import org.junit.runner.Description
import org.junit.runner.notification.RunNotifier
import org.junit.runners.ParentRunner

class CustomRunner(
    val cls: Class<*>, val initialChildren: List<ParentRunner<*>> = listOf(
        Cucumber(cls)
    )
) : ParentRunner<ParentRunner<*>>(cls) {

    override fun runChild(child: ParentRunner<*>, notifier: RunNotifier?) {
        child.run(notifier)
    }

    override fun describeChild(child: ParentRunner<*>): Description {
        return child.description
    }

    override fun getChildren(): MutableList<ParentRunner<*>> {
        return initialChildren.toMutableList()
    }
}
