package br.com.joaovq.mydailypet.pet.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import br.com.joaovq.core.model.Attach
import br.com.joaovq.core_ui.databinding.ItemTitleLayoutBinding
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.databinding.ItemAttachPetBinding

class DetailsPetAdapter(private val onClickEvent: OnClickEvent) :
    ListAdapter<DetailItem, DetailPetViewHolder>(DetailDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailPetViewHolder {
        return when (viewType) {
            DetailItem.TITLE_VIEW_TYPE -> {
                DetailPetViewHolder.TitleViewHolder(
                    ItemTitleLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    ),
                )
            }

            DetailItem.ATTACH_VIEW_TYPE -> {
                DetailPetViewHolder.AttachViewHolder(
                    ItemAttachPetBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    ),
                    onClickAttach = onClickEvent::setOnClickAttach,
                    onClickShare = onClickEvent::setOnClickShareAttach,
                )
            }

            else -> throw IllegalArgumentException("Cannot attribute view type an view type")
        }
    }

    override fun onBindViewHolder(holder: DetailPetViewHolder, position: Int) {
        when (holder) {
            is DetailPetViewHolder.AttachViewHolder -> {
                holder.bind(
                    (getItem(position) as DetailItem.AttachItem).attach,
                )
            }

            is DetailPetViewHolder.TitleViewHolder -> {
                holder.bind(
                    (getItem(position) as DetailItem.TitleItem).textTitle,
                )
            }
        }
    }

    fun renderList(pet: br.com.joaovq.pet_domain.model.Pet) {
        val detailItems = mutableListOf<DetailItem>()
        when {
            pet.attachs.isNotEmpty() -> {
                detailItems.add(DetailItem.TitleItem(R.string.text_attach_title))
                val attachItems = pet.attachs.map { DetailItem.AttachItem(it) }
                detailItems.addAll(attachItems)
            }
        }
        submitList(detailItems)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DetailItem.TitleItem -> DetailItem.TITLE_VIEW_TYPE
            is DetailItem.AttachItem -> DetailItem.ATTACH_VIEW_TYPE
        }
    }

    object DetailDiffUtil : DiffUtil.ItemCallback<DetailItem>() {
        override fun areItemsTheSame(oldItem: DetailItem, newItem: DetailItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DetailItem, newItem: DetailItem): Boolean {
            return when (oldItem) {
                is DetailItem.TitleItem -> {
                    oldItem.textTitle == (newItem as DetailItem.TitleItem).textTitle
                }

                is DetailItem.AttachItem -> {
                    oldItem.attach == (newItem as DetailItem.AttachItem).attach
                }
            }
        }
    }

    interface OnClickEvent {
        fun setOnClickShareAttach(uri: String)
        fun setOnClickAttach(attach: Attach)
    }
}
