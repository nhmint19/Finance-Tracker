package com.example.financetracker.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.financetracker.R
import com.example.financetracker.data.viewmodel.CategoryViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CategoryListsFragment : Fragment() {
    private lateinit var categoryVM : CategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_category_lists, container, false)

        // set up Recycler View
        val categoryList = view.findViewById<RecyclerView>(R.id.categories_list)
        val adapter = CategoryAdapter()
        categoryList.adapter = adapter
        categoryList.layoutManager = LinearLayoutManager(requireContext())

        // CategoryVM
        categoryVM = ViewModelProvider(this)[CategoryViewModel::class.java]
        categoryVM.readAllCategories.observe(viewLifecycleOwner) { category ->
            adapter.setData(category)
        }

        // set up listen to float action button
        view.findViewById<FloatingActionButton>(R.id.ic_add_category).setOnClickListener {
            findNavController().navigate(R.id.action_categoryListsFragment_to_addCategoryFragment)
        }

        return view
    }
}