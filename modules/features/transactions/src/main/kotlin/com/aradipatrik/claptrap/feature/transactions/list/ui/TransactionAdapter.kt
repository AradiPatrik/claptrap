package com.aradipatrik.claptrap.feature.transactions.list.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aradipatrik.claptrap.feature.transactions.databinding.ListItemTransactionHeaderBinding
import com.aradipatrik.claptrap.feature.transactions.databinding.ListItemTransactionItemBinding
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionListItem
import com.aradipatrik.claptrap.feature.transactions.list.model.TransactionsViewEvent
import com.aradipatrik.claptrap.mvi.Flows.launchInWhenResumed
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import ru.ldralighieri.corbind.view.clicks
import timber.log.Timber
import kotlin.math.max

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

    override fun bindItem(item: TransactionListItem) {
      require(item is TransactionListItem.Header) { "Expected transaction list item got: $item" }
      binding.headerText.text = item.title
    }
  }

  class TransactionItemViewHolder(
    private val binding: ListItemTransactionItemBinding
  ) : TransactionViewHolder(binding.root) {
    private var transactionListItem: TransactionListItem.Item? = null

    val clicks = binding.root.clicks()
      .map { transactionListItem!!.transactionPresentation.domain.id }

    override fun bindItem(item: TransactionListItem) {
      require(item is TransactionListItem.Item) { "Expected transaction list item got: $item" }
      transactionListItem = item
      binding.transactionDate.text = item.transactionPresentation.date
      binding.transactionNote.text = item.transactionPresentation.note
      binding.transactionAmount.text = item.transactionPresentation.amount
      binding.transactionAmountIcon.text = item.transactionPresentation.currencySymbol
      binding.categoryIcon.setImageResource(item.transactionPresentation.categoryIcon)
    }
  }
}

class TransactionAdapter(private val lifecycleScope: LifecycleCoroutineScope) :
  ListAdapter<TransactionListItem, TransactionViewHolder>(TransactionItemItemCallback) {
  private val _headerChangeEvents = MutableStateFlow<String?>(null)
  val headerChangeEvents: Flow<String> = _headerChangeEvents.filterNotNull()
    .distinctUntilChanged()

  private val viewEventFlow =
    MutableSharedFlow<TransactionsViewEvent.TransactionItemClicked>()
  val viewEvents: Flow<TransactionsViewEvent> = viewEventFlow

  private var _layoutManager: LinearLayoutManager? = null
  private val layoutManager: LinearLayoutManager get() = _layoutManager!!

  private var _recyclerView: RecyclerView? = null
  private val recyclerView: RecyclerView get() = _recyclerView!!

  var currentScrollTargetId: String? = null

  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    _layoutManager = recyclerView.layoutManager as LinearLayoutManager
    _recyclerView = recyclerView
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

    currentScrollTargetId?.let {
      findItemAndScrollTo(currentList, it)
    }
  }

  private fun findItemAndScrollTo(
    currentList: List<TransactionListItem>,
    scrollTargetId: String
  ) {
    val scrollTargetPosition = currentList.indexOfFirst {
      it is TransactionListItem.Item && it.transactionPresentation.domain.id == scrollTargetId
    }

    currentScrollTargetId = null

    lifecycleScope.launchWhenResumed {
      // Needed because we want to wait until our recycler view is shown by the animation
      delay(SMOOTH_SCROLL_DELAY)
      recyclerView.smoothScrollToPosition(scrollTargetPosition)
    }
  }

  private fun getFirstVisiblePosition() = layoutManager.findFirstVisibleItemPosition()

  override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
    _layoutManager = null
    _recyclerView = null
  }

  override fun onViewAttachedToWindow(holder: TransactionViewHolder) {
    notifyFirstItemChanged()
  }

  override fun onViewDetachedFromWindow(holder: TransactionViewHolder) {
    notifyFirstItemChanged()
  }

  private fun notifyFirstItemChanged() {
    val position = max(0, layoutManager.findFirstCompletelyVisibleItemPosition() - 1)

    if (position >= 0) {
      _headerChangeEvents.value = when (val item = getItem(position)) {
        is TransactionListItem.Header -> item.title
        is TransactionListItem.Item -> item.transactionPresentation.monthAsText
      }
    }
  }

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
    ).also(::listenToItemClicks)
    else -> error("Unexpected view type: $viewType")
  }

  private fun listenToItemClicks(viewHolder: TransactionViewHolder.TransactionItemViewHolder) =
    viewHolder.clicks
      .map {
        Timber.tag("Clicks").d("clicked item")
        TransactionsViewEvent.TransactionItemClicked(it)
      }
      .onEach(viewEventFlow::emit)
      .launchInWhenResumed(lifecycleScope)

  override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
    holder.bindItem(getItem(position))
  }
}

private const val VIEW_TYPE_HEADER = 0
private const val VIEW_TYPE_ITEM = 1
private const val SMOOTH_SCROLL_DELAY = 600L
