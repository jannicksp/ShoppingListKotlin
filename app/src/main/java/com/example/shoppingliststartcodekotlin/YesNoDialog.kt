package com.example.shoppingliststartcodekotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.yes_no_dialog.view.*

class YesNoDialog(val addRemoveAllFunc: (Boolean) -> (Unit),val add:Boolean) : DialogFragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.yes_no_dialog, container)
        view.addremoveAll_yes_button.setOnClickListener {
            dismiss()
            addRemoveAllFunc(add)
        }
        view.addremoveAll_no_button.setOnClickListener {
            dismiss()
        }
        return view
    }
}
