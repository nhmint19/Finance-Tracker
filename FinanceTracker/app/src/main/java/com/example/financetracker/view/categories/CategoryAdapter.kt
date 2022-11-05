package com.example.financetracker.view.categories

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.financetracker.R
import com.example.financetracker.model.Category

class CategoryAdapter: RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private var categories = emptyList<Category>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.category_layout, parent, false) as View
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = categories[position]
        holder.bind(item)
    }

    inner class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {
        private val nameView = v.findViewById<TextView>(R.id.category_name)
        private val colorView = v.findViewById<TextView>(R.id.category_color)

        fun bind(item: Category) {
            nameView.text = item.name
            colorView.setBackgroundColor(Color.parseColor(item.colorCode))
            // click a transaction to open the update screen
            v.findViewById<LinearLayout>(R.id.category_layout).setOnClickListener {
                val action = CategoryListsFragmentDirections.actionCategoryListsFragmentToUpdateCategoryFragment(item)
                v.findNavController().navigate(action)
            }
        }
    }

    fun setData(categories: List<Category>) {
        this.categories = categories
        notifyDataSetChanged()
    }
}