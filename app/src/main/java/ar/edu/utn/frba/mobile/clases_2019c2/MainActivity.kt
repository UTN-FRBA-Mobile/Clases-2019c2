package ar.edu.utn.frba.mobile.clases_2019c2

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ar.edu.utn.frba.mobile.clases_2019c2.fragments.EditImageFiltersFragment
import ar.edu.utn.frba.mobile.clases_2019c2.fragments.EditImageSubFiltersFragment
import ar.edu.utn.frba.mobile.clases_2019c2.fragments.ImagesFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ImagesFragment.ImagesFragmentInteractionListener, EditImageFiltersFragment.OnFragmentInteractionListener, EditImageSubFiltersFragment.OnFragmentInteractionListener {    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ImagesFragment.newInstance())
                .commit()
        }
    }

    override fun editImageFilters(imageUri: Uri) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, EditImageFiltersFragment.newInstance(imageUri))
            .addToBackStack(null)
            .commit()
    }

    override fun editImageSubFilters() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, EditImageSubFiltersFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    override fun showGrid() {
        // No hagan esto ja
        supportFragmentManager.popBackStack()
        supportFragmentManager.popBackStack()
    }

    override fun popFragment() {
        supportFragmentManager.popBackStack()
    }
}
