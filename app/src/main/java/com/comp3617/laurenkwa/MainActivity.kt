package com.comp3617.laurenkwa

import android.Manifest
import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import java.io.IOException
import android.support.v4.content.FileProvider
import android.os.Environment.getExternalStorageDirectory
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.File


class MainActivity : AppCompatActivity() {

    val REQUEST_WRITE_PERMISSION = 200
    lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mDatabase = FirebaseDatabase.getInstance().reference
    }

    fun onScan(view: View) {

        ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_WRITE_PERMISSION)

    }

    fun onAdd(view: View) {
        val todoItem = Employee.create()
        todoItem.empName = "John Smith"
        //We first make a push so that a new item is made with a unique ID
        val newItem = mDatabase.push()
        todoItem.empId = newItem.key
        //then, we used the reference to set the value on that ID
        newItem.setValue(todoItem)
        Toast.makeText(this, "Item saved with ID " + todoItem.empId, Toast.LENGTH_SHORT).show()
    }

    fun onSearch(view: View) {
        var intent = Intent(this, EmployeeViewActivity::class.java)
        startActivity(intent)
    }

    fun onViewByDate(view: View) {
        var intent = Intent(this, ViewByDateActivity::class.java)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_WRITE_PERMISSION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                var intent = Intent(this, ScanActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@MainActivity, "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
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


