package com.aradipatrik.claptrap.backdrop.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.core.content.res.getDrawableOrThrow
import androidx.core.content.res.getStringOrThrow
import com.aradipatrik.claptrap.R
import com.aradipatrik.claptrap.theme.widget.ViewUtil.getDimenValue
import com.aradipatrik.claptrap.theme.widget.ViewUtil.inflate
import com.aradipatrik.claptrap.theme.widget.ViewUtil.withStyleable
import kotlinx.android.synthetic.main.view_backdrop_menu_item.view.*
import ru.ldralighieri.corbind.view.clicks

class BackdropBackLayerMenuItemView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  @AttrRes defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
  private lateinit var iconDrawable: Drawable
  private lateinit var text: String
  private val deactivatedAlpha by lazy { context.getDimenValue(R.dimen.alpha_medium) }
  private val clickAreaActiveAlpha by lazy { context.getDimenValue(R.dimen.alpha_low) }

  init {
    withStyleable(R.styleable.BackdropBackLayerMenuItemView, attrs) {
      iconDrawable = getDrawableOrThrow(R.styleable.BackdropBackLayerMenuItemView_icon)
      text = getStringOrThrow(R.styleable.BackdropBackLayerMenuItemView_title)
    }

    initView()
  }

  private fun initView() {
    inflate(R.layout.view_backdrop_menu_item)

    backdrop_menu_item_icon.setImageDrawable(iconDrawable)
    backdrop_menu_item_text.text = text
  }

  fun activate() {
    backdrop_menu_item_icon.alpha = 1.0f
    backdrop_menu_item_text.alpha = 1.0f
    backdrop_menu_item_tap_area.isClickable = false
    backdrop_menu_item_tap_area.alpha = clickAreaActiveAlpha
  }

  fun deactivate() {
    backdrop_menu_item_icon.alpha = deactivatedAlpha
    backdrop_menu_item_text.alpha = deactivatedAlpha
    backdrop_menu_item_tap_area.isClickable = true
    backdrop_menu_item_tap_area.alpha = 0.0f
  }

  val clicks = backdrop_menu_item_tap_area.clicks()
}