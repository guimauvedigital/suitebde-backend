package me.nathanfallet.suitebde.usecases

interface ISuspendUseCase<I, O> {

    suspend operator fun invoke(input: I): O

}