package com.comp3617.laurenkwa

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.tasks.Task

public class EmployeeAdapter(private val ctx: Context, private val employees: MutableList<Employee>) : BaseAdapter() {

    private val mInflater: LayoutInflater = LayoutInflater.from(ctx)
    private var itemList = employees
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val objectId: String = itemList.get(position).empId as String
        val itemName: String = itemList.get(position).empName as String
        val view: View
        val vh: ListRowHolder
        if (convertView == null) {
            view = mInflater.inflate(R.layout.row_layout, parent, false)
            vh = ListRowHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ListRowHolder
        }
        vh.empName.text = itemName
        vh.empId.text = objectId
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
        val empName: TextView = row!!.findViewById<TextView>(R.id.employeeName) as TextView
        val empId: TextView = row!!.findViewById<CheckBox>(R.id.employeeId) as TextView
        //val ibDeleteObject: ImageButton = row!!.findViewById<ImageButton>(R.id.iv_cross) as ImageButton
    }
}
