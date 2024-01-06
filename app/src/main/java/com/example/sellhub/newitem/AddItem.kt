package com.example.sellhub.newitem

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.sellhub.R
import com.example.sellhub.managers.UserManager
import com.google.android.material.textfield.TextInputEditText

class AddItem : Fragment() {

    companion object {
        fun newInstance() = AddItem()
    }

    private lateinit var viewModel: AddItemViewModel
    private lateinit var titleEditText: TextInputEditText
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var submitButton: Button
    private val userManager = UserManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_item, container, false)

        titleEditText = view.findViewById(R.id.titleEditText)
        descriptionEditText = view.findViewById(R.id.descriptionEditText)
        submitButton = view.findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            onSubmitClicked()
        }
        return view
    }

    private fun onSubmitClicked() {
        val title = titleEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val userDisplayName = userManager.getCurrentUser()
        val item = Item(userDisplayName?.displayName, title, description)
        viewModel.uploadItemToFirestore(item) { isSuccess, errorMessage ->
            if (!isSuccess) {
                Toast.makeText(
                    requireContext(),
                    "Failed to post the item",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddItemViewModel::class.java)
        // TODO: Use the ViewModel
    }

}