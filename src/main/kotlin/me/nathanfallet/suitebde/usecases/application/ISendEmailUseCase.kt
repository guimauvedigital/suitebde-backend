package me.nathanfallet.suitebde.usecases.application

import me.nathanfallet.suitebde.usecases.IUseCase

interface ISendEmailUseCase : IUseCase<Triple<String, String, String>, Unit>