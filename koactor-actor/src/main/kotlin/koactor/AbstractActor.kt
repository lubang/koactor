package koactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.sendBlocking

@Suppress("EXPERIMENTAL_API_USAGE")
abstract class AbstractActor<T>(
    private val capacity: Int = Int.MAX_VALUE
) : Actor<T> {

    private var scope = createScope()

    private var mailbox = createMailbox()

    protected abstract fun receive(msg: T)

    override fun tell(msg: T) {
        if (mailbox.isClosedForSend) {
            throw IllegalStateException("Actor was closed")
        }
        mailbox.sendBlocking(msg)
    }

    protected fun stop() {
        mailbox.close()
    }

    private fun createScope() = CoroutineScope(Dispatchers.Default + Job())

    private fun createMailbox(): SendChannel<T> {
        return scope.actor(capacity = capacity) {
            for (msg in channel) {
                receive(msg)
            }
        }
    }
}
