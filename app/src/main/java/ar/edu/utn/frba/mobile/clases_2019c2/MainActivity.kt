package ar.edu.utn.frba.mobile.clases_2019c2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val host = NavHostFragment.create(R.navigation.nav_graph)
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,host).setPrimaryNavigationFragment(host).commit()
    }
}



