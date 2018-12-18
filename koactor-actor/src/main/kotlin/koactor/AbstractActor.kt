package koactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.sendBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("EXPERIMENTAL_API_USAGE")
abstract class AbstractActor<T> : Actor<T> {
    protected val log: Logger = LoggerFactory.getLogger(this.javaClass.canonicalName)

    private val scope = CoroutineScope(Dispatchers.Default + Job())
    private val mailbox = createMailbox()

    abstract fun receive(msg: T)

    override fun tell(msg: T) {
        if (mailbox.isClosedForSend) {
            throw IllegalStateException("Actor was closed")
        }
        mailbox.sendBlocking(msg)
    }

    protected fun stop() {
        mailbox.close()
    }

    private fun createMailbox(): SendChannel<T> {
        return scope.actor(capacity = Int.MAX_VALUE) {
            for (msg in channel) {
                receive(msg)
            }
        }
    }
}
