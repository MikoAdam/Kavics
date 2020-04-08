package com.kavics

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.kavics.fragament.KavicDetailFragment
import kotlinx.android.synthetic.main.activity_kavic_detail.*

class KavicDetailActivity : AppCompatActivity() {

    companion object {
        const val KEY_DESC = "KEY_DESC"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kavic_detail)
        setSupportActionBar(detail_toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            val fragment = KavicDetailFragment.newInstance(intent.getStringExtra(KEY_DESC))

            supportFragmentManager.beginTransaction()
                .add(R.id.kavic_detail_container, fragment)
                .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                navigateUpTo(Intent(this, KavicListActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}
