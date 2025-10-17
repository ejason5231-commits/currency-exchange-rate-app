package com.example.currencyexchange

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment

class CalculateFragment : Fragment() {

    private val currencies = arrayOf("USD", "EUR", "MMK") // Add more as needed
    private val rates = mapOf( // Hardcoded rates; replace with API later
        "USD_MMK" to 2100.0,
        "EUR_MMK" to 2300.0,
        "MMK_USD" to 1/2100.0,
        "MMK_EUR" to 1/2300.0,
        "USD_EUR" to 0.91,
        "EUR_USD" to 1.1
    )

    private var fromCurrency: String = "USD"
    private var toCurrency: String = "MMK"
    private var isUpdating = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_calculate, container, false)

        val fromSpinner = view.findViewById<Spinner>(R.id.from_spinner)
        val toSpinner = view.findViewById<Spinner>(R.id.to_spinner)
        val fromAmount = view.findViewById<EditText>(R.id.from_amount)
        val toAmount = view.findViewById<EditText>(R.id.to_amount)
        val resultText = view.findViewById<TextView>(R.id.result_text)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fromSpinner.adapter = adapter
        toSpinner.adapter = adapter

        fromSpinner.setSelection(0) // Default USD
        toSpinner.setSelection(2) // Default MMK

        fromSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                fromCurrency = currencies[position]
                updateConversion(fromAmount, toAmount)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        toSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                toCurrency = currencies[position]
                updateConversion(fromAmount, toAmount)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        fromAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (!isUpdating) {
                    updateConversion(fromAmount, toAmount)
                }
            }
        })

        toAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (!isUpdating) {
                    updateConversion(toAmount, fromAmount, reverse = true)
                }
            }
        })

        return view
    }

    private fun updateConversion(source: EditText, target: EditText, reverse: Boolean = false) {
        isUpdating = true
        val amountStr = source.text.toString()
        if (amountStr.isNotEmpty()) {
            val amount = amountStr.toDouble()
            val key = if (!reverse) "${fromCurrency}_${toCurrency}" else "${toCurrency}_${fromCurrency}"
            val rate = rates[key] ?: 1.0
            target.setText(String.format("%.2f", amount * rate))
        } else {
            target.setText("")
        }
        isUpdating = false
    }
}
