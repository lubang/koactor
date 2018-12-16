package koactor.fixture

import koactor.AbstractActor
import koactor.Actor

class HelloActor(
    private val name: String,
    private val count: Int
) : AbstractActor<Message>() {

    override fun receive(msg: Message) {
        when (msg) {
            is Hello -> msg.replyTo.tell(Reply("yo"))
            is Say -> {
                (1..count).forEach {
                    log.debug("$name say '#$it. ${msg.msg}'")
                }
            }
            is Stop -> stop()
        }
    }
}

interface Message

data class Reply(val msg: String)

data class Hello(val msg: String, val replyTo: Actor<Reply>) : Message

data class Say(val msg: String) : Message

object Stop : Message