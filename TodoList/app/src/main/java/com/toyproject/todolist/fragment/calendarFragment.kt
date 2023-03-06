package com.toyproject.todolist.fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.Toast
import com.toyproject.todolist.R
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_calendar.add_btn
import kotlinx.android.synthetic.main.fragment_calendar.mypage_btn
import kotlinx.android.synthetic.main.fragment_calendar.option_btn
import kotlinx.android.synthetic.main.fragment_mypage.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class calendarFragment : Fragment(), View.OnClickListener {

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
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = readData()
        Toast.makeText(activity, "item size : ${item.size}", Toast.LENGTH_SHORT).show()
        todoList.adapter =
            activity?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, item) }

        navController = Navigation.findNavController(view)

        back_btn.setOnClickListener(this)
        mypage_btn.setOnClickListener(this)
        add_btn.setOnClickListener(this)
        option_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.back_btn -> {navController.popBackStack()}
            R.id.mypage_btn -> {navController.navigate(R.id.action_calendarFragment_to_mypageFragment)}
            R.id.add_btn -> {navController.navigate(R.id.action_calendarFragment_to_writeFragment)}
            R.id.option_btn -> {navController.navigate(R.id.action_calendarFragment_to_optionFragment)}
        }
    }

    private fun setDate() : String {
        val today = LocalDateTime.now()
        val sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return today.format(sdf)
    }

    private fun setTime() : String {
        val current = LocalDateTime.now()
        val sdf = DateTimeFormatter.ofPattern("h:mm:ss")
        return current.format(sdf)
    }

    private fun readData() : ArrayList<String>{
        var data: ArrayList<String> = ArrayList()

        val dbReference = databaseReference.child(date)
        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    data.add(postSnapshot.child("title").getValue<String>().toString())

//                    Log.w(TAG, "postSnapshot.children : ${postSnapshot.child("title").children}")
//                    Toast.makeText(activity, "postSnapshot.children : ${postSnapshot.child("title").getValue<String>().toString()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled")
                Toast.makeText(activity, resources.getString(R.string.read_false), Toast.LENGTH_SHORT).show()
            }
        })

        return data
    }
}