package spaceEngineers.transport

import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.DataExtendedSpaceEngineers
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.proxy.BatchProcessableSpaceEngineers

fun Any?.closeIfCloseable() {
    this?.let {
        if (it is AutoCloseable) {
            it.close()
        }
    }
}

fun SpaceEngineers?.closeIfCloseable() {
    //TODO: use a multiplatform Closeable solution to make this part of the original interface
    this?.let {
        if (it is ContextControllerWrapper) {
            it.spaceEngineers.closeIfCloseable()
        }
        if (it is DataExtendedSpaceEngineers) {
            it.spaceEngineers.closeIfCloseable()
        }
        if (it is BatchProcessableSpaceEngineers) {
            it.spaceEngineers.closeIfCloseable()
        }

        if (it is AutoCloseable) {
            it.close()
        }
    }
}
