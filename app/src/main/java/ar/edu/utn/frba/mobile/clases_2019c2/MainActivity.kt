package ar.edu.utn.frba.mobile.clases_2019c2

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MainFragment.newInstance(getString(R.string.hello)))
                .commit()
        }
    }

    override fun onOkTapped() {
            AlertDialog.Builder(this)
                .setTitle(R.string.hello)
                .setPositiveButton(R.string.ok, { _, _ -> })
                .create().show()
    }
}
