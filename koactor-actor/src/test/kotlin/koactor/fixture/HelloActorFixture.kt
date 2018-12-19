package koactor.fixture

import koactor.AbstractActor
import koactor.Actor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class HelloActor(
    private val name: String,
    private val count: Int
) : AbstractActor<Message>() {

    private val log: Logger = LoggerFactory.getLogger(HelloActor::class.qualifiedName)

    private var child: Actor<Message>? = null

    override fun receive(msg: Message) {
        when (msg) {
            is Hello -> msg.replyTo.tell(Reply("yo"))
            is Say -> {
                (1..count).forEach {
                    log.debug("$name say '#$it. ${msg.msg}'")
                }
            }
            is Stop -> stop()
            is CreateChild -> child = HelloChildActor()
            is HelloToChild -> child?.tell(msg)
        }
    }
}

interface Message

data class Reply(val msg: String)

data class Hello(val msg: String, val replyTo: Actor<Reply>) : Message

data class Say(val msg: String) : Message

object Stop : Message

object CreateChild : Message

data class HelloToChild(val replyTo: Actor<Reply>) : Message


class HelloChildActor : AbstractActor<Message>() {
    override fun receive(msg: Message) {
        when (msg) {
            is HelloToChild -> msg.replyTo.tell(Reply("Child say yo"))
        }
    }
}