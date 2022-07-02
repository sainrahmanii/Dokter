package com.sain.dokterapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.sain.dokterapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        binding.tvDontHaveAccount.setOnClickListener {
            val i = Intent(this, RegisterActivity::class.java)
            startActivity(i)
        }


        binding.btnSignIn.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val pass = binding.edtPass.text.toString()

            if (email.isNullOrEmpty()) {
                binding.edtEmail.error = "Email is required"
                binding.edtEmail.requestFocus()
                return@setOnClickListener
            }

            if (pass.isNullOrEmpty()) {
                binding.edtPass.error = "Password is required"
                binding.edtPass.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.edtEmail.error = "Email not valid"
                binding.edtEmail.requestFocus()
                return@setOnClickListener
            }
            if (pass.length <8){
                binding.edtPass.error = "Password kurang dari 8"
                binding.edtPass.requestFocus()
                return@setOnClickListener
            }

            loginUser(email, pass)

        }
    }

    private fun loginUser(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    Toast.makeText(this, "Welcome, $email", Toast.LENGTH_SHORT).show()
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                    finish()
                }else{
                    Toast.makeText(this, "Email isn't registered", Toast.LENGTH_SHORT).show()
                    val i = Intent(this, RegisterActivity::class.java)
                    startActivity(i)
                }
            }
    }
}