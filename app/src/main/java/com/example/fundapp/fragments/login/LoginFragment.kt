package com.example.fundapp.fragments.login

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fundapp.R
import com.example.fundapp.base.BindingFragment
import com.example.fundapp.databinding.FragmentLoginBinding
import com.example.fundapp.model.User
import com.example.fundapp.remote.FirebaseDataSource
import com.example.fundapp.viewmodel.UserViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


class LoginFragment : BindingFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val RC_SIGN_IN = 13
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private  val userViewModel: UserViewModel by viewModels()
    private lateinit var firestore: FirebaseFirestore
    private lateinit var sharedpreferences: SharedPreferences


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedpreferences = requireContext().getSharedPreferences("FundAppPrefs", Activity.MODE_PRIVATE)
        val isLoggedIn = sharedpreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            return
        }

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.buttonLoginGoogle.setOnClickListener {
            binding.buttonLoginGoogle.isEnabled = false
            binding.progressBar.visibility = View.VISIBLE

            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!

                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                binding.progressBar.visibility = View.GONE
                binding.buttonLoginGoogle.isEnabled = true
                Toast.makeText(
                    requireContext(),
                    "Google sign in failed: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        binding.progressBar.visibility = View.VISIBLE
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                binding.progressBar.visibility = View.GONE
                binding.buttonLoginGoogle.isEnabled = true
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userId = user?.uid ?: ""
                    val name = user?.displayName ?: ""
                    val email = user?.email ?: ""
                    val photoUrl = user?.photoUrl?.toString() ?: ""

                    lifecycleScope.launch {
                        val dataSource = FirebaseDataSource(firestore)
                        val existingUser = dataSource.getUser(userId)

                        if (existingUser != null) {
                            val updatedUser = existingUser.copy(
                                name = name,
                                email = email,
                                photoUrl = photoUrl,
                            )
                            dataSource.saveUser(updatedUser)
                        } else {
                            val newUser = User(
                                userId = userId,
                                name = name,
                                email = email,
                                photoUrl = photoUrl,
                            )
                            userViewModel.saveUser(newUser)
                        }

                        // Save login status in SharedPreferences
                        sharedpreferences = requireContext().getSharedPreferences("FundAppPrefs", Activity.MODE_PRIVATE)
                        val editor = sharedpreferences.edit()
                        editor.putBoolean("isLoggedIn", true)
                        editor.apply()

                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Authentication Failed: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}
