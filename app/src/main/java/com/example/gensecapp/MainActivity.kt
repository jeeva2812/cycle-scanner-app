package com.example.gensecapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import android.R.attr.scheme
import android.net.Uri
import com.android.volley.toolbox.Volley
import android.R.attr.start
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import org.json.JSONException
import org.json.JSONObject
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.squareup.picasso.Picasso
import org.json.JSONArray
import java.net.URL


class MainActivity : AppCompatActivity() {

    var serial : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.scan).setOnClickListener {
            val intent = Intent(applicationContext, ScanActivity::class.java)
            startActivityForResult(intent,0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        serial = data?.getStringExtra("serial").toString()
        //Toast.makeText(this,serial,Toast.LENGTH_LONG).show()

        val builder = Uri.Builder()
        builder.scheme("https")
            .authority("students.iitm.ac.in")
            .appendPath("studentsapp")
            .appendPath("general")
            .appendPath("get_roll_from_sno.php")

        val url = builder.build().toString()

        Log.d("URL",url)

        //val queue = Volley.newRequestQueue(this)


        /*{
            "code": 1,
            "body": "[{\"rollno\":\"ME18B030\",\"fullname\":\"S JEEVA\",\"roomno\":\"258\",
                    \"hostel\":\"Cauvery\",\"url\":\"https:\\/\\/photos.iitm.ac.in\\/byroll.php?roll=ME18B030\"}]"
                }*/

        val req = object : StringRequest(
            Request.Method.POST,url,

            Response.Listener <String> {
                response ->
                try {
                    //Toast.makeText(this, response, Toast.LENGTH_LONG).show()
                    //Log.d("JSON",response)
                    val json = JSONObject(response)
                    if(json.getInt("code") == 1){
                        var body = json.getString("body")
                        body = body.replace("\\","")
                        body = body.replace("\\","")

                        Log.d("JSON",body)
                        try {
                            val jsonArrayBody = JSONArray(body)
                            val jsonBody = jsonArrayBody.getJSONObject(0)
                            val details = LayoutInflater.from(this).inflate(R.layout.student_details,null)

                            Picasso.get().load(jsonBody.getString("url")).into(details.findViewById<ImageView>(R.id.photo))
                            details.findViewById<TextView>(R.id.name).text = jsonBody.getString("fullname")
                            details.findViewById<TextView>(R.id.roll_no).text = jsonBody.getString("rollno")
                            details.findViewById<TextView>(R.id.room_no).text = jsonBody.getString("roomno")
                            details.findViewById<TextView>(R.id.hostel).text = jsonBody.getString("hostel")

                            val layout = findViewById<LinearLayout>(R.id.root_layout)

                            layout.removeViewAt(0)
                            layout.addView(details,0)

                            findViewById<Button>(R.id.scan).text = "Scan Again"



                        }catch (e : JSONException){
                            Log.e("JSONException","Body Error")
                            Toast.makeText(this,"Invalid",Toast.LENGTH_SHORT).show()
                        }


                    }else{
                        Toast.makeText(this,"Invalid",Toast.LENGTH_SHORT).show()
                    }

                }catch ( e : JSONException){
                    Log.e("JSONException","Response Error")
                }
            },

            Response.ErrorListener { Log.e("Volley","Error Listener") })

        {
            override fun getParams(): Map<String, String> = mapOf("sno" to serial)
        }

        VolleySingleton.getInstance(this).addToRequestQueue(req)


    }

}

