package com.example.lab3_androidnetworking

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.lab3_androidnetworking.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder

public const val SERVER_URL = "https://apiandroidnetworkingdieunn.herokuapp.com"
public const val GUEST_ID = "62bd17832f3041da2882b32c"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFetchWithId.setOnClickListener {
            if (binding.edtId.text.isNullOrBlank()) {
                Toast.makeText(this, "Enter id first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            LoadSingleData(SERVER_URL, binding.edtId.text.toString()).execute()
        }

        binding.btnFetchList.setOnClickListener {
            LoadDataAsList(SERVER_URL).execute()
        }

        binding.btnPostData.setOnClickListener {
            if (binding.edtFirstName.text.isNullOrBlank() || binding.edtLastName.text.isNullOrBlank() || binding.edtEmail.text.isNullOrBlank()) {
                Toast.makeText(this, "Check your input", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            PostData(
                SERVER_URL,
                binding.edtFirstName.text.toString(),
                binding.edtLastName.text.toString(),
                binding.edtEmail.text.toString()
            ).execute()
        }


    }


    inner class LoadSingleData(var url: String, val guestId: String) :
        AsyncTask<String, String, String>() {
        override fun doInBackground(vararg p0: String?): String? {
            return try {
                val urlParsed = URL("$url/guest/get/$guestId")
                val bufferedReader =
                    BufferedReader(InputStreamReader(urlParsed.openConnection().getInputStream()))
                bufferedReader.readLine()
            } catch (e: MalformedURLException) {
                e.toString();
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            binding.txtResult.text = result
        }
    }

    inner class LoadDataAsList(var url: String) :
        AsyncTask<String, String, String>() {
        override fun doInBackground(vararg p0: String?): String? {
            return try {
                val urlParsed = URL("$url/guest")
                val bufferedReader =
                    BufferedReader(InputStreamReader(urlParsed.openConnection().getInputStream()))
                bufferedReader.readLine()
            } catch (e: MalformedURLException) {
                e.toString();
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            binding.txtResult.text = result
        }
    }

    inner class PostData(
        var url: String,
        val firstName: String,
        val lastName: String,
        val email: String
    ) :
        AsyncTask<String, String, String>() {
        override fun doInBackground(vararg p0: String?): String? {
            return try {
                val params = "?first_name=${
                    URLEncoder.encode(
                        firstName,
                        "utf-8"
                    )
                }&last_name=${URLEncoder.encode(lastName, "utf-8")}&email=${
                    URLEncoder.encode(
                        email,
                        "utf-8"
                    )
                }"
                val urlParsed = URL("$url/guest/add/$params")
                print(urlParsed)
                val httpConnection = urlParsed.openConnection() as HttpURLConnection
                httpConnection.apply {
                    this.doOutput = true
                    this.requestMethod = "POST"
                    this.setFixedLengthStreamingMode(params.encodeToByteArray().size)
                    this.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                }
                val bufferedReader =
                    BufferedReader(InputStreamReader(urlParsed.openConnection().getInputStream()))
                bufferedReader.readLine()
            } catch (e: MalformedURLException) {
                e.toString();
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            binding.txtResult.text = result
        }
    }


}