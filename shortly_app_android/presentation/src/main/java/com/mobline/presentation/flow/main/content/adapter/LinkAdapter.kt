package com.mobline.presentation.flow.main.content.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobline.domain.model.link.Link
import com.mobline.shortly.presentation.R
import com.mobline.shortly.presentation.databinding.LinkItemBinding
import java.util.*

class LinkAdapter(
    private val dataList: ArrayList<Link>?,
    private val onItemClicked: (link: Link) -> Unit,
    private val onCopyClipboard: (str: String) -> Unit
) : RecyclerView.Adapter<LinkAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LinkItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: kotlin.run { 0 }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        dataList?.get(position)?.let { link ->
            holder.loadData(link)

            with(holder.binding) {
                btnDelete.setOnClickListener { onItemClicked(link) }

                btnCopy.setOnClickListener {
                    onCopyClipboard(link.shortLink)
                    dataList.forEach { it.isCopied = false }
                    link.isCopied = true
                    notifyDataSetChanged()
                }
            }
        }
    }

    class ViewHolder(
        val binding: LinkItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun loadData(link: Link?) = with(binding) {
            tvLongLink.text = link?.longLink
            tvShortLink.text = link?.shortLink
            if(link?.isCopied == true){
                btnCopy.setBackgroundResource(R.drawable.bg_violet_button_with_ripple)
                btnCopy.setText(R.string.copied)
            } else {
                btnCopy.setBackgroundResource(R.drawable.bg_cyan_button_with_ripple)
                btnCopy.setText(R.string.copy)
            }
        }
    }

    fun setData(records: List<Link>) {
        dataList?.clear()
        dataList?.addAll(records)
        notifyDataSetChanged()
    }
}
