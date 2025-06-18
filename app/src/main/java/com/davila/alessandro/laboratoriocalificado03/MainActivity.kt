package com.davila.alessandro.laboratoriocalificado03

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.davila.alessandro.laboratoriocalificado03.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TeacherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = TeacherAdapter(
            list = emptyList(),
            onItemClick = { callTeacher(it.phone) },
            onItemLongClick = { sendEmail(it.email, it.name) }
        )

        binding.rvTeachers.layoutManager = LinearLayoutManager(this)
        binding.rvTeachers.adapter = adapter

        binding.fabRefresh.setOnClickListener {
            loadTeachers()
        }

        loadTeachers()
    }

    private fun getRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://private-effe28-tecsup1.apiary-mock.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun loadTeachers() {
        showLoading(true)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = getRetrofit().create(TeacherApi::class.java).getTeachers()
                withContext(Dispatchers.Main) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        val teachers = response.body()?.teachers.orEmpty()
                        adapter.updateList(teachers)
                    } else {
                        showError("Error al cargar datos")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showLoading(false)
                    showError("Error: ${e.message}")
                }
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.rvTeachers.visibility = if (show) View.GONE else View.VISIBLE
        binding.tvError.visibility = View.GONE
    }

    private fun showError(msg: String) {
        binding.tvError.visibility = View.VISIBLE
        binding.tvError.text = msg
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun callTeacher(phone: String) {
        val cleanPhone = phone.replace(" ", "")
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$cleanPhone"))
        startActivity(intent)
    }

    private fun sendEmail(email: String, name: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
            putExtra(Intent.EXTRA_SUBJECT, "Consulta")
            putExtra(Intent.EXTRA_TEXT, "Hola $name,\n\n")
        }
        startActivity(intent)
    }
}
