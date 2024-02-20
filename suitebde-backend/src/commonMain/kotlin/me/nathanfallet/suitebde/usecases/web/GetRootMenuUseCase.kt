package me.nathanfallet.suitebde.usecases.web

import io.ktor.server.application.*
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.models.web.WebMenu

class GetRootMenuUseCase(
    private val getLocaleForCallUseCase: IGetLocaleForCallUseCase,
) : IGetRootMenuUseCase {

    override suspend fun invoke(input: ApplicationCall): List<WebMenu> = listOf(

    )

}
