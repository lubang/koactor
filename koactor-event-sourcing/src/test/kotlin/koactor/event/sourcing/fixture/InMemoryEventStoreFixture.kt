package koactor.event.sourcing.fixture

import koactor.event.sourcing.EventStore
import koactor.event.sourcing.PersistEvent

class InMemoryEventStore : EventStore {
    val events = mutableListOf<PersistEvent>()

    override fun save(persistEvent: PersistEvent, action: () -> Unit) {
        events.add(persistEvent)
        action()
    }

    override fun load(id: String, startVersion: Long, count: Long): List<PersistEvent> {
        val filter = events.filter { it.id == id }
        val fromIndex = startVersion - 1L
        val toIndex = Math.min(count, filter.size.toLong())
        return filter.subList(fromIndex.toInt(), toIndex.toInt())
    }

}
