package com.mobline.domain.usecase.link

import com.mobline.domain.base.BaseUseCase
import com.mobline.domain.exceptions.ErrorConverter
import com.mobline.domain.model.link.Link
import com.mobline.domain.repository.LinkLocalRepository
import kotlin.coroutines.CoroutineContext

class DeleteLinkUseCase(
    context: CoroutineContext,
    converter: ErrorConverter,
    private val repository: LinkLocalRepository,
) : BaseUseCase<DeleteLinkUseCase.Params, Unit>(context, converter) {

    override suspend fun executeOnBackground(params: Params) {
        repository.deleteLink(params.link)
    }

    class Params(val link: Link)
}