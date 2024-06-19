package com.suitebde.usecases.files

import dev.kaccelero.usecases.ITripleSuspendUseCase
import io.ktor.http.*
import me.nathanfallet.cloudflare.models.r2.InputStream

interface IUploadFileUseCase : ITripleSuspendUseCase<String, InputStream, ContentType, Unit>
