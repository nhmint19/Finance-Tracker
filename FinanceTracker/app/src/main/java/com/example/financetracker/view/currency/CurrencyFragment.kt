package com.example.financetracker.view.currency

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.financetracker.R
import com.example.financetracker.api.DEFAULT_CURRENCY
import com.example.financetracker.model.Currency
import com.example.financetracker.viewmodel.CurrencyViewModel

class CurrencyFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var currencyVM: CurrencyViewModel
    private var currencies = emptyList<Currency>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_currency, container, false)
        val spinner = view.findViewById<Spinner>(R.id.currency_setting)

        // get the currencies in the database
        currencyVM = ViewModelProvider(this)[CurrencyViewModel::class.java]
        currencyVM.readCurrency.observe(viewLifecycleOwner) { currencyList ->
            run {
                currencies = currencyList
                val adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.dropdown_currency,
                    currencies.map { currency -> currency.code })
                spinner.adapter = adapter
                // set the current setting
                val pref = this.activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)
                spinner.setSelection(adapter.getPosition(pref?.getString("currency_code", DEFAULT_CURRENCY)))
            }
        }

        // set selected listener
        spinner.onItemSelectedListener = this
        return view
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        val selectedCode = parent.getItemAtPosition(pos).toString()
        val selectedValue = currencies.find { currency -> currency.code == selectedCode }?.value
        // update the text view
        parent.rootView.findViewById<TextView>(R.id.currency_setting_text).text = "$selectedCode = $selectedValue x $DEFAULT_CURRENCY"
        // add the settings to shared preferences
        val sharedPref = this.activity?.getSharedPreferences(
            "settings",
            Context.MODE_PRIVATE) ?: return
        if (selectedValue != null) {
            with (sharedPref.edit()) {
                putString("currency_code", selectedCode)
                putString("currency_value", selectedValue.toString())
                apply()
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
    }
}