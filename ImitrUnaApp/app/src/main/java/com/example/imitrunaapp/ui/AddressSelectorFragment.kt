package com.example.imitrunaapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imitrunaapp.R
import com.google.android.material.button.MaterialButton

data class AddressModel(val id: Int, val title: String, val subtitle: String, var isSelected: Boolean)

class AddressSelectorFragment : Fragment() {

    private lateinit var rvAddresses: RecyclerView
    private lateinit var adapter: AddressAdapter
    private val addresses = mutableListOf<AddressModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_address_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnClose = view.findViewById<ImageButton>(R.id.btnClose)
        btnClose.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        rvAddresses = view.findViewById(R.id.rvAddresses)
        rvAddresses.layoutManager = LinearLayoutManager(requireContext())
        rvAddresses.setHasFixedSize(true)

        loadMockAddresses()

        adapter = AddressAdapter(addresses) { selectedAddress ->
            // Update selection state
            addresses.forEach { it.isSelected = false }
            selectedAddress.isSelected = true
            adapter.notifyDataSetChanged()
        }
        rvAddresses.adapter = adapter

        val btnConfirmar = view.findViewById<MaterialButton>(R.id.btnConfirmar)
        btnConfirmar.setOnClickListener {
            val selected = addresses.find { it.isSelected }
            val addressText = if (selected != null) {
                "${selected.title} - ${selected.subtitle}"
            } else {
                "Casa - José egusquiza &"
            }
            
            parentFragmentManager.setFragmentResult("addressRequest", bundleOf("address" to addressText))
            parentFragmentManager.popBackStack()
        }
    }

    private fun loadMockAddresses() {
        addresses.add(AddressModel(1, "Universidad", "QGQ6+VCC", false))
        addresses.add(AddressModel(2, "Casa", "José egusquiza &", true))
        addresses.add(AddressModel(3, "Casa", "Jose Egusquiza Oe8-237", false))
        
        for (i in 4..100) {
            addresses.add(AddressModel(i, "Prueba del taller $i", "Dirección simulada #$i", false))
        }
    }

    inner class AddressAdapter(
        private val list: List<AddressModel>,
        private val onAddressClick: (AddressModel) -> Unit
    ) : RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvTitle: TextView = view.findViewById(R.id.tvAddressTitle)
            val tvSubtitle: TextView = view.findViewById(R.id.tvAddressSubtitle)
            val rbAddress: RadioButton = view.findViewById(R.id.rbAddress)

            init {
                view.setOnClickListener {
                    onAddressClick(list[adapterPosition])
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_address_radio, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val address = list[position]
            holder.tvTitle.text = address.title
            holder.tvSubtitle.text = address.subtitle
            holder.rbAddress.isChecked = address.isSelected
        }

        override fun getItemCount(): Int = list.size
    }
}
