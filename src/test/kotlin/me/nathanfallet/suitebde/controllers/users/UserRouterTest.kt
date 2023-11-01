package me.nathanfallet.suitebde.controllers.users

import io.mockk.mockk
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User
import kotlin.reflect.typeOf
import kotlin.test.Test
import kotlin.test.assertEquals

class UserRouterTest {

    @Test
    fun testConstructor() {
        val router = UserRouter(mockk(), mockk(), mockk())
        assertEquals("users", router.route)
        assertEquals(typeOf<User>(), router.typeInfo.kotlinType)
        assertEquals(typeOf<List<User>>(), router.lTypeInfo.kotlinType)
        assertEquals(typeOf<Unit>(), router.pTypeInfo.kotlinType)
        assertEquals(typeOf<UpdateUserPayload>(), router.qTypeInfo.kotlinType)
    }

}