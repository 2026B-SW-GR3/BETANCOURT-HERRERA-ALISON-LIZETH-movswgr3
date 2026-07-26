package com.example.imitrunaapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.imitrunaapp.R
import com.google.android.material.button.MaterialButton
import com.example.imitrunaapp.MainActivity

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCupones = view.findViewById<MaterialButton>(R.id.btnCupones)
        val btnDomicilio = view.findViewById<MaterialButton>(R.id.btnDomicilio)
        val btnRecoge = view.findViewById<MaterialButton>(R.id.btnRecoge)
        val llTopBar = view.findViewById<View>(R.id.llTopBar)

        llTopBar.setOnClickListener {
            (requireActivity() as? FragmentNavigation)?.navigateToAddressSelector()
        }

        btnCupones.setOnClickListener {
            (requireActivity() as? MainActivity)?.switchFragment(CouponsFragment())
        }

        btnDomicilio.setOnClickListener {
            (requireActivity() as? MainActivity)?.switchFragment(MenuFragment())
        }

        btnRecoge.setOnClickListener {
            (requireActivity() as? MainActivity)?.switchFragment(StoresFragment())
        }
    }
}
