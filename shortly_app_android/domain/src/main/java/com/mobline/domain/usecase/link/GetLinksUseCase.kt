package com.mobline.domain.usecase.link

import com.mobline.domain.base.BaseUseCase
import com.mobline.domain.exceptions.ErrorConverter
import com.mobline.domain.model.link.Link
import com.mobline.domain.repository.LinkLocalRepository
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class GetLinksUseCase(
    context: CoroutineContext,
    converter: ErrorConverter,
    private val repository: LinkLocalRepository,
) : BaseUseCase<Unit, Flow<List<Link>>>(context, converter) {

    override suspend fun executeOnBackground(params: Unit): Flow<List<Link>> {
        return repository.getLinks()
    }
}