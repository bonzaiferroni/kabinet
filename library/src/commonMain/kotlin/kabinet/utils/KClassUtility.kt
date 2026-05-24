package kabinet.utils

import kotlin.reflect.KClass

val KClass<*>.nameOrError get() = this.simpleName ?: error("cannot use on anonymous classes")