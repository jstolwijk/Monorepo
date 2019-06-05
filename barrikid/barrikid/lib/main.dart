import 'dart:async';

import 'package:barrikid/device_details_screen.dart';
import 'package:barrikid/device_service.dart';
import 'package:flutter/material.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  final GlobalKey<AnimatedListState> _listKey = GlobalKey<AnimatedListState>();
  DeviceService _deviceService = DeviceService();
  ListModel<Device> _nearbyDevices;
  int _nextItem;

  @override
  void initState() {
    super.initState();
    _nearbyDevices = ListModel<Device>(
      listKey: _listKey,
      initialItems: [],
      removedItemBuilder: _buildRemovedItem,
    );
    _nextItem = 0;
  }

  Widget _buildRemovedItem(
      Device item, BuildContext context, Animation<double> animation) {
    return CardItem(
      animation: animation,
      device: item,
      selected: false,
    );
  }


  _MyHomePageState() {
    const oneSec = const Duration(seconds: 2);
    var timer = new Timer.periodic(oneSec, (Timer t) => _findNearbyDevices());
  }

  void _findNearbyDevices() {
      var newDevices = _deviceService.scanForNearbyDevices();
      var removedDevice = _nearbyDevices._items.indexWhere((device) =>  !newDevices.contains(device));
      var newDevice = newDevices.where((device) =>  !_nearbyDevices._items.contains(device));

      if(removedDevice != null && removedDevice != -1) {
        _nearbyDevices.removeAt(removedDevice);
      }

      if(newDevice.length > 0) {
        _nearbyDevices.insert(_nearbyDevices.length, newDevice.first);
      }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: AnimatedList(
          initialItemCount: _nearbyDevices.length,
          itemBuilder: (context, position, animation) {
            return CardItem(
              animation: animation,
              device: _nearbyDevices[position],
              onTap: () => Navigator.of(context).push(PageRouteBuilder(
                  opaque: false,
                  pageBuilder: (BuildContext context, _, __) =>
                      DeviceDetailsScreen(_nearbyDevices[position]))),
            );
          },
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _findNearbyDevices,
        tooltip: 'Increment',
        child: Icon(Icons.add),
      ),
    );
  }
}

class CardItem extends StatelessWidget {
  const CardItem(
      {Key key,
      @required this.animation,
      this.onTap,
      @required this.device,
      this.selected: false})
      : assert(animation != null),
        assert(device != null),
        assert(selected != null),
        super(key: key);

  final Animation<double> animation;
  final VoidCallback onTap;
  final Device device;
  final bool selected;

  @override
  Widget build(BuildContext context) {
    TextStyle textStyle = Theme.of(context).textTheme.display1;
    if (selected)
      textStyle = textStyle.copyWith(color: Colors.lightGreenAccent[400]);
    return Padding(
      padding: const EdgeInsets.all(2.0),
      child: SizeTransition(
        axis: Axis.vertical,
        sizeFactor: animation,
        child: GestureDetector(
          behavior: HitTestBehavior.opaque,
          onTap: onTap,
          child: SizedBox(
            height: 64.0,
            child: Card(
              color: Colors
                  .primaries[device.name.length % Colors.primaries.length],
              child: Center(
                child: Text(device.name),
              ),
            ),
          ),
        ),
      ),
    );
  }
}
//                child: Padding(
//                  padding: const EdgeInsets.all(16.0),
//                  child: Text(
//                    _nearbyDevices[position].name,
//                    style: TextStyle(fontSize: 22.0),
//                  ),
//                ),


class ListModel<E> {
  ListModel({
    @required this.listKey,
    @required this.removedItemBuilder,
    Iterable<E> initialItems,
  })  : assert(listKey != null),
        assert(removedItemBuilder != null),
        _items = List<E>.from(initialItems ?? <E>[]);

  final GlobalKey<AnimatedListState> listKey;
  final dynamic removedItemBuilder;
  final List<E> _items;

  AnimatedListState get _animatedList => listKey.currentState;

  void insert(int index, E item) {
    _items.insert(index, item);
    _animatedList.insertItem(index);
  }

  E removeAt(int index) {
    final E removedItem = _items.removeAt(index);
    if (removedItem != null) {
      _animatedList.removeItem(index,
              (BuildContext context, Animation<double> animation) {
            return removedItemBuilder(removedItem, context, animation);
          });
    }
    return removedItem;
  }

  int get length => _items.length;

  E operator [](int index) => _items[index];

  int indexOf(E item) => _items.indexOf(item);
}