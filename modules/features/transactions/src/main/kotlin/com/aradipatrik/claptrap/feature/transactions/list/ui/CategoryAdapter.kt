package com.aradipatrik.claptrap.feature.transactions.list.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aradipatrik.claptrap.feature.transactions.databinding.ListItemCategoryBinding
import com.aradipatrik.claptrap.feature.transactions.list.model.CategoryIconMapper.drawableRes
import com.aradipatrik.claptrap.feature.transactions.list.model.CategoryListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

object CategoryItemCallback : DiffUtil.ItemCallback<CategoryListItem>() {
  override fun areItemsTheSame(
    oldItem: CategoryListItem,
    newItem: CategoryListItem
  ) = oldItem.category.id == newItem.category.id

  override fun areContentsTheSame(
    oldItem: CategoryListItem,
    newItem: CategoryListItem
  ) = oldItem == newItem
}

class CategoryViewHolder(
  val binding: ListItemCategoryBinding
) : RecyclerView.ViewHolder(binding.root) {
  fun bind(categoryListItem: CategoryListItem, onClick: (CategoryListItem) -> Unit) {
    binding.categoryIcon.setImageResource(categoryListItem.category.icon.drawableRes)
    binding.categoryNameTextView.text = categoryListItem.category.name
    binding.root.setOnClickListener { onClick(categoryListItem) }
    binding.root.isActivated = categoryListItem.isSelected
  }
}

class CategoryAdapter : ListAdapter<CategoryListItem, CategoryViewHolder>(CategoryItemCallback) {
  private val _categorySelectedEvents = MutableStateFlow<CategoryListItem?>(null)
  val categorySelectedEvents: Flow<CategoryListItem> = _categorySelectedEvents
    .filterNotNull()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoryViewHolder(
    ListItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
  )

  override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) =
    holder.bind(getItem(position)) { clickedCategory ->
      _categorySelectedEvents.value = clickedCategory
    }
}
