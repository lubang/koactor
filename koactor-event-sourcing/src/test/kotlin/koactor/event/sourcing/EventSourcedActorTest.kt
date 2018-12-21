package koactor.event.sourcing

import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.timeout
import com.nhaarman.mockitokotlin2.verify
import koactor.Actor
import koactor.event.sourcing.fixture.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

@DisplayName("An EventSourcedActor")
internal class EventSourcedActorTest {

    @Test
    fun `persist should save a persist event into an event store`() {
        val eventStore = InMemoryEventStore()
        DefaultEventStore.eventStore = eventStore

        val actor = HelloHistoryActor()
        actor.tell(Say("hi #01"))

        Thread.sleep(100)

        val persistEvent = eventStore.events[0]
        assertEquals("hello", persistEvent.id)
        assertEquals(1L, persistEvent.version)
        assertNotNull(persistEvent.occurredAt)
        assertEquals(Said("hi #01"), persistEvent.event)
    }

    @Test
    fun `load when actor is created should replay persist events from an event store`() {
        val eventStore = InMemoryEventStore()
        eventStore.events.add(PersistEvent("hello", 1L, ZonedDateTime.now(), Said("hi #01")))
        eventStore.events.add(PersistEvent("hello", 1L, ZonedDateTime.now(), Said("hi #02")))
        DefaultEventStore.eventStore = eventStore
        val replyTo = mock<Actor<String>>()

        val actor = HelloHistoryActor()
        actor.tell(ShowHistory(replyTo))

        verify(replyTo, timeout(100)).tell(eq("hi #01,hi #02"))
    }

}
