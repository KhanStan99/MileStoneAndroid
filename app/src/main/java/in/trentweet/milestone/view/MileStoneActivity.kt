package `in`.trentweet.milestone.view

import `in`.trentweet.milestone.R
import `in`.trentweet.milestone.model.Model
import `in`.trentweet.milestone.utils.LoggerUtils
import `in`.trentweet.milestone.viewmodel.MileStoneAdapter
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_milestone.*
import org.json.JSONObject
import java.util.*


class MileStoneActivity : AppCompatActivity() {

    private val mAuth = FirebaseAuth.getInstance().currentUser
    private val db = FirebaseFirestore.getInstance()
    private var myList: ArrayList<Model> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_milestone)
        getDate()

        btnFab.setOnClickListener {
            showDialogBox()
        }

    }

    private fun showDialogBox() {
        val dialogs = Dialog(this@MileStoneActivity)
        dialogs.setContentView(R.layout.layout_add_mile_stone)
        dialogs.show()

        val etText = dialogs.findViewById<EditText>(R.id.etText)
        val etDesc = dialogs.findViewById<EditText>(R.id.etDesc)
        val etStart = dialogs.findViewById<EditText>(R.id.etStart)
        val etEnd = dialogs.findViewById<EditText>(R.id.etEnd)
        val etCrr = dialogs.findViewById<EditText>(R.id.etCrr)
        val etMax = dialogs.findViewById<EditText>(R.id.etMax)
        val btn = dialogs.findViewById<Button>(R.id.btn)

        etStart.showSoftInputOnFocus = false
        etEnd.showSoftInputOnFocus = false

        etStart.setOnClickListener {
            showDatePicker(etStart)
        }

        etEnd.setOnClickListener {
            showDatePicker(etEnd)
        }

        btn.setOnClickListener {
            when {
                getTextFromET(etText).isEmpty() -> etText.error = "This is a required field"
                getTextFromET(etStart).isEmpty() -> etStart.error = "This is a required field"
                getTextFromET(etEnd).isEmpty() -> etEnd.error = "This is a required field"
                getTextFromET(etMax).isEmpty() -> etMax.error = "This is a required field"
                else -> {
                    val model = Model(
                        getTextFromET(etText), getTextFromET(etDesc), getTextFromET(etStart),
                        getTextFromET(etEnd), getTextFromET(etCrr), getTextFromET(etMax)
                    )
                    db.collection("root")
                        .document("mile_stones_data")
                        .collection(mAuth!!.email.toString())
                        .add(model)
                        .addOnSuccessListener {
                            getDate()
                            dialogs.dismiss()
                        }
                        .addOnFailureListener { e ->
                            LoggerUtils().error("Failed =>" + e.localizedMessage)
                        }
                }
            }
        }

    }

    private fun showDatePicker(editText: EditText?) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        var selectedDate: String
        var selectedMonth: String

        val dpd = DatePickerDialog(
            this@MileStoneActivity,
            DatePickerDialog.OnDateSetListener { _,
                                                 _year, _month, _day ->
                selectedDate = _day.toString()
                selectedMonth = _month.inc().toString()

                if (selectedDate.toInt() < 10)
                    selectedDate = "0$selectedDate"

                if (selectedMonth.toInt() < 10)
                    selectedMonth = "0$selectedMonth"

                editText!!.text = Editable.Factory.getInstance()
                    .newEditable("$selectedDate/$selectedMonth/$_year")
            }, year, month, day
        )
        dpd.show()
    }

    private fun getDate() {
        db.collection("root").document("mile_stones_data").collection(mAuth!!.email.toString())
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    myList = ArrayList()
                    for (document in task.result!!) {
                        val obj = JSONObject(document.data)
                        myList.add(
                            Model(
                                obj.optString("title"),
                                obj.optString("description"),
                                obj.optString("startDate"),
                                obj.optString("endDate"),
                                obj.optString("currentProgress"),
                                obj.optString("maxProgress")
                            )
                        )
                    }
                    val adapter =
                        MileStoneAdapter(applicationContext, myList)
                    val listView = findViewById<RecyclerView>(R.id.listView)
                    listView.adapter = adapter
                    listView.layoutManager = LinearLayoutManager(applicationContext)
                    adapter.notifyDataSetChanged()
                } else {
                    LoggerUtils().error("Error getting documents." + task.exception)
                }
            }
    }

    private fun getTextFromET(et: EditText): String {
        return et.text.toString().trim()
    }

}
