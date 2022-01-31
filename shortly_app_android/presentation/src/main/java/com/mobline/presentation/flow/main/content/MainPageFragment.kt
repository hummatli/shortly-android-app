package com.mobline.presentation.flow.main.content

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobline.shortly.presentation.R
import com.mobline.shortly.presentation.databinding.FragmentMainPageBinding
import com.mobline.presentation.base.BaseFragment
import com.mobline.presentation.common.RecyclerViewDisabler
import com.mobline.presentation.extensions.clear
import com.mobline.presentation.extensions.makeInvisible
import com.mobline.presentation.extensions.makeVisible
import com.mobline.presentation.flow.main.content.adapter.LinkAdapter
import com.mobline.presentation.tools.setSafeOnClickListener
import kotlin.reflect.KClass


class MainPageFragment :
    BaseFragment<State, Effect, MainPageViewModel, FragmentMainPageBinding>() {

    override val vmClazz: KClass<MainPageViewModel>
        get() = MainPageViewModel::class
    override val bindingCallback: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMainPageBinding
        get() = FragmentMainPageBinding::inflate

    private val rvDisabler = RecyclerViewDisabler()

    private val linkListAdapter by lazy {
        LinkAdapter(
            ArrayList(),
            onItemClicked = {
                viewModel.deleteLink(it)
            },
            onCopyClipboard = {
                viewModel.copyToClipboard(it)
                showToastShort(R.string.url_copied)
            }
        )
    }

    override val bindViews: FragmentMainPageBinding.() -> Unit = {
        initViews()
        initListeners()
        initSubscribers()
    }

    override fun observeState(state: State) {
        when (state) {
            is State.GetLinksSuccess -> {
                state.links.let { links ->
                    linkListAdapter.setData(links)
                    setScreenMode(links.isEmpty())
                }
            }
            is State.GetLinksError -> {
                setScreenMode(true)
            }
        }
    }

    override fun observeEffect(effect: Effect) {
        when (effect) {
            is Effect.ShortenLinkSuccess -> {
                showToastShort(R.string.created_new_link)
                binding.etLink.clear()
                viewModel.setEtLinkErrorMode(false)
                binding.lytHistory.rvLinksHistory.scrollToPosition(0)
            }

            is Effect.DeleteLinkSuccess -> {
                showToastShort(R.string.deleted_link)
            }
            is Effect.DeleteLinkError -> {
                showToastShort(R.string.deleted_link_error)
            }
        }
    }

    //Initiates views
    private fun initViews() = with(binding) {
        lytHistory.rvLinksHistory.layoutManager = LinearLayoutManager(requireContext())
        lytHistory.rvLinksHistory.adapter = linkListAdapter
    }

    //Initiates listeners
    private fun initListeners() = with(binding) {
        requireActivity().onBackPressedDispatcher.addCallback {
            requireActivity().finish()
        }

        btnShortenLink.setSafeOnClickListener {
            etLink.text.run {
                viewModel.setEtLinkErrorMode(this.isBlank())
                if (this.isNotBlank()) {
                    viewModel.shortenLink(etLink.text.toString())
                }
            }
        }
    }

    //Initiates subscribers
    private fun initSubscribers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { setLoading(it) }
        viewModel.isEtLinkInErrorMode.observe(viewLifecycleOwner) { setEtLinkErrorMode(it) }
    }

    private fun setLoading(isLoading: Boolean) = with(binding) {
        if (isLoading) {
            lytRoot.alpha = 0.4F
            pbLoading.makeVisible()
        } else {
            lytRoot.alpha = 1F
            pbLoading.makeInvisible()
        }
        pbLoading.isIndeterminate = isLoading
        btnShortenLink.isEnabled = !isLoading
        etLink.isEnabled = !isLoading

        if (isLoading) {
            lytHistory.rvLinksHistory.addOnItemTouchListener(rvDisabler)
        } else {
            lytHistory.rvLinksHistory.removeOnItemTouchListener(rvDisabler)
        }
    }

    private fun setScreenMode(isEmpty: Boolean) {
        binding.lytHistory.root.visibility = if (isEmpty) View.GONE else View.VISIBLE
        binding.lytEmpty.root.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    /*
        Note:
        TextInputLayout component from material design lib doesn't work as was illustrated in our current design.
        So I manage error states of EditText manually.
     */

    private fun setEtLinkErrorMode(isInErrorMode: Boolean) = with(binding.etLink) {
        if (isInErrorMode) {
            setHint(R.string.please_add_a_link)
            setHintTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            setBackgroundResource(R.drawable.bg_edit_text_box_error)
        } else {
            setHint(R.string.share_a_link)
            setHintTextColor(ContextCompat.getColor(requireContext(), R.color.light_grey))
            setBackgroundResource(R.drawable.bg_edit_text_box)
        }
    }
}
