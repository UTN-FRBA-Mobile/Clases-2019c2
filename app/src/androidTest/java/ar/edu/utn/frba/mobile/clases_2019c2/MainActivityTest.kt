package ar.edu.utn.frba.mobile.clases_2019c2

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import org.hamcrest.core.IsNot.not
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest{

    @Rule
    @JvmField
    var activityRule = ActivityTestRule<MainActivity>(
        MainActivity::class.java
    )

    @Test
    fun botonesDesactivadosSinTexto (){
        onView(withId(R.id.temperaturaIngresadaC)).check(matches(withText("")))
        onView(withId(R.id.botonCelsius)).check(matches(not(isEnabled())))
    }

    @Test
    fun ingresoValorYconvierto (){
        onView(withId(R.id.temperaturaIngresadaC)).perform(ViewActions.typeText("10"))
        onView(withId(R.id.botonCelsius)).perform(ViewActions.click())
        onView(withId(R.id.temperaturaIngresadaF)).check(matches(withText("50.0")))
    }
}