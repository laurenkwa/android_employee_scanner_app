package com.comp3617.laurenkwa

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import android.widget.TextView



public class EmployeeTimeRecordAdapter(private val ctx: Context, private val employees: MutableList<EmployeeTimeRecord>) : BaseAdapter() {
    var empName : String = ""
    private val mInflater: LayoutInflater = LayoutInflater.from(ctx)
    private var itemList = employees
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var mDatabase = FirebaseDatabase.getInstance().reference
        val recordId: String = itemList.get(position).recordId as String
        val empId: String = itemList.get(position).empId as String
        mDatabase = mDatabase.child("emp_item")
        mDatabase.orderByChild("empId").equalTo(empId).addListenerForSingleValueEvent(itemListener)
        val timeIn: Long = itemList.get(position).timeIn as Long
        var timeOut: Long = itemList.get(position).timeOut as Long
        val view: View
        val vh: ListRowHolder
        var timeOutText = ""
        if (convertView == null) {
            view = mInflater.inflate(R.layout.timerecord_layout, parent, false)
            vh = ListRowHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ListRowHolder
        }
        var sdf = SimpleDateFormat("MM/dd/yyyy HH:mm")


        vh.timeIn.text = sdf.format(Date(timeIn))
        if(timeOut == 0L) {
            timeOutText = ""
        } else {
            timeOutText = sdf.format(Date(timeOut))
        }
        vh.timeOut.text = " - " + timeOutText

        return view
    }
    override fun getItem(index: Int): Any {
        return itemList.get(index)
    }
    override fun getItemId(index: Int): Long {
        return index.toLong()
    }
    override fun getCount(): Int {
        return itemList.size
    }
    private class ListRowHolder(row: View?) {
        val timeIn: TextView = row!!.findViewById<TextView>(R.id.timeIn) as TextView
        val timeOut: TextView = row!!.findViewById<TextView>(R.id.timeOut) as TextView
        //val ibDeleteObject: ImageButton = row!!.findViewById<ImageButton>(R.id.iv_cross) as ImageButton
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
        val items = dataSnapshot.children.iterator()
        if (items.hasNext()) {
            while (items.hasNext()) {
                val currentItem = items.next()
                val map = currentItem.getValue() as HashMap<String, Any>
                empName = map.get("empName") as String


            }
        }


    }
}
