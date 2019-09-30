package ar.edu.utn.frba.mobile.clases_2019c2

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("Conversor de Unidades de temperatura")

        val conversor = ConversorUnidades()
        //val gradosCelsius = temperaturaIngresadaC


        val botonC = findViewById(R.id.botonCelsius) as ImageButton
        botonC.setOnClickListener {
            // Llamo a celsiustof y celsius to k
            val valorC = temperaturaIngresadaC.text.toString().toDouble()
            temperaturaIngresadaF.setText(conversor.celsiusToF(valorC).toString())
            temperaturaIngresadaK.setText(conversor.celsiusToK(valorC).toString())
        }

        val botonK = findViewById(R.id.botonKelvin) as ImageButton

        botonK.setOnClickListener {
            // Llamo a kelvintoc y kelvintof
            val valorK = temperaturaIngresadaK.text.toString().toDouble()
            temperaturaIngresadaC.setText(conversor.kelvinToC(valorK).toString())
            temperaturaIngresadaF.setText(conversor.kelvinToF(valorK).toString())
        }

        val botonF = findViewById(R.id.botonFahrenheit) as ImageButton

        botonF.setOnClickListener {
            // Llamo a fahtoc y fahtok
            val valorF = temperaturaIngresadaF.text.toString().toDouble()
            temperaturaIngresadaC.setText( conversor.fahToC(valorF).toString())
            temperaturaIngresadaK.setText(conversor.fahToK(valorF).toString())
        }


}
}
