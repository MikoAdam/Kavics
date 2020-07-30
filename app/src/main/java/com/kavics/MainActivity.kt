package com.kavics

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.kavics.adapter.DateHelper
import com.kavics.adapter.KavicItemClickListener
import com.kavics.adapter.SimpleItemRecyclerViewAdapter
import com.kavics.create.CreateKavicActivity
import com.kavics.database.KavicDatabase
import com.kavics.edit.EditKavicActivity
import com.kavics.model.OneTimeKavicItem
import com.kavics.viewmodel.KavicViewModel
import kotlinx.android.synthetic.main.activity_kavic_list.*
import kotlinx.android.synthetic.main.kavic_list.*
import kotlinx.coroutines.*
import java.util.*

class MainActivity : AppCompatActivity(), KavicItemClickListener, CoroutineScope by MainScope() {

    private lateinit var simpleItemRecyclerViewAdapter: SimpleItemRecyclerViewAdapter
    private lateinit var kavicViewModel: KavicViewModel
    private lateinit var database: KavicDatabase

    override fun onResume() {
        super.onResume()

        val settings = PreferenceManager.getDefaultSharedPreferences(this)
        val lastTimeStarted = settings.getInt("last_time_started", -1)
        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)

        if (today != lastTimeStarted) {

            archiveOldKavics()
            addRepeatingKavicsForToday()

            val editor = settings.edit()
            editor.putInt("last_time_started", today)
            editor.apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kavic_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        database = KavicDatabase.getDatabase(applicationContext)

        fab.setOnClickListener {
            startActivity(Intent(this, CreateKavicActivity::class.java))
        }

        kavicViewModel = ViewModelProvider(this).get(KavicViewModel::class.java)
        kavicViewModel.allOneTimeKavics.observe(this) { kavic ->
            simpleItemRecyclerViewAdapter.addAll(kavic.sortedBy { it.deadline })
        }

        setupRecyclerView()
    }

    private fun archiveOldKavics() {
        kavicViewModel.setArchiveAllOfOneTimeKavics(DateHelper().getToday())
    }

    private fun addRepeatingKavicsForToday() = launch {
        val repeatingKavics =
            withContext(Dispatchers.IO) { database.repeatingKavicItemDao().getAll() }

        val dateHelper = DateHelper()

        for (i in repeatingKavics) {
            if (dateHelper.equalDate(i)) {
                kavicViewModel.insertOneTimeKavic(
                    OneTimeKavicItem(
                        title = i.title,
                        deadline = dateHelper.getToday()
                    )
                )
            }
        }

    }

    private fun setupRecyclerView() {
        simpleItemRecyclerViewAdapter = SimpleItemRecyclerViewAdapter()
        simpleItemRecyclerViewAdapter.itemClickListener = this
        kavic_list.adapter = simpleItemRecyclerViewAdapter
    }

    override fun onItemClick(oneTimeKavicItem: OneTimeKavicItem) {
        //i haven't decided yet what to have here
    }

    override fun onItemLongClick(
        position: Int,
        view: View,
        oneTimeKavicItem: OneTimeKavicItem
    ): Boolean {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.menu_kavic)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.editKavic -> {
                    val intent = Intent(this, EditKavicActivity::class.java)
                    intent.putExtra(EditKavicActivity.KAVIC_ITEM, oneTimeKavicItem)
                    startActivity(intent)
                    return@setOnMenuItemClickListener true
                }
                R.id.delete -> {
                    deleteKavic(oneTimeKavicItem)
                    return@setOnMenuItemClickListener true
                }
            }
            false
        }
        popup.show()
        return false
    }

    private fun deleteKavic(oneTimeKavicItem: OneTimeKavicItem) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete")
        builder.setMessage("Do you want to delete this Kavic?")

        builder.setPositiveButton("Yes") { _, _ ->
            kavicViewModel.deleteOneTimeKavic(oneTimeKavicItem)
        }

        builder.setNegativeButton("No") { _, _ -> }
        builder.show()
    }

    override fun checkBoxChecked(oneTimeKavicItem: OneTimeKavicItem) {
        Snackbar.make(
            findViewById(R.id.mCoordinatorlayout),
            "Undo kavic done action",
            Snackbar.LENGTH_SHORT
        )
            .setAction("Undo") { reverseKavicDone(oneTimeKavicItem) }
            .show()

        oneTimeKavicItem.done = true
        kavicViewModel.updateOneTimeKavic(oneTimeKavicItem)
    }

    private fun reverseKavicDone(oneTimeKavicItem: OneTimeKavicItem) {
        oneTimeKavicItem.done = false
        kavicViewModel.updateOneTimeKavic(oneTimeKavicItem)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return false
    }

}