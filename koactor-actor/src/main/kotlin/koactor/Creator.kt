package koactor

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.valueParameters

class Creator(
    actorClass: KClass<*>,
    private vararg val parameters: Any
) {

    private val constructor: KFunction<Any>
    private val args: Map<KParameter, Any?>

    init {
        constructor = actorClass.constructors.find { isSameParameter(it) }
                ?: throw IllegalArgumentException("Creator should have valid parameters")

        args = mutableMapOf()
        constructor.parameters.forEachIndexed { index, kParameter ->
            args[kParameter] = parameters[index]
        }
    }

    private fun isSameParameter(constructor: KFunction<Any>): Boolean {
        if (parameters.size != constructor.parameters.size) {
            return false
        }

        (0..(parameters.size - 1)).forEach { index ->
            val arg = parameters[index].javaClass.kotlin
            val constructorArg = constructor.valueParameters[index].type.classifier
            if (arg != constructorArg) {
                return false
            }
        }

        return true
    }

    fun <M> newActor(): Actor<M> {
        @Suppress("UNCHECKED_CAST")
        return constructor.callBy(args) as Actor<M>
    }
}