package com.example.gensecapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import android.R.attr.scheme
import android.net.Uri
import com.android.volley.toolbox.Volley
import android.R.attr.start
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import org.json.JSONException
import org.json.JSONObject
import com.android.volley.toolbox.JsonObjectRequest




class MainActivity : AppCompatActivity() {

    //var rollNo = findViewById<TextView>(R.id.roll_no)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.scan).setOnClickListener {
            val intent = Intent(applicationContext, ScanActivity::class.java)
            startActivityForResult(intent,0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val serial = data?.getStringExtra("serial")
        Toast.makeText(this,serial,Toast.LENGTH_LONG).show()

        val builder = Uri.Builder()
        builder.scheme("https")
            .authority("students.iitm.ac.in")
            .appendPath("studentsapp")
            .appendPath("general")
            .appendPath("get_roll_from_sno.php")
            .appendQueryParameter("sno",serial)
        val url = builder.build().toString()

        //Log.d("URL",url)

        val queue = Volley.newRequestQueue(this)

        val req = JsonObjectRequest(Request.Method.POST,url,null,
            Response.Listener<JSONObject> {
                    response -> var rollNo = findViewById<TextView>(R.id.roll_no)
                                rollNo.text = response.getInt("status").toString()},
            Response.ErrorListener { Log.e("JSON","Error") })

        queue.add(req)
        queue.start()


    }
}

