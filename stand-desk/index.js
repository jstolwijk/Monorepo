console.log("Requesting Bluetooth Device...");

navigator.bluetooth
  .requestDevice()
  .then(device => {
    console.log("> Name:             " + device.name);
    console.log("> Id:               " + device.id);
    console.log("> Initially, connected?        " + device.gatt.connected);
    return device.gatt.connect();
    console.log("> Now, connected?        " + device.gatt.connected);
  })
  .catch(error => {
    console.log("Argh! " + error);
  });
