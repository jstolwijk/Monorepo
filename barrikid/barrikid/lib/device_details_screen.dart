import 'package:barrikid/device_service.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class DeviceDetailsScreen extends StatelessWidget {
  final Device _device;

  DeviceDetailsScreen(this._device) {}

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // Add 6 lines from here...
      appBar: AppBar(
        title: Text(_device.name),
      ),
      body: Container(
        padding: EdgeInsets.all(16.0),
        child: ListView(children: <Widget>[
          Text('Mac address: ${_device.macAddress}'),
          Text('Device status: ${_device.blocked ? 'blocked' : 'active'}'),
        ]),
      ),
    ); // ... to here.
  }
}
