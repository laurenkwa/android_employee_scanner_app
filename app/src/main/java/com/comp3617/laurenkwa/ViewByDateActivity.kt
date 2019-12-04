package com.comp3617.laurenkwa

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_view_by_date.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class ViewByDateActivity : AppCompatActivity() {
    var employeeList: MutableList<EmployeeTimeRecord>? = null
    lateinit var adapter: EmployeeTimeRecordAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_by_date)


    }

    fun onSelectFromDate(view: View) {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        var sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy")
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            dateFromField.setText("$dayOfMonth-${monthOfYear + 1}-$year")


        }, year, month, day)

        dpd.show()
    }

    fun onSelectToDate(view: View) {

        val c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            dateToField.setText("$dayOfMonth-${monthOfYear + 1}-$year")



        }, year, month, day)

        dpd.show()
    }

    fun onGetTimeRecord(view : View) {
        var mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase = mDatabase.child("emp_record_item")
        var lvEmployees = findViewById<ListView>(R.id.listEmployeeRecords)
        employeeList = mutableListOf<EmployeeTimeRecord>()
        adapter = EmployeeTimeRecordAdapter(this, employeeList!!)
        lvEmployees!!.setAdapter(adapter)
        var sdf = SimpleDateFormat("dd-MM-yyyy")
        var dateFrom = sdf.parse(dateFromField.text.toString())
        var dateTo = sdf.parse(dateToField.text.toString())
        var timeStart = dateFrom.getTime()
        var timeEnd = dateTo.getTime()
        Toast.makeText(this@ViewByDateActivity, timeStart.toString(), Toast.LENGTH_LONG).show()
        mDatabase.orderByChild("timeIn").endAt(timeEnd.toDouble()).addListenerForSingleValueEvent(itemListener)

    }

    var itemListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            addDataToList(dataSnapshot)
        }
        override fun onCancelled(databaseError: DatabaseError) {
            Log.w("EmployeeViewActivity", "loadItem:onCancelled", databaseError.toException())
        }
    }
    private fun addDataToList(dataSnapshot: DataSnapshot) {
        val items = dataSnapshot.children.iterator()
        if (items.hasNext()) {

            while (items.hasNext()) {
                val currentItem = items.next()
                val empItem = EmployeeTimeRecord.create()
                val map = currentItem.getValue() as HashMap<String, Any>
                empItem.recordId = currentItem.key
                empItem.empId = map.get("empId") as String?
                empItem.empName = map.get("empName") as String?
                empItem.timeIn = map.get("timeIn") as Long?
                empItem.timeOut = map.get("timeOut") as Long?
                employeeList!!.add(empItem);
            }
        }

        adapter.notifyDataSetChanged()
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
