package com.example.imitrunaapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.imitrunaapp.R
import com.example.imitrunaapp.model.CouponCategory
import com.example.imitrunaapp.model.MenuCategory
import com.example.imitrunaapp.model.Store

fun addClickAnimation(view: View) {
    view.setOnClickListener {
        it.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).withEndAction {
            it.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
        }.start()
    }
}

class MenuGridAdapter : ListAdapter<MenuCategory, MenuGridAdapter.MenuViewHolder>(MenuDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu_grid, parent, false)
        return MenuViewHolder(view)
    }
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvMenuTitle)
        private val ivImage: ImageView = itemView.findViewById(R.id.ivMenuImage)

        init {
            addClickAnimation(itemView)
        }

        fun bind(menu: MenuCategory) {
            tvTitle.text = menu.title
            
            if (menu.imageResId != null) {
                ivImage.setImageResource(menu.imageResId)
                ivImage.setBackgroundColor(Color.TRANSPARENT)
            } else {
                ivImage.setBackgroundColor(Color.parseColor("#D3D3D3"))
                ivImage.setImageDrawable(null)
            }
        }
    }
}
class MenuDiffCallback : DiffUtil.ItemCallback<MenuCategory>() {
    override fun areItemsTheSame(oldItem: MenuCategory, newItem: MenuCategory) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: MenuCategory, newItem: MenuCategory) = oldItem == newItem
}


class StoreAdapter : ListAdapter<Store, StoreAdapter.StoreViewHolder>(StoreDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_store, parent, false)
        return StoreViewHolder(view)
    }
    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    class StoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvStoreName)
        private val tvDist: TextView = itemView.findViewById(R.id.tvDistanceAddress)
        private val tvSched: TextView = itemView.findViewById(R.id.tvSchedule)
        private val rbStore: RadioButton = itemView.findViewById(R.id.rbStore)

        init {
            addClickAnimation(itemView)
        }

        fun bind(store: Store) {
            tvName.text = store.name
            tvDist.text = store.distanceAddress
            tvSched.text = store.scheduleText
            rbStore.isChecked = store.isSelected
        }
    }
}
class StoreDiffCallback : DiffUtil.ItemCallback<Store>() {
    override fun areItemsTheSame(oldItem: Store, newItem: Store) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Store, newItem: Store) = oldItem == newItem
}


class CouponCategoryAdapter : ListAdapter<CouponCategory, CouponCategoryAdapter.CouponCategoryViewHolder>(CouponCatDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponCategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_coupon_category, parent, false)
        return CouponCategoryViewHolder(view)
    }
    override fun onBindViewHolder(holder: CouponCategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    class CouponCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvCouponCatTitle)
        private val ivImage: ImageView = itemView.findViewById(R.id.ivCouponCatImage)

        init {
            addClickAnimation(itemView)
        }

        fun bind(category: CouponCategory) {
            tvTitle.text = category.title
            
            if (category.imageResId != null) {
                ivImage.setImageResource(category.imageResId)
                ivImage.setBackgroundColor(Color.TRANSPARENT)
            } else {
                ivImage.setBackgroundColor(Color.parseColor("#E0E0E0"))
                ivImage.setImageDrawable(null)
            }
        }
    }
}
class CouponCatDiffCallback : DiffUtil.ItemCallback<CouponCategory>() {
    override fun areItemsTheSame(oldItem: CouponCategory, newItem: CouponCategory) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: CouponCategory, newItem: CouponCategory) = oldItem == newItem
}
