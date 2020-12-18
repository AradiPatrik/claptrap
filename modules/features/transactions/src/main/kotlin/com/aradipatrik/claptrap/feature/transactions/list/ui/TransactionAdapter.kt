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
import com.aradipatrik.claptrap.feature.transactions.mapper.DateToStringMapper
import com.aradipatrik.claptrap.mvi.Flows.launchInWhenResumed
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import ru.ldralighieri.corbind.view.clicks
import ru.ldralighieri.corbind.view.longClicks
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
      .map { binding to transactionListItem!!.transactionPresentation.domain.id }

    val longClicks = binding.root.longClicks()
      .map { transactionListItem!!.transactionPresentation.domain.id }

    override fun bindItem(item: TransactionListItem) {
      require(item is TransactionListItem.Item) { "Expected transaction list item got: $item" }
      transactionListItem = item
      with(binding) {
        transactionDate.text = item.transactionPresentation.date
        transactionNote.text = item.transactionPresentation.note
        transactionAmount.text = item.transactionPresentation.amount
        transactionAmountIcon.text = item.transactionPresentation.currencySymbol
        categoryIcon.setImageResource(item.transactionPresentation.categoryIcon)

        root.transitionName = item.transactionPresentation.domain.id
      }
    }
  }
}

class TransactionAdapter @AssistedInject constructor(
  private val dateToStringMapper: DateToStringMapper,
  @Assisted private val lifecycleScope: LifecycleCoroutineScope
) : ListAdapter<TransactionListItem, TransactionViewHolder>(TransactionItemItemCallback) {
  @AssistedInject.Factory
  interface Factory {
    fun create(lifecycleScope: LifecycleCoroutineScope): TransactionAdapter
  }

  private val _headerChangeEvents = MutableStateFlow<String?>(null)
  val headerChangeEvents: Flow<String> = _headerChangeEvents.filterNotNull()
    .distinctUntilChanged()

  private val viewEventFlow = MutableSharedFlow<TransactionsViewEvent>()
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
        is TransactionListItem.Item -> dateToStringMapper.mapLongMonthDay(
          firstItem.transactionPresentation.domain.date
        )
      }
    }

    currentScrollTargetId?.let {
      findItemAndScrollTo(currentList, it)
    }
  }

  /**
   * This is a great big hack, I am driven to the edge of madness trying to figure out why
   * when there's a deletion or insertion happening inside the recycler view, it can't figure
   * out, that it should remove it's view, unless the user scrolls it. So now, when the item list
   * changes we scroll the recycler view by a tiny amount so it actually removes the deleted item
   * from itself. This might be happening because we are in a motion layout.
   */
  private fun makeRecyclerViewRealizeThatItsActuallyShowingItems() =
    lifecycleScope.launchWhenResumed {
      delay(SMOOTH_SCROLL_DELAY)
      // I'm so sad :(
      recyclerView.smoothScrollBy(100, 1)
    }

  private fun findItemAndScrollTo(
    currentList: List<TransactionListItem>,
    scrollTargetId: String
  ) {
    val scrollTargetPosition = currentList.indexOfFirst {
      it is TransactionListItem.Item && it.transactionPresentation.domain.id == scrollTargetId
    }

    if (scrollTargetPosition != -1) {
      currentScrollTargetId = null

      lifecycleScope.launchWhenResumed {
        // Needed because we want to wait until our recycler view is shown by the animation
        delay(SMOOTH_SCROLL_DELAY)
        recyclerView.smoothScrollToPosition(scrollTargetPosition)
      }
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
        is TransactionListItem.Item -> dateToStringMapper.mapLongMonthDay(
          item.transactionPresentation.domain.date
        )
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
    merge(
      viewHolder.longClicks.map { TransactionsViewEvent.DeleteTransactionRequested(it) },
      viewHolder.clicks
        .map { (itemView, id) ->
          TransactionsViewEvent.TransactionItemClicked(itemView, id)
        }
    )
      .onEach(viewEventFlow::emit)
      .launchInWhenResumed(lifecycleScope)

  override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
    holder.bindItem(getItem(position))
  }
}

private const val VIEW_TYPE_HEADER = 0
private const val VIEW_TYPE_ITEM = 1
private const val SMOOTH_SCROLL_DELAY = 600L
