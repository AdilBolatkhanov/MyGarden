
package com.example.mygarden2
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.viewpager2.widget.ViewPager2

import com.example.mygarden2.adapters.GardenPlantingAdapter
import com.example.mygarden2.adapters.PLANT_LIST_PAGE_INDEX
import com.example.mygarden2.databinding.FragmentGardenBinding
import com.example.mygarden2.utilities.InjectorUtils
import com.example.mygarden2.viewmodels.GardenPlantingListViewModel
import com.google.android.material.snackbar.Snackbar


class GardenFragment : Fragment() {

    private lateinit var binding: FragmentGardenBinding

    private val viewModel: GardenPlantingListViewModel by viewModels {
        InjectorUtils.provideGardenPlantingListViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGardenBinding.inflate(inflater, container, false)
        val adapter = GardenPlantingAdapter()
        binding.gardenList.adapter = adapter

        binding.addPlant.setOnClickListener {
            navigateToPlantListPage()
        }
        setHasOptionsMenu(true)
        subscribeUi(adapter, binding)
        return binding.root
    }

    private fun subscribeUi(adapter: GardenPlantingAdapter, binding: FragmentGardenBinding) {
        viewModel.plantAndGardenPlantings.observe(viewLifecycleOwner) { result ->
            binding.hasPlantings = !result.isNullOrEmpty()
            adapter.submitList(result)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_plant_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter_zone -> {
                viewModel.clearGarden()
                Snackbar.make(binding.root,"Garden cleared", Snackbar.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToPlantListPage() {
        requireActivity().findViewById<ViewPager2>(R.id.view_pager).currentItem =
            PLANT_LIST_PAGE_INDEX
    }
}

