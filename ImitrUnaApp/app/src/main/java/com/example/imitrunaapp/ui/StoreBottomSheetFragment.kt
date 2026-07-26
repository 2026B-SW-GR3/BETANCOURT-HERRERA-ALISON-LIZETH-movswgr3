package com.example.imitrunaapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imitrunaapp.R
import com.example.imitrunaapp.model.Store
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

class StoreBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var rvStores: RecyclerView
    private lateinit var adapter: StoreSelectorAdapter
    private val stores = mutableListOf<Store>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_store_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageButton>(R.id.btnClose).setOnClickListener {
            dismiss()
        }

        rvStores = view.findViewById(R.id.rvStores)
        rvStores.layoutManager = LinearLayoutManager(requireContext())
        rvStores.setHasFixedSize(true)

        loadMockStores()

        adapter = StoreSelectorAdapter(stores) { selectedStore ->
            stores.forEach { it.isSelected = false }
            selectedStore.isSelected = true
            adapter.notifyDataSetChanged()
        }
        rvStores.adapter = adapter

        view.findViewById<MaterialButton>(R.id.btnConfirmar).setOnClickListener {
            val selected = stores.find { it.isSelected }
            if (selected != null) {
                parentFragmentManager.setFragmentResult("storeRequest", bundleOf("storeName" to selected.name))
            }
            dismiss()
        }
    }

    private fun loadMockStores() {
        stores.add(Store(1, "KFC Colón", "1.2 km - Av. 10 de Agosto y Colón", "Abierto - Entrega en 15 - 25 min", isSelected = true))
        stores.add(Store(2, "KFC Plaza de las Américas", "2.8 km - Av. República y Av. América", "Abierto - Entrega en 20 - 30 min", isSelected = false))
        stores.add(Store(3, "KFC El Jardín", "3.1 km - CC Mall El Jardín", "Abierto - Entrega en 25 - 35 min", isSelected = false))
        stores.add(Store(4, "KFC Naciones Unidas", "4.5 km - Quicentro Shopping Norte", "Abierto - Entrega en 30 - 45 min", isSelected = false))
        stores.add(Store(5, "KFC El Recreo", "6.7 km - CC El Recreo", "Abierto - Entrega en 40 - 55 min", isSelected = false))
        stores.add(Store(6, "KFC Quicentro Sur", "12.4 km - CC Quicentro Sur", "Abierto - Entrega en 50 - 65 min", isSelected = false))
        
        for (i in 7..100) {
            stores.add(Store(i, "Sucursal de prueba $i", "${String.format("%.1f", 12.4 + (i-6)*0.2)} km - Dirección simulada #$i", "Abierto - Entrega en ${40 + i} - ${50 + i} min", isSelected = false))
        }
    }

    inner class StoreSelectorAdapter(
        private val list: List<Store>,
        private val onStoreClick: (Store) -> Unit
    ) : RecyclerView.Adapter<StoreSelectorAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvName: TextView = view.findViewById(R.id.tvStoreName)
            val tvDist: TextView = view.findViewById(R.id.tvDistanceAddress)
            val tvSched: TextView = view.findViewById(R.id.tvSchedule)
            val rbStore: RadioButton = view.findViewById(R.id.rbStore)

            init {
                view.setOnClickListener {
                    onStoreClick(list[adapterPosition])
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_store, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val store = list[position]
            holder.tvName.text = store.name
            holder.tvDist.text = store.distanceAddress
            holder.tvSched.text = store.scheduleText
            holder.rbStore.isChecked = store.isSelected
        }

        override fun getItemCount(): Int = list.size
    }
}
