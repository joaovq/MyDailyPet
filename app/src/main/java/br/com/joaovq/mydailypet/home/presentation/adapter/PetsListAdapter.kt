package br.com.joaovq.mydailypet.home.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.joaovq.core_ui.extension.loadImage
import br.com.joaovq.mydailypet.databinding.ItemPetListBinding
import br.com.joaovq.pet_domain.model.Pet

class PetsListAdapter(
    private val petsListOnLongClickListener: PetListItemClickListener,
) : ListAdapter<Pet, PetsListAdapter.PetsListViewHolder>(ItemPetDiff) {
    inner class PetsListViewHolder(private val binding: ItemPetListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            pet: Pet,
            setOnClickListener: () -> Unit,
            setOnLongClickListener: (View, Pet) -> Unit,
        ) {
            binding.apply {
                tvNamePet.text = pet.name
                tvType.text = pet.breed
                ivPet.loadImage(url = pet.imageUrl)
                root.setOnLongClickListener {
                    setOnLongClickListener(it, pet)
                    true
                }
                root.setOnClickListener { setOnClickListener() }
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
        holder.bind(
            getItem(position),
            petsListOnLongClickListener::setOnClickListener,
            petsListOnLongClickListener::setOnLongClickListItem,
        )
    }

    object ItemPetDiff : DiffUtil.ItemCallback<Pet>() {
        override fun areItemsTheSame(oldItem: Pet, newItem: Pet): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Pet, newItem: Pet): Boolean {
            return oldItem.id == newItem.id
        }
    }

    interface PetListItemClickListener {
        fun setOnClickListener()
        fun setOnLongClickListItem(view: View, pet: Pet)
    }
}
