package com.generalmobi.ss4bikers.service

import android.text.TextUtils
import java.lang.Exception
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Packet_No.1:- PK1,IMEINumber,X-Direction_value,Y-Direction_value,Z-Direction_value,Instant_X_change_flag,Instant_Y_change_flag,Instant_Z_change_flag, Overall_dir_changeflag, GPSData
 * PK1,869867030076261,2265,2291,2602,0,0,0,0,GNRMC,123519,A,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A
 *
 * GPSData:
 * RMC          Recommended Minimum sentence C
 * 123519       Fix taken at 12:35:19 UTC
 * A            Status A=active or V=Void.
 * 4807.038,N   Latitude 48 deg 07.038' N
 * 01131.000,E  Longitude 11 deg 31.000' E
 * 022.4        Speed over the ground in knots
 * 084.4        Track angle in degrees True
 * 230394       Date - 23rd of March 1994
 * 003.1,W      Magnetic Variation
 * 6A          The checksum data, always begins with *
 *
 * Packet_No.2:- PK2,IMEINumber,X-Direction_value,Y-Direction_value,Z-Direction_value,Instant_X_change_flag,Instant_Y_change_flag,Instant_Z_change_flag, Overall_dir_changeflag, Longitude_String, Latitude_String, LBS_Date, LBS_Time
 * PK2,869867035452558,2170,2101,2643,0,0,0,0,77.300361,28.541914,141019,110946
 */
class GenericResponse{

    var lastUpdate: String? = ""
    var IMEINumber: String? = ""
    var speed: Int = 0
    var latitude: Double = 28.6139
    var longitude: Double = 77.2090
    var isShakeDetected: Boolean? = false
    private val DMS_PATTERN = Pattern.compile(
        "(-?)([0-9]{1,2})([0-9]{2}\\.[0-9]{1,4})([NS])_(-?)([0-9]{1,3})([0-9]{2}\\.[0-9]{1,4})([EW])"
    )

    constructor()

    constructor(data: String) {
        if (TextUtils.isEmpty(data))
            return

        val dataArray = data.split(",")
        val updateArray = data.split("|")

        lastUpdate = updateArray.elementAtOrNull(1)?.trim()
        IMEINumber = dataArray.elementAtOrNull(1)?.trim()
        val x = dataArray.elementAtOrNull(5)?.trim()
        val y = dataArray.elementAtOrNull(6)?.trim()
        val z = dataArray.elementAtOrNull(7)?.trim()

        isShakeDetected = ((x=="1"||y=="1") || (x=="0"&& y=="0"&& z=="0"))

        when {
            data.startsWith("200-PK1") -> {
                val dms = "${dataArray.elementAtOrNull(12)?.trim()}" +
                        "${dataArray.elementAtOrNull(13)?.trim()}" +
                        "_" +
                        "${dataArray.elementAtOrNull(14)?.trim()}" +
                        "${dataArray.elementAtOrNull(15)?.trim()}"

                try{
                val latLon = convertToDecimal(dms)
                latitude = latLon[0]
                longitude = latLon[1]
                }catch (e:Exception){
                    e.printStackTrace()
                }

                dataArray.elementAtOrNull(16)?.let {
                    val knotSpeed = it.trim().toDouble()
                    speed = (knotSpeed * 1.852).roundToInt()
                }

            }

            data.startsWith("200-PK2") -> {
                dataArray.elementAtOrNull(9)?.let {
                    longitude = it.trim().toDouble()
                }

                dataArray.elementAtOrNull(10)?.let {
                    latitude = it.trim().toDouble()
                }
            }
        }
    }

    private fun toDouble(m: Matcher, offset: Int): Double {
        val sign = if (m.group(1 + offset) == "") 1 else -1
        val degrees = (m.group(2 + offset)).toDouble()
        val minutes = (m.group(3 + offset)).toDouble()

        return sign * (degrees + minutes / 60)
    }

    private fun convertToDecimal(dms: String): DoubleArray {
        val m = DMS_PATTERN.matcher(dms.trim())

        if (m.matches()) {
            val latitude = toDouble(m, 0)
            val longitude = toDouble(m, 4)

            if (abs(latitude) > 90 || abs(longitude) > 180) {
                throw NumberFormatException("Invalid latitude or longitude")
            }

            return doubleArrayOf(latitude, longitude)
        } else {
            throw NumberFormatException(
                "Malformed degrees/minutes/seconds/direction coordinates"
            )
        }
    }
}
