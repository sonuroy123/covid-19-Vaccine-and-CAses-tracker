package com.example.vaccineavilabilitytracker

import android.app.DatePickerDialog
import android.app.DownloadManager
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.textclassifier.TextLanguage
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList
import android.view.Menu
import android.view.MenuItem

class Show_data : AppCompatActivity() {

    // creating a variable for our button.
    private lateinit var searchButton: Button

    // creating variable for our edit text.
    lateinit var pinCodeEdt: EditText

    // creating a variable for our recycler view.
    lateinit var centersRV: RecyclerView

    // creating a variable for adapter class.
    lateinit var centerRVAdapter: Adapter

    // creating a variable for our list
    lateinit var centerList: List<DataModel>

    // creating a variable for progress bar.
    lateinit var loadingPB: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_data)

        centersRV = findViewById(R.id.centersRV)
        pinCodeEdt = findViewById(R.id.edtPinCode)
        searchButton = findViewById(R.id.btnBtnSearch)
        loadingPB = findViewById(R.id.idPBLoading)

        centerList = ArrayList<DataModel>()

        searchButton.setOnClickListener {

            // inside on click listener we are getting data from
            // edit text and creating a val for ite on below line.
            val pinCode  = pinCodeEdt.text.toString()

            // on below line we are validating
            // our pin code as 6 digit or not.
            if(pinCode.length != 6){
                Toast.makeText(this@Show_data,"Please Enter valid pin code",Toast.LENGTH_LONG).show()
            }
            else{

                // if the pincode is correct.
                // first of all we are clearing our array list this
                // will clear the data in it if already present.
                (centerList as ArrayList<DataModel>).clear()

                // on below line we are getting instance of our calendar.
                val c = Calendar.getInstance()

                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                // on below line we are creating our date picker dialog.

                val datePicker = DatePickerDialog(
                        this,
                        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                            // after that we are making our progress bar as visible.
                            loadingPB.setVisibility(View.VISIBLE)

                            // on below line we are creating a date string for our date
                            val dateStr: String = """$dayOfMonth - ${monthOfYear + 1} - $year"""

                            // on below line we are calling a method to get
                            // the appointment info for vaccination centers
                            // and we are passing our pin code to it.
                            getAppointments(pinCode, dateStr)
                        },
                        year,
                        month,
                        day
                )
                datePicker.show()

            }
        }

    }

    // below this method for redirecting to the respective URL(Cowin website)
    fun appointment(view: View){
        val openUrl = Intent(Intent.ACTION_VIEW)

        openUrl.data = Uri.parse("https://www.cowin.gov.in/home")

        startActivity(openUrl)
    }

    // below is the method for getting data from API
    fun getAppointments(pinCode : String, date : String){

        val url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=" + pinCode + "&date=" + date

        val queue = Volley.newRequestQueue(this@Show_data)

        //on below line we are creating a request
        // variable for making our json object request.
        val request =
                JsonObjectRequest(Request.Method.GET, url, null,{response ->
                    // this method is called when we get successful response from API.
                    Log.e("TAG", "SUCCESS RESPONSE IS $response")
                    print("Success : - $response")


                    // we are setting the visibility of progress bar as gone.
                    loadingPB.setVisibility(View.GONE)
                    // on below line we are adding a try catch block.
                    try {
                        // in try block we are creating a variable for center
                        // array and getting our array from our object.
                        val centerArray = response.getJSONArray("centers")

                        // on below line we are checking if the length of the array is 0.
                        // the zero length indicates that there is no data for the given pincode.
                        if (centerArray.length().equals(0)) {
                            Toast.makeText(this, "No Center Found", Toast.LENGTH_SHORT).show()
                        }
                        for (i in 0 until centerArray.length()) {

                            // on below line we are creating a variable for our center object.
                            val centerObj = centerArray.getJSONObject(i)

                            // on below line we are getting data from our session
                            // object and we are storing that in a different variable.
                            val centerName: String = centerObj.getString("name")
                            val centerAddress: String = centerObj.getString("address")
                            val centerFromTime: String = centerObj.getString("from")
                            val centerToTime: String = centerObj.getString("to")
                            val fee_type: String = centerObj.getString("fee_type")

                            // on below line we are creating a variable for our session object
                            val sessionObj = centerObj.getJSONArray("sessions").getJSONObject(0)
                            val ageLimit: Int = sessionObj.getInt("min_age_limit")
                            val vaccineName: String = sessionObj.getString("vaccine")
                            val avaliableCapacity: Int = sessionObj.getInt("available_capacity")

                            // after extracting all the data we are passing this
                            // data to our modal class we have created
                            // a variable for it as center.
                            val center = DataModel(
                                    centerName,
                                    centerAddress,
                                    centerFromTime,
                                    centerToTime,
                                    fee_type,
                                    ageLimit,
                                    vaccineName,
                                    avaliableCapacity
                            )
                            // after that we are passing this modal to our list on the below line.
                            // after that we are passing this modal to our list on the below line.
                            centerList = centerList + center
                        }

                        // on the below line we are passing this list to our adapter class.
                        centerRVAdapter = Adapter(centerList)

                        // on the below line we are setting layout manager to our recycler view.
                        centersRV.layoutManager = LinearLayoutManager(this)

                        // on the below line we are setting an adapter to our recycler view.
                        centersRV.adapter = centerRVAdapter

                        // on the below line we are notifying our adapter as the data is updated.
                        centerRVAdapter.notifyDataSetChanged()

                    } catch (e: JSONException) {
                        // below line is for handling json exception.
                        e.printStackTrace();
                    }
                },
                        { error ->
                            // this method is called when we get any
                            // error while fetching data from our API
                            Log.e("TAG", "RESPONSE IS $error")
                            // in this case we are simply displaying a toast message.
                            Toast.makeText(this@Show_data, "Fail to get response", Toast.LENGTH_SHORT).show()
                        })

        // at last we are adding
        // our request to our queue.
        queue.add(request)
    }



//  Menu implementation


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //Inflating menu by overriding inflate() method of MenuInflater class.
        //Inflating here means parsing layout XML to views.
        menuInflater.inflate(R.menu.menu_drawer, menu)
        return true
    }

    //Overriding onOptionsItemSelected to perform event on menu items
    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        //Toast.makeText(this, "You chose : " + menuItem.title, Toast.LENGTH_SHORT).show()
        return when (menuItem.itemId) {
            R.id.vaccin -> {
                Toast.makeText(this@Show_data,"Currently You are viewing vaccines data",Toast.LENGTH_LONG).show()
                true
            }
            R.id.cases ->    {
                val intent = Intent(this,Show_cases::class.java)
                startActivity(intent)
                true
            }            //Your code here

            R.id.dashboard ->    {
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.developer -> {
                val intent = Intent(this,Developer_info::class.java)
                startActivity(intent)
                true
            }

            R.id.aboutApp ->  {
                val intent = Intent(this,AppInfo::class.java)
                startActivity(intent)
                true
            }


            else -> super.onOptionsItemSelected(menuItem)
        }
    }
}