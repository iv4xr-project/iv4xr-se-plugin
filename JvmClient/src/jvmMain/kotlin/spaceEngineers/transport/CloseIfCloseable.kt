package spaceEngineers.transport

import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.DataExtendedSpaceEngineers
import spaceEngineers.controller.SpaceEngineers

fun Any?.closeIfCloseable() {
    this?.let {
        if (it is AutoCloseable) {
            it.close()
        }
    }
}

fun SpaceEngineers?.closeIfCloseable() {
    this?.let {
        if (it is ContextControllerWrapper) {
            it.spaceEngineers.closeIfCloseable()
        }
        if (it is DataExtendedSpaceEngineers) {
            it.spaceEngineers.closeIfCloseable()
        }
        if (it is AutoCloseable) {
            it.close()
        }
    }
}
