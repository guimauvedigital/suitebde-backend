package me.nathanfallet.suitebde.usecases.files

import me.nathanfallet.cloudflare.models.r2.Object
import me.nathanfallet.usecases.base.ISuspendUseCase

interface IListFilesUseCase : ISuspendUseCase<String, List<Object>>
