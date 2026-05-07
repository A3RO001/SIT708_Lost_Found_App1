package com.example.lostfoundapp.activities

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.lostfoundapp.R
import com.example.lostfoundapp.database.DatabaseHelper
import com.example.lostfoundapp.model.Item

class ItemListActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var listViewLost: ListView
    private lateinit var listViewFound: ListView
    private lateinit var spinner: Spinner

    private var lostItems: List<Item> = listOf()
    private var foundItems: List<Item> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        dbHelper = DatabaseHelper(this)

        listViewLost = findViewById(R.id.listViewLost)
        listViewFound = findViewById(R.id.listViewFound)
        spinner = findViewById(R.id.spinnerFilter)

        setupLongClickListeners()

        val categories = arrayOf("All", "Electronics", "Pets", "Wallets")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                loadItems(categories[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }

    private fun setupLongClickListeners() {
        listViewLost.setOnItemLongClickListener { _, _, position, _ ->
            showDeleteDialog(lostItems[position])
            true
        }
        listViewFound.setOnItemLongClickListener { _, _, position, _ ->
            showDeleteDialog(foundItems[position])
            true
        }
    }

    private fun showDeleteDialog(item: Item) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle(R.string.delete_item)
        builder.setMessage(R.string.delete_confirm)

        builder.setPositiveButton(R.string.yes) { _, _ ->
            dbHelper.deleteItem(item.id)
            loadItems(spinner.selectedItem.toString())
            Toast.makeText(this, R.string.item_deleted, Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton(R.string.no, null)
        builder.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun loadItems(category: String) {
        val allItems = dbHelper.getAllItems()

        // Filter by category
        val filteredByCategory = if (category == "All") {
            allItems
        } else {
            allItems.filter { it.category == category }
        }

        // Divide into Lost and Found sections
        lostItems = filteredByCategory.filter { it.type == "Lost" }
        foundItems = filteredByCategory.filter { it.type == "Found" }

        // Populate Lost ListView
        val lostTitles = lostItems.map { "${it.title} (${it.category}) - ${it.dateTime}" }
        listViewLost.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lostTitles)

        // Populate Found ListView
        val foundTitles = foundItems.map { "${it.title} (${it.category}) - ${it.dateTime}" }
        listViewFound.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, foundTitles)
    }

    override fun onResume() {
        super.onResume()
        val selectedCategory = spinner.selectedItem?.toString() ?: "All"
        loadItems(selectedCategory)
    }
}