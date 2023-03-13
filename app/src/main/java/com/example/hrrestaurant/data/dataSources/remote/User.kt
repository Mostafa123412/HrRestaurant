package com.example.hrrestaurant.data.dataSources.remote

class User() {
    var userId: String = ""
        get() = field
        set(value) {
            field = value
        }
    var userName: String = ""
        get() = field
        set(value) {
            field = value
        }
    var userLocation: String = ""
        set(value) {
            field = value
        }
        get() = field
    var userPrimaryPhoneNumber: Int = -1
        get() = field
        set(value) {
            field = value
        }
    var userSecondaryPhoneNumber: Int = -1
        get() = field
        set(value) {
            field = value
        }
    var userEmail: String = ""
        get() = field
        set(value) {
            field = value
        }
}