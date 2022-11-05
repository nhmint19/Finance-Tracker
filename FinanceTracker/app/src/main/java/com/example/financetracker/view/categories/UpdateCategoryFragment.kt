package com.example.financetracker.view.categories

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.financetracker.R
import com.example.financetracker.model.Category
import com.example.financetracker.viewmodel.CategoryViewModel
import com.google.android.material.textfield.TextInputEditText
import yuku.ambilwarna.AmbilWarnaDialog

class UpdateCategoryFragment : Fragment() {
    private lateinit var categoryVM: CategoryViewModel
    private val args by navArgs<UpdateCategoryFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_category, container, false)

        // get category view model
        categoryVM = ViewModelProvider(this)[CategoryViewModel::class.java]

        // Get current transaction data
        view.let {
            val nameView = it.findViewById<TextInputEditText>(R.id.category_name_edit)
            val colorView = it.findViewById<TextView>(R.id.category_color_edit)

            // color input click listener
            colorView.setOnClickListener {
                onClickColorInput(colorView)
            }

            // preset fields for update screen
            nameView.setText(args.curCategory.name)
            colorView.setBackgroundColor(Color.parseColor(args.curCategory.colorCode))

            // set listener for update button
            it.findViewById<Button>(R.id.update_category_btn).setOnClickListener {
                val name = nameView.text.toString()
                val colorCode = getColorCode(colorView)

                if (TextUtils.isEmpty(name))
                    nameView.error = "Name must not be empty"
                else
                {
                    val category = Category(args.curCategory.id, name, colorCode)
                    categoryVM.updateCategory(category)
                    Toast.makeText(requireContext(), "Updated category successfully!", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_updateCategoryFragment_to_categoryListsFragment)
                }
            }

            // set listener for delete button
            it.findViewById<Button>(R.id.delete_category_btn).setOnClickListener {
                // create a confirmation dialog
                val alertDialog = AlertDialog.Builder(requireContext())
                alertDialog.setTitle("Delete ${args.curCategory.name}?")
                alertDialog.setMessage("Are you sure you want to delete ${args.curCategory.name}?")
                alertDialog.setPositiveButton("Yes") {_, _ ->
                    categoryVM.deleteCategory(args.curCategory)
                    Toast.makeText(requireContext(), "Deleted category successfully!", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_updateCategoryFragment_to_categoryListsFragment)
                }
                alertDialog.setNegativeButton("No") {_, _ ->}
                alertDialog.create().show()
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