package com.arafatporosh.hallmanagement

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class StudentAdapter(
    private val studentList: MutableList<Student>,
    private val context: Context
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvStudentName)
        val id: TextView = itemView.findViewById(R.id.tvStudentID)
        val department: TextView = itemView.findViewById(R.id.tvDepartment)
        val phone: TextView = itemView.findViewById(R.id.tvPhoneNumber)
        val btnRemove: Button = itemView.findViewById(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.name.text = student.name ?: "N/A"
        holder.id.text = "Student ID: ${student.studentID ?: "N/A"}"
        holder.department.text = "Department: ${student.dept ?: "N/A"}"
        holder.phone.text = "Phone: ${student.mobileNo ?: "N/A"}"

        holder.btnRemove.setOnClickListener {
            showRemoveConfirmationDialog(student, position)
        }
    }


    private fun showRemoveConfirmationDialog(student: Student, position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Remove Student")
        builder.setMessage("Do you want to remove the student?")

        builder.setPositiveButton("Yes") { _, _ ->
            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("users").child(student.studentID)

            ref.removeValue().addOnSuccessListener {
                studentList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, studentList.size)
            }.addOnFailureListener {
                it.printStackTrace()
            }
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    override fun getItemCount(): Int {
        return studentList.size
    }
}
