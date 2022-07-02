package com.sain.dokterapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sain.dokterapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding
    lateinit var auth: FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.tvHaveAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()
        binding.btnSignUp.setOnClickListener {
            val name = binding.edtName.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPass.text.toString()

            database = FirebaseDatabase.getInstance().getReference("Users")
            val User = User(name)
            database.child(name).setValue(User).addOnCompleteListener {
                binding.edtName.text.clear()
            }

            if (name.isEmpty()) {
                binding.edtName.error = "Nama belum terisi"
                binding.edtName.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                binding.edtEmail.error = "Email belum terisi"
                binding.edtEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.edtPass.error = "Password belum terisi"
                binding.edtPass.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmail.error = "Email tidak valid"
                binding.edtEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 8) {
                binding.edtPass.error = "Password kurang dari 8 karakter"
                binding.edtPass.requestFocus()
                return@setOnClickListener
            }

            registerFirebase(name, email, password)
        }

    }

    private fun registerFirebase(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "$name berhasil terdaftar", Toast.LENGTH_SHORT).show()
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                    finish()
                } else {
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
