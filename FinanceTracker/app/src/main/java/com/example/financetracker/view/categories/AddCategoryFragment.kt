package com.example.financetracker.view.categories

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.financetracker.R
import com.example.financetracker.model.Category
import com.example.financetracker.viewmodel.CategoryViewModel
import com.google.android.material.textfield.TextInputEditText
import yuku.ambilwarna.AmbilWarnaDialog


class AddCategoryFragment : Fragment() {
    private lateinit var categoryVM: CategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_category, container, false)

        val nameView = view.findViewById<TextInputEditText>(R.id.category_name_add)
        val colorView = view.findViewById<TextView>(R.id.category_color_add)

        // get category view model
        categoryVM = ViewModelProvider(this)[CategoryViewModel::class.java]

        // color input click listener
        colorView.setOnClickListener {
            onClickColorInput(colorView)
        }

        // add button click listener
        view.findViewById<Button>(R.id.add_category_btn).setOnClickListener {
            val name = nameView.text.toString()
            val colorCode = getColorCode(colorView)

            if (TextUtils.isEmpty(name))
                nameView.error = "Name must not be empty"
            else
            {
                val category = Category(0, name, colorCode)
                categoryVM.addCategory(category)
                Toast.makeText(requireContext(), "Added category successfully!", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_addCategoryFragment_to_categoryListsFragment)
            }
        }
        return view
    }

    // handle open color dialog
    private fun onClickColorInput(v : View) {
        // References: "com.github.yukuku:ambilwarna:2.0.1"
        val colorPickerDialogue = AmbilWarnaDialog(requireContext(), ResourcesCompat.getColor(resources, R.color.primary, null),
            object : AmbilWarnaDialog.OnAmbilWarnaListener {
                override fun onCancel(dialog: AmbilWarnaDialog?) {
                    // action when click on cancel
                }
                override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                    // change the background color of the text view
                    v.setBackgroundColor(color)
                }
            })
        colorPickerDialogue.show()
    }

    // get the color code of a text view
    private fun getColorCode(v : TextView) : String {
        // default value of color
        var color: Int = ResourcesCompat.getColor(resources, R.color.primary, null)
        val background = v.background
        if (background is ColorDrawable) color = background.color
        // return color code
        return String.format("#%06X", (0xFFFFFF and color)) // #%06X ensures always 6 characters long
    }
}