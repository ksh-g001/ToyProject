package com.toyproject.todolist.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toyproject.todolist.R
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_write.*

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class writeFragment : Fragment(), View.OnClickListener {

    lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_write, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        content.setOnClickListener{
        }
        back_btn.setOnClickListener(this)
        mypage_btn.setOnClickListener(this)
        option_btn.setOnClickListener(this)
        calendar_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.back_btn -> {

                navController.popBackStack()

            }
            R.id.mypage_btn -> {navController.navigate(R.id.action_writeFragment_to_mypageFragment) }
            R.id.option_btn -> {navController.navigate(R.id.action_writeFragment_to_optionFragment)}
            R.id.calendar_btn -> {navController.navigate(R.id.action_writeFragment_to_calendarFragment)}
        }
    }
}