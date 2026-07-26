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
import com.example.imitrunaapp.adapter.CouponCategoryAdapter
import com.example.imitrunaapp.model.CouponCategory

class CouponsFragment : Fragment() {

    private lateinit var couponCatAdapter: CouponCategoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_coupons, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        couponCatAdapter = CouponCategoryAdapter()
        val rvCoupons = view.findViewById<RecyclerView>(R.id.rvCouponCategories)
        rvCoupons.layoutManager = GridLayoutManager(requireContext(), 2)
        rvCoupons.setHasFixedSize(true)
        rvCoupons.adapter = couponCatAdapter

        simulateNetworkLoad(view)
    }

    private fun simulateNetworkLoad(view: View) {
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val rvCoupons = view.findViewById<RecyclerView>(R.id.rvCouponCategories)
        val tvCouponsLabel = view.findViewById<TextView>(R.id.tvCouponsLabel)
        
        progressBar.visibility = View.VISIBLE
        rvCoupons.visibility = View.GONE
        tvCouponsLabel.visibility = View.GONE

        Handler(Looper.getMainLooper()).postDelayed({
            progressBar.visibility = View.GONE
            rvCoupons.visibility = View.VISIBLE
            tvCouponsLabel.visibility = View.VISIBLE
            loadDummyData()
        }, 800)
    }

    private fun loadDummyData() {
        val couponCats = mutableListOf<CouponCategory>()
        couponCats.add(CouponCategory(1, "DOMICILIO", R.drawable.tienda_kfc))
        couponCats.add(CouponCategory(2, "PIDE Y RECOGE", R.drawable.tienda_kfc))
        couponCats.add(CouponCategory(3, "KIOSKO", R.drawable.hamburguesa))
        couponCats.add(CouponCategory(4, "RESTAURANTES", R.drawable.tienda_kfc))
        couponCats.add(CouponCategory(5, "HELADERÍAS", R.drawable.helado))
        couponCats.add(CouponCategory(6, "AUTORÁPIDO", R.drawable.coche))
        
        for (i in 7..100) {
            couponCats.add(CouponCategory(i, "Prueba del taller $i", R.drawable.tienda_kfc))
        }
        
        couponCatAdapter.submitList(couponCats)
    }
}
