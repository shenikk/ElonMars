package com.example.elonmars.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.elonmars.*
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var mShimmerViewContainer: ShimmerFrameLayout
    private lateinit var weatherDay: TextView
    private lateinit var title: TextView
    private lateinit var today: TextView
    private lateinit var highTemp: TextView
    private lateinit var lowTemp: TextView

    private var dataSet: ArrayList<Soles> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.weather_recycler)
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container)
        weatherDay = view.findViewById(R.id.weather_day)
        title = view.findViewById(R.id.title)
        today = view.findViewById(R.id.today)
        highTemp = view.findViewById(R.id.temp_high)
        lowTemp = view.findViewById(R.id.temp_low)

        setUpRecycler(recyclerView)
        setUpRetrofit(weatherAdapter)
    }

    override fun onResume() {
        super.onResume()
        mShimmerViewContainer.startShimmerAnimation()
    }

    private fun setUpRecycler(recyclerView: RecyclerView) {
        weatherAdapter = WeatherAdapter(dataSet)
        recyclerView.adapter = weatherAdapter
    }

    private fun setUpRetrofit(weatherAdapter: WeatherAdapter) {
        val call = Common.weatherRetrofit.getWeatherData("weather", "msl", "json")
        call.enqueue(object : Callback<WeatherItem> {

            override fun onResponse(call: Call<WeatherItem>, response: Response<WeatherItem>) {
                if (response.isSuccessful) {

                    response.body()?.soles?.let {
                        dataSet.clear()
                        for (i in 0 until 10) {
                            dataSet.add(
                                Soles("Sol ${it[i].weatherDay}",
                                    it[i].earthDate,
                                    "High: ${it[i].highTemp} °C",
                                    "Low: ${it[i].lowTemp} °C")
                            )
                        }
                    }

                    mShimmerViewContainer.stopShimmerAnimation()
                    mShimmerViewContainer.visibility = View.GONE
                    setTodayWeather()
                    weatherAdapter.notifyDataSetChanged()

                    Toast.makeText(this@WeatherFragment.context, "Success!", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "Successfully get a list of data")
                }
            }

            override fun onFailure(call: Call<WeatherItem>, t: Throwable) {
                Toast.makeText(this@WeatherFragment.context, "Fail!", Toast.LENGTH_SHORT).show()
                Log.e("TAG", "Failed to get a list of data")
            }
        })
    }

    private fun setTodayWeather() {
        weatherDay.text = dataSet[0].weatherDay
        highTemp.text = dataSet[0].highTemp
        lowTemp.text = dataSet[0].lowTemp
    }
}