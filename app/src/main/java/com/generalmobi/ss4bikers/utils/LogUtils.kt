package com.generalmobi.ss4bikers.utils

/*
 *  Created by gmobi on 26/3/18.
 *  Copyright  ©  2018 General Mobile Corporation
 *
 *  Last Modified 26/3/18.
 */

import android.util.Log
import com.generalmobi.ss4bikers.BuildConfig


object LogUtils {

    /**
     * Send a VERBOSE log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    fun v(tag: String, msg: String) {
        if (isLoggable(tag, Log.VERBOSE)) {
            println(Log.VERBOSE, tag, msg)
        }
    }

    /**
     * Send a VERBOSE log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    fun v(tag: String, msg: String, tr: Throwable) {
        if (isLoggable(tag, Log.VERBOSE)) {
            println(
                Log.VERBOSE, tag, msg + '\n'.toString()
                        + Log.getStackTraceString(tr)
            )
        }
    }

    /**
     * Send a DEBUG log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    fun d(tag: String, msg: String) {
        if (isLoggable(tag, Log.DEBUG)) {
            println(Log.DEBUG, tag, msg)
        }
    }

    /**
     * Send a DEBUG log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    fun d(tag: String, msg: String, tr: Throwable) {
        if (isLoggable(tag, Log.DEBUG)) {
            println(
                Log.DEBUG, tag, msg + '\n'.toString()
                        + Log.getStackTraceString(tr)
            )
        }
    }

    /**
     * Send an INFO log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    fun i(tag: String, msg: String) {
        if (isLoggable(tag, Log.INFO)) {
            println(Log.INFO, tag, msg)
        }
    }

    /**
     * Send a INFO log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    fun i(tag: String, msg: String, tr: Throwable) {
        if (isLoggable(tag, Log.INFO)) {
            println(
                Log.INFO, tag, msg + '\n'.toString()
                        + Log.getStackTraceString(tr)
            )
        }
    }

    /**
     * Send a WARN log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    fun w(tag: String, msg: String) {
        if (isLoggable(tag, Log.WARN)) {
            println(Log.WARN, tag, msg)
        }
    }

    /**
     * Send a WARN log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    fun w(tag: String, msg: String, tr: Throwable) {
        if (isLoggable(tag, Log.WARN)) {
            println(Log.WARN, tag, msg)
            println(Log.WARN, tag, Log.getStackTraceString(tr))
        }
    }

    /**
     * Send an ERROR log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    fun e(tag: String, msg: String) {
        if (isLoggable(tag, Log.ERROR)) {
            println(Log.ERROR, tag, msg)
        }
    }

    /**
     * Send a ERROR log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    fun e(tag: String, msg: String, tr: Throwable) {
        if (isLoggable(tag, Log.ERROR)) {
            println(Log.ERROR, tag, msg)
            println(Log.ERROR, tag, Log.getStackTraceString(tr))
        }
    }

    /**
     * What a Terrible Failure: Report a condition that should never happen.
     * The error will always be logged at level ASSERT with the call stack.
     * Depending on system configuration, a report may be added to the
     * [android.os.DropBoxManager] and/or the process may be terminated
     * immediately with an error dialog.
     *
     * @param tag Used to identify the source of a log message.
     * @param msg The message you would like logged.
     */
    fun wtf(tag: String, msg: String) {
        // Make sure this goes into our log buffer
        if (isLoggable(tag, Log.ASSERT)) {
            println(Log.ASSERT, tag, "wtf\n$msg")
            Log.wtf(tag, msg, Exception())
        }
    }

    /**
     * What a Terrible Failure: Report a condition that should never happen.
     * The error will always be logged at level ASSERT with the call stack.
     * Depending on system configuration, a report may be added to the
     * [android.os.DropBoxManager] and/or the process may be terminated
     * immediately with an error dialog.
     *
     * @param tag Used to identify the source of a log message.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    fun wtf(tag: String, msg: String, tr: Throwable) {
        // Make sure this goes into our log buffer
        if (isLoggable(tag, Log.ASSERT)) {
            println(
                Log.ASSERT, tag, "wtf\n" + msg + '\n'.toString() +
                        Log.getStackTraceString(tr)
            )
            Log.wtf(tag, msg, tr)
        }
    }

    /**
     * Checks to see whether or not a log for the specified tag is loggable at the specified level.
     * See [android.util.Log.isLoggable] for more discussion.
     */
    private fun isLoggable(tag: String, level: Int): Boolean {
        return Log.isLoggable(tag, level) && BuildConfig.DEBUG
    }

    private fun println(level: Int, tag: String, msg: String) {
        Log.println(level, tag, msg)
    }
}
