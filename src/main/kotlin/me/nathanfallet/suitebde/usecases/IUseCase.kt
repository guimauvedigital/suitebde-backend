package me.nathanfallet.suitebde.usecases

interface IUseCase<I, O> {

    operator fun invoke(input: I): O

}