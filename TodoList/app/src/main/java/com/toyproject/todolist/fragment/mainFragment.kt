package com.toyproject.todolist.fragment

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.toyproject.todolist.R
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class mainFragment : Fragment(), View.OnClickListener {

    lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            refreshFragment(this, getFragmentManager())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        login_btn.setOnClickListener(this)
        signup_btn.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.login_btn -> {
                login(email.text, password.text)
            }
            R.id.signup_btn -> {
                navController.navigate(R.id.action_mainFragment_to_signupFragment)
            }
        }
    }

    private fun login(email: Editable, passward: Editable) {
        val em = email.toString()
        val pwd = passward.toString()

        auth?.signInWithEmailAndPassword(em, pwd)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.sign_in_false),
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate(R.id.action_mainFragment_to_mypageFragment)
                } else {
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.sign_in_success),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager?) {
    var ft: FragmentTransaction = fragmentManager!!.beginTransaction()
    ft.detach(fragment).attach(fragment).commit()
}
