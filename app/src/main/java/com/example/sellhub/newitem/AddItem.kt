package com.example.sellhub.newitem

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.sellhub.R
import com.example.sellhub.managers.UserManager
import com.example.sellhub.services.StorageService
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import java.io.IOException

class AddItem : Fragment() {

    companion object {
        fun newInstance() = AddItem()
    }

    private lateinit var viewModel: AddItemViewModel
    private lateinit var titleEditText: TextInputEditText
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var submitButton: Button
    private lateinit var imageView: ImageView
    private lateinit var chipGroup: ChipGroup
    private lateinit var errorEditText: MaterialTextView
    private var selectedImage: Uri? = null

    private val userManager = UserManager()
    private val PICK_IMAGE_REQUEST = 1 // Unique request code for picking an image


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_item, container, false)

        titleEditText = view.findViewById(R.id.titleEditText)
        descriptionEditText = view.findViewById(R.id.descriptionEditText)
        submitButton = view.findViewById(R.id.submitButton)
        imageView = view.findViewById(R.id.imageViewSelectImages)
        chipGroup = view.findViewById(R.id.chipGroup)
        errorEditText = view.findViewById(R.id.errorTextView)

        submitButton.setOnClickListener {
            onSubmitClicked()
        }
        imageView.setOnClickListener {
            checkGalleryPermissionAndOpen()
        }
        return view
    }

    private fun onSubmitClicked() {
        val title = titleEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val userDisplayName = userManager.getCurrentUser()
        val selectedTypes = viewModel.getSelectedChipTexts(chipGroup)

        if (!validatePostForm(title, description, selectedImage, chipGroup.checkedChipIds))
            return

        StorageService.uploadFileToFirebaseStorage(selectedImage!!) { isSuccess, imageId ->
            if (isSuccess) {
                val item =
                    Item(userDisplayName?.displayName, title, description, imageId, selectedTypes, null,userDisplayName?.uid)
                viewModel.uploadItemToFirestore(item) { isSuccess, errorMessage ->
                    if (!isSuccess) {
                        Toast.makeText(
                            requireContext(),
                            "Failed to post the item",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddItemViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImage = data.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                    requireContext().contentResolver,
                    selectedImage
                )
                imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun openGalleryForImage() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }

    fun checkGalleryPermissionAndOpen() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                1
            )
        } else {
            openGalleryForImage() // Permission already granted, open gallery
        }
    }

    fun validatePostForm(title: String?, description: String?, image: Uri?, selectedType: List<Int>?): Boolean {
        if (viewModel.validateImage(image)) {
            errorEditText.visibility = View.GONE
        } else {
            errorEditText.visibility = View.VISIBLE
            errorEditText.text = "Please select image"
            return false
        }

        if (viewModel.validateText(title)) {
            titleEditText.error = null
        } else {
            titleEditText.error = "Title cannot be empty"
            return false
        }

        if (viewModel.validateText(description)) {
            descriptionEditText.error = null
        } else {
            descriptionEditText.error = "Description cannot be empty"
            return false
        }

        if (viewModel.validateTypeSelection(selectedType)) {
            errorEditText.visibility = View.GONE
        } else {
            errorEditText.visibility = View.VISIBLE
            errorEditText.text = "Please select type"
            return false
        }
        return true
    }
}