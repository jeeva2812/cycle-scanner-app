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

    }
}
