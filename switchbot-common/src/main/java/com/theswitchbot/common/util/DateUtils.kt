package com.theswitchbot.common.util

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.theswitchbot.common.CommonApp
import com.theswitchbot.common.R
import java.nio.ByteBuffer
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 日期操作工具类
 * @version  1.0
 */
@SuppressLint("SimpleDateFormat")
object DateUtils {
    private const val FORMAT = "yyyy-MM-dd HH:mm:ss"
    private val datetimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    private val timeFormat = SimpleDateFormat("HH:mm:ss")
    val hourMinFormat = SimpleDateFormat("HH:mm")
    const val oneDayMilliseconds = 24 * 60 * 60 * 1000L
    /**
     * 字符串转时间
     * @param str
     * @param format
     * @return
     */
    @JvmOverloads
    fun str2Date(str: String?, format: String? = null): Date? {
        var format = format
        if (str == null || str.length == 0) {
            return null
        }
        if (format == null || format.length == 0) {
            format = FORMAT
        }
        var date: Date? = null
        try {
            val sdf = SimpleDateFormat(format)
            date = sdf.parse(str)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return date
    }

    @JvmOverloads
    fun str2Calendar(str: String?, format: String? = null): Calendar? {
        val date = str2Date(str, format) ?: return null
        val c = Calendar.getInstance()
        c.time = date
        return c
    }

    @JvmOverloads
    fun date2Str(c: Calendar?, format: String? = null): String? {
        return if (c == null) {
            null
        } else date2Str(c.time, format)
    }

    @JvmOverloads
    fun date2Str(d: Date?, format: String? = null): String? { // yyyy-MM-dd HH:mm:ss
        var format = format
        if (d == null) {
            return null
        }
        if (format == null || format.length == 0) {
            format = FORMAT
        }
        val sdf = SimpleDateFormat(format)
        return sdf.format(d)
    }

    val curDateStr: String
        get() {
            val c = Calendar.getInstance()
            c.time = Date()
            return (c[Calendar.YEAR].toString() + "-" + (c[Calendar.MONTH] + 1) + "-" + c[Calendar.DAY_OF_MONTH] + "-"
                    + c[Calendar.HOUR_OF_DAY] + ":" + c[Calendar.MINUTE] + ":" + c[Calendar.SECOND])
        }

    /**
     * 获得当前日期的字符串格式
     *
     * @param format
     * @return
     */
    fun getCurDateStr(format: String?): String? {
        val c = Calendar.getInstance()
        return date2Str(c, format)
    }

    /**
     * 格式到秒
     *
     * @param time
     * @return
     */
    fun getMillon(time: Long): String {
        return SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(time)
    }

    /**
     * 格式到天
     *
     * @param time
     * @return
     */
    fun getDay(time: Long): String {
        return SimpleDateFormat("yyyy-MM-dd").format(time)
    }

     fun parseDate(timestamp:Long):String{
        val thisYear = Calendar.getInstance().also { it.time = Date() }[Calendar.YEAR]
        val dataCalendar=Calendar.getInstance().also { it.time = Date(timestamp) }

        val format = if(thisYear==dataCalendar[Calendar.YEAR]){
            SimpleDateFormat("MMM dd (EEEE)",LocaleUtil.getLocale())
        }else{
            SimpleDateFormat("yyyy MMM dd (EEEE)",LocaleUtil.getLocale())
        }
        val date = Date(timestamp)
        return format.format(date)
    }

    /**
     * 格式到毫秒
     *
     * @param time
     * @return
     */
    fun getSMillon(time: Long): String {
        return SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(time)
    }

    /**
     * 格式到毫秒
     *
     * @param time
     * @return
     */
    fun getHMS(time: Long): String {
        val localFormater = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        localFormater.timeZone = TimeZone.getTimeZone("UTC")
        return localFormater.format(time)
    }

    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    fun timeStamp2Date(seconds: String?, format: String?): String {
        var format = format
        if (seconds == null || seconds.isEmpty() || seconds == "null") {
            return ""
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss"
        }
        val sdf = SimpleDateFormat(format)
        return sdf.format(Date(java.lang.Long.valueOf(seconds + "000")))
    }

    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    fun timeStampToHM(seconds: String?, format: String?): String {
        var format = format
        if (seconds == null || seconds.isEmpty() || seconds == "null") {
            return ""
        }
        var curDate = Date(java.lang.Long.valueOf(seconds + "000"))
        if (format == null || format.isEmpty()) {
            var is24Time = getTime_12_24(CommonApp.instance)
            if (is24Time){
                format = "HH:mm"
                var sdf = SimpleDateFormat(format)
                return sdf.format(curDate)
            }else{
                var curMinute = if(curDate.minutes<10){
                    "0"+curDate.minutes.toString()
                }else{
                    curDate.minutes
                }
                if (curDate.hours > 12){
                    return String.format(CommonApp.instance.getString(R.string.timer_pm_string),(curDate.hours - 12).toString()+":"+curMinute)
                }else{
                    return String.format(CommonApp.instance.getString(R.string.timer_am_string),curDate.hours.toString()+":"+curMinute)
                }
            }
        }
        var sdf = SimpleDateFormat(format)
        return sdf.format(curDate)
    }

    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到分钟的字符串（格式：yyyy-MM-dd HH:mm）
     * @param format
     * @return
     */
    @JvmStatic
    fun timeStampToYHM(seconds: String?, format: String?): String {
        var format = format
        if (seconds == null || seconds.isEmpty() || seconds == "null") {
            return ""
        }

        var curDate = Date(java.lang.Long.valueOf(seconds + "000"))
        if (format == null || format.isEmpty()) {
            var is24Time = getTime_12_24(CommonApp.instance)
            if (is24Time){
                format = "yyyy-MM-dd HH:mm"
                var sdf = SimpleDateFormat(format)
                return sdf.format(curDate)
            }else{
                var curMinute = if(curDate.minutes<10){
                    "0"+curDate.minutes.toString()
                }else{
                    curDate.minutes
                }
                var curFormat = "yyyy-MM-dd"
                var curYmd = SimpleDateFormat(curFormat).format(curDate)
                if (curDate.hours > 12){
                    return curYmd + " " +String.format(CommonApp.instance.getString(R.string.timer_pm_string),(curDate.hours - 12).toString()+":"+curMinute)
                }else{
                    return curYmd + " " +String.format(CommonApp.instance.getString(R.string.timer_am_string),curDate.hours.toString()+":"+curMinute)
                }
            }
        }
        return SimpleDateFormat(format).format(curDate)
    }

    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到分钟的字符串（格式：yyyy/MM/dd HH:mm）
     * @param format
     * @return
     */
    fun timeStampToType2YHM(seconds: String?, format: String?): String {
        var format = format
        if (seconds == null || seconds.isEmpty() || seconds == "null") {
            return ""
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy/MM/dd HH:mm"
        }
        val sdf = SimpleDateFormat(format)
        return sdf.format(Date(java.lang.Long.valueOf(seconds + "000")))
    }

    /**
     * 日期格式字符串转换成时间戳
     * @param date_str 字符串日期
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    fun date2TimeStamp(date_str: String?, format: String?): String {
        try {
            val sdf = SimpleDateFormat(format)
            return (sdf.parse(date_str).time / 1000).toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 取得当前时间戳（精确到秒）
     * @return
     */
    fun timeStamp(): String {
        val time = System.currentTimeMillis()
        return (time / 1000).toString()
    }

    fun utc2Local(
        utcTime: String?, utcTimePatten: String?,
        localTimePatten: String?
    ): String {
        val utcFormater = SimpleDateFormat(utcTimePatten)
        utcFormater.timeZone = TimeZone.getTimeZone("UTC")
        var gpsUTCDate: Date? = null
        try {
            gpsUTCDate = utcFormater.parse(utcTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val localFormater = SimpleDateFormat(localTimePatten)
        localFormater.timeZone = TimeZone.getDefault()
        return localFormater.format(gpsUTCDate!!.time)
    }

    /**
     * 字符串转换到时间格式
     *
     * @param dateStr
     * 需要转换的字符串
     * @param formatStr
     * 需要格式的目标字符串 举例 yyyy-MM-dd
     * @return Date 返回转换后的时间
     * @throws ParseException
     * 转换异常
     */
    fun StringToDate(dateStr: String?, formatStr: String?): Date? {
        val sdf: DateFormat = SimpleDateFormat(formatStr)
        var date: Date? = null
        try {
            date = sdf.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }


    /**
     * 把字符串转化为时间格式
     *
     * @param timestamp
     * @return
     */
    fun getStandardTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("MM月dd日 HH:mm")
        val date = Date(timestamp * 1000)
        sdf.format(date)
        return sdf.format(date)
    }

    /**
     * 获得当前日期时间 日期时间格式yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    fun currentDatetime(): String {
        return datetimeFormat.format(now())
    }

    /**
     * 格式化日期时间 日期时间格式yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    fun formatDatetime(date: Date?): String {
        return datetimeFormat.format(date)
    }

    /**
     * 获得当前时间 时间格式HH:mm:ss
     *
     * @return
     */
    fun currentTime(): String {
        return timeFormat.format(now())
    }

    /**
     * 格式化时间 时间格式HH:mm:ss
     *
     * @return
     */
    fun formatTime(date: Date?): String {
        return timeFormat.format(date)
    }

    /**
     * 获得当前时间的`java.util.Date`对象
     *
     * @return
     */
    fun now(): Date {
        return Date()
    }

    fun calendar(): Calendar {
        val cal = GregorianCalendar.getInstance(Locale.CHINESE)
        cal.firstDayOfWeek = Calendar.MONDAY
        return cal
    }

    /**
     * 获得当前时间的毫秒数
     *
     * 详见[System.currentTimeMillis]
     *
     * @return
     */
    fun millis(): Long {
        return System.currentTimeMillis()
    }

    /**
     *
     * 获得当前Chinese月份
     *
     * @return
     */
    fun month(): Int {
        return calendar()[Calendar.MONTH] + 1
    }

    /**
     * 获得月份中的第几天
     *
     * @return
     */
    fun dayOfMonth(): Int {
        return calendar()[Calendar.DAY_OF_MONTH]
    }

    /**
     * 今天是星期的第几天
     *
     * @return
     */
    fun dayOfWeek(): Int {
        return calendar()[Calendar.DAY_OF_WEEK]
    }

    /**
     * 今天是年中的第几天
     *
     * @return
     */
    fun dayOfYear(): Int {
        return calendar()[Calendar.DAY_OF_YEAR]
    }

    /**
     * 判断原日期是否在目标日期之前
     *
     * @param src
     * @param dst
     * @return
     */
    fun isBefore(src: Date, dst: Date?): Boolean {
        return src.before(dst)
    }

    /**
     * 判断原日期是否在目标日期之后
     *
     * @param src
     * @param dst
     * @return
     */
    fun isAfter(src: Date, dst: Date?): Boolean {
        return src.after(dst)
    }

    /**
     * 判断两日期是否相同
     *
     * @param date1
     * @param date2
     * @return
     */
    fun isEqual(date1: Date, date2: Date?): Boolean {
        return date1.compareTo(date2) == 0
    }

    /**
     * 判断某个日期是否在某个日期范围
     *
     * @param beginDate
     * 日期范围开始
     * @param endDate
     * 日期范围结束
     * @param src
     * 需要判断的日期
     * @return
     */
    fun between(beginDate: Date, endDate: Date, src: Date?): Boolean {
        return beginDate.before(src) && endDate.after(src)
    }

    /**
     * 获得当前月的最后一天
     *
     * HH:mm:ss为0，毫秒为999
     *
     * @return
     */
    fun lastDayOfMonth(): Date {
        val cal = calendar()
        cal[Calendar.DAY_OF_MONTH] = 0 // M月置零
        cal[Calendar.HOUR_OF_DAY] = 0 // H置零
        cal[Calendar.MINUTE] = 0 // m置零
        cal[Calendar.SECOND] = 0 // s置零
        cal[Calendar.MILLISECOND] = 0 // S置零
        cal[Calendar.MONTH] = cal[Calendar.MONTH] + 1 // 月份+1
        cal[Calendar.MILLISECOND] = -1 // 毫秒-1
        return cal.time
    }

    /**
     * 获得当前月的第一天
     *
     * HH:mm:ss SS为零
     *
     * @return
     */
    fun firstDayOfMonth(): Date {
        val cal = calendar()
        cal[Calendar.DAY_OF_MONTH] = 1 // M月置1
        cal[Calendar.HOUR_OF_DAY] = 0 // H置零
        cal[Calendar.MINUTE] = 0 // m置零
        cal[Calendar.SECOND] = 0 // s置零
        cal[Calendar.MILLISECOND] = 0 // S置零
        return cal.time
    }

    private fun weekDay(week: Int): Date {
        val cal = calendar()
        cal[Calendar.DAY_OF_WEEK] = week
        return cal.time
    }

    /**
     * 获得周五日期
     *
     * 注：日历工厂方法[.calendar]设置类每个星期的第一天为Monday，US等每星期第一天为sunday
     *
     * @return
     */
    fun friday(): Date {
        return weekDay(Calendar.FRIDAY)
    }

    /**
     * 获得周六日期
     *
     * 注：日历工厂方法[.calendar]设置类每个星期的第一天为Monday，US等每星期第一天为sunday
     *
     * @return
     */
    fun saturday(): Date {
        return weekDay(Calendar.SATURDAY)
    }

    /**
     * 获得周日日期 注：日历工厂方法[.calendar]设置类每个星期的第一天为Monday，US等每星期第一天为sunday
     *
     * @return
     */
    fun sunday(): Date {
        return weekDay(Calendar.SUNDAY)
    }

    /**
     * 将字符串日期时间转换成java.util.Date类型 日期时间格式yyyy-MM-dd HH:mm:ss
     *
     * @param datetime
     * @return
     */
    @Throws(ParseException::class)
    fun parseDatetime(datetime: String?): Date {
        return datetimeFormat.parse(datetime)
    }

    /**
     * 将字符串日期转换成java.util.Date类型 日期时间格式yyyy-MM-dd
     *
     * @param date
     * @return
     * @throws ParseException
     */
    @Throws(ParseException::class)
    fun parseDate(date: String?): Date {
        return dateFormat.parse(date)
    }

    /**
     * 将字符串日期转换成java.util.Date类型 时间格式 HH:mm:ss
     *
     * @param time
     * @return
     * @throws ParseException
     */
    @Throws(ParseException::class)
    fun parseTime(time: String?): Date {
        return timeFormat.parse(time)
    }

    /**
     * 根据自定义pattern将字符串日期转换成java.util.Date类型
     *
     * @param datetime
     * @param pattern
     * @return
     * @throws ParseException
     */
    @Throws(ParseException::class)
    fun parseDatetime(datetime: String?, pattern: String?): Date {
        val format = datetimeFormat.clone() as SimpleDateFormat
        format.applyPattern(pattern)
        return format.parse(datetime)
    }


    /**
     * 比较时间大小
     * @param begin
     * @param end
     * @return
     */
    fun compareDate(begin: String?, end: String?): Int {
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm")
        try {
            val beginDate = df.parse(begin)
            val endDate = df.parse(end)
            return if (beginDate.time < endDate.time) {
                1
            } else if (beginDate.time > endDate.time) {
                -1
            } else {
                0
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        return 0
    }

    fun getCurrentUtcTimestamp(): ByteArray {
        val dateInMillis = System.currentTimeMillis()
        val dateInSec = dateInMillis / 1000
        return ByteBuffer.allocate(8).putLong(dateInSec).array()
    }

    fun getCurrentLocalTimestamp(): ByteArray {
        val dateInMillis = System.currentTimeMillis()
        val offset = TimeZone.getDefault().getOffset(dateInMillis)
        var dateInSec = dateInMillis + offset
        dateInSec /= 1000
        return ByteBuffer.allocate(8).putLong(dateInSec).array()
    }

    /**
     * 获取星期的显示(固件那边的显示顺序)
     * @param dayIndex
     * @return
     */
    @JvmStatic
    fun getDayShowStr(dayIndex: Int): String? {
        return when (dayIndex) {
            0 -> CommonApp.instance.getString(R.string.text_week_sun)
            1 -> CommonApp.instance.getString(R.string.text_week_monday)
            2 -> CommonApp.instance.getString(R.string.text_week_tuesday)
            3 -> CommonApp.instance.getString(R.string.text_week_wed)
            4 -> CommonApp.instance.getString(R.string.text_week_thu)
            5 -> CommonApp.instance.getString(R.string.text_week_fri)
            6 -> CommonApp.instance.getString(R.string.text_week_sat)
            else -> ""
        }
    }

    /**
     * 获取开始和结束时间的显示
     * @return
     */
    @JvmStatic
    fun generatePeriodTimeShow(
        startHour: Int?,
        startMinute: Int?,
        endHour: Int?,
        endMinute: Int?
    ): String {
        var s = ""
        val maxTime = 24
        val hour: Int =
            (startHour!! + getTimeZoneOffSecond() / 3600 + maxTime) % maxTime
        val endHourResult: Long =
            ((endHour!! + getTimeZoneOffSecond() / 3600 + startHour) % maxTime).toLong()
        var nextDay = ""
        if (hour > endHourResult) {
            nextDay = CommonApp.instance.getString(R.string.union_time_second_day)
        }
        s = String.format(
            "%02d:%02d-%s%02d:%02d",
            hour,
            startMinute,
            nextDay,
            endHourResult,
            endMinute
        )
        return s
    }

    fun getTimeZoneOffSecond(): Int {
        val startHour = 11
        val startMinute = 11
        val utcHHFormat = String.format(Locale.getDefault(), "%02d", startHour)
        val utcMmFormat = String.format(Locale.getDefault(), "%02d", startMinute)
        val formatter = SimpleDateFormat("yyyyMMdd HH:mm", Locale.US)
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        var value: Date? = null
        val date = Date()
        val calendar = Calendar.getInstance()
        calendar.time = date
        try {
            val yearMonthStr = String.format(
                "%04d%02d%02d",
                calendar[Calendar.YEAR], calendar[Calendar.MONTH] + 1, calendar[Calendar.DATE]
            )
            value = formatter.parse("$yearMonthStr $utcHHFormat:$utcMmFormat")
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val timerCalendar = Calendar.getInstance()
        timerCalendar.timeInMillis = value!!.time
        timerCalendar.timeZone = TimeZone.getDefault()
        val ansHour =
            (timerCalendar[Calendar.DATE] - calendar[Calendar.DATE]) * 24 + timerCalendar[Calendar.HOUR_OF_DAY] - startHour
        val ansMinute = timerCalendar[Calendar.MINUTE] - startMinute
        //      int rawOffset = TimeZone.getDefault().getRawOffset();
        return ansHour * 3600 + ansMinute * 60
    }

    /**
     * 获取开始和结束时间的显示
     * @param startTime
     * @param endTime
     * @return
     */
    fun generatePeriodTimeDescription(startTime: Int?, endTime: Int?): String {
        var s = ""
        val maxTime = 60 * 60 * 24
        if (endTime != null) {
            if (endTime >= maxTime) {
                return CommonApp.instance.getString(R.string.union_all_day)
            }
        }
        val t: Long =
            ((startTime!! + getTimeZoneOffSecond() + maxTime) % maxTime).toLong()
        val newT: Long =
            ((endTime!! + getTimeZoneOffSecond() + maxTime) % maxTime).toLong()
        val hour = t / 3600
        val minute = (t - hour * 3600) / 60
        val second = t % 60
        val endHour = newT / 3600
        val endMinute = (newT - endHour * 3600) / 60
        val endSecond = newT % 60
        var nextDay = ""
        if (t > newT) {
            nextDay = CommonApp.instance.getString(R.string.union_time_second_day)
        }
        s = String.format("%02d:%02d-%s%02d:%02d", hour, minute, nextDay, endHour, endMinute)
        return s
    }


    /**
     * ①当utcTime + getTimeZoneOffSecond < 0时，即*Utc0设置的时间是当前时区的前一天的时间，也就是fromTime是toTime的前一天。
     * 此时需要将repeatDay[7]内的值循环左移一位。
     * ②当utcTime + getTimeZoneOffSecond >= 86400时， 即Utc0设置的时间是当前时区的后一天时间，也就是fromTime是toTime的后一天，
     * 此时需要将repeat[7]内的值循环右移一位。
     *
     * @param repeatDay boolean[]形式的重复天数，表示周日-周六是否重复
     * @param timeStamp 原时间戳，即UTC0时间
     * @return 本地时间的重复天数
     */
    fun getRepeatFromUtc0(repeatDay: BooleanArray, timeStamp: Long): BooleanArray {
        val repeat = BooleanArray(repeatDay.size)
        for (i in repeatDay.indices) {
            repeat[i] = repeatDay[i]
        }
        if (timeStamp + getTimeZoneOffSecond() >= 86400) {
            arrMoveRight(repeat, repeat.size)
        } else if (timeStamp + getTimeZoneOffSecond() < 0) {
            arrayMoveLeft(repeat, repeat.size)
        }
        return repeat
    }

    /**
     * boolean数组循环右移
     *
     * @param repeat 数组
     * @param length 数组长度
     */
    fun arrMoveRight(repeat: BooleanArray, length: Int) {
        for (i in 1 until length) {
            val t = repeat[0]
            repeat[0] = repeat[i % length]
            repeat[i % length] = t
        }
    }

    /**
     * boolean数组循环左移
     *
     * @param arr    数组
     * @param length 数组长度
     */
    fun arrayMoveLeft(arr: BooleanArray, length: Int) {
        for (i in length - 2 downTo 0) {
            val t = arr[length - 1]
            arr[length - 1] = arr[i % length]
            arr[i % length] = t
        }
    }

    /**
     * ①当utcTime - getTimeZoneOffSecond < 0时，即*Utc0设置的时间是当前时区的前一天的时间，也就是fromTime是toTime的前一天。
     * 此时需要将repeatDay[7]内的值循环左移一位。
     * ②当utcTime - getTimeZoneOffSecond >= 86400时， 即Utc0设置的时间是当前时区的后一天时间，也就是fromTime是toTime的后一天，
     * 此时需要将repeat[7]内的值循环右移一位。
     *
     * @param repeatDay boolean[]形式的重复天数，表示周日-周六是否重复
     * @param timeStamp 原时间戳，即本地时间
     * @return utc0时间的重复天数
     */
    fun getRepeatFromCurrentRegion(repeatDay: BooleanArray, timeStamp: Long): BooleanArray {
        val repeat = BooleanArray(repeatDay.size)
        for (i in repeatDay.indices) {
            repeat[i] = repeatDay[i]
        }
        if (timeStamp - getTimeZoneOffSecond() >= 86400) {
            arrMoveRight(repeat, repeat.size)
        } else if (timeStamp - getTimeZoneOffSecond() < 0) {
            arrayMoveLeft(repeat, repeat.size)
        }
        return repeat
    }

    /**
     * 获取当前时间
     *
     * @param pattern
     * @return
     */
    fun getCurDate(pattern: String): String {
        val sDateFormat = SimpleDateFormat(pattern)
        return sDateFormat.format(Date())
    }

    /**
     * 将字符串转为时间戳
     *
     * @param dateString
     * @param pattern
     * @return
     */
    fun getStringToDate(dateString: String?, pattern: String?): Long {
        try {
            val calendar = Calendar.getInstance()
            calendar.time = SimpleDateFormat(pattern, Locale.getDefault()).parse(dateString)
            return calendar.timeInMillis
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return 0
    }

    /**
     * 把字符串转化为时间格式
     *
     * @param timestamp
     * @return
     */
    fun getPTwoElecFormat(timestamp: Long): String? {
        val sdf = SimpleDateFormat( "yyyyMMdd")
        val date = Date(timestamp)
        sdf.format(date)
        return sdf.format(date)
    }

    /**
     * 把字符串转化为时间格式
     *
     * @param timestamp
     * @return
     */
    fun getPTwoElecFormat(timestamp: Long, format: String): String {
        val sdf = SimpleDateFormat(format)
        val date = Date(timestamp)
        sdf.format(date)
        return sdf.format(date)
    }


    /**
     * 获取设备采用的时间制式(12小时制式或者24小时制式)
     */
    fun getTime_12_24(context: Context): Boolean {
        val contentResolver = context.contentResolver
        val time_12_24 = Settings.System.getString(contentResolver, Settings.System.TIME_12_24)
        if (time_12_24 != null && "24" == time_12_24) {
            return true
        }
        return !(time_12_24 != null && "12" == time_12_24)
    }

    /**
     * 当前时间之前的时间与当前时间相差多少秒
     * @param startDate 当前时间之前的时间
     * @return
     */
    fun calLastedTime(endTime: Long, startTime: Long): Int {
        return ((endTime - startTime) / 1000).toInt()
    }



    //获取某年某月多少天
    fun getDaysOfYearMonth(year: Int, month: Int): Int {
        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = year
        cal[Calendar.MONTH] = month - 1
        cal[Calendar.DAY_OF_MONTH] = 1
        cal.add(Calendar.MONTH, 1)
        // 将日历里的日设为0，日历就会倒转到上一个月的最后一天
        cal[Calendar.DAY_OF_MONTH] = 0
        return cal[Calendar.DAY_OF_MONTH]
    }

}