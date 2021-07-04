package com.example.vaccineavilabilitytracker


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import android.graphics.Color
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import android.view.Menu
import android.view.MenuItem
import java.util.*

class MainActivity : AppCompatActivity() {

    var list: MutableList<SliderItem>? = null
    var adapter: SliderAdapterExample? = null

    lateinit var buttonCheck: Button

    lateinit var buttonCheck1 : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonCheck = findViewById(R.id.btnCheck)
        buttonCheck1 = findViewById(R.id.btnCheck1)

        buttonCheck1.setOnClickListener {
            val intent = Intent(this,Show_cases::class.java)
               startActivity(intent)
            }

            buttonCheck.setOnClickListener {
                val intent  = Intent(this,Show_data ::class.java)
                startActivity(intent)
            }
        initView()
        }

    private fun initView() {
        list = ArrayList()
        adapter = SliderAdapterExample(this@MainActivity)
        val sliderView = findViewById<SliderView>(R.id.imageSlider)
        sliderView.setSliderAdapter(adapter!!)
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM)
        //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        sliderView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        sliderView.indicatorSelectedColor = Color.WHITE
        sliderView.indicatorUnselectedColor = Color.GRAY
        sliderView.scrollTimeInSec = 4 //set scroll delay in seconds :
        sliderView.startAutoCycle()
        addImages()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun addImages() {
        val model1 = SliderItem()
        model1.image = resources.getDrawable(R.drawable.protocol_7)
        list!!.add(model1)
        val model2 = SliderItem()
        model2.image = resources.getDrawable(R.drawable.protocol_5)
        list!!.add(model2)
        val model3 = SliderItem()
        model3.image = resources.getDrawable(R.drawable.protocol_6)
        list!!.add(model3)
        val model4 = SliderItem()
        model4.image = resources.getDrawable(R.drawable.protocol_2)
        list!!.add(model4)
        adapter!!.renewItems(list!!)
    }

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
                val intent = Intent(this,Show_cases::class.java)
                startActivity(intent)
                true
            }            //Your code here

            R.id.dashboard ->    {
               Toast.makeText(this@MainActivity,"Currently You are on dashboard",Toast.LENGTH_LONG).show()
                true
            }

            R.id.developer ->  {
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