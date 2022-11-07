package com.example.beeapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.beeapp.model.User
import com.example.beeapp.service.ApiInterface
import com.example.beeapp.service.RetrofitService

//import com.google.firebase.database.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.logging.Level
import java.util.logging.Logger

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerUsername: EditText
    private lateinit var registerEmail: EditText
    private lateinit var registerPassword: EditText
    private lateinit var registerRepeatPassword: EditText
    private lateinit var registerButton: Button
    private lateinit var registerGoLoginButton: Button
    private var apiInterface:ApiInterface = RetrofitService().getRetrofit().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        registerUsername = findViewById(R.id.registerUsername)
        registerEmail = findViewById(R.id.registerEmail)
        registerPassword = findViewById(R.id.registerPassword)
        registerRepeatPassword = findViewById(R.id.registerRepeatPassword)
        registerButton = findViewById(R.id.registerRegisterButton)
        registerGoLoginButton = findViewById(R.id.registerGoLoginButton)


        registerButton.setOnClickListener {
            val username = registerUsername.text.toString()
            val email = registerEmail.text.toString()
            val password = registerPassword.text.toString()
            val repeatPassword = registerRepeatPassword.text.toString()

            if (checkEmpty(username, email, password, repeatPassword)) {

                if(checkEmailValid(email)){
                    if (password == repeatPassword) {

                        register(username, email, password)

                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Passwords don't match",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }else{
                    Toast.makeText(
                        applicationContext,
                        "Invalid Email",
                        Toast.LENGTH_LONG
                    ).show()
                }


            } else {
                Toast.makeText(
                    applicationContext,
                    "There are empty fields",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        registerGoLoginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()

        }
    }


    private fun register(username: String, email: String, password: String){


        var user = User(null,username,email,password)



        apiInterface.insertUser(user).enqueue(object:Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {

                    if (response.code()==400){
                        Toast.makeText(
                            applicationContext,
                            "Username or email already in use",
                            Toast.LENGTH_LONG
                        ).show()
                    }else{
                        Toast.makeText(
                            applicationContext,
                            "Account successfully created ",
                            Toast.LENGTH_LONG
                        ).show()

                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()


                    }

            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                ).show()
                Logger.getLogger("NO IDEA").log(Level.SEVERE, "ERROR",t)
            }
        })

    }

    private fun checkEmailValid(email:String):Boolean{

        return email.matches(Regex("^[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\$"))
    }

    private fun checkEmpty(
        username: String,
        email: String,
        password: String,
        repeatPassword: String
    ): Boolean {
        return username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && repeatPassword.isNotEmpty()
    }

}