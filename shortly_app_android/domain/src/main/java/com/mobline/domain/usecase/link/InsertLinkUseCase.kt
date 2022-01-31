package com.mobline.domain.usecase.link

import com.mobline.domain.base.BaseUseCase
import com.mobline.domain.exceptions.ErrorConverter
import com.mobline.domain.model.link.Link
import com.mobline.domain.repository.LinkLocalRepository
import kotlin.coroutines.CoroutineContext

class InsertLinkUseCase(
    context: CoroutineContext,
    converter: ErrorConverter,
    private val repository: LinkLocalRepository,
) : BaseUseCase<InsertLinkUseCase.Params, Unit>(context, converter) {

    override suspend fun executeOnBackground(params: Params) {
        return repository.insertLink(params.link)
    }

    class Params(val link: Link)
}