package com.example.mescontacts
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.cardview.widget.CardView

class ContactAdapter(
    private var contacts: List<Contact>,
    private val onMenuClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    fun updateList(newContacts: List<Contact>) {
        contacts = newContacts
        notifyDataSetChanged()
    }
    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.nomContact)
        val phoneNumber: TextView = view.findViewById(R.id.numeroContact)
        val menuButton: ImageButton = view.findViewById(R.id.button)
        val bgContactInitial: CardView = view.findViewById(R.id.bgContactInitial)
        val contactInitial: TextView = view.findViewById(R.id.contactInitial)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contacts, parent, false)
        return ContactViewHolder(view)
    }
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.name.text = contact.nomContact
        holder.phoneNumber.text = contact.numeroContact
        holder.menuButton.setOnClickListener {
            onMenuClick(contact)
        }
        val initial = contact.nomContact.firstOrNull()?.uppercaseChar() ?: ' '
        holder.contactInitial.text = initial.toString()
        val color = getColorForCercle(initial)
        holder.bgContactInitial.setCardBackgroundColor(
            ContextCompat.getColor(holder.itemView.context, color)
        )
    }
    override fun getItemCount() = contacts.size
    private fun getColorForCercle(letter: Char): Int {
        return when (letter) {
            'A' -> R.color.color_A
            'B' -> R.color.color_B
            'C' -> R.color.color_C
            'D' -> R.color.color_D
            'E' -> R.color.color_E
            'F' -> R.color.color_F
            'G' -> R.color.color_G
            'H' -> R.color.color_H
            'I' -> R.color.color_I
            'J' -> R.color.color_J
            'K' -> R.color.color_K
            'L' -> R.color.color_L
            'M' -> R.color.color_M
            'N' -> R.color.color_N
            'O' -> R.color.color_O
            'P' -> R.color.color_P
            'Q' -> R.color.color_Q
            'R' -> R.color.color_R
            'S' -> R.color.color_S
            'T' -> R.color.color_T
            'U' -> R.color.color_U
            'V' -> R.color.color_V
            'W' -> R.color.color_W
            'X' -> R.color.color_X
            'Y' -> R.color.color_Y
            'Z' -> R.color.color_Z
            else -> R.color.defaultColor
        }
    }
}
