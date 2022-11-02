package com.example.financetracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.financetracker.data.model.Category

class CategoryDropdownAdapter(context: Context, categories: List<Category>) :
    ArrayAdapter<Category>(context, R.layout.dropdown_category, categories) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup) : View {
        val category = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.dropdown_category, parent, false)
        if (category != null) {
            view.findViewById<TextView>(R.id.drop_down_category).text = category.name
        }

        return view
    }
}