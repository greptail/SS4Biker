package com.generalmobi.ss4bikers

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.*
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.generalmobi.ss4bikers.service.GenericResponse
import com.generalmobi.ss4bikers.utils.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.dialog_alert.view.*
import kotlinx.android.synthetic.main.dialog_device_imei.view.*
import kotlinx.android.synthetic.main.dialog_device_imei.view.btn_update
import kotlinx.android.synthetic.main.dialog_update_freq.view.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var context: Context
    private var backPressed: Boolean = false
    private var origin: String? = null
    private var destination: String? = null
    private var polyLineList: List<LatLng> = ArrayList()
    private lateinit var marker: Marker
    private var v: Float = 0.toFloat()
    private var lat: Double = 0.toDouble()
    private var lng: Double = 0.toDouble()
    private var dirHandler = Handler()
    private var startPosition: LatLng? = null
    private var endPosition: LatLng? = null
    private var index: Int = 0
    private var next: Int = 0
    private val sydney: LatLng? = null
    private val blackPolyline: Polyline? = null
    private val greyPolyLine: Polyline? = null

    private val handler: Handler = Handler()
    private val runnable: Runnable = Runnable {
        callGetData()
    }

    companion object{
        fun startActivity(context: Context){
            val intent = Intent(context, MapsActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(action: Any) {
        if (action is String) {
            showAlert(context, action, null, null, false)
        }else if (action is Boolean){
            if (action){

                try {
                    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
                    }else{
                        vibrator.vibrate(1000)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val view = this.layoutInflater.inflate(R.layout.dialog_alert, null)
                val builder = AlertDialog.Builder(context)
                builder.setView(view)
                builder.setCancelable(false)
                val dialog = builder.create()
                dialog.show()
                view.close.setOnClickListener { dialog.dismiss() }

            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        context = this@MapsActivity

        setSupportActionBar(toolbar)

        supportActionBar?.setTitle(R.string.app_name)
        supportActionBar?.setIcon(R.mipmap.ic_launcher_round)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (TextUtils.isEmpty(fetchPreference(context, PREF_DEVICE_IMEI))){
            dialogDeviceIMEI(false)
        } else{
            handler.postDelayed(runnable, 1000)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_map, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_sync -> {
                callGetData()
                return true
            }

            R.id.action_setting -> {
                dialogDeviceIMEI()
                return true
            }
            R.id.action_update -> {
                dialogUpdateFreq()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun dialogDeviceIMEI(cancellable: Boolean = true){
        val builder = AlertDialog.Builder(context)
        val view = this.layoutInflater.inflate(R.layout.dialog_device_imei, null)
        builder.setView(view)
        builder.setCancelable(cancellable)
        val dialog = builder.create()
        view.btn_update.setOnClickListener {  if (TextUtils.isEmpty(view.input_imei.text.toString())){
            showToast(context, "Please enter device IMEI")
        }else {
            storePreference(context, PREF_DEVICE_IMEI, view.input_imei.text.toString())
            callGetData()
            dialog.dismiss()
        }}
        dialog.show()
    }

    private fun dialogUpdateFreq(){
        val builder = AlertDialog.Builder(context)
        val view = this.layoutInflater.inflate(R.layout.dialog_update_freq, null)
        builder.setView(view)
        val dialog = builder.create()
        view.btn_update.setOnClickListener {  if (TextUtils.isEmpty(view.input_time.text.toString())){
            showToast(context, "Please enter time in seconds")
        }else if(view.input_time.text.startsWith("0")) {
            showToast(context, "Entered value should be 10 or more")
        }else {
            storePreference(context, PREF_UPDATE_FREQ, view.input_time.text.toString())
            dialog.dismiss()
        }}
        dialog.show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.isTrafficEnabled = false
        mMap.isIndoorEnabled = false
        mMap.isBuildingsEnabled = false
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.setAllGesturesEnabled(true)
        mMap.uiSettings.isZoomGesturesEnabled = true

    }


    private fun callGetData(){
        val data =
            GenericResponse("PK1,869867030076261,2265,2291,2602,0,0,0,0,GNRMC,123519,A,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A")
        EventBus.getDefault().post(data.isShakeDetected)
        digit_speed_view.updateSpeed(data.speed)
        val biker = LatLng(data.latitude, data.longitude)

        val location = Location("Biker")
        location.latitude = data.latitude
        location.longitude = data.longitude
        val title = getLocationDetails(location)

        mMap.uiSettings.setAllGesturesEnabled(true)
        mMap.addMarker(
            MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
                .position(biker)
                .title(title)
        )
        mMap.moveCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                    .target(biker)
                    .zoom(17.0f)
                    .bearing(30.0f)
                    .tilt(45.0f)
                    .build()
            )
        )

        destination = origin
        origin = "${data.latitude},${data.longitude}"

        lastUpdate.text = "Last Updated: ${data.lastUpdate}"
    }/*{
        val apiObserver = ApiObserver(context, object : ServiceCallback<String> {
            override fun onResponse(response: String) {
                if (response.startsWith("200")) {
        val data = GenericResponse("PK1,869867030076261,2265,2291,2602,0,0,0,0,GNRMC,123519,A,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A")
        EventBus.getDefault().post(data.isShakeDetected)
        digit_speed_view.updateSpeed(data.speed)
        val biker = LatLng(data.latitude, data.longitude)

        val location = Location("Biker")
        location.latitude = data.latitude
        location.longitude = data.longitude
        val title = getLocationDetails(location)

        mMap.uiSettings.setAllGesturesEnabled(true)
        mMap.addMarker(MarkerOptions()
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
            .position(biker)
            .title(title)
        )
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
            CameraPosition.Builder()
                .target(biker)
                .zoom(17.0f)
                .bearing(30.0f)
                .tilt(45.0f)
                .build()))

        destination = origin
        origin = "${data.latitude},${data.longitude}"

        lastUpdate.text = "Last Updated: ${data.lastUpdate}"
    }else{
                    EventBus.getDefault().post(response.split("-")[1])
                }
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 30000)
            }

        })
        ApiHelper.callGetData(context, fetchPreference(context, PREF_DEVICE_IMEI)!!, apiObserver)
    }*/

    override fun onBackPressed() {

        //double click back to exit functionality
        if (backPressed) {
            finishAffinity()
        } else {
            backPressed = true
            showToast(context, "Press BACK again to close ${getString(R.string.app_name)}")
            Handler().postDelayed(Runnable { backPressed = false }, 2000)
        }

    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        super.onDestroy()
    }

    private fun getLocationDetails(location: Location): String {
        var address: Address? = null
        try {
            Log.i("LocationTracker", "latitude --> " + location.latitude.toString())
            Log.i("LocationTracker", "longitude --> " + location.longitude.toString())
            val geoCoder = Geocoder(context, Locale.getDefault())
            val addresses = geoCoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses != null && addresses.size > 0) {
                address = addresses[0]
                Log.i("LocationTracker", "address --> " + address!!.toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        var title = "Biker"

        address?.let { title = it.getAddressLine(0) }

        return title
    }
}
