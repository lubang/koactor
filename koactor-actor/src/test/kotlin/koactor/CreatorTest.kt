package koactor

import koactor.fixture.HelloActor
import koactor.fixture.Message
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@DisplayName("A Creator")
internal class CreatorTest {

    @Test
    fun `create should create an actor instance`() {
        val creator = Creator(HelloActor::class, "id", 2)
        val instance = creator.newActor<Message>()

        assertNotNull(instance)
    }

    @Test
    fun `create with an invalid parameter count should throw an IllegalArgumentException`() {
        val exception = assertThrows<IllegalArgumentException> {
            Creator(HelloActor::class, "id", 2, 3)
        }

        assertEquals("Creator should have valid parameters", exception.message)
    }

    @Test
    fun `create with an invalid parameter type should throw an IllegalArgumentException`() {
        val exception = assertThrows<IllegalArgumentException> {
            Creator(HelloActor::class, "id", "hi")
        }

        assertEquals("Creator should have valid parameters", exception.message)
    }
}