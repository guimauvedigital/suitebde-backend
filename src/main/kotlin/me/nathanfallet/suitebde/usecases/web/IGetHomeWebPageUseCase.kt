package me.nathanfallet.suitebde.usecases.web

import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.usecases.base.ISuspendUseCase

interface IGetHomeWebPageUseCase : ISuspendUseCase<String, WebPage?>
