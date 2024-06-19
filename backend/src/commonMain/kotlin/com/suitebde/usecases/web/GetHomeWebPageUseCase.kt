package com.suitebde.usecases.web

import com.suitebde.models.web.WebPage
import com.suitebde.repositories.web.IWebPagesRepository
import dev.kaccelero.models.UUID

class GetHomeWebPageUseCase(
    private val repository: IWebPagesRepository,
) : IGetHomeWebPageUseCase {

    override suspend fun invoke(input: UUID): WebPage? = repository.getHome(input)

}
