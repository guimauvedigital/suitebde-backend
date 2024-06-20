package com.suitebde.usecases.web

import com.suitebde.models.web.WebMenu
import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import io.ktor.server.application.*

class GetRootMenuUseCase(
    private val getLocaleForCallUseCase: IGetLocaleForCallUseCase,
) : IGetRootMenuUseCase {

    override suspend fun invoke(input: ApplicationCall): List<WebMenu> = listOf(

    )

}
