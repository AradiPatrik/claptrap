package com.aradipatrik.claptrap.feature.transactions.list.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aradipatrik.claptrap.feature.transactions.databinding.ListItemTransactionHeaderBinding
import com.aradipatrik.claptrap.feature.transactions.databinding.ListItemTransactionItemBinding
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull

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

sealed class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
  abstract fun bindItem(item: TransactionListItem)

  class TransactionHeaderViewHolder(
    private val binding: ListItemTransactionHeaderBinding
  ) : TransactionViewHolder(binding.root) {
    private var _headerItem: TransactionListItem.Header? = null
    val headerItem get() = _headerItem!!

    override fun bindItem(item: TransactionListItem) {
      require(item is TransactionListItem.Header) { "Expected transaction list item got: $item" }
      _headerItem = item
      binding.headerText.text = item.title
    }
  }

  class TransactionItemViewHolder(
    private val binding: ListItemTransactionItemBinding
  ) : TransactionViewHolder(binding.root) {
    private var _transactionListItem: TransactionListItem.Item? = null
    val transactionListItem get() = _transactionListItem!!

    override fun bindItem(item: TransactionListItem) {
      require(item is TransactionListItem.Item) { "Expected transaction list item got: $item" }
      _transactionListItem = item
      binding.transactionDate.text = item.transactionPresentation.date
      binding.transactionNote.text = item.transactionPresentation.note
      binding.transactionAmount.text = item.transactionPresentation.amount
      binding.transactionAmountIcon.text = item.transactionPresentation.currencySymbol
      binding.categoryIcon.setImageResource(item.transactionPresentation.categoryIcon)
    }
  }
}

class TransactionAdapter :
  ListAdapter<TransactionListItem, TransactionViewHolder>(TransactionItemItemCallback) {
  private val _headerChangeEvents = MutableStateFlow<String?>(null)
  val headerChangeEvents: Flow<String> = _headerChangeEvents.filterNotNull()
    .distinctUntilChanged()

  private var _layoutManager: LinearLayoutManager? = null
  private val layoutManager: LinearLayoutManager get() = _layoutManager!!

  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    _layoutManager = recyclerView.layoutManager as LinearLayoutManager
  }

  override fun onCurrentListChanged(
    previousList: MutableList<TransactionListItem>,
    currentList: MutableList<TransactionListItem>
  ) {
    val firstItemPosition = getFirstVisiblePosition()

    val firstVisibleItem = if (firstItemPosition == -1) {
      currentList.firstOrNull()
    } else {
      currentList[firstItemPosition]
    }

    firstVisibleItem?.let { firstItem ->
      _headerChangeEvents.value = when (firstItem) {
        is TransactionListItem.Header -> firstItem.title
        is TransactionListItem.Item -> firstItem.transactionPresentation.monthAsText
      }
    }
  }

  private fun getFirstVisiblePosition() = layoutManager.findFirstVisibleItemPosition()

  override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
    _layoutManager = null
  }

  override fun onViewDetachedFromWindow(holder: TransactionViewHolder) {
    if (isFirstInList(holder)) {
      notifyFirstItemChanged(holder)
    }
  }

  override fun onViewAttachedToWindow(holder: TransactionViewHolder) {
    if (isFirstInList(holder)) {
      notifyFirstItemChanged(holder)
    }
  }

  private fun notifyFirstItemChanged(holder: TransactionViewHolder) {
    _headerChangeEvents.value = when (holder) {
      is TransactionViewHolder.TransactionHeaderViewHolder -> holder.headerItem.title
      is TransactionViewHolder.TransactionItemViewHolder -> holder
        .transactionListItem
        .transactionPresentation
        .monthAsText
    }
  }

  private fun isFirstInList(holder: TransactionViewHolder) =
    holder.adapterPosition == layoutManager.findFirstVisibleItemPosition() - 1

  override fun getItemViewType(position: Int) = when (getItem(position)) {
    is TransactionListItem.Header -> VIEW_TYPE_HEADER
    is TransactionListItem.Item -> VIEW_TYPE_ITEM
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
    VIEW_TYPE_HEADER -> TransactionViewHolder.TransactionHeaderViewHolder(
      ListItemTransactionHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
    VIEW_TYPE_ITEM -> TransactionViewHolder.TransactionItemViewHolder(
      ListItemTransactionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
    else -> error("Unexpected view type: $viewType")
  }

  override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
    holder.bindItem(getItem(position))
  }
}

private const val VIEW_TYPE_HEADER = 0
private const val VIEW_TYPE_ITEM = 1
