package com.example.lostfoundapp.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.lostfoundapp.R
import com.example.lostfoundapp.database.DatabaseHelper
import com.example.lostfoundapp.model.Item
import java.text.SimpleDateFormat
import java.util.*

class AddItemActivity : AppCompatActivity() {

    private var imageUri: Uri? = null
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        dbHelper = DatabaseHelper(this)

        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val spinner = findViewById<Spinner>(R.id.spinnerCategory)
        val btnImage = findViewById<Button>(R.id.btnImage)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val imagePreview = findViewById<ImageView>(R.id.imagePreview)
        val rgType = findViewById<RadioGroup>(R.id.rgType)
        val tvHeader = findViewById<TextView>(R.id.tvHeader)

        rgType.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbLost) {
                tvHeader.text = getString(R.string.report_lost_item)
            } else {
                tvHeader.text = getString(R.string.report_found_item)
            }
        }

        // Spinner setup
        val categories = arrayOf("Electronics", "Pets", "Wallets")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Image picker
        btnImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1)
        }

        // Save button
        btnSave.setOnClickListener {

            val title = etTitle.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val category = spinner.selectedItem.toString()

            // VALIDATION
            if (title.isEmpty()) {
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (description.isEmpty()) {
                Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (imageUri == null) {
                Toast.makeText(this, "Please upload an image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // SAVE DATA
            val dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

            val item = Item(
                title = title,
                description = description,
                category = category,
                imageUri = imageUri.toString(),
                dateTime = dateTime,
                type = "Lost"
            )

            dbHelper.insertItem(item)

            Toast.makeText(this, "Item Saved!", Toast.LENGTH_SHORT).show()
            finish()
        }

        val btnBack = findViewById<Button>(R.id.btnBackHome)

        btnBack.setOnClickListener {
            finish()
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data!!
            val imagePreview = findViewById<ImageView>(R.id.imagePreview)
            imagePreview.setImageURI(imageUri)
            imagePreview.visibility = View.VISIBLE
        }
    }
}