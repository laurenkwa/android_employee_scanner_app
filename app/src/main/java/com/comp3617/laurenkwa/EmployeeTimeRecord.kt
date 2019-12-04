package com.comp3617.laurenkwa

import java.sql.Timestamp
import java.util.*

class EmployeeTimeRecord {
    companion object Factory {
        fun create(): EmployeeTimeRecord = EmployeeTimeRecord()
    }
    var recordId: String? = null
    var empId: String? = null
    var timeIn: Long? = null
    var timeOut: Long? = null
    var empName: String? = null
}