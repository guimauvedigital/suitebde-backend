package me.nathanfallet.suitebde.controllers.users

import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals

class UserRouterTest {

    @Test
    fun testConstructor() {
        val router = UserRouter(mockk(), mockk(), mockk())
        assertEquals("users", router.route)
    }

}