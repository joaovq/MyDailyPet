package br.com.joaovq.mydailypet.pet.presentation.adapter

import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import br.com.joaovq.core.model.Attach
import br.com.joaovq.core.util.extension.format
import br.com.joaovq.core_ui.databinding.ItemTitleLayoutBinding
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.databinding.ItemAttachPetBinding

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
                ivTypeAttach.setImageResource(br.com.joaovq.core_ui.R.drawable.ic_close)
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
