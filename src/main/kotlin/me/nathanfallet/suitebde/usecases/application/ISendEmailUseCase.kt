package me.nathanfallet.suitebde.usecases.application

import me.nathanfallet.suitebde.usecases.ISuspendUseCase

interface ISendEmailUseCase : ISuspendUseCase<Triple<String, String, String>, Unit>