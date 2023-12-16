package com.example.project_final

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var imageIcon: ImageView
    private lateinit var editTextEmail: EditText
    private lateinit var editTextName: EditText
    private lateinit var btnSignUp: Button

    companion object {
        private const val REQUEST_GALLERY = 0
        private const val REQUEST_CAMERA = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        imageIcon = findViewById(R.id.imageIcon)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextName = findViewById(R.id.editTextName)
        btnSignUp = findViewById(R.id.btnSignUp)


        // Check if the user is already logged in
        if (FirebaseAuth.getInstance().currentUser != null) {
            // User is already logged in, redirect to HomeActivity
            startActivity(Intent(this, HomeScreen::class.java))
            finish() // Finish the SignUpActivity
        }

        imageIcon.setOnClickListener {
            showImageSelectionDialog()
        }

        btnSignUp.setOnClickListener {
            val email = editTextEmail.text.toString()
            val name = editTextName.text.toString()

            if (email.isEmpty() || name.isEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, name)
                    .addOnCompleteListener {
                        if (!it.isSuccessful) return@addOnCompleteListener

                        // else if successful
                        val intent = Intent(this, HomeScreen::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        // else if unsuccessful
                        return@addOnFailureListener
                    }
            }
        }
    }

    private fun showImageSelectionDialog() {
        val items = arrayOf("Gallery", "Camera")
        AlertDialog.Builder(this)
            .setTitle("Select Image Source")
            .setItems(items) { _, which ->
                when (which) {
                    0 -> openGallery()
                    1 -> openCamera()
                }
            }
            .show()
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, REQUEST_GALLERY)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_GALLERY -> handleGalleryResult(data)
                REQUEST_CAMERA -> handleCameraResult(data)
            }
        }
    }

    private fun handleGalleryResult(data: Intent?) {
        val selectedImageUri = data?.data
        displayImage(selectedImageUri)
    }

    private fun handleCameraResult(data: Intent?) {
        val photo: Bitmap? = data?.extras?.get("data") as Bitmap?
        displayImage(photo)
    }

    private fun displayImage(imageUri: Any?) {
        // Logic to display the selected image in the ImageView
        imageIcon.setImageURI(null) // Clear previous image
        imageIcon.setImageURI(imageUri as? android.net.Uri)
    }
}
