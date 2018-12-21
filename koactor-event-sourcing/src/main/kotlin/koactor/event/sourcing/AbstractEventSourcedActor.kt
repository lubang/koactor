package koactor.event.sourcing

import koactor.AbstractActor
import koactor.event.DomainEvent
import java.time.ZonedDateTime

abstract class AbstractEventSourcedActor<T>(
    capacity: Int = Int.MAX_VALUE,
    private val eventStore: EventStore = DefaultEventStore.eventStore!!
) : AbstractActor<T>(capacity) {

    private var version = 0L

    protected abstract fun id(): String

    protected abstract fun receiveEvent(event: DomainEvent)

    protected fun replay(targetVersion: Long) {
        val persistEvents = eventStore.load(id(), 1L, targetVersion)
        if (persistEvents.isEmpty()) {
            return
        }

        for (persistEvent in persistEvents) {
            receiveEvent(persistEvent.event)
            version = persistEvent.version
        }
    }

    protected fun persist(event: DomainEvent) {
        eventStore.save(PersistEvent(id(), nextVersion(), ZonedDateTime.now(), event)) {
            incrementVersion()
        }
    }

    private fun nextVersion(): Long {
        return version + 1L
    }

    private fun incrementVersion() {
        version += 1L
    }
}
