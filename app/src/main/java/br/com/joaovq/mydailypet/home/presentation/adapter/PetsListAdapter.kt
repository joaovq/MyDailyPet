package br.com.joaovq.mydailypet.home.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.joaovq.mydailypet.core.util.extension.loadImage
import br.com.joaovq.mydailypet.core.util.extension.rotateX
import br.com.joaovq.mydailypet.databinding.ItemPetListBinding
import br.com.joaovq.mydailypet.core.domain.model.Pet

class PetsListAdapter(
    private val setOnClickListItem: () -> Unit,
    private val setOnLongClickListItem: (view: View, id: Int) -> Unit,
) : ListAdapter<Pet, PetsListAdapter.PetsListViewHolder>(ItemPetDiff) {
    inner class PetsListViewHolder(private val binding: ItemPetListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pet: Pet) {
            binding.apply {
                tvNickname.text = pet.name
                tvType.text = pet.type
                ivPet.loadImage(url = pet.imageUrl)
                root.setOnClickListener {
                    rvTasksPet.isVisible = !rvTasksPet.isVisible
                    ivDropdown.rotateX()
                    setOnClickListItem()
                }
                root.setOnLongClickListener { view ->
                    setOnLongClickListItem(view, pet.id)
                    true
                }
            }
        }
    }

    override fun onViewRecycled(holder: PetsListViewHolder) {
        holder.itemView.setOnLongClickListener(null)
        super.onViewRecycled(holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetsListViewHolder {
        val binding: ItemPetListBinding =
            ItemPetListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PetsListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PetsListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object ItemPetDiff : DiffUtil.ItemCallback<Pet>() {
        override fun areItemsTheSame(oldItem: Pet, newItem: Pet): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Pet, newItem: Pet): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
