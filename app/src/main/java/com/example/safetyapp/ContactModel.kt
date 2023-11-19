package com.example.safetyapp

class ContactModel(val id: Int, name: String, phoneNo: String) {
    val phoneNo: String
    var name: String

    // constructor
    init {
        this.phoneNo = validate(phoneNo)
        this.name = name
    }

    // validate the phone number, and reformat is necessary
    private fun validate(phone: String): String {

        // creating StringBuilder for both the cases
        val case1 = StringBuilder("+91")
        val case2 = StringBuilder("")

        // check if the string already has a "+"
        return if (phone[0] != '+') {
            for (i in 0 until phone.length) {
                // remove any spaces or "-"
                if (phone[i] != '-' && phone[i] != ' ') {
                    case1.append(phone[i])
                }
            }
            case1.toString()
        } else {
            for (i in 0 until phone.length) {
                // remove any spaces or "-"
                if (phone[i] != '-' || phone[i] != ' ') {
                    case2.append(phone[i])
                }
            }
            case2.toString()
        }
    }
}
