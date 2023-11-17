package me.nathanfallet.suitebde.repositories.web

import me.nathanfallet.suitebde.models.web.CreateWebMenuPayload
import me.nathanfallet.suitebde.models.web.UpdateWebMenuPayload
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository

interface IWebMenusRepository :
    IChildModelSuspendRepository<WebMenu, String, CreateWebMenuPayload, UpdateWebMenuPayload, String> {

    suspend fun getMenus(associationId: String): List<WebMenu>

}
