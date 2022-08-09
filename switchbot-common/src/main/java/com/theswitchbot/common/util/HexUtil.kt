package com.theswitchbot.common.util

import android.util.Log
import org.apache.commons.lang3.ArrayUtils
import org.greenrobot.essentials.PrimitiveArrayUtils
import java.math.BigDecimal
import java.math.BigInteger
import java.nio.ByteBuffer
import java.util.*
import java.util.zip.CRC32
import kotlin.experimental.and

object HexUtil {
    fun bytesToHex(src: ByteArray): String {
        val stringBuilder = StringBuilder()
        return if (src.isNotEmpty()) {
            for (i in src.indices) {
                val v: Int = src[i].toInt() and 255
                val hv = Integer.toHexString(v)
                if (hv.length < 2) {
                    stringBuilder.append(0)
                }
                stringBuilder.append(hv)
            }
            stringBuilder.toString().toUpperCase()
        } else {
            ""
        }
    }

    /**
     * 字节数组转long   bigEndian 默认大端序
     */
    fun bytesToLong(bytes: ByteArray,bigEndian:Boolean=true): Long {
        val newBytes=ByteArray(Long.SIZE_BYTES)
        val dis=newBytes.size-bytes.size
        if (bigEndian){
            for (i in dis..newBytes.lastIndex){
                newBytes[i]=bytes[i-dis]
            }
        }else{
            for (i in newBytes.lastIndex downTo dis){
                newBytes[i]=bytes[newBytes.lastIndex-i]
            }
        }
        return BigInteger(newBytes).toLong()
    }

    /**
     * 字节数组转int   bigEndian 默认大端序
     */
    fun bytesToInt(bytes: ByteArray,bigEndian:Boolean=true): Int {
        val newBytes=ByteArray(Int.SIZE_BYTES)
        val dis=newBytes.size-bytes.size
        if (bigEndian){
            for (i in dis..newBytes.lastIndex){
                newBytes[i]=bytes[i-dis]
            }
        }else{
            for (i in newBytes.lastIndex downTo dis){
                newBytes[i]=bytes[newBytes.lastIndex-i]
            }
        }
        return BigInteger(newBytes).toInt()

    }


    fun crc32(inputString: String): ByteArray{
        val crc = CRC32()
        crc.update(inputString.toByteArray())
        val keyLong = crc.value
        return longToBytes(keyLong)
    }

    fun crc32(inputString: ByteArray): ByteArray{
        val crc = CRC32()
        crc.update(inputString)
        val keyLong = crc.value
        return longToBytes(keyLong)
    }

    fun longToBytes(x: Long): ByteArray {
        return byteArrayOf(
            (x shr 24).toByte(),
            (x shr 16).toByte(),
            (x shr 8).toByte(),
            x.toByte()
        )
    }

    fun isAsciiPrintable(str: String): Boolean {
        val sz = str.length
        for (i in 0 until sz) {
            if (!isAsciiPrintable(str[i]) || isSpaceChar(
                    str[i]
                )
            ) {
                return false
            }
        }
        return true
    }

    fun getRandomBase62Bytes(): String {
        val base62Array =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray()
        val randomArray = CharArray(2)
        val r = Random()
        randomArray[0] = base62Array[r.nextInt(61)]
        randomArray[1] = base62Array[r.nextInt(61)]
        return String(randomArray)
    }

    fun isAsciiPrintable(ch: Char): Boolean {
        return ch.toInt() in 32..126
    }

    fun isSpaceChar(ch: Char): Boolean {
        return Character.isSpaceChar(ch)
    }

    fun serviceBytes(serviceString: String): ByteArray {
        val len = serviceString.length / 2
        val serviceBytes = ByteArray(len)
        for (i in 0 until len) {
            val splitString = serviceString.substring(i * 2, i * 2 + 2)
            val hex = splitString.toInt(16)
            serviceBytes[i] = hex.toByte()
        }
        return serviceBytes
    }

    fun macToHex(bytes: ByteArray): String {
        val hexArray = "0123456789ABCDEF".toCharArray()
        val stringLen = bytes.size * 3 - 1
        val hexChars = CharArray(stringLen)
        for (j in bytes.indices) {
            val v: Int = bytes[j].toInt() and 0xFF
            hexChars[j * 3] = hexArray[v ushr 4]
            hexChars[j * 3 + 1] = hexArray[v and 0x0F]
            if (j != bytes.size - 1) {
                hexChars[j * 3 + 2] = ':'
            }
        }
        return String(hexChars)
    }


    fun hexToBytes(str: String): ByteArray {
        if (str.isBlank()) {
            return ByteArray(0)
        }
        val bytes = ByteArray(str.length / 2)
        for (i in 0 until str.length / 2) {
            val subStr = str.substring(i * 2, i * 2 + 2)
            bytes[i] = subStr.toInt(16).toByte()
        }
        return bytes
    }

    fun hexStringToByteArray(s: String): ByteArray {
        if (s.isBlank()) {
            return ByteArray(0)
        }
        val len = s.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(s[i], 16) shl 4)
                    + Character.digit(s[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }

    /**
     * reversed 为true时靠后的在高位 否则靠后的在低位，如new boolean[]{false, false, false, false, false, false, true} = 64
     * new boolean[]{true, false, false, false, false, false, false} = 1
     * 也就是a[0] = bit0; a[6] = bit0
     */
    fun boolArray2Int(boolList: BooleanArray,reversed:Boolean=true): Int {
        var boolList = if (reversed){boolList}else{boolList.reversed().toBooleanArray()}
        if (boolList.size > 16) boolList = boolList.sliceArray(0..16)
        var p = 0
        for (i in boolList.indices) {
            p = p or if (boolList[i]) 0x01 shl i else 0x00
        }
        return p
    }

    /**
     * int按位转bool数组
     */
    fun int2BoolArray(p: Int, len: Int): BooleanArray {
        var len = len
        if (len > 16) len = 16
        if (len < 0) len = 0
        val b = BooleanArray(len)
        for (i in 0 until len) {
            b[i] = p and (0x01 shl i) > 0
        }
        return b
    }



    @JvmStatic
    fun byteToHex(b: Byte): String {
        return String.format("%02x", b)
    }


}

fun ByteArray.toHex():String=HexUtil.bytesToHex(this)