package koactor

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.timeout
import com.nhaarman.mockitokotlin2.verify
import koactor.fixture.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("An Actor")
internal class ActorTest {

    @Test
    fun `should handle a message`() {
        val helloActor = HelloActor("Actor", 3)
        val replyTo = mock<Actor<Reply>>()

        helloActor.tell(Hello("hello", replyTo))
        helloActor.tell(Say("say"))

        verify(replyTo, timeout(500).times(1)).tell(any())
    }

    @Test
    fun `should close the mailbox to ignore messages`() {
        val helloActor = HelloActor("Actor", 3)
        val replyTo = mock<Actor<Reply>>()

        helloActor.tell(Hello("say 1", replyTo))
        helloActor.tell(Hello("say 2", replyTo))
        helloActor.tell(Stop)

        Thread.sleep(100)

        val exception = assertThrows(IllegalStateException::class.java) {
            helloActor.tell(Hello("say 3", replyTo))
            helloActor.tell(Hello("say 4", replyTo))
        }

        verify(replyTo, timeout(500).times(2)).tell(any())
        assertEquals("Actor was closed", exception.message)
    }

    @Test
    fun `should bypass a message to it's child actor`() {
        val helloActor = HelloActor("Actor", 3)
        val replyTo = mock<Actor<Reply>>()

        helloActor.tell(CreateChild)
        helloActor.tell(HelloToChild(replyTo))

        verify(replyTo, timeout(500).times(1)).tell(any())
    }
}