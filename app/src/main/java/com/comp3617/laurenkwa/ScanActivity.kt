package com.comp3617.laurenkwa

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.util.Log
import android.util.SparseArray
import android.view.*
import android.widget.TextView
import android.widget.Toast
import java.io.File
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.Detector
import com.google.firebase.database.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.io.IOException
import com.google.zxing.Result
import kotlinx.android.synthetic.main.timerecord_layout.*
import java.sql.Timestamp
import java.util.*


class ScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    lateinit var mDatabase: DatabaseReference
    val PHOTO_REQUEST = 10
    lateinit var scanResults : TextView
    lateinit var imageUri : Uri
    private var cameraView: SurfaceView? = null
    private var barcodeValue: TextView? = null
    private var mScannerView: ZXingScannerView? = null
    lateinit var empID : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase = mDatabase.child("emp_record_item");
        mScannerView = ZXingScannerView(this)
        setContentView(mScannerView)

    }

    public override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this)
        mScannerView!!.startCamera()
    }

    public override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()
    }

    override fun handleResult(rawResult: Result) {
        empID = rawResult.text
        val empRecord = EmployeeTimeRecord.create()
        empRecord.empId = rawResult.text
        mDatabase.orderByChild("empId").equalTo(empID).addListenerForSingleValueEvent(itemListener)
        /**val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        empRecord.timeIn = ts
        empRecord.timeOut = ""
        val newItem = mDatabase.push()
        empRecord.recordId = newItem.key
        newItem.setValue(empRecord)
        Toast.makeText(this@ScanActivity, "Logged successfully!", Toast.LENGTH_LONG).show();**/
        onBackPressed()

        //mScannerView.resumeCameraPreview(this);
    }

    var itemListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            addDataToList(dataSnapshot)
        }
        override fun onCancelled(databaseError: DatabaseError) {
            Log.w("ViewEmployeeTimeRecord", "loadItem:onCancelled", databaseError.toException())
        }
    }

    private fun addDataToList(dataSnapshot: DataSnapshot) {
        val tsLong = System.currentTimeMillis()
        val date = tsLong.toLong()
        val items = dataSnapshot.children.iterator()
        if (items.hasNext()) {
            var currentItem = items.next()
            while (items.hasNext()) {
                currentItem = items.next()
            }

            val map = currentItem.getValue() as HashMap<String, Any>
            var timeOut = map.get("timeOut") as Long?
            if(timeOut == 0L) {
                map.put("timeOut", date)
                var childUpdates = HashMap<String, Any>()
                childUpdates.put("/" + currentItem.key , map);
                mDatabase.updateChildren(childUpdates);


            } else {
                val empRecord = EmployeeTimeRecord.create()
                empRecord.empId = empID
                empRecord.timeIn = date
                empRecord.timeOut = 0
                val newItem = mDatabase.push()
                empRecord.recordId = newItem.key
                newItem.setValue(empRecord)
            }
        } else {
            val empRecord = EmployeeTimeRecord.create()
            empRecord.empId = empID
            empRecord.timeIn = date
            empRecord.timeOut = 0
            val newItem = mDatabase.push()
            empRecord.recordId = newItem.key
            newItem.setValue(empRecord)
        }

        Toast.makeText(this@ScanActivity, "Recorded successfully!", Toast.LENGTH_LONG).show()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {

        R.id.navHome -> {
            val intent2 = Intent(this, MainActivity::class.java)
            startActivity(intent2)
            true
        }


        else -> super.onOptionsItemSelected(item)
    }
}



