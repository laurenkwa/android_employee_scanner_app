package com.comp3617.laurenkwa

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.row_layout.*

class EmployeeViewActivity : AppCompatActivity() {

    var employeeList: MutableList<Employee>? = null
    lateinit var adapter: EmployeeAdapter
    private var employeeItems: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_employee_view)

        var lvEmployees = findViewById<ListView>(R.id.listEmployees)

        var mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase = mDatabase.child("emp_item");
        employeeList = mutableListOf<Employee>()
        adapter = EmployeeAdapter(this, employeeList!!)
        lvEmployees!!.setAdapter(adapter)

        mDatabase.orderByKey().addListenerForSingleValueEvent(itemListener)

        lvEmployees!!.setOnItemClickListener { adapterView, view, i, l  ->

            var intent = Intent(this, ViewEmployeeTimeRecordActivity::class.java)
            var obj = lvEmployees.getItemAtPosition(i)
            var employeeIdView = view.findViewById(R.id.employeeId) as TextView
            var employeeNameView = view.findViewById(R.id.employeeName) as TextView
            var employeeId = employeeIdView.text
            var employeeName = employeeNameView.text
            intent.putExtra("id", employeeId)
            intent.putExtra("name", employeeName)
            startActivity(intent)
        }

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
                val empItem = Employee.create()
                val map = currentItem.getValue() as HashMap<String, Any>
                empItem.empId = currentItem.key
                empItem.empName = map.get("empName") as String?
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
