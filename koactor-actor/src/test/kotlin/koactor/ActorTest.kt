package koactor

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.timeout
import com.nhaarman.mockitokotlin2.verify
import koactor.fixture.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ActorTest {

    @Test
    fun `tell a message should handle a message`() {
        val context = ActorContext()
        val helloActor = context.spawn<Message>(Creator(HelloActor::class, "Actor", 3))
        val replyTo = mock<Actor<Reply>>()

        helloActor.tell(Hello("hello", replyTo))
        helloActor.tell(Say("say"))

        verify(replyTo, timeout(500).times(1)).tell(any())
    }

    @Test
    fun `tell a stop should close the mailbox to ignore messages`() {
        val context = ActorContext()
        val helloActor = context.spawn<Message>(Creator(HelloActor::class, "Actor", 1))
        val replyTo = mock<Actor<Reply>>()

        helloActor.tell(Hello("say 1", replyTo))
        helloActor.tell(Hello("say 2", replyTo))
        helloActor.tell(Stop)

        Thread.sleep(100)

        val exception = assertThrows<IllegalStateException> {
            helloActor.tell(Hello("say 3", replyTo))
            helloActor.tell(Hello("say 4", replyTo))
        }

        verify(replyTo, timeout(500).times(2)).tell(any())
        assertEquals("Actor was closed", exception.message)
    }
}