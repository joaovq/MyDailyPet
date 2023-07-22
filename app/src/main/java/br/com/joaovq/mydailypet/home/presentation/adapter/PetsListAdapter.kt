package br.com.joaovq.mydailypet.home.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.joaovq.mydailypet.databinding.ItemPetListBinding
import br.com.joaovq.mydailypet.pet.domain.model.Pet
import br.com.joaovq.mydailypet.ui.util.extension.loadImage

class PetsListAdapter(
    private val setOnLongClickListItem: (view: View, pet: Pet) -> Unit,
) : ListAdapter<Pet, PetsListAdapter.PetsListViewHolder>(ItemPetDiff) {
    inner class PetsListViewHolder(private val binding: ItemPetListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pet: Pet) {
            binding.apply {
                tvNamePet.text = pet.name
                tvType.text = pet.breed
                ivPet.loadImage(url = pet.imageUrl)
                root.setOnLongClickListener { view ->
                    setOnLongClickListItem(view, pet)
                    true
                }
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.toLong()
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
