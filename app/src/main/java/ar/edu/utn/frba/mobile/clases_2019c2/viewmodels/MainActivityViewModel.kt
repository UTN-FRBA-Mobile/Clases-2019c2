package ar.edu.utn.frba.mobile.clases_2019c2.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import ar.edu.utn.frba.mobile.clases_2019c2.utils.storage.fileSystem.ExternalContent
import com.zomato.photofilters.imageprocessors.Filter
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter

class MainActivityViewModel : ViewModel() {
    lateinit var imageUri: Uri
    var filterSelected = Filter()
    var brightnessSelected = 0
    var saturationSelected = 1.0f
    var contrastSelected = 1.0f

    private fun originalImage(context: Context): Bitmap {
        return ExternalContent.getBitmapFromGallery(context, imageUri, 100, 100)
    }

    private fun generatedFilter(): Filter {
        return Filter().apply {
            addSubFilter(BrightnessSubFilter(brightnessSelected))
            addSubFilter(ContrastSubFilter(contrastSelected))
            addSubFilter(SaturationSubfilter(saturationSelected))
        }
    }

    fun editedImage(context: Context): Bitmap? {
        if (::imageUri.isInitialized) {
            val prefilteredImage = filterSelected.processFilter(
                originalImage(context).copy(
                    Bitmap.Config.ARGB_8888,
                    true
                )
            )
            return generatedFilter().processFilter(
                prefilteredImage.copy(
                    Bitmap.Config.ARGB_8888,
                    true
                )
            )
        }
        else
            return null
    }

    fun init() {
        filterSelected = Filter()
        brightnessSelected = 0
        saturationSelected = 1.0f
        contrastSelected = 1.0f
    }
}