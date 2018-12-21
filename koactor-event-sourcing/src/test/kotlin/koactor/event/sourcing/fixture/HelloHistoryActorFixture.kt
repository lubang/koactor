package koactor.event.sourcing.fixture

import koactor.Actor
import koactor.event.DomainEvent
import koactor.event.sourcing.AbstractEventSourcedActor

class HelloHistoryActor : AbstractEventSourcedActor<Protocol>() {

    private var state = HelloHistoryState()

    init {
        replay(Long.MAX_VALUE)
    }

    override fun id(): String {
        return "hello"
    }

    override fun receive(msg: Protocol) {
        when (msg) {
            is Say -> persist(Said(msg.message))
            is ShowHistory -> msg.replyTo.tell(state.getHistoryText())
        }
    }

    override fun receiveEvent(event: DomainEvent) {
        when (event) {
            is Said -> state.update(event)
        }
    }
}

class HelloHistoryState {

    private val histories = mutableListOf<String>()

    fun update(event: DomainEvent) {
        when (event) {
            is Said -> histories.add(event.message)
        }
    }

    fun getHistoryText(): String {
        return histories.joinToString(",")
    }

}


interface Protocol

data class Say(val message: String) : Protocol

data class ShowHistory(val replyTo: Actor<String>) : Protocol

data class Said(val message: String) : DomainEvent
