import 'dart:math';
import 'package:faker/faker.dart';

class Device {
  String name;
  bool blocked;
  String macAddress;

  Device(this.name, this.macAddress, this.blocked);
}

List<String> deviceNames = ["PS4", "Macbook", "WII", "IPhone", "surveillance van", "XBOX", "IPad", "Smart cup holder"];

class DeviceService {
  List<Device> devices = [];
  Random random = new Random();
  var faker = new Faker();

  String _generateDeviceName() {
    var name = faker.person.firstName();
    var deviceName = deviceNames[random.nextInt(deviceNames.length)];

    return '$name\'s $deviceName';
  }

  Device _generateDevice() {
    return Device(_generateDeviceName(), faker.internet.macAddress(), random.nextBool());
  }

  List<Device> scanForNearbyDevices() {
    print("scanForNearbyDevices");
    var randomNumber = random.nextInt(3);

    if (randomNumber == 2 && devices.length > 0) {
      devices.removeAt(random.nextInt(devices.length));
    } else {
      if (devices.length < 8) {
        devices.add(_generateDevice());
      }
    }

    return devices;
  }
}