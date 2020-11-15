package com.aradipatrik.claptrap.feature.transactions.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aradipatrik.claptrap.feature.transactions.R
import com.aradipatrik.claptrap.feature.transactions.model.TransactionListItem

object TransactionItemItemCallback : DiffUtil.ItemCallback<TransactionListItem>() {
  override fun areItemsTheSame(
    oldItem: TransactionListItem,
    newItem: TransactionListItem
  ) = when {
    oldItem is TransactionListItem.Header && newItem is TransactionListItem.Header ->
      oldItem.title == newItem.title
    oldItem is TransactionListItem.Item && newItem is TransactionListItem.Item ->
      oldItem.transactionPresentation.domain.id == newItem.transactionPresentation.domain.id
    else -> false
  }

  override fun areContentsTheSame(
    oldItem: TransactionListItem,
    newItem: TransactionListItem
  ) = when {
    oldItem is TransactionListItem.Header && newItem is TransactionListItem.Header ->
      oldItem.title == newItem.title
    oldItem is TransactionListItem.Item && newItem is TransactionListItem.Item ->
      oldItem.transactionPresentation == newItem.transactionPresentation
    else -> false
  }
}

sealed class TransactionViewHolder(view: View) :
  RecyclerView.ViewHolder(view) {
  abstract fun bindItem(item: TransactionListItem)

  class TransactionHeaderViewHolder(view: View) : TransactionViewHolder(view) {
    override fun bindItem(item: TransactionListItem) {
      require(item is TransactionListItem.Header) { "Expected transaction list item got: $item" }

    }
  }

  class TransactionItemViewHolder(view: View) : TransactionViewHolder(view) {
    override fun bindItem(item: TransactionListItem) {
      require(item is TransactionListItem.Item) { "Expected transaction list item got: $item" }

    }
  }
}

class TransactionAdapter(lifecycleScope: LifecycleCoroutineScope) :
  ListAdapter<TransactionListItem, TransactionViewHolder>(TransactionItemItemCallback) {
  override fun getItemViewType(position: Int) = when (getItem(position)) {
    is TransactionListItem.Header -> VIEW_TYPE_HEADER
    is TransactionListItem.Item -> VIEW_TYPE_ITEM
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
    VIEW_TYPE_HEADER -> TransactionViewHolder.TransactionHeaderViewHolder(
      LayoutInflater.from(parent.context).inflate(R.layout.list_item_transaction_item, parent, false)
    )
    VIEW_TYPE_ITEM -> TransactionViewHolder.TransactionItemViewHolder(
      LayoutInflater.from(parent.context).inflate(R.layout.list_item_transaction_item, parent, false)
    )
    else -> error("Unexpected view type: $viewType")
  }

  override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
    holder.bindItem(getItem(position))
  }
}

private const val VIEW_TYPE_HEADER = 0
private const val VIEW_TYPE_ITEM = 1
