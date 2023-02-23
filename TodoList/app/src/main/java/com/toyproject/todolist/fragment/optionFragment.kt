package com.toyproject.todolist.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.toyproject.todolist.R
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_option.*

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class optionFragment : Fragment(), View.OnClickListener {

    lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_option, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        darkmode.setOnClickListener(this)
        copyright.setOnClickListener(this)
        logout_btn.setOnClickListener(this)
        back_btn.setOnClickListener(this)
        mypage_btn.setOnClickListener(this)
        add_btn.setOnClickListener(this)
        calendar_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.darkmode -> {}
            R.id.copyright -> {copyright_string.visibility = View.VISIBLE}
            R.id.logout_btn -> {
                auth.signOut()
                Toast.makeText(activity, "현재 계정 : ${auth.currentUser?.email.toString()}", Toast.LENGTH_SHORT).show()
                navController.navigate(R.id.action_optionFragment_to_mainFragment)
            }
            R.id.back_btn -> {navController.popBackStack()}
            R.id.mypage_btn -> {navController.navigate(R.id.action_optionFragment_to_mypageFragment) }
            R.id.add_btn -> {navController.navigate(R.id.action_optionFragment_to_writeFragment)}
            R.id.calendar_btn -> {navController.navigate(R.id.action_optionFragment_to_calendarFragment)}
        }
    }
}