package com.mobline.domain.usecase.link

import com.mobline.domain.base.BaseUseCase
import com.mobline.domain.exceptions.ErrorConverter
import com.mobline.domain.model.link.Link
import com.mobline.domain.repository.LinkRemoteRepository
import kotlin.coroutines.CoroutineContext

class ShortenLinkUseCase(
    context: CoroutineContext,
    converter: ErrorConverter,
    private val repository: LinkRemoteRepository,
) : BaseUseCase<ShortenLinkUseCase.Params, Link?>(context, converter) {

    override suspend fun executeOnBackground(params: Params): Link? {
        return repository.shortenLink(params.url)
    }

    class Params(val url: String)
}