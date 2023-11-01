package me.nathanfallet.suitebde.controllers.associations

import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals

class AssociationRouterTest {

    @Test
    fun testConstructor() {
        val router = AssociationRouter(mockk(), mockk(), mockk())
        assertEquals("associations", router.route)
    }

}