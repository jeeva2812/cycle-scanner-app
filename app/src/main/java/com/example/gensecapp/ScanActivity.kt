package com.example.gensecapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import android.app.Activity



class ScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                0)
        }

        setScannerProperties()

        val qrCodeScanner = findViewById<ZXingScannerView>(R.id.qrCodeScanner)


        qrCodeScanner.startCamera()
        qrCodeScanner.setResultHandler(this)
    }

    private fun setScannerProperties() {
        val qrCodeScanner = findViewById<ZXingScannerView>(R.id.qrCodeScanner)
        qrCodeScanner.setFormats(listOf(BarcodeFormat.QR_CODE))
        qrCodeScanner.setAutoFocus(true)
        qrCodeScanner.setLaserColor(R.color.colorAccent)
        qrCodeScanner.setMaskColor(R.color.colorAccent)
    }

    override fun onResume() {
        super.onResume()
        val qrCodeScanner = findViewById<ZXingScannerView>(R.id.qrCodeScanner)
        qrCodeScanner.startCamera()
        qrCodeScanner.setResultHandler(this)
    }


    override fun onPause() {
        super.onPause()
        val qrCodeScanner = findViewById<ZXingScannerView>(R.id.qrCodeScanner)
        qrCodeScanner.stopCamera()
    }

    override fun handleResult(p0: Result?) {
        if (p0 != null) {
            //startActivity(ScannedActivity.getScannedActivity(this, p0.text))
            val returnIntent = Intent()
            returnIntent.putExtra("serial", p0.text)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }


}
