package me.nathanfallet.suitebde.usecases.web

import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.usecases.base.ISuspendUseCase

interface IGetWebMenusUseCase : ISuspendUseCase<String, List<WebMenu>>
