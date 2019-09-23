package ar.edu.utn.frba.mobile.clases_2019c2.utils.storage.preferences

import android.content.Context
import android.content.SharedPreferences

object MyPreferences {
    private const val showGridKey = "preference_is_grid_images_list_preferred_view"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
    }

    fun setGridImagesListPreferredView(context: Context, value: Boolean) {
        val preferences = getPreferences(context).edit()
        preferences.putBoolean(showGridKey, value)
        preferences.apply()
    }

    fun isGridImagesListPreferredView(context: Context): Boolean {
        return getPreferences(context).getBoolean(showGridKey, false)
    }
}