package com.toyproject.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {
    lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        navController = nav_host_fragment.findNavController()
//        navController = findNavController(R.id.nav_host_fragment)

        var navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragement) as NavHostFragment

        navController = navHostFragment.navController

    }
}