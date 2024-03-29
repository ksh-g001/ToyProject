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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.toyproject.todolist.TodoContext
import kotlinx.android.synthetic.main.fragment_write.*
import kotlinx.android.synthetic.main.fragment_write.calendar_btn
import kotlinx.android.synthetic.main.fragment_write.mypage_btn
import kotlinx.android.synthetic.main.fragment_write.option_btn
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class writeFragment : Fragment(), View.OnClickListener {

    private lateinit var navController: NavController
    private lateinit var database : FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var sKey : String
    private lateinit var auth :FirebaseAuth
    private lateinit var date : String
    private lateinit var time : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        FirebaseDatabase.getInstance()
        database = Firebase.database("https://todo-list-e6634-default-rtdb.asia-southeast1.firebasedatabase.app/")
        sKey = auth.currentUser!!.uid
        date = setDate()
        time = setTime()
        databaseReference = database.reference.child("Users").child(sKey)
    }

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

        back_btn.setOnClickListener(this)
        save_btn.setOnClickListener(this)
        mypage_btn.setOnClickListener(this)
        option_btn.setOnClickListener(this)
        calendar_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.back_btn -> {navController.popBackStack()}
            R.id.save_btn -> {
                writeData(title.text.toString(), content.text.toString())
                navController.navigate(R.id.action_writeFragment_to_mypageFragment)
            }
            R.id.mypage_btn -> {navController.navigate(R.id.action_writeFragment_to_mypageFragment) }
            R.id.option_btn -> {navController.navigate(R.id.action_writeFragment_to_optionFragment)}
            R.id.calendar_btn -> {navController.navigate(R.id.action_writeFragment_to_calendarFragment)}
        }
    }

    private fun writeData(title : String, context : String) {
        val todo = TodoContext(title, context)

//        databaseReference.child(date).child(time).child("title").setValue(title)
//        databaseReference.child(date).child(time).child("context").setValue(context)

        databaseReference.child(date).child(time).setValue(todo)

        Toast.makeText(activity, resources.getString(R.string.save_success), Toast.LENGTH_SHORT).show()
    }

    private fun setDate() : String {
        val today = LocalDateTime.now()
        val sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return today.format(sdf)
    }

    private fun setTime() : String {
        val current = LocalDateTime.now()
        val sdf = DateTimeFormatter.ofPattern("hh:mm:ss")
        return current.format(sdf)
    }
}