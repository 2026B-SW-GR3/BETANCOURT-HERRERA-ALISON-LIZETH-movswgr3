package com.example.imitrunaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.imitrunaapp.ui.CouponsFragment
import com.example.imitrunaapp.ui.FragmentNavigation
import com.example.imitrunaapp.ui.MenuFragment
import com.example.imitrunaapp.ui.StoresFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), FragmentNavigation {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    switchFragment(com.example.imitrunaapp.ui.HomeFragment())
                    true
                }
                R.id.nav_coupons -> {
                    switchFragment(CouponsFragment())
                    true
                }
                R.id.nav_cart, R.id.nav_profile -> {
                    // Placeholders for empty views right now
                    true
                }
                else -> false
            }
        }

        // Load default fragment
        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.nav_home
        }
    }

    fun switchFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.fragment_container, fragment)
            
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    override fun navigateToStores() {
        switchFragment(StoresFragment(), addToBackStack = true)
    }

    override fun navigateToAddressSelector() {
        switchFragment(com.example.imitrunaapp.ui.AddressSelectorFragment(), addToBackStack = true)
    }
}