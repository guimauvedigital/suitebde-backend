package com.suitebde.usecases.files

import dev.kaccelero.usecases.ISuspendUseCase
import me.nathanfallet.cloudflare.models.r2.Object

interface IListFilesUseCase : ISuspendUseCase<String, List<Object>>
