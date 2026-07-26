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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imitrunaapp.R
import com.example.imitrunaapp.adapter.StoreAdapter
import com.example.imitrunaapp.model.Store
import com.google.android.material.button.MaterialButton

class StoresFragment : Fragment() {

    private lateinit var storeAdapter: StoreAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_stores, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storeAdapter = StoreAdapter()
        val rvStores = view.findViewById<RecyclerView>(R.id.rvStoresList)
        rvStores.layoutManager = LinearLayoutManager(requireContext())
        rvStores.setHasFixedSize(true)
        rvStores.adapter = storeAdapter

        view.findViewById<View>(R.id.btnClose).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        view.findViewById<View>(R.id.btnConfirm).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        simulateNetworkLoad(view)
    }

    private fun simulateNetworkLoad(view: View) {
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val rvStores = view.findViewById<RecyclerView>(R.id.rvStoresList)
        val tvStoresLabel = view.findViewById<TextView>(R.id.tvStoresLabel)
        val btnConfirm = view.findViewById<MaterialButton>(R.id.btnConfirm)
        
        progressBar.visibility = View.VISIBLE
        rvStores.visibility = View.GONE
        tvStoresLabel.visibility = View.GONE
        btnConfirm.visibility = View.GONE

        Handler(Looper.getMainLooper()).postDelayed({
            progressBar.visibility = View.GONE
            rvStores.visibility = View.VISIBLE
            tvStoresLabel.visibility = View.VISIBLE
            btnConfirm.visibility = View.VISIBLE
            loadDummyData()
        }, 800)
    }

    private fun loadDummyData() {
        val stores = mutableListOf<Store>()
        stores.add(Store(1, "MICHELENA Y CABO MINACHO", "780m Cerca de ti", "Abierto - Cierra: 23:45", isSelected = true))
        stores.add(Store(2, "CORAL MAGDALENA", "1.57km Cerca de ti", "Abierto - Cierra: 20:15", isSelected = false))
        stores.add(Store(3, "RECREO PATIO NUEVO", "1.92km Cerca de ti", "Abierto - Cierra: 19:45", isSelected = false))
        
        for (i in 4..100) {
            stores.add(Store(i, "Local de prueba del taller $i", "${1 + i*0.1}km Cerca de ti", "Abierto - Cierra: 22:00", isSelected = false))
        }
        
        storeAdapter.submitList(stores)
    }
}
