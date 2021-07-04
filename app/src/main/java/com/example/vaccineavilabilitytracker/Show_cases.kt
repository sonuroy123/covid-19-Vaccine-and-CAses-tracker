package com.example.vaccineavilabilitytracker


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import android.view.Menu
import android.view.MenuItem

class Show_cases : AppCompatActivity() {

    private var recycler : RecyclerView? = null

    lateinit var adapter : Adapter_cases

    lateinit var dataList : List<DataModel_Cases>

    var loadingPB :ProgressBar?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_cases)

        recycler = findViewById(R.id.recyclerView)

        dataList = ArrayList<DataModel_Cases>()
        loadingPB = findViewById(R.id.idLoading)

        loadingPB?.visibility = View.VISIBLE
        getAppointments()
    }

    fun getAppointments(){

        val url = "https://disease.sh/v3/covid-19/gov/India"

        val queue = Volley.newRequestQueue(this@Show_cases)

        //on below line we are creating a request
        // variable for making our json object request.
        val request =
                JsonObjectRequest(
                        Request.Method.GET, url, null,{ response ->
                    // this method is called when we get successful response from API.
                    Log.e("TAG", "SUCCESS RESPONSE IS $response")
                    print("Success : - $response")
                    loadingPB?.visibility = View.GONE


                    try {
                        // in try block we are creating a variable for center
                        // array and getting our array from our object.
                        val centerArray = response.getJSONArray("states")
                        //val totalCases = response.getJSONArray("total")

                        //val obj = totalCases.getJSONObject(0)

                        // on below line we are checking if the length of the array is 0.
                        // the zero length indicates that there is no data for the given pincode.
                        if (centerArray.length() == 0) {
                            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show()
                        }
                        for (i in 0 until centerArray.length()) {

                            // on below line we are creating a variable for our center object.
                            val centerObj = centerArray.getJSONObject(i)

                            // on below line we are getting data from our session
                            // object and we are storing that in a different variable.
                            val stateName: String = centerObj.getString("state")
                            val recovered: String = centerObj.getString("recovered")
                            val deaths: String = centerObj.getString("deaths")
                            val cases: String = centerObj.getString("cases")
                            val active: String = centerObj.getString("active")
                            val todayActive: String = centerObj.getString("todayActive")
                            val todayRecovered: String = centerObj.getString("todayRecovered")
                            val todayDeaths: String = centerObj.getString("todayDeaths")
                            val todayCases: String = centerObj.getString("todayCases")

                            val center = DataModel_Cases(
                                    stateName,
                                    active,
                                    recovered,
                                    deaths,
                                    cases,
                                    todayActive,
                                    todayRecovered,
                                    todayDeaths,
                                    todayCases
                            )
                            // after that we are passing this modal to our list on the below line.
                            // after that we are passing this modal to our list on the below line.
                            dataList = dataList + center
                        }

                        // on the below line we are passing this list to our adapter class.
                        adapter =Adapter_cases(dataList)

                        // on the below line we are setting layout manager to our recycler view.
                        recycler?.layoutManager = LinearLayoutManager(this)

                        // on the below line we are setting an adapter to our recycler view.
                        recycler?.adapter = adapter

                        // on the below line we are notifying our adapter as the data is updated.
                        adapter.notifyDataSetChanged()

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
                            Toast.makeText(this@Show_cases, "Fail to get response", Toast.LENGTH_SHORT).show()
                        })

        // at last we are adding
        // our request to our queue.
        queue.add(request)
    }




// Menu implementation



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
                val intent = Intent(this, Show_data::class.java)
                startActivity(intent)//Your code here
                true
            }
            R.id.cases ->    {
                Toast.makeText(this@Show_cases,"Currently You are on viewing cases",Toast.LENGTH_LONG).show()
                true
            }            //Your code here

            R.id.dashboard ->    {
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.developer ->{
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
