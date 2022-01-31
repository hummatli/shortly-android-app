package com.mobline.domain.di

import com.mobline.domain.exceptions.ErrorConverter
import com.mobline.domain.exceptions.ErrorConverterImpl
import com.mobline.domain.usecase.link.DeleteLinkUseCase
import com.mobline.domain.usecase.link.GetLinksUseCase
import com.mobline.domain.usecase.link.InsertLinkUseCase
import com.mobline.domain.usecase.link.ShortenLinkUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module


const val IO_CONTEXT = "IO_CONTEXT"
const val ERROR_MAPPER_GENERAL = "ERROR_MAPPER_NETWORK"

val domainModule = module {
    single<ErrorConverter> {
        ErrorConverterImpl(
            setOf(
                get(named(ERROR_MAPPER_GENERAL))
            )
        )
    }

    factory {
        DeleteLinkUseCase(
            context = get(named(IO_CONTEXT)),
            converter = get(),
            repository = get()
        )
    }

    factory {
        GetLinksUseCase(
            context = get(named(IO_CONTEXT)),
            converter = get(),
            repository = get()
        )
    }

    factory {
        InsertLinkUseCase(
            context = get(named(IO_CONTEXT)),
            converter = get(),
            repository = get()
        )
    }

    factory {
        ShortenLinkUseCase(
            context = get(named(IO_CONTEXT)),
            converter = get(),
            repository = get()
        )
    }
}
