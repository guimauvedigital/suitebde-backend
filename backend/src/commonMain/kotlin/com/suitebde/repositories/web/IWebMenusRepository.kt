package com.suitebde.repositories.web

import com.suitebde.models.web.CreateWebMenuPayload
import com.suitebde.models.web.UpdateWebMenuPayload
import com.suitebde.models.web.WebMenu
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.IChildModelSuspendRepository

interface IWebMenusRepository :
    IChildModelSuspendRepository<WebMenu, UUID, CreateWebMenuPayload, UpdateWebMenuPayload, UUID>
