package br.com.joaovq.mydailypet.pet.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import br.com.joaovq.core_ui.extension.loadImage
import br.com.joaovq.mydailypet.databinding.ItemPetLayoutBinding
import br.com.joaovq.pet_domain.model.Pet

class SelectorPetsAdapter(private val context: Context, private val pets: List<Pet>) :
    ArrayAdapter<Pet>(context, 0, pets) {
    override fun getItem(position: Int): Pet {
        return pets[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(parent, position)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(parent, position)
    }

    private fun initView(parent: ViewGroup, position: Int): View {
        val binding = ItemPetLayoutBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false,
        )
        val pet = getItem(position)
        binding.tvNamePet.text = pet.name
        binding.civPetImage.loadImage(pet.imageUrl ?: "")
        return binding.root
    }
}
