package fr.imt.henrypotier

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration


class LibraryActivity : AppCompatActivity() {

    private lateinit var realm: Realm
    private val viewModel by lazy { BookViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        val config = RealmConfiguration.Builder(schema = setOf())
            .build()
        this.realm = Realm.open(config)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        viewModel.state.observe(this) { state ->
            Toast.makeText(
                this@LibraryActivity,
                "${state.books.size} books | isLoading ${state.isLoading}",
                Toast.LENGTH_SHORT
            )
                .show()
        }

        viewModel.loadBooks()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.realm.close()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_library, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }
}
