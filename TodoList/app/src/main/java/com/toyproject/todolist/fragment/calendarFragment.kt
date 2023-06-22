package com.toyproject.todolist.fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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
import com.toyproject.todolist.TodoContext
import com.toyproject.todolist.TodoContextAdapter
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_calendar.add_btn
import kotlinx.android.synthetic.main.fragment_calendar.mypage_btn
import kotlinx.android.synthetic.main.fragment_calendar.option_btn
import kotlinx.android.synthetic.main.fragment_mypage.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

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

    override fun onResume() {
        super.onResume()
        (todidList.adapter as TodoContextAdapter?)?.notifyDataSetChanged()
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

        navController = Navigation.findNavController(view)

        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            var date = "$year-${changeCalendarDateForm(month)}-${changeCalendarDateForm(dayOfMonth)}"
            Toast.makeText(activity, "date : $date", Toast.LENGTH_SHORT).show()
            var item = readData(date)
            Toast.makeText(activity, "item size : ${item.size}", Toast.LENGTH_SHORT).show()
            todidList.adapter = activity?.let { TodoContextAdapter(it, item) }
            (todidList.adapter as TodoContextAdapter?)?.notifyDataSetChanged()
        }

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

    private fun changeCalendarDateForm(date: Int) : String {
        return when(date){
            1 -> "01"
            2 -> "02"
            3 -> "03"
            4 -> "04"
            5 -> "05"
            6 -> "06"
            7 -> "07"
            8 -> "08"
            9 -> "09"
            else -> date.toString()
        }

    }

    private fun readData(date : String) : ArrayList<TodoContext>{
        var data: ArrayList<TodoContext> = ArrayList()

        val dbReference = databaseReference.child(date)
        dbReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    postSnapshot.getValue<TodoContext>()?.let { data.add(it) }
                    Log.w(ContentValues.TAG, "postSnapshot.children : ${postSnapshot.child("title").children}")
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