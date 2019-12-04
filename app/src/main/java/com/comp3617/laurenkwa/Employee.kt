package com.comp3617.laurenkwa

class Employee {
    companion object Factory {
        fun create(): Employee = Employee()
    }
    var empId: String? = null
    var empName: String? = null
}