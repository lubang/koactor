package koactor.event.sourcing

import koactor.event.DomainEvent
import java.time.ZonedDateTime

data class PersistEvent(
    val id: String,
    val version: Long,
    val occurredAt: ZonedDateTime,
    val event: DomainEvent
)
