package dev.pimentel.data

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MyClassTest {

    @Test
    fun `must return two`() {
        val myClass = MyClass()

        assertEquals(myClass.mustReturnTwo(), 2)
    }
}
