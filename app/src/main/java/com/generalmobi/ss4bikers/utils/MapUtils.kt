//package com.generalmobi.ss4bikers.utils
//
//import android.R
//import android.animation.ValueAnimator
//import android.os.Handler
//import android.view.animation.LinearInterpolator
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.model.*
//import kotlin.math.abs
//import okhttp3.Route
//
//
//
//
///*
// *  Created by gmobi on 8/11/19.
// *  Copyright (c) 2019 General Mobile Technology India Pvt Ltd. All rights reserved.
// */
//
//private var polyLineList: List<LatLng>? = null
//private var marker: Marker? = null
//private var v: Float = 0.toFloat()
//private var lat: Double = 0.toDouble()
//private var lng: Double = 0.toDouble()
//private var handler = Handler()
//private var startPosition: LatLng? = null
//private var endPosition: LatLng? = null
//private var index: Int = 0
//private var next: Int = 0
//private val sydney: LatLng? = null
//private val blackPolyline: Polyline? = null
//private val greyPolyLine: Polyline? = null
//
//fun drawPolyLineAndAnimateCar(mMap: GoogleMap) {
//    //Adjusting bounds
//    val builder = LatLngBounds.Builder()
//    if (polyLineList != null) {
//        for (latLng in polyLineList) {
//            builder.include(latLng)
//        }
//    }else{
//        return
//    }
//    val bounds = builder.build()
//    val mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2)
//    mMap.animateCamera(mCameraUpdate)
//
//    mMap.addMarker(
//        MarkerOptions()
//            .position(polyLineList.get(polyLineList.size - 1))
//    )
//
//    val polylineAnimator = ValueAnimator.ofInt(0, 100)
//    polylineAnimator.duration = 2000
//    polylineAnimator.interpolator = LinearInterpolator()
//    polylineAnimator.addUpdateListener { valueAnimator ->
//        val points = greyPolyLine?.points
//        val percentValue = valueAnimator.animatedValue as Int
//        val size = points!!.size
//        val newPoints = (size * (percentValue / 100.0f)).toInt()
//        val p = points.subList(0, newPoints)
//        blackPolyline!!.points = p
//    }
//    polylineAnimator.start()
//    marker = mMap.addMarker(
//        MarkerOptions().position(sydney)
//            .flat(true)
//            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
//    )
//    index = -1
//    next = 1
//    handler = Handler()
//    handler.postDelayed(object : Runnable {
//        override fun run() {
//            if (index < polyLineList.size - 1) {
//                index++
//                next = index + 1
//            }
//            if (index < polyLineList.size - 1) {
//                startPosition = polyLineList.get(index)
//                endPosition = polyLineList.get(next)
//            }
////            if (index === 0) {
////                val beginJourneyEvent = BeginJourneyEvent()
////                beginJourneyEvent.setBeginLatLng(startPosition)
////                JourneyEventBus.getInstance().setOnJourneyBegin(beginJourneyEvent)
////            }
////            if (index === polyLineList.size() - 1) {
////                val endJourneyEvent = EndJourneyEvent()
////                endJourneyEvent.setEndJourneyLatLng(
////                    LatLng(
////                        polyLineList.get(index).latitude,
////                        polyLineList.get(index).longitude
////                    )
////                )
////                JourneyEventBus.getInstance().setOnJourneyEnd(endJourneyEvent)
////            }
//            val valueAnimator = ValueAnimator.ofInt(0, 1)
//            valueAnimator.duration = 3000
//            valueAnimator.interpolator = LinearInterpolator()
//            valueAnimator.addUpdateListener { valueAnimator ->
//                v = valueAnimator.animatedFraction
//                lng = v * endPosition!!.longitude + (1 - v) * startPosition!!.longitude
//                lat = v * endPosition!!.latitude + (1 - v) * startPosition!!.latitude
//                val newPos = LatLng(lat, lng)
////                val currentJourneyEvent = CurrentJourneyEvent()
////                currentJourneyEvent.setCurrentLatLng(newPos)
////                JourneyEventBus.getInstance().setOnJourneyUpdate(currentJourneyEvent)
//                marker.setPosition(newPos)
//                marker.setAnchor(0.5f, 0.5f)
//                marker.setRotation(getBearing(startPosition!!, newPos))
//                mMap.animateCamera(
//                    CameraUpdateFactory.newCameraPosition(
//                        CameraPosition.Builder().target(newPos)
//                            .zoom(15.5f).build()
//                    )
//                )
//            }
//            valueAnimator.start()
//            if (index != polyLineList.size - 1) {
//                handler.postDelayed(this, 3000)
//            }
//        }
//    }, 3000)
//}
//
//
//fun decodePoly(encoded: String): List<LatLng> {
//    val poly = ArrayList<LatLng>()
//    var index = 0
//    val len = encoded.length
//    var lat = 0
//    var lng = 0
//
//    while (index < len) {
//        var b: Int
//        var shift = 0
//        var result = 0
//        do {
//            b = encoded[index++].toInt() - 63
//            result = result or (b and 0x1f shl shift)
//            shift += 5
//        } while (b >= 0x20)
//        val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
//        lat += dlat
//
//        shift = 0
//        result = 0
//        do {
//            b = encoded[index++].toInt() - 63
//            result = result or (b and 0x1f shl shift)
//            shift += 5
//        } while (b >= 0x20)
//        val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
//        lng += dlng
//
//        val p = LatLng(
//            lat.toDouble() / 1E5,
//            lng.toDouble() / 1E5
//        )
//        poly.add(p)
//    }
//
//    return poly
//}
//
//fun getBearing(begin: LatLng, end: LatLng): Float {
//    val lat = abs(begin.latitude - end.latitude)
//    val lng = abs(begin.longitude - end.longitude)
//
//    if (begin.latitude < end.latitude && begin.longitude < end.longitude)
//        return Math.toDegrees(Math.atan(lng / lat)).toFloat()
//    else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
//        return (90 - Math.toDegrees(Math.atan(lng / lat)) + 90).toFloat()
//    else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
//        return (Math.toDegrees(Math.atan(lng / lat)) + 180).toFloat()
//    else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
//        return (90 - Math.toDegrees(Math.atan(lng / lat)) + 270).toFloat()
//    return -1f
//}