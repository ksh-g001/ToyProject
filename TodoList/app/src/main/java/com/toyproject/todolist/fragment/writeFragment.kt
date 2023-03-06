package com.toyproject.todolist.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.Toast
import com.toyproject.todolist.R
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_mypage.*
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
        databaseReference.child(date).child(time).child("title").setValue(title)
        databaseReference.child(date).child(time).child("context").setValue(context)

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

    private fun countData() :Int {
        val dbReference =  databaseReference.child(date)
        var cnt = 1

        dbReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    cnt++
                }
                Toast.makeText(activity, "cnt : $cnt", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled")
            }
        })

        Toast.makeText(activity, "$cnt", Toast.LENGTH_SHORT).show()
        return cnt
    }
}