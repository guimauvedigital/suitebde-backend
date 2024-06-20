package com.suitebde.usecases.application

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlin.test.Test
import kotlin.test.assertEquals

class AddDurationUseCaseTest {

    @Test
    fun testInvokeOneSecond() {
        val useCase = AddDurationUseCase()
        val now = Clock.System.now()
        assertEquals(
            now.plus(1, DateTimeUnit.SECOND, TimeZone.currentSystemDefault()),
            useCase(now, "1s")
        )
    }

    @Test
    fun testInvokeOneMinute() {
        val useCase = AddDurationUseCase()
        val now = Clock.System.now()
        assertEquals(
            now.plus(1, DateTimeUnit.MINUTE, TimeZone.currentSystemDefault()),
            useCase(now, "1m")
        )
    }

    @Test
    fun testInvokeOneHour() {
        val useCase = AddDurationUseCase()
        val now = Clock.System.now()
        assertEquals(
            now.plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault()),
            useCase(now, "1h")
        )
    }

    @Test
    fun testInvokeOneDay() {
        val useCase = AddDurationUseCase()
        val now = Clock.System.now()
        assertEquals(
            now.plus(1, DateTimeUnit.DAY, TimeZone.currentSystemDefault()),
            useCase(now, "1d")
        )
    }

    @Test
    fun testInvokeOneMonth() {
        val useCase = AddDurationUseCase()
        val now = Clock.System.now()
        assertEquals(
            now.plus(1, DateTimeUnit.MONTH, TimeZone.currentSystemDefault()),
            useCase(now, "1M")
        )
    }

    @Test
    fun testInvokeOneYear() {
        val useCase = AddDurationUseCase()
        val now = Clock.System.now()
        assertEquals(
            now.plus(1, DateTimeUnit.YEAR, TimeZone.currentSystemDefault()),
            useCase(now, "1y")
        )
    }

    @Test
    fun testInvokeOneCombined() {
        val useCase = AddDurationUseCase()
        val now = Clock.System.now()
        assertEquals(
            now
                .plus(1, DateTimeUnit.YEAR, TimeZone.currentSystemDefault())
                .plus(1, DateTimeUnit.MONTH, TimeZone.currentSystemDefault())
                .plus(1, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
                .plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault())
                .plus(1, DateTimeUnit.MINUTE, TimeZone.currentSystemDefault())
                .plus(1, DateTimeUnit.SECOND, TimeZone.currentSystemDefault()),
            useCase(now, "1y1M1d1h1m1s")
        )
    }

    @Test
    fun testInvokeOneCombinedWithSpaces() {
        val useCase = AddDurationUseCase()
        val now = Clock.System.now()
        assertEquals(
            now
                .plus(1, DateTimeUnit.YEAR, TimeZone.currentSystemDefault())
                .plus(1, DateTimeUnit.MONTH, TimeZone.currentSystemDefault())
                .plus(1, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
                .plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault())
                .plus(1, DateTimeUnit.MINUTE, TimeZone.currentSystemDefault())
                .plus(1, DateTimeUnit.SECOND, TimeZone.currentSystemDefault()),
            useCase(now, "1y 1M 1d 1h 1m 1s")
        )
    }

}
