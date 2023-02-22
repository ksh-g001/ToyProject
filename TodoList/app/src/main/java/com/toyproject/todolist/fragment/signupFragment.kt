package com.toyproject.todolist.fragment

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.toyproject.todolist.R
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_signup.email
import kotlinx.android.synthetic.main.fragment_signup.password
import kotlinx.android.synthetic.main.fragment_signup.signup_btn

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [signupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class signupFragment : Fragment(), View.OnClickListener {

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
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        back_btn.setOnClickListener(this)
        signup_btn.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back_btn -> {navController.popBackStack()}
            R.id.signup_btn -> {createAccount(email.text, password.text)}
        }
    }


    private fun createAccount(email: Editable, password: Editable) {
        val em = email.toString()
        val pwd = password.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth?.createUserWithEmailAndPassword(em, pwd)
                ?.addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity, resources.getString(R.string.account_success), Toast.LENGTH_SHORT).show()
                        navController.navigate(R.id.action_signupFragment_to_mypageFragment)
                    } else {
                        Toast.makeText(activity, resources.getString(R.string.account_false), Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

}