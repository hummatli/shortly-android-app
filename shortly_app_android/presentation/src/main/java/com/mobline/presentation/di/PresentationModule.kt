package com.mobline.presentation.di

import com.mobline.presentation.flow.main.MainViewModel
import com.mobline.presentation.flow.main.content.MainPageViewModel
import com.mobline.presentation.flow.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel {
        SplashViewModel()
    }

    viewModel {
        MainPageViewModel(
            insertLinkUseCase = get(),
            getLinksUseCase = get(),
            deleteLinkUseCase = get(),
            shortenLinkUseCase = get(),
            clipboardManager = get()
        )
    }

    viewModel {
        MainViewModel()
    }
}