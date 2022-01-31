package com.mobline.presentation.flow.main.content

import android.content.ClipData
import android.content.ClipboardManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mobline.domain.model.link.Link
import com.mobline.domain.usecase.link.DeleteLinkUseCase
import com.mobline.domain.usecase.link.GetLinksUseCase
import com.mobline.domain.usecase.link.InsertLinkUseCase
import com.mobline.domain.usecase.link.ShortenLinkUseCase
import com.mobline.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MainPageViewModel(
    private val insertLinkUseCase: InsertLinkUseCase,
    private val getLinksUseCase: GetLinksUseCase,
    private val deleteLinkUseCase: DeleteLinkUseCase,
    private val shortenLinkUseCase: ShortenLinkUseCase,
    private val clipboardManager: ClipboardManager?
) : BaseViewModel<State, Effect>() {

    private val _isEtLinkInErrorMode = MutableLiveData(false)
    val isEtLinkInErrorMode: LiveData<Boolean>
        get() = _isEtLinkInErrorMode

    fun setEtLinkErrorMode(isError: Boolean) {
        _isEtLinkInErrorMode.value = isError
    }

    init {
        getLinks()
    }

    private fun getLinks() {
        getLinksUseCase.launchNoLoading(Unit) {
            onSuccess = {
                it.collect {
                    postState(State.GetLinksSuccess(it))
                }
            }

            onError = {
                postState(State.GetLinksError)
            }
        }
    }

    private fun insertLink(link: Link) {
        insertLinkUseCase.launchNoLoading(InsertLinkUseCase.Params(link = link)) {
            onSuccess = {
                postEffect(Effect.InsertLinkSuccess)
            }

            onError = {
                postEffect(Effect.InsertLinkError)
            }
        }
    }

    fun deleteLink(link: Link) {
        deleteLinkUseCase.launchNoLoading(DeleteLinkUseCase.Params(link = link)) {
            onSuccess = {
                postEffect(Effect.DeleteLinkSuccess)
            }
            onError = {
                postEffect(Effect.DeleteLinkError)
            }
        }
    }

    fun shortenLink(url: String) {
        shortenLinkUseCase.launch(ShortenLinkUseCase.Params(url = url)) {
            onSuccess = { link ->
                link?.let { insertLink(it) }
                postEffect(Effect.ShortenLinkSuccess)
            }
        }
    }

    fun copyToClipboard(str: String) {
        val clip = ClipData.newPlainText("URL", str)
        clipboardManager?.setPrimaryClip(clip)
    }
}

sealed class Effect {
    object ShortenLinkSuccess : Effect()

    object DeleteLinkSuccess : Effect()
    object DeleteLinkError : Effect()

    object InsertLinkSuccess : Effect()
    object InsertLinkError : Effect()
}

sealed class State {
    class GetLinksSuccess(val links: List<Link>) : State()
    object GetLinksError : State()
}
