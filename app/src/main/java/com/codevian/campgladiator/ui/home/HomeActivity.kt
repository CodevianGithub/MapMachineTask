package com.codevian.campgladiator.ui.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.codevian.campgladiator.R
import com.codevian.campgladiator.models.PlacesDetails
import com.codevian.campgladiator.rest.ApiClient
import com.codevian.campgladiator.utils.ProgressDialog
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class HomeActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {


    var mMap: GoogleMap? = null
    var mLastLocation: Location? = null
    var mCurrLocationMarker: Marker? = null
    var mGoogleApiClient: GoogleApiClient? = null
    var mLocationRequest: LocationRequest? = null


    val AUTOCOMPLETE_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val fields: List<Place.Field> =
            Arrays.asList(Place.Field.LAT_LNG, Place.Field.ID, Place.Field.NAME)


        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)


        etPlace.setOnClickListener {
            val intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
            ).build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode === Activity.RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(data!!)

            val lat = place.latLng?.latitude
            val lng = place.latLng?.longitude
            etPlace.setText(place.name)

            if(lat!=null && lng!=null){
                getPlaces(lat.toString(), lng.toString(), "50")
            }else {
                Toast.makeText(this,"latitude & longitude not availble for this place",Toast.LENGTH_SHORT).show()
            }


        } else if (resultCode === AutocompleteActivity.RESULT_ERROR) { // TODO: Handle the error.
            val status: Status = Autocomplete.getStatusFromIntent(data!!)

        } else if (resultCode === Activity.RESULT_CANCELED) { // The user canceled the operation.
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                buildGoogleApiClient()
                mMap?.setMyLocationEnabled(true)
            }
        } else {
            buildGoogleApiClient()
            mMap?.setMyLocationEnabled(true)
        }
    }


    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient?.connect()
    }

    override fun onLocationChanged(location: Location?) {

        mLastLocation = location
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker?.remove()
        }
        setMarker(location?.latitude!!,location?.longitude!!,"Current location")
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
        }

    }

    override fun onConnected(p0: Bundle?) {

        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 1000
        mLocationRequest!!.setFastestInterval(1000)
        mLocationRequest!!.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
            )
        }

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }


    private fun getPlaces(lat: String, lng: String, radius: String) {

        ProgressDialog.getInstance().show(this)

        val call = ApiClient.create().getPlacesDetails(lat, lng, radius)

        call.enqueue(object : Callback<PlacesDetails> {
            override fun onFailure(call: Call<PlacesDetails>?, t: Throwable?) {
                ProgressDialog.getInstance().dismiss()
                Toast.makeText(this@HomeActivity, t?.message.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<PlacesDetails>?,
                response: Response<PlacesDetails>?
            ) {

                ProgressDialog.getInstance().dismiss()
                if (response?.body()?.data?.isNotEmpty()!!) {

                    for (place in response.body().data) {

                        setMarker(place.placeLatitude.toDouble(),place.placeLongitude.toDouble(),place.placeName)

                    }




                } else {
                    Toast.makeText(this@HomeActivity, "No results found", Toast.LENGTH_SHORT).show()
                }

            }
        })
    }


    private fun setMarker(lat: Double, lng: Double, title: String){
        val latLng = LatLng(lat,lng)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title(title)
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        mCurrLocationMarker = mMap?.addMarker(markerOptions)
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap?.animateCamera(CameraUpdateFactory.zoomTo(5F))
    }
}
