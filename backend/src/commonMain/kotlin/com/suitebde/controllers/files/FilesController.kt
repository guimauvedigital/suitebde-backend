package com.suitebde.controllers.files

import com.suitebde.usecases.associations.IRequireAssociationForCallUseCase
import com.suitebde.usecases.files.IListFilesUseCase
import com.suitebde.usecases.files.IUploadFileUseCase
import dev.kaccelero.commons.responses.RedirectResponse
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import kotlinx.datetime.Clock
import me.nathanfallet.cloudflare.models.r2.Object

class FilesController(
    private val requireAssociationForCallUseCase: IRequireAssociationForCallUseCase,
    private val listFilesUseCase: IListFilesUseCase,
    private val uploadFileUseCase: IUploadFileUseCase,
) : IFilesController {

    override suspend fun list(call: ApplicationCall): List<Object> =
        emptyList() //listFilesUseCase(association.id) // To get fixed in cloudflare-api-client

    override suspend fun upload(call: ApplicationCall): RedirectResponse {
        val association = requireAssociationForCallUseCase(call)
        val multipart = call.receiveMultipart()
        multipart.forEachPart { part ->
            if (part is PartData.FileItem) {
                val filename = part.originalFileName ?: "file-${Clock.System.now()}"
                uploadFileUseCase(
                    "${association.id}/$filename",
                    part.streamProvider(),
                    part.contentType ?: ContentType.Application.OctetStream
                )
            }
            part.dispose()
        }
        return RedirectResponse("files")
    }

}
