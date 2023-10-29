package me.nathanfallet.suitebde.extensions

import me.nathanfallet.suitebde.usecases.ISuspendUseCase
import me.nathanfallet.suitebde.usecases.IUseCase

operator fun <T, U, V> IUseCase<Pair<T, U>, V>.invoke(t: T, u: U) = invoke(Pair(t, u))
operator fun <T, U, V, W> IUseCase<Triple<T, U, V>, W>.invoke(t: T, u: U, v: V) = invoke(Triple(t, u, v))

suspend operator fun <T, U, V> ISuspendUseCase<Pair<T, U>, V>.invoke(t: T, u: U) = invoke(Pair(t, u))
suspend operator fun <T, U, V, W> ISuspendUseCase<Triple<T, U, V>, W>.invoke(t: T, u: U, v: V) = invoke(Triple(t, u, v))
