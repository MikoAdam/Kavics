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
import com.kavics.database.KavicDAO
import com.kavics.fragament.KavicCreateFragment
import com.kavics.model.Kavic
import com.kavics.viewmodel.KavicViewModel
import kotlinx.android.synthetic.main.activity_kavic_list.*
import kotlinx.android.synthetic.main.kavic_list.*


class KavicListActivity : AppCompatActivity(), KavicItemClickListener,
    KavicCreateFragment.KavicCreatedListener {

    companion object {
        lateinit var kavicDAO: KavicDAO
            private set
    }

    private lateinit var simpleItemRecyclerViewAdapter: SimpleItemRecyclerViewAdapter
    private lateinit var kavicViewModel: KavicViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kavic_list)

        //this is the top right menu
        setSupportActionBar(toolbar)
        toolbar.title = title

        // this is the bottom left plus button, you can add new kavics with this
        fab.setOnClickListener {
            val kavicCreateFragment = KavicCreateFragment()
            kavicCreateFragment.show(supportFragmentManager, "TAG")
        }

        kavicViewModel = ViewModelProvider(this).get(KavicViewModel::class.java)
        kavicViewModel.allKavics.observe(this) { kavic ->
            simpleItemRecyclerViewAdapter.addAll(kavic)
        }

        setupRecyclerView()
    }

    //this is the initialization of the kavics list
    private fun setupRecyclerView() {
        simpleItemRecyclerViewAdapter = SimpleItemRecyclerViewAdapter()
        simpleItemRecyclerViewAdapter.itemClickListener = this
        kavic_list.adapter = simpleItemRecyclerViewAdapter
    }

    // when you click on a kavic
    override fun onItemClick(kavic: Kavic) {
        val intent = Intent(this, KavicDetailActivity::class.java)
        intent.putExtra(KavicDetailActivity.KEY_DESC, kavic.description)
        startActivity(intent)
    }

    //long click on a kavic
    override fun onItemLongClick(position: Int, view: View, kavic: Kavic): Boolean {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.menu_kavic)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.delete -> {
                    kavicViewModel.delete(kavic)
                    return@setOnMenuItemClickListener true
                }
            }
            false
        }
        popup.show()
        return false
    }

    //this makes the upper right menu with the menu list
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //this handles what you clicked on in the upper right menu
    //idk yet what to add here because creating new kavic was moved to fab so it is empty now
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
/*        if (item?.itemId == R.id.itemCreateKavic) {
            val kavicCreateFragment = KavicCreateFragment()
            kavicCreateFragment.show(supportFragmentManager, "TAG")
        }
        return super.onOptionsItemSelected(item)*/
        return false
    }

    //putting the newly created kavic into the database and recyclerview
    override fun onKavicCreated(kavic: Kavic) {
        kavicViewModel.insert(kavic)
    }
}
