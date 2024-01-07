package com.example.sellhub.services

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.sellhub.newitem.Item
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import java.io.File
import java.util.UUID

object StorageService {

    val storageRef = Firebase.storage
    fun downloadImage(imageID: String, callback: (Boolean, Bitmap?) -> Unit) {
        val storageRef = Firebase.storage.reference.child(imageID)
        val localFile = File.createTempFile("images", "jpg")

        storageRef.getFile(localFile)
            .addOnSuccessListener { _ ->
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                callback(true, bitmap)
            }
            .addOnFailureListener { exception ->
                callback(false, null)
            }
    }

    fun uploadFileToFirebaseStorage(uri: Uri, callback: (Boolean, String?) -> Unit) {
        val id = UUID.randomUUID().toString()
        val fileRef = Firebase.storage.reference.child(id) // Unique ID for the file

        val uploadTask = fileRef.putFile(uri)

        uploadTask.addOnSuccessListener {
            callback(true, id)
        }.addOnFailureListener {
            callback(false, null)
        }
    }
}