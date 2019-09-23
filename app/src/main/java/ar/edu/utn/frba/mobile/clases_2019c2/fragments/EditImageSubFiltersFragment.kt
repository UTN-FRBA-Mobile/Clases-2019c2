package ar.edu.utn.frba.mobile.clases_2019c2.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ar.edu.utn.frba.mobile.clases_2019c2.R
import ar.edu.utn.frba.mobile.clases_2019c2.utils.storage.fileSystem.InternalStorage
import ar.edu.utn.frba.mobile.clases_2019c2.viewmodels.MainActivityViewModel
import kotlinx.android.synthetic.main.fragment_edit_image_subfilters.*
import kotlinx.android.synthetic.main.fragment_edit_image_subfilters.view.*
import java.util.*

class EditImageSubFiltersFragment : Fragment(), SeekBar.OnSeekBarChangeListener  {
    init {
        System.loadLibrary("NativeImageProcessor")
    }

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var processHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(activity!!).get(MainActivityViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_edit_image_subfilters, container, false)

        // keeping brightness value b/w -100 / +100
        view.seekbar_brightness.max = 200
        view.seekbar_brightness.progress = viewModel.brightnessSelected + 100

        // keeping contrast value b/w 1.0 - 3.0
        view.seekbar_contrast.max = 20
        view.seekbar_contrast.progress = (viewModel.contrastSelected / .10f).toInt()

        // keeping saturation value b/w 0.0 - 3.0
        view.seekbar_saturation.max = 30
        view.seekbar_saturation.progress = (viewModel.saturationSelected / .10f).toInt()

        view.seekbar_brightness.setOnSeekBarChangeListener(this)
        view.seekbar_contrast.setOnSeekBarChangeListener(this)
        view.seekbar_saturation.setOnSeekBarChangeListener(this)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        image_to_edit.setImageBitmap(viewModel.editedImage(context!!))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
        Thread({
            Looper.prepare()
            processHandler = Handler(Looper.myLooper())
            Looper.loop()
        }).start()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        processHandler.post {
            Looper.myLooper().quit()
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
        if (listener != null) {
            if (seekBar.id == R.id.seekbar_brightness) {
                // brightness values are b/w -100 to +100
                viewModel.brightnessSelected = progress - 100
                updateImage()
            }

            if (seekBar.id == R.id.seekbar_contrast) {
                // converting int value to float
                // contrast values are b/w 1.0f - 3.0f
                // progress = progress > 10 ? progress : 10;
                viewModel.saturationSelected = .10f * (progress + 10)
                updateImage()
            }

            if (seekBar.id == R.id.seekbar_saturation) {
                // converting int value to float
                // saturation values are b/w 0.0f - 3.0f
                viewModel.contrastSelected = .10f * progress
                updateImage()
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_subfilters, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_back -> {
                listener!!.popFragment()
                return true
            }
            R.id.action_cancel -> {
                listener!!.showGrid()
                return true
            }
            R.id.action_save -> {
                save()
                listener!!.showGrid()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateImage() {
        processHandler.removeCallbacksAndMessages(null)
        processHandler.post {
            activity?.runOnUiThread { image_to_edit.setImageBitmap(viewModel.editedImage(context!!)) }
        }
    }

    private fun save() {
        InternalStorage.saveFile(context!!, viewModel.editedImage(context!!)!!, Calendar.getInstance().time.toString())
    }

    interface OnFragmentInteractionListener {
        fun popFragment()
        fun showGrid()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            EditImageSubFiltersFragment()
    }
}
