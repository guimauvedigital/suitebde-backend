package me.nathanfallet.suitebde.usecases.web

import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.usecases.base.IPairSuspendUseCase

interface IGetWebPageByUrlUseCase : IPairSuspendUseCase<String, String, WebPage?>
