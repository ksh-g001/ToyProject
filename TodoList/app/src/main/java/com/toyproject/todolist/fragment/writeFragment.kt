package com.toyproject.todolist.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.withResumed
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
import kotlinx.android.synthetic.main.fragment_write.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class writeFragment : Fragment(), View.OnClickListener {

    private lateinit var navController: NavController
    private lateinit var database : FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var sKey : String
    private lateinit var auth :FirebaseAuth
    private lateinit var date : String
    private var cnt : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        FirebaseDatabase.getInstance()
        database = Firebase.database("https://todo-list-e6634-default-rtdb.asia-southeast1.firebasedatabase.app/")
        sKey = auth.currentUser!!.uid
        date = setDate()
        databaseReference = database.reference.child("Users")
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
            R.id.save_btn -> { writeData(title.text.toString(), content.text.toString())}
            R.id.mypage_btn -> {navController.navigate(R.id.action_writeFragment_to_mypageFragment) }
            R.id.option_btn -> {navController.navigate(R.id.action_writeFragment_to_optionFragment)}
            R.id.calendar_btn -> {navController.navigate(R.id.action_writeFragment_to_calendarFragment)}
        }
    }

    fun writeData(title : String, context : String) {
        databaseReference.child(sKey).child(date).child(cnt.toString()).child("title").setValue(title)
        databaseReference.child(sKey).child(date).child(cnt.toString()).child("context").setValue(context)



        Toast.makeText(activity, resources.getString(R.string.save_success), Toast.LENGTH_SHORT).show()

        // Read from the database
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.child(sKey).child(date).child(cnt.toString()).child("title").getValue<String>()
                Toast.makeText(activity, "title : $value", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        cnt++
    }

    private fun setDate() : String {
        val today = LocalDateTime.now()
        val sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return today.format(sdf)
    }
}