package me.nathanfallet.suitebde.usecases.application

import me.nathanfallet.usecases.triple.ITripleUseCase

interface ISendEmailUseCase : ITripleUseCase<String, String, String, Unit>