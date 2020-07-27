package `in`.trentweet.milestone.viewmodel

import `in`.trentweet.milestone.R
import `in`.trentweet.milestone.model.Model
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import java.util.*

class MileStoneAdapter(private val context: Context, private val itemList: ArrayList<Model>) :
    RecyclerView.Adapter<MileStoneAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.layout_milestone, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.txtMsTitle.text = itemList[position].title

        if (itemList[position].description.isEmpty())
            holder.txtMsDesc.visibility = View.GONE
        else
            holder.txtMsDesc.text = itemList[position].description

        if (itemList[position].currentProgress.isEmpty())
            holder.pbMs.progress = 0F
        else
            holder.pbMs.progress = itemList[position].currentProgress.toFloat()

        if (splitAndReturnYear(itemList[position].startDate) ==
            Calendar.getInstance().get(Calendar.YEAR).toString()
        ) {
            val myDate = itemList[position].startDate.split("/")
            holder.txtStartDate.text = myDate[0] + "/" + myDate[1]
        } else {
            holder.txtStartDate.text = itemList[position].startDate
        }

        if (splitAndReturnYear(itemList[position].endDate) ==
            Calendar.getInstance().get(Calendar.YEAR).toString()
        ) {
            val myDate = itemList[position].endDate.split("/")
            holder.txtEndDate.text = myDate[0] + "/" + myDate[1]
        } else {
            holder.txtEndDate.text = itemList[position].endDate
        }

        if (!itemList[position].currentProgress.isEmpty()) {
            when {
                itemList[position].currentProgress.toFloat() in 0.00..25.0 -> {
                    holder.pbMs.progressColor = R.color._0to25
                }
                itemList[position].currentProgress.toFloat() in 26.0..50.0 -> {
                    holder.pbMs.progressColor = R.color._26to50
                }
                itemList[position].currentProgress.toFloat() in 51.0..75.0 -> {
                    holder.pbMs.progressColor = R.color._51to75
                }
                itemList[position].currentProgress.toFloat() in 76.0..100.0 -> {
                    holder.pbMs.progressColor = R.color._76to100
                }
            }
        } else
            holder.pbMs.progress = 0f


    }

    private fun splitAndReturnYear(date: String): String {
        val myDate = date.split("/")
        return myDate[2]
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtMsTitle: TextView = view.findViewById(R.id.txt_ms_title)
        val txtMsDesc: TextView = view.findViewById(R.id.txt_ms_desc)
        val txtStartDate: TextView = view.findViewById(R.id.txt_start_date)
        val txtEndDate: TextView = view.findViewById(R.id.txt_end_date)
        val pbMs: RoundCornerProgressBar = view.findViewById(R.id.pb_ms)
    }


}
