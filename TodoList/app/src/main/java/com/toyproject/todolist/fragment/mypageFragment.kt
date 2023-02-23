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
import kotlinx.android.synthetic.main.fragment_mypage.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class mypageFragment : Fragment(), View.OnClickListener {

    lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(activity, "계정연결 실패", Toast.LENGTH_SHORT).show()
            navController.navigate(R.id.action_mypageFragment_to_mainFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mypage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        today_date.text = setDate()

        navController = Navigation.findNavController(view)

        option_btn.setOnClickListener(this)
        add_btn.setOnClickListener(this)
        calendar_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.option_btn -> {navController.navigate(R.id.action_mypageFragment_to_optionFragment)}
            R.id.add_btn -> {navController.navigate(R.id.action_mypageFragment_to_writeFragment)}
            R.id.calendar_btn -> {navController.navigate(R.id.action_mypageFragment_to_calendarFragment)}
        }
    }

    private fun setDate() : String? {
        val today = LocalDateTime.now()
        val SDF = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        return today.format(SDF)
    }
}