package com.aradipatrik.claptrap.feature.transactions.list.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aradipatrik.claptrap.domain.Category
import com.aradipatrik.claptrap.feature.transactions.databinding.ListItemCategoryBinding
import com.aradipatrik.claptrap.feature.transactions.list.model.CategoryIconMapper.drawableRes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

object CategoryItemCallback : DiffUtil.ItemCallback<Category>() {
  override fun areItemsTheSame(oldItem: Category, newItem: Category) = oldItem.id == newItem.id

  override fun areContentsTheSame(oldItem: Category, newItem: Category) = oldItem == newItem
}

class CategoryViewHolder(
  val binding: ListItemCategoryBinding
) : RecyclerView.ViewHolder(binding.root) {
  fun bind(category: Category, onClick: (Category) -> Unit) {
    binding.categoryIcon.setImageResource(category.icon.drawableRes)
    binding.categoryNameTextView.text = category.name
    binding.root.setOnClickListener { onClick(category) }
  }
}

class CategoryAdapter : ListAdapter<Category, CategoryViewHolder>(CategoryItemCallback) {
  private val _categorySelectedEvents = MutableStateFlow<Category?>(null)
  val categorySelectedEvents: Flow<Category> = _categorySelectedEvents
    .filterNotNull()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoryViewHolder(
    ListItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
  )

  override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) =
    holder.bind(getItem(position)) { clickedCategory ->
      _categorySelectedEvents.value = clickedCategory
    }
}
