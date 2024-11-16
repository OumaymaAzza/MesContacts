package com.example.mescontacts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ContactsActivity : AppCompatActivity() {
    private val REQUEST_CONTACTS_PERMISSION = 1
    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var searchInput: EditText
    private val contactsList = mutableListOf<Contact>()
    private val allContacts = mutableListOf<Contact>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_contacts)
        recyclerView = findViewById(R.id.recyclerViewContacts)
        searchInput = findViewById(R.id.searchInput)
        recyclerView.layoutManager = LinearLayoutManager(this)
        contactAdapter = ContactAdapter(contactsList) { contact ->
            showContextMenu(contact)
        }
        recyclerView.adapter = contactAdapter
        checkPermission()
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                filterContacts(query)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_CONTACTS_PERMISSION
            )
        } else {
            loadContacts()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CONTACTS_PERMISSION && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            loadContacts()
        } else {
            Toast.makeText(this, "Permission refusée", Toast.LENGTH_SHORT).show()
        }
    }
    private fun loadContacts() {
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
        )
        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val phoneIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val name = it.getString(nameIndex)
                val phoneNumber = it.getString(phoneIndex)
                val contact = Contact(name, phoneNumber)
                contactsList.add(contact)
                allContacts.add(contact)
            }
        }
        contactAdapter.updateList(contactsList)
    }
    private fun filterContacts(query: String) {
        val filteredContacts = if (query.isEmpty()) {
            allContacts
        } else {
            allContacts.filter {
                it.nomContact.contains(query, ignoreCase = true) || it.numeroContact.contains(query)
            }
        }
        contactAdapter.updateList(filteredContacts)
    }
    private fun showContextMenu(contact: Contact) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_contact_options, null)
        val callOption: LinearLayout = dialogView.findViewById(R.id.callOption)
        val smsOption: LinearLayout = dialogView.findViewById(R.id.smsOption)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val dialog = builder.create()
        callOption.setOnClickListener {
            makeCall(contact.numeroContact)
            dialog.dismiss()
        }
        smsOption.setOnClickListener {
            sendSms(contact.numeroContact)
            dialog.dismiss()
        }
        dialog.show()
    }
    private fun makeCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        startActivity(intent)
    }
    private fun sendSms(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$phoneNumber"))
        intent.putExtra("sms", "")
        startActivity(intent)
    }
}
