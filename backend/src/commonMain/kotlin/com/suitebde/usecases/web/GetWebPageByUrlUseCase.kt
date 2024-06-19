package com.suitebde.usecases.web

import com.suitebde.models.web.WebPage
import com.suitebde.repositories.web.IWebPagesRepository
import dev.kaccelero.models.UUID

class GetWebPageByUrlUseCase(
    private val repository: IWebPagesRepository,
) : IGetWebPageByUrlUseCase {

    override suspend fun invoke(input1: String, input2: UUID): WebPage? = repository.getByUrl(input1, input2)

}
