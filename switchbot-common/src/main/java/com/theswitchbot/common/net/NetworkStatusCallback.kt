package com.theswitchbot.common.net

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.theswitchbot.common.logger.Logger

/**
 * 网络状态变化监听
 */
class NetworkStatusCallback : ConnectivityManager.NetworkCallback() {

    companion object {

        val TAG: String = NetworkStatusCallback::class.java.simpleName

        var mType = NetworkType.NETWORK_NO

        private var networkCallback: NetworkStatusCallback? = null

        private val mObservers = mutableListOf<NetStateChangeObserver>()

        fun init(context: Context) {
            try {
                val request = NetworkRequest.Builder().build()
                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

                unRegisterAll()
                networkCallback = NetworkStatusCallback()
                cm?.registerNetworkCallback(request, networkCallback!!)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

        fun release(context: Context) {

            unRegisterAll()
            networkCallback?.let {
                val cm =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
                cm?.unregisterNetworkCallback(it)
            }
        }

        /**
         * 注册网络监听
         */
        fun registerObserver(observer: NetStateChangeObserver?) {
            if (observer == null) {
                return
            }
            if (!mObservers.contains(observer)) {
                mObservers.add(observer)
            }
        }

        /**
         * 取消注册网络监听
         */
        fun unRegisterObserver(observer: NetStateChangeObserver?) {

            if (observer == null) {
                return
            }

            mObservers.remove(observer)
        }

        private fun unRegisterAll() {
            for (observer in mObservers) {
                unRegisterObserver(observer)
            }
            mObservers.clear()
        }

        private fun notifyObservers(networkType: NetworkType) {

            if (mType === networkType) {
                return
            }

            mType = networkType
            if (networkType === NetworkType.NETWORK_NO) {
                for (observer in mObservers) {
                    observer.onNetDisconnected()
                }
            } else {
                if (networkType == NetworkType.NETWORK_UNKNOWN){
                    return
                }
                for (observer in mObservers) {
                    observer.onNetConnected(networkType)
                }
            }
        }
    }

    init {
        Logger.d(TAG, "mType=$mType ")
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        Logger.d(TAG, "onAvailable ")
        notifyObservers(NetworkType.NETWORK_UNKNOWN)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        Logger.d(TAG, "onLost ")
        notifyObservers(NetworkType.NETWORK_NO)
    }

    override fun onCapabilitiesChanged(
        network: Network,
        networkCapabilities: NetworkCapabilities
    ) {
        super.onCapabilitiesChanged(network, networkCapabilities)

        Logger.d(TAG, "onCapabilitiesChanged networkCapabilities=$networkCapabilities")

        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {

            when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> notifyObservers(
                    NetworkType.NETWORK_WIFI
                )

                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> notifyObservers(
                    NetworkType.NETWORK_4G
                )

                else -> notifyObservers(NetworkType.NETWORK_UNKNOWN)
            }
        }
    }
}

enum class NetworkType(private val desc: String) {

    NETWORK_WIFI("WiFi"),
    NETWORK_4G("4G"),
    NETWORK_2G("2G"),
    NETWORK_3G("3G"),
    NETWORK_UNKNOWN("Unknown"),
    NETWORK_NO("No network");

    override fun toString(): String {
        return desc
    }
}

interface NetStateChangeObserver {
    fun onNetDisconnected()
    fun onNetConnected(networkType: NetworkType)
}