package ar.edu.utn.frba.mobile.clases_2019c2.fragments

import android.content.Context
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.clases_2019c2.R
import ar.edu.utn.frba.mobile.clases_2019c2.adapters.ThumbnailsAdapter
import ar.edu.utn.frba.mobile.clases_2019c2.utils.storage.fileSystem.ExternalContent
import ar.edu.utn.frba.mobile.clases_2019c2.viewmodels.MainActivityViewModel
import com.zomato.photofilters.FilterPack
import com.zomato.photofilters.imageprocessors.Filter
import com.zomato.photofilters.utils.ThumbnailItem
import com.zomato.photofilters.utils.ThumbnailsManager
import kotlinx.android.synthetic.main.fragment_edit_image_filters.*
import kotlinx.android.synthetic.main.fragment_edit_image_filters.view.*
import java.util.*

class EditImageFiltersFragment : Fragment(), ThumbnailsAdapter.ThumbnailsAdapterListener {
    val IMAGE_PATH = "IMAGE_PATH"

    init {
        System.loadLibrary("NativeImageProcessor")
    }

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var processHandler: Handler
    private lateinit var mAdapter: ThumbnailsAdapter
    private lateinit var thumbnailItemList: MutableList<ThumbnailItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(activity!!).get(MainActivityViewModel::class.java)

        arguments?.let {
            viewModel.imageUri = Uri.parse(it.getString(IMAGE_PATH))
        }

        val view = inflater.inflate(R.layout.fragment_edit_image_filters, container, false)

        prepareThumbnail(view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setImage()
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

    override fun onFilterSelected(filter: Filter) {
        viewModel.filterSelected = filter
        setImage()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_filters, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_cancel -> {
                listener!!.popFragment()
                return true
            }
            R.id.action_next -> {
                listener!!.editImageSubFilters()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setImage(){
        processHandler.removeCallbacksAndMessages(null)
        processHandler.post {
            activity?.runOnUiThread { image_to_edit.setImageBitmap(viewModel.editedImage(context!!)) }
        }
    }

    private fun prepareThumbnail(view: View) {
        thumbnailItemList = ArrayList()
        mAdapter = ThumbnailsAdapter(activity!!, thumbnailItemList, this)

        val mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val space = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics).toInt()

        view.recycler_view.layoutManager = mLayoutManager
        view.recycler_view.itemAnimator = DefaultItemAnimator()
        view.recycler_view.addItemDecoration(SpacesItemDecoration(space))
        view.recycler_view.adapter = mAdapter

        val r = Runnable {
            val thumbImage = ExternalContent.getBitmapFromGallery(activity!!, viewModel.imageUri, 100, 100)

            ThumbnailsManager.clearThumbs()
            thumbnailItemList.clear()

            val thumbnailItem = ThumbnailItem()
            thumbnailItem.image = thumbImage
            thumbnailItem.filterName = "Normal"
            ThumbnailsManager.addThumb(thumbnailItem)

            val filters = FilterPack.getFilterPack(activity)

            for (filter in filters) {
                val tI = ThumbnailItem()
                tI.image = thumbImage
                tI.filter = filter
                tI.filterName = filter.name
                ThumbnailsManager.addThumb(tI)
            }

            thumbnailItemList.addAll(ThumbnailsManager.processThumbs(activity))

            activity!!.runOnUiThread { mAdapter.notifyItemRangeInserted(0, thumbnailItemList.size - 1) }
        }

        Thread(r).start()
    }

    interface OnFragmentInteractionListener {
        fun editImageSubFilters()
        fun popFragment()
    }

    companion object {
        @JvmStatic
        fun newInstance(imageUri: Uri) =
            EditImageFiltersFragment().apply {
                arguments = Bundle().apply {
                    putString(IMAGE_PATH, imageUri.toString())
                }
            }
    }
}

class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) == state.itemCount - 1) {
            outRect.left = space
            outRect.right = 0
        } else {
            outRect.right = space
            outRect.left = 0
        }
    }
}