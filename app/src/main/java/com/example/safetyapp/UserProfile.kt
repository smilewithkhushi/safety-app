package com.example.safetyapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.ContactsContract
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase



class UserProfile : AppCompatActivity() {

    // create instances of various classes to be used
    var listView: ListView? = null
    var db: DbHelper? = null
    lateinit var list: MutableList<ContactModel?>
    var customAdapter: CustomAdapter? = null

    lateinit var btn: Button
    override fun onCreate(savedInstanceState: Bundle?) {


        lateinit var bottomNavigationView : BottomNavigationView


        super.onCreate(savedInstanceState)
        setContentView(com.example.safetyapp.R.layout.activity_profile)


        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener {
                menuItem ->
            when(menuItem.itemId){
                R.id.bottom_home -> {
                    intent = Intent(this, UserProfile::class.java)
                    startActivity(intent)
                    finish()
                    true
                }

                R.id.bottom_sms -> {
//                    intent = Intent(this, Login::smsActivity.java)
//                    startActivity(intent)
//                    finish()
                    true
                }


                R.id.bottom_analytics -> {
                    intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                    finish()
                    true
                }

                R.id.bottom_profile -> {
                    intent = Intent(this, ShowUserProfile::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false

            }
        }


        // check for runtime permissions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_DENIED
                    ) {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.SEND_SMS,
                                Manifest.permission.READ_CONTACTS
                            ), 100
                        )
                    }
                }

        // this is a special permission required only by devices using
        // Android Q and above. The Access Background Permission is responsible
        // for populating the dialog with "ALLOW ALL THE TIME" option
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), 100)
        }

        // check for BatteryOptimization,
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (pm != null && !pm.isIgnoringBatteryOptimizations(packageName)) {
                askIgnoreOptimization()
            }
        }

        // start the service
        val sensorService = SensorService()
        val intent = Intent(this, sensorService.javaClass)
        if (!isMyServiceRunning(sensorService.javaClass)) {
            startService(intent)
        }
        btn = findViewById<Button>(com.example.safetyapp.R.id.Button1)
        listView = findViewById<View>(com.example.safetyapp.R.id.ListView) as ListView
        db = DbHelper(this)
        list = db!!.allContacts.toMutableList()
        customAdapter = CustomAdapter(this, list)
        listView!!.adapter = customAdapter
        btn.setOnClickListener(View.OnClickListener { // calling of getContacts()
            if (db!!.count() != 5) {
                val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
                startActivityForResult(intent, UserProfile.Companion.PICK_CONTACT)
            } else {
                Toast.makeText(
                    this@UserProfile,
                    "Can't Add more than 5 Contacts",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    // method to check if the service is running
    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.i("Service status", "Running")
                return true
            }
        }
        Log.i("Service status", "Not running")
        return false
    }

    override fun onDestroy() {
        val broadcastIntent = Intent()
        broadcastIntent.action = "restartservice"
        broadcastIntent.setClass(this, ReactivateService::class.java)
        this.sendBroadcast(broadcastIntent)
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Permissions Denied!\n Can't use the App!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            UserProfile.Companion.PICK_CONTACT -> if (resultCode == RESULT_OK) {
                val contactData = data!!.data
                val c = managedQuery(contactData, null, null, null, null)
                if (c.moveToFirst()) {
                    val id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                    val hasPhone =
                        c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                    var phone: String? = null
                    try {
                        if (hasPhone.equals("1", ignoreCase = true)) {
                            val phones = contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                null,
                                null
                            )
                            phones!!.moveToFirst()
                            phone = phones.getString(phones.getColumnIndex("data1"))
                        }
                        val name =
                            c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        db!!.addcontact(ContactModel(0, name, phone!!))
                        list = db!!.allContacts.toMutableList()
                        customAdapter!!.refresh(list)
                    } catch (ex: Exception) {
                    }
                }
            }
        }
    }

    // this method prompts the user to remove any
    // battery optimisation constraints from the App
    private fun askIgnoreOptimization() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            @SuppressLint("BatteryLife") val intent =
                Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.data = Uri.parse("package:$packageName")
            startActivityForResult(
                intent,
                UserProfile.Companion.IGNORE_BATTERY_OPTIMIZATION_REQUEST
            )
        }
    }


    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }

    companion object {
        private const val IGNORE_BATTERY_OPTIMIZATION_REQUEST = 1002
        private const val PICK_CONTACT = 1
    }
}
