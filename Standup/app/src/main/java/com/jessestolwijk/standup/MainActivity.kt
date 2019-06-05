package com.jessestolwijk.standup

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGatt.*
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_COARSE_LOCATION = 456

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val BluetoothAdapter.isDisabled: Boolean
        get() = !isEnabled

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_COARSE_LOCATION);
        }

//        bluetoothAdapter!!.bluetoothLeScanner!!.startScan { callbackType, result ->
//            print("Success!: ${result?.device?.address}")
//        }
    }

    private val callback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            when {
                status == GATT_SUCCESS && newState == STATE_CONNECTED -> {
                    gatt?.discoverServices()
                }
                status == GATT_SUCCESS && newState == STATE_DISCONNECTED -> {
                    // HANDLE disconnect
                }
                status != GATT_SUCCESS -> gatt?.disconnect()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            gatt?.services
            print("Services discorved!")
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_COARSE_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    bluetoothAdapter!!.bluetoothLeScanner!!.startScan(scanCallback)
                } else {
                    // Alert the user that this application requires the location permission to perform the scan.
                }
            }
        }
    }

    val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            result?.device?.let { device ->
                if (device.name == "Desk") {
                    result.device.connectGatt(this@MainActivity, true, callback)
                }
            }
        }
    }
}
