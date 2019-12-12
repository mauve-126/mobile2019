package com.example.diary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        var editTextEmail: EditText? = findViewById(R.id.emailInput)
        var editTextPassword: EditText? =  findViewById(R.id.passwordInput)

        signupButton.setOnClickListener{
            email = editTextEmail?.getText().toString()
            password = editTextPassword?.getText().toString()
            createUser(email, password)
        }
        loginButton.setOnClickListener{
            email = editTextEmail?.getText().toString()
            password = editTextPassword?.getText().toString()
            loginUser(email, password)

        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.action_login -> {
                val Intent = Intent(this,com.example.diary.Login::class.java)
                startActivity(Intent)
                return true
            }
            /*
            R.id.action_viewAll -> {
                val Intent2 = Intent(this,com.example.diary.view_all::class.java)
                startActivity(Intent2)
                return true
            }*/
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    // 회원가입
    private fun createUser(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                override fun onComplete(task: Task<AuthResult>) {
                    if (task.isSuccessful()) {
                        // 회원가입 성공
                        Toast.makeText(
                            this@Login,
                            "successs",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // 회원가입 실패
                        Toast.makeText(
                            this@Login,
                            "failed",
                            Toast.LENGTH_SHORT
                        ).show()
                        Toast.makeText(
                            this@Login,
                            email+password,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }
    //회원 로그인
    private fun loginUser(email: String, password: String) {

        if (email.length == 0 || password.length == 0){
            Toast.makeText(this, "email 혹은 password를 반드시 입력하세요.", Toast.LENGTH_SHORT).show()
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(applicationContext, "성공", Toast.LENGTH_SHORT).show()
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(applicationContext, "실패", Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }

                }
        }



    }

    fun updateUI(cUser : FirebaseUser? = null){

        var editTextEmail: EditText? = findViewById(R.id.emailInput)
        var editTextPassword: EditText? =  findViewById(R.id.passwordInput)
        editTextEmail?.setText("")
        editTextPassword?.setText("")
        val Intent = Intent(this, MainActivity::class.java)
        startActivity(Intent)
    }
}
