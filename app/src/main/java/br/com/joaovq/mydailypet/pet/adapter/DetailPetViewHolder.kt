package br.com.joaovq.mydailypet.pet.adapter

import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.core.util.extension.format
import br.com.joaovq.mydailypet.databinding.ItemAttachPetBinding
import br.com.joaovq.mydailypet.databinding.ItemTitleLayoutBinding
import br.com.joaovq.mydailypet.pet.domain.model.Attach

sealed class DetailPetViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    class TitleViewHolder(private val titleBinding: ItemTitleLayoutBinding) :
        DetailPetViewHolder(titleBinding) {
        fun bind(@StringRes title: Int) {
            titleBinding.tvTitleItem.setText(title)
        }
    }

    class AttachViewHolder(
        private val attachBinding: ItemAttachPetBinding,
        private val onClickShare: (uri: String) -> Unit,
        private val onClickAttach: (Attach) -> Unit,
    ) : DetailPetViewHolder(attachBinding) {
        fun bind(attach: Attach) {
            attachBinding.apply {
                tvNameAttach.text = attach.name
                tvDateAttach.text = attach.insertedAt.format()
                ivTypeAttach.setImageResource(R.drawable.ic_close)
                ltAttachPet.setOnClickListener {
                    onClickAttach(attach)
                }
                ivShareAttach.setOnClickListener {
                    onClickShare(attach.path)
                }
                /*TODO: Add Types for archives*/
            }
        }
    }
}
