package koactor.event.sourcing

interface EventStore {
    fun save(persistEvent: PersistEvent, action: () -> Unit)
    fun load(id: String, startVersion: Long, count: Long): List<PersistEvent>
}
