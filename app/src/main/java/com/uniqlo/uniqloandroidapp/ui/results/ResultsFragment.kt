package com.uniqlo.uniqloandroidapp.ui.results


import android.app.SharedElementCallback
import android.graphics.Interpolator
import android.os.Bundle
import android.transition.Transition
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dropbox.android.external.store4.StoreResponse
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialContainerTransformSharedElementCallback

import com.uniqlo.uniqloandroidapp.R
import com.uniqlo.uniqloandroidapp.adapter.ResultsItemAdapter
import com.uniqlo.uniqloandroidapp.model.Item
import kotlinx.android.synthetic.main.fragment_results.*
import kotlinx.android.synthetic.main.main_appbar.view.*
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class ResultsFragment : Fragment() {

//    private lateinit var dataBinding: FragmentResultsBinding
    private val args: ResultsFragmentArgs by navArgs()
    private lateinit var viewModel: ResultsViewModel
    private lateinit var viewModelFactory: ResultsViewModelFactory

    private lateinit var itemCountText: TextView
    private val itemAdapter = ResultsItemAdapter()
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState:  Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = MaterialContainerTransform(requireContext()).apply {
            duration = 400
            interpolator = FastOutSlowInInterpolator()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        dataBinding = FragmentResultsBinding.inflate(inflater, container, false)
        val root = inflater.inflate(R.layout.fragment_results, container, false)

        // set view model
        viewModelFactory = ResultsViewModelFactory(activity!!.application)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ResultsViewModel::class.java)

        viewModel.itemList.observe(this, Observer { storeResponse: StoreResponse<List<Item>> ->

            when (storeResponse) {
                is StoreResponse.Error -> {
                    // if server isn't up. However navigation won't make sense.
                    updateItemList(mutableListOf(
                        Item(imageUrl = "https://objectstorage.us-ashburn-1.oraclecloud.com/n/idi3qahxtzru/b/Uniqlo/o/STAR_WARS_FOREVER_UT_STASH_Blue.jpg"),
                        Item(imageUrl = "https://objectstorage.us-ashburn-1.oraclecloud.com/n/idi3qahxtzru/b/Uniqlo/o/TODDLER_MAGIC_FOR_ALL_ICONS_UT_SHORT-SLEEVE_GRAPHIC_T-SHIRT_Yellow.jpg"),
                        Item(imageUrl = "https://objectstorage.us-ashburn-1.oraclecloud.com/n/idi3qahxtzru/b/Uniqlo/o/STAR_WARS_FOREVER_UT_STASH_SHORT-SLEEVE_GRAPHIC_T-SHIRT_Black.jpg")))
                        updateItemCount(3)
                    Timber.d(storeResponse.error)}
                is StoreResponse.Data -> updateItemList(storeResponse.requireData())
            }
        })

        viewModel.itemCount.observe(this, Observer{
            itemCount: Int ->
            updateItemCount(itemCount)
        })

        viewModel.resultsName.observe(this, Observer{
                resultsName: String ->
            updateToolbar(resultsName)
        })

        // update items on init before views are created
        if(args.adId!=null) {
            var id: String = args.adId!!
            viewModel.updateResultItems(id)
            Timber.d("Ad ID: %s", id)
        }


//        dataBinding.viewmodel = viewModel

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
//        dataBinding.lifecycleOwner = this.viewLifecycleOwner

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // init views
        // back button
        view.toolbar.navigationIcon =  AppCompatResources.getDrawable(requireContext(), R.drawable.ic_arrow_back_primary_32dp )
        (view.toolbar as Toolbar).setNavigationOnClickListener(
            View.OnClickListener { findNavController().navigateUp() }
        )
        recycler_view.adapter = itemAdapter
        itemCountText = view.findViewById(R.id.itemCount)
        toolbar = view.findViewById(
            R.id.toolbar)




    }

    private fun updateToolbar(name: String) {
        if(toolbar!=null) {
            toolbar.title=name
        }
    }

    private fun updateItemCount(itemCount: Int) {
        if(itemCountText!=null)
        itemCountText.text = String.format("%d Items", itemCount)

    }

    private fun updateItemList(data: List<Item>) {

        if(recycler_view!=null && recycler_view.adapter==null)
            recycler_view.adapter = itemAdapter

        itemAdapter.submitList(data)

    }




}