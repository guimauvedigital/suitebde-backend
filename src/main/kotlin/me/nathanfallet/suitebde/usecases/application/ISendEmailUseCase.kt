package me.nathanfallet.suitebde.usecases.application

import me.nathanfallet.usecases.base.triple.ITripleUseCase

interface ISendEmailUseCase : ITripleUseCase<String, String, String, Unit>