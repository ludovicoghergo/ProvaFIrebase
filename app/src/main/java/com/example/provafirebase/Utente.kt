package com.example.provafirebase

class Utente(firstName: String, lastName: String, id: String ) {
    var firstName = firstName
    var lastName=lastName
    var id = id

    override fun toString(): String {
        return "Utente(firstName='$firstName', lastName='$lastName', id='$id')"
    }


}