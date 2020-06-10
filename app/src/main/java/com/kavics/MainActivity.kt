package com.kavics

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.kavics.adapter.KavicItemClickListener
import com.kavics.adapter.SimpleItemRecyclerViewAdapter
import com.kavics.create.CreateKavicActivity
import com.kavics.model.KavicItem
import com.kavics.viewmodel.KavicViewModel
import kotlinx.android.synthetic.main.activity_kavic_list.*
import kotlinx.android.synthetic.main.kavic_list.*

class MainActivity : AppCompatActivity(), KavicItemClickListener {

    private lateinit var simpleItemRecyclerViewAdapter: SimpleItemRecyclerViewAdapter
    private lateinit var kavicViewModel: KavicViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kavic_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener {
            startActivity(Intent(this, CreateKavicActivity::class.java))
        }

        kavicViewModel = ViewModelProvider(this).get(KavicViewModel::class.java)
        kavicViewModel.allKavics.observe(this) { kavic ->
            simpleItemRecyclerViewAdapter.addAll(kavic.sortedBy { it.deadline })
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        simpleItemRecyclerViewAdapter = SimpleItemRecyclerViewAdapter()
        simpleItemRecyclerViewAdapter.itemClickListener = this
        kavic_list.adapter = simpleItemRecyclerViewAdapter
    }

    override fun onItemClick(kavicItem: KavicItem) {
        val intent = Intent(this, EditKavicActivity::class.java)
        intent.putExtra(EditKavicActivity.KAVIC_ITEM, kavicItem)
        startActivity(intent)
    }

    override fun onItemLongClick(position: Int, view: View, kavicItem: KavicItem): Boolean {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.menu_kavic)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.delete -> {
                    kavicViewModel.delete(kavicItem)
                    return@setOnMenuItemClickListener true
                }
            }
            false
        }
        popup.show()
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return false
    }

}