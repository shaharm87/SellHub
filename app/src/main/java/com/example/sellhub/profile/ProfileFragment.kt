package com.example.sellhub.profile

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sellhub.CardAdapter
import com.example.sellhub.CardData
import com.example.sellhub.MainActivity
import com.example.sellhub.R
import com.example.sellhub.Register
import com.example.sellhub.services.StorageService
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText


enum class EditMode {
    Edit,
    NoEdit
}

class ProfileFragment : Fragment() {
    private lateinit var viewModel: ProfileViewModel
    private val cardList = mutableListOf<CardData>()
    private val adapter = CardAdapter(cardList)
    private lateinit var editButton: MaterialButton
    private lateinit var logOutButton: MaterialButton

    private lateinit var displayInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText


    private var buttonMode = EditMode.NoEdit


    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        editButton = view.findViewById(R.id.editButton)
        logOutButton = view.findViewById(R.id.logOut)
        displayInput = view.findViewById(R.id.displayInput)
        emailInput = view.findViewById(R.id.emailEditText)

        setNoEditMode()
        editButton.setOnClickListener {
            if (buttonMode == EditMode.NoEdit) {
                setEditMode()
            } else {
                viewModel.updateDisplayName(displayInput.text.toString()) { success, message ->
                    onEditDisplay(success, message)
                }
            }
        }

        logOutButton.setOnClickListener {
            viewModel.logOut()
            val intent = Intent(activity, Register::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        setCardsData()
        displayInput.setText(viewModel.getDisplayName())
        emailInput.setText(viewModel.getEmail())
    }

    private fun setCardsData() {
        viewModel.getItems { success, items ->
            if (success) {
                val newCards = mutableListOf<CardData>();
                for (item in items) {
                    val cardData = CardData(item, null)
                    newCards.add(cardData)
                    if (item.imageId != null) {
                        StorageService.downloadImage(cardData.item.imageId.toString()) { success, imageBit ->
                            if (success) {
                                cardData.image = imageBit
                                adapter.notifyItemChanged(cardList.indexOf(cardData))
                            }
                        }
                    }
                }
                cardList.clear()
                cardList.addAll(newCards)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun setEditMode() {
        displayInput.isFocusableInTouchMode = true
        displayInput.isClickable = true
        displayInput.isFocusable = true
        displayInput.requestFocus()
        buttonMode = EditMode.Edit
        editButton.setIconResource(0)
        editButton.text = "Save"
    }

    private fun onEditDisplay(isSuccess: Boolean, errorMessage: String?) {
        if (isSuccess) {
            setNoEditMode()
        } else {
            Toast.makeText(
                requireContext(),
                "Failed update DisplayName",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setNoEditMode() {
        displayInput.isFocusableInTouchMode = false
        displayInput.isClickable = false
        displayInput.isFocusable = false
        buttonMode = EditMode.NoEdit
        editButton.setIconResource(R.drawable.edit)
        editButton.text = null
    }
}