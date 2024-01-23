package br.com.joaovq.mydailypet.onboarding.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.joaovq.mydailypet.databinding.ItemOnBoardingContentBinding

class OnboardingPageAdapter :
    ListAdapter<OnboardingItem, OnboardingPageAdapter.OnboardingPageViewHolder>(
        OnboardingDiffUtil,
    ) {
    inner class OnboardingPageViewHolder(private val binding: ItemOnBoardingContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(onboardingItems: OnboardingItem) {
            binding.tvTitleOnBoarding.setText(onboardingItems.title)
            binding.tvDescriptionOnBoarding.setText(onboardingItems.description)
            binding.ivContentOnBoarding.setImageResource(onboardingItems.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingPageViewHolder {
        val binding =
            ItemOnBoardingContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnboardingPageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnboardingPageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object OnboardingDiffUtil : DiffUtil.ItemCallback<OnboardingItem>() {
        override fun areItemsTheSame(oldItem: OnboardingItem, newItem: OnboardingItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: OnboardingItem, newItem: OnboardingItem): Boolean {
            return oldItem.title == newItem.title
        }
    }
}
