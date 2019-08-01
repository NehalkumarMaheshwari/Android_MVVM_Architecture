@file:JvmName("Utilities")

package com.v4u.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.security.Key
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

/**
 * Created by Nehalkumar Maheshwari on 7/2/2019.
 */

/**
 * Extension method to display toast message on your screen
 * @param text CharSequence: The text to show. Can be formatted text.
 * @param duration int: How long to display the message. Either LENGTH_SHORT or LENGTH_LONG
 */
fun Context?.toast(text: CharSequence, duration: Int = Toast.LENGTH_LONG) =
    this?.let { Toast.makeText(it, text, duration).show() }

/**
 * Extension method to display snackbar on your screen
 * For snackbar androidx add dependency "com.google.android.material:material:version"
 * @param message String: The text to show. Can be formatted text.
 * @param duration int: How long to display the message. Either LENGTH_SHORT or LENGTH_LONG
 * @param function Unit: Set the action to be displayed and callback to be invoked when the action is clicked
 */
inline fun View.snack(message: String, duration: Int = Snackbar.LENGTH_LONG, function: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, message, duration)
    snack.function()
    snack.show()
}

/**
 * Snackbar set the action to be displayed
 * @param action String: Text to display for the action
 * @param color Int: Action color
 * @param listener ClickListener: callback to be invoked when the action is clicked
 */
fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(ContextCompat.getColor(context, color)) }
}

/**
 * Check whether network is connected or not
 * @return boolean true if network connectivity exists, false otherwise
 */
fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        (networkCapabilities != null && (
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)))
    } else {
        val networkInfo = connectivityManager.activeNetworkInfo
        (networkInfo != null && networkInfo.isConnected)
    }
}

/**
 * Extension method to provide hide keyboard for Activity.
 */
fun Activity.hideSoftKeyboard() {
    if (currentFocus != null) {
        val inputMethodManager = getSystemService(
            Context
                .INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }
}

/**
 * Extension method to provide hide keyboard for Fragment.
 */
fun Fragment.hideSoftKeyboard() {
    activity?.hideSoftKeyboard()
}

/**
 * Extension method to get formatted current date.
 * @param format date format. For example dd/MM/yyyy
 * @return String specified format string date
 */
fun currentDateInFormat(format: String): String? {
    val dateFormat = SimpleDateFormat(format, Locale.US)
    return dateFormat.format(Date())
}

/**
 * Extension method to convert date to String with specified format.
 * @param format date format. For example dd/MM/yyyy
 * @return String Converted date with specified format
 */
@SuppressLint("SimpleDateFormat")
fun Date.dateToString(format: String): String {
    return SimpleDateFormat(format).format(this)
}

/**
 * Extension method to convert string date to Date with appropriate format.
 * @param currentFormat date format. For example date is 22/07/2019 so you have pass this format dd/MM/yyyy
 * @return Date Convert string date to Date
 */
@SuppressLint("SimpleDateFormat")
fun String.stringToDate(currentFormat: String): Date {
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    return SimpleDateFormat(currentFormat).parse(this)
}

/**
 * Extension method to find a device width in pixels
 * @return int device width in pixels
 */
inline val Context.displayWidth: Int
    get() = resources.displayMetrics.widthPixels

/**
 * Extension method to find a device height in pixels
 * @return int device height in pixels
 */
inline val Context.displayHeight: Int
    get() = resources.displayMetrics.heightPixels

/**
 * Extension method to check if String is Phone Number.
 * @return boolean true if string is Phone Number, false otherwise
 */
fun String.isValidPhoneNumber(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.PHONE.matcher(this).matches()
}

/**
 * Extension method to check if email is valid or not.
 * @return boolean true if string is valid email, false otherwise
 */
fun String.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

/**
 * Extension method to build a validator using user input to check password is valid or not.
 *
 * @param forceLetter True if you required at least one letter
 * @param forceSpecialChar True if you required at least one special character
 * @param forceCapitalLetter True if you required at least one uppercase letter
 * @param forceNumber True if you required at least one number
 * @param minLength Minimum length off password
 * @param maxLength Maximum length off password
 * @return boolean True if password is valid according user custom regex , false otherwise
 */
fun String.isValidPassword(forceLetter: Boolean, forceSpecialChar: Boolean, forceCapitalLetter: Boolean,
    forceNumber: Boolean, minLength: Int, maxLength: Int): Boolean {

    val patternBuilder = StringBuilder("^")
    val optionRegex = StringBuilder("[")

    if (forceLetter) {
        patternBuilder.append("(?=.*[a-z])")
        optionRegex.append("a-z")
    }

    if (forceCapitalLetter) {
        patternBuilder.append("(?=.*[A-Z])")
        optionRegex.append("A-Z")
    }

    if (forceNumber) {
        patternBuilder.append("(?=.*\\d)")
        optionRegex.append("\\d")
    }

    if (forceSpecialChar) {
        patternBuilder.append("(?=.*[_#@$!%*?&])")
        optionRegex.append("_#@$!%*?&")
    }

    patternBuilder.append("$optionRegex]")
    patternBuilder.append("{$minLength,$maxLength}$")

    val p = patternBuilder.toString().toRegex()
    return matches(p)
}

/**
 * Extension method to check if String is Number.
 * @return boolean true if string is Number, false otherwise
 */
fun String.isNumeric(): Boolean {
    val p = "^[0-9]+$".toRegex()
    return matches(p)
}

/**
 * Extension method to int time to 2 digit String
 * @return String 2 digit String if time is less then 10, return same otherwise
 */
fun Int.twoDigitTime() = if (this < 10) "0" + toString() else toString()

/**
 * Extension method to get SpannableString with Bold text from start to end.
 * @param start Text start value
 * @param end Text end value
 * @return SpannableString bold SpannableString
 */
fun String.spannableBold(start: Int, end: Int): SpannableString {
    val sp = SpannableString(this)
    sp.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
    return sp
}

/**
 * Extension method to get SpannableString with text with color resource from start to end.
 * @param colorId Text color value
 * @param start Text start value
 * @param end Text end value
 * @return SpannableString colored SpannableString
 */
fun String.spannableColor(colorId: Int, start: Int, end: Int): SpannableString {
    val sp = SpannableString(this)
    sp.setSpan(ForegroundColorSpan(colorId), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return sp
}

/**
 * This view is visible
 */
fun View.visible() {
    this.visibility = View.VISIBLE
}

/**
 * This view is invisible
 */
fun View.invisible() {
    this.visibility = View.INVISIBLE
}

/**
 * This view is gone
 */
fun View.gone() {
    this.visibility = View.GONE
}

/**
 * Extension method to inflate layout for ViewGroup.
 * @param layoutID Layout name use for inflating
 * @return View Inflated layout
 */
fun ViewGroup.inflate(layoutID: Int): View? {
    return LayoutInflater.from(context).inflate(layoutID, this, false)
}

/**
 * Extension method to convert int value to string
 * @return String Int value convert into String
 */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
fun Int.toString(): String {
    return String.format("%d", this)
}

/**
 * Extension method to convert float value to string
 * @return String Float value convert into String
 */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
fun Float.toString(): String {
    return String.format("%.2f", this)
}

/**
 * Extension method to convert double value to string
 * @return String Double value convert into String
 */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
fun Double.toString(): String {
    return String.format("%.2f", this)
}

/**
 * Extension method to convert String value to int
 * @return Int If provided string is numeric otherwise
 * it will return string which you have passed
 */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
fun String.toInt(): Any {
    return if (this.isNumeric()) {
        Integer.parseInt(this)
    } else {
        this
    }
}

/**
 * Converts a number to a string in cool format.
 * For example, 7800000 will be formatted as '7.8M'.
 * Numbers under 1000 will be unchanged.
 * Refer to the tests for further examples.
 * @return String Converted number in string cool format
 */
fun Number.coolFormat(): String {
    val suffix = charArrayOf(' ', 'k', 'M', 'B', 'T', 'P', 'E')
    val numValue = this.toLong()
    val value = floor(log10(numValue.toDouble())).toInt()
    val base = value / 3

    return if (value >= 3 && base < suffix.size) {
        DecimalFormat("#0.00").format(numValue / 10.0.pow((base * 3.0))) + suffix[base]
    } else {
        DecimalFormat("#,##0").format(numValue)
    }
}

/**
 * Open your play store app
 */
fun Context.openPlayStore() {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
    } catch (anfe: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }
}

/**
 * Gets the version name of the application. For e.g. 1.9.3
 * @return String Application version name
 */
fun Context.getApplicationVersionNumber(): String {
    return packageManager.getPackageInfo(packageName, 0).versionName
}

/**
 * Shares an application over the social network like Facebook, Twitter etc.
 *
 * @param title title of the sharing options prompt. For e.g. "Share via" or "Share using"
 * @param msg   Message to be pre-populated when the 3rd party app dialog opens up.
 * @param subject Message that shows up as a subject while sharing through email.
 */
fun Context.share(title: String, msg: String, subject: String) {
    val sharingIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, msg)
        putExtra(Intent.EXTRA_SUBJECT, subject)
    }
    this.startActivity(Intent.createChooser(sharingIntent, title))
}

/**
 *The string is decrypted according to the specified key and algorithm.
 *
 * @param key Use same key which you have used for encryption.
 * @param algorithm Use same algorithm  which you have used for encryption.
 * @param initVector IV must be specified in CBC mode.
 * @return String The result of the decryption. It is recreated as a String object by the decrypted byteArray.
 * If the decryption fails, it will return null.
 */
fun String.decrypt(key: Key, algorithm: String, initVector: Boolean): String {
    val cipher: Cipher = Cipher.getInstance(algorithm).apply {
        if (initVector) {
            init(Cipher.DECRYPT_MODE, key, IvParameterSpec(ByteArray(16)))
        } else {
            init(Cipher.DECRYPT_MODE, key)
        }
    }
    return String(cipher.doFinal(Base64.decode(this.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)))
}

/**
 * Reversible encryption of the specified string based on the specified key and algorithm.
 *
 * @param key key.
 * @param algorithm algorithm.
 * @param initVector IV must be specified in CBC mode
 * @return String The encrypted result will be converted from a byteArray to an array of hexadecimal representations.
 * If the encryption process fails, it will return null.
 */
fun String.encrypt(key: Key, algorithm: String, initVector: Boolean): String {
    val cipher: Cipher = Cipher.getInstance(algorithm).apply {
        if (initVector) {
            init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(ByteArray(16)))
        } else {
            init(Cipher.ENCRYPT_MODE, key)
        }
    }
    return Base64.encodeToString(cipher.doFinal(this.toByteArray(Charsets.UTF_8)), Base64.NO_WRAP)
}

/**
 * Decrypt encoded text by AES-128/256-CBC algorithm
 *
 * @param secretKey 16/32 -characters secret password. 16 for 128 and 32 for 256 AES algorithm
 * @return String The result of the decryption. It is recreated as a String object by the decrypted byteArray.
 * If the decryption fails, it will return null.
 */
fun String.decryptAES(secretKey: String): String {
    if (secretKey.length == 16 || secretKey.length == 32) {
        val secretKeySpec = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), "AES")
        return this.decrypt(secretKeySpec, "AES/CBC/PKCS5Padding", true)
    } else {
        throw Exception("Secret key's length must be 128")
    }
}

/**
 * Encrypt input text by AES-128/256-CBC algorithm
 *
 * @param secretKey 16/32 -characters secret password. 16 for 128 and 32 for 256 AES algorithm
 * @return Encoded string or NULL if error
 */
fun String.encryptAES(secretKey: String): String {
    if (secretKey.length == 16 || secretKey.length == 32) {
        val secretKeySpec = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), "AES")
        return this.encrypt(secretKeySpec, "AES/CBC/PKCS5Padding", true)
    } else {
        throw Exception("Secret key's length must be 128")
    }
}

/**
 * Convert from dp unit to px (pixel) according to the resolution of the phone
 *
 * @param dpValue size dip
 * @return Int pixel value
 */
fun Context.dip2px(dpValue: Float): Int {
    val scale = this.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

/**
 * Convert from px (pixels) to dp depending on the resolution of the phone
 *
 * @param pxValue size pixels
 * @return Int DIP value
 */
fun Context.px2dip(pxValue: Float): Int {
    val scale = this.resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

/**
 * Extension method to deserializes the specified Json into an object of the specified class.
 * @param classOfT the class of T
 * @return an object of type class of T from the string.
 */
fun <T> String.fromJson(classOfT:  Class<T> ): T {
    return Gson().fromJson(this, classOfT)
}
