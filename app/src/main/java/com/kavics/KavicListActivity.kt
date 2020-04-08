package com.kavics

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.kavics.adapter.KavicItemClickListener
import com.kavics.adapter.SimpleItemRecyclerViewAdapter
import com.kavics.database.AppDatabase
import com.kavics.database.KavicDAO
import com.kavics.fragament.KavicCreateFragment
import com.kavics.fragament.KavicDetailFragment
import com.kavics.model.Kavic
import kotlinx.android.synthetic.main.activity_kavic_list.*
import kotlinx.android.synthetic.main.kavic_list.*

class KavicListActivity : AppCompatActivity(), KavicItemClickListener,
    KavicCreateFragment.KavicCreatedListener {

    //creation of the database
    companion object {
        lateinit var kavicDAO: KavicDAO
            private set
    }

    //this is for the tablet mode
    private var twoPane: Boolean = false
    private lateinit var simpleItemRecyclerViewAdapter: SimpleItemRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kavic_list)

        //building up the database
        kavicDAO = Room.databaseBuilder(this, AppDatabase::class.java, "Kavics")
            .allowMainThreadQueries()
            .build()
            .kavicDAO!!

        //this is the top right menu
        setSupportActionBar(toolbar)
        toolbar.title = title

        // this is the bottom left plus button, you can add new kavics with this
        fab.setOnClickListener {
            val kavicCreateFragment = KavicCreateFragment()
            kavicCreateFragment.show(supportFragmentManager, "TAG")
        }

        // it is for tablet mode
        if (kavic_detail_container != null) {
            twoPane = true
        }

        setupRecyclerView()
    }

    //to not to show outdated kavics on the recyclerview it reloads every time
    override fun onStart() {
        setupRecyclerView()
        super.onStart()
    }

    //to not to show outdated kavics on the recyclerview it reloads every time
    override fun onResume() {
        setupRecyclerView()
        super.onResume()
    }

    //this is the initialization of the kavics list
    private fun setupRecyclerView() {
        
        //getting the already existing kavics from the database
        val kavics = kavicDAO.getKavics()
        simpleItemRecyclerViewAdapter = SimpleItemRecyclerViewAdapter()
        simpleItemRecyclerViewAdapter.itemClickListener = this
        simpleItemRecyclerViewAdapter.addAll(kavics as List<Kavic>)
        kavic_list.adapter = simpleItemRecyclerViewAdapter
    }

    // when you click on a kavic
    override fun onItemClick(kavic: Kavic) {
        if (twoPane) {
            val fragment = KavicDetailFragment.newInstance(kavic.description)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.kavic_detail_container, fragment)
                .commit()
        } else {
            val intent = Intent(this, KavicDetailActivity::class.java)
            intent.putExtra(KavicDetailActivity.KEY_DESC, kavic.description)
            startActivity(intent)
        }
    }

    //long click on a kavic
    override fun onItemLongClick(position: Int, view: View): Boolean {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.menu_kavic)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.delete -> deleteFromDatabaseAndList(position)
            }
            false
        }
        popup.show()
        return false
    }

    //deletes both from database and recycle list
    private fun deleteFromDatabaseAndList(position: Int) {
        //get the id from position then kavic by id then deleting it from the database
        val kavicId = simpleItemRecyclerViewAdapter.getKavicIdFromPosition(position)
        val kavic = kavicDAO.getKavicsById(kavicId)
        kavic?.let { kavicDAO.delete(it) }

        //delete kavic from the recyclerview too
        simpleItemRecyclerViewAdapter.deleteRow(position)
        //update the recyclerview
        simpleItemRecyclerViewAdapter.notifyDataSetChanged()
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
        simpleItemRecyclerViewAdapter.addItem(kavic)
        kavicDAO.insert(kavic)
    }
}
