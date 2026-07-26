package com.example.imitrunaapp.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imitrunaapp.R
import com.example.imitrunaapp.adapter.MenuGridAdapter
import com.example.imitrunaapp.model.MenuCategory

class MenuFragment : Fragment() {

    private lateinit var menuAdapter: MenuGridAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Listen for address changes
        parentFragmentManager.setFragmentResultListener("addressRequest", viewLifecycleOwner) { _, bundle ->
            val newAddress = bundle.getString("address")
            if (newAddress != null) {
                view.findViewById<TextView>(R.id.tvHeaderSubtitle).text = newAddress
            }
        }

        val btnLocationSelector = view.findViewById<View>(R.id.btnLocationSelector)
        btnLocationSelector.setOnClickListener {
            (requireActivity() as? FragmentNavigation)?.navigateToAddressSelector()
        }

        // Listen for store changes
        parentFragmentManager.setFragmentResultListener("storeRequest", viewLifecycleOwner) { _, bundle ->
            val newStore = bundle.getString("storeName")
            if (newStore != null) {
                view.findViewById<TextView>(R.id.tvSelectedStore).text = newStore
            }
        }

        val llStoreSelector = view.findViewById<View>(R.id.llStoreSelector)
        llStoreSelector.setOnClickListener {
            val bottomSheet = StoreBottomSheetFragment()
            bottomSheet.show(parentFragmentManager, "StoreBottomSheet")
        }

        menuAdapter = MenuGridAdapter()
        val rvMenu = view.findViewById<RecyclerView>(R.id.rvMenuGrid)
        rvMenu.layoutManager = GridLayoutManager(requireContext(), 2)
        rvMenu.setHasFixedSize(true)
        rvMenu.adapter = menuAdapter

        // Removed duplicate listener

        simulateNetworkLoad(view)
    }

    private fun simulateNetworkLoad(view: View) {
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val rvMenu = view.findViewById<RecyclerView>(R.id.rvMenuGrid)
        val tvMenuLabel = view.findViewById<TextView>(R.id.tvMenuLabel)
        
        progressBar.visibility = View.VISIBLE
        rvMenu.visibility = View.GONE
        tvMenuLabel.visibility = View.GONE

        Handler(Looper.getMainLooper()).postDelayed({
            progressBar.visibility = View.GONE
            rvMenu.visibility = View.VISIBLE
            tvMenuLabel.visibility = View.VISIBLE
            loadDummyData()
        }, 800)
    }

    private fun loadDummyData() {
        val menuItems = mutableListOf<MenuCategory>()
        menuItems.add(MenuCategory(1, "AROS DE CEBOLLA", R.drawable.aros_cebolla, isRedBorder = false))
        menuItems.add(MenuCategory(2, "FIESTAS JULIANAS", R.drawable.hamburguesa, isRedBorder = false))
        menuItems.add(MenuCategory(3, "FESTINES Y PRESAS SOLAS", R.drawable.bucket_pollo, isRedBorder = true))
        menuItems.add(MenuCategory(4, "PARA COMPARTIR", R.drawable.bucket_pollo, isRedBorder = true))
        menuItems.add(MenuCategory(5, "BOXES", R.drawable.hamburguesa, isRedBorder = true))
        
        for (i in 6..100) {
            menuItems.add(MenuCategory(i, "Menú de prueba del taller $i", R.drawable.bucket_pollo, isRedBorder = (i % 2 == 0)))
        }
        
        menuAdapter.submitList(menuItems)
    }
}
