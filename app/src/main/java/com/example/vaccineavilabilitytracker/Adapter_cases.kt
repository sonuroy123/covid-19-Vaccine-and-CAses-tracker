package com.example.vaccineavilabilitytracker

import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// on below line we are creating our adapter class
// in this class we are passing our array list
// and our View Holder class which we have created.


class Adapter_cases(private val centerList: List<DataModel_Cases>) : RecyclerView.Adapter<Adapter_cases.CasesViewHolder>() {

    class CasesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val stateName: TextView = itemView.findViewById(R.id.txtStateName)
        val cases: TextView = itemView.findViewById(R.id.txtCases1)
        val recovered: TextView = itemView.findViewById(R.id.txtRecovered1)
        val deaths: TextView = itemView.findViewById(R.id.txtDeaths1)
        val active : TextView = itemView.findViewById(R.id.txtActive1)
        val todayActive : TextView = itemView.findViewById(R.id.txtTodayActive1)
        val todayRecovered : TextView = itemView.findViewById(R.id.txtTodayRecovered1)
        val todayDeaths : TextView = itemView.findViewById(R.id.txtTodayDeaths1)
        val todayCases : TextView = itemView.findViewById(R.id.txtTodayCases1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CasesViewHolder {
        // this method is use to inflate the layout file
        // which we have created for our recycler view.
        // on below line we are inflating our layout file.
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.single_row_cases,
            parent, false
        )
        // at last we are returning our view holder
        // class with our item View File.
        return CasesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CasesViewHolder, position: Int) {
        // on below line we are getting item
        // from our list along with its position.
        val currentItem = centerList[position]

        // after getting current item we are setting
        // data from our list to our text views.
        holder.stateName.text = currentItem.stateName
        holder.recovered.text = currentItem.recovered
        holder.deaths.text = currentItem.deaths
        holder.cases.text = currentItem.cases
        holder.active.text = currentItem.active
        holder.todayActive.text = currentItem.todayActive
        holder.todayRecovered.text = currentItem.todayRecovered
        holder.todayDeaths.text = currentItem.todayDeaths
        holder.todayCases.text = currentItem.todayCases
    }

    override fun getItemCount(): Int {
        return centerList.size
    }
}