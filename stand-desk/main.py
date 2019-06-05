from bluepy.btle import Scanner, Peripheral, ADDR_TYPE_RANDOM

scanner = Scanner()
devices = scanner.scan(10.0)

desk = None

for dev in devices:
    print("Device %s (%s), RSSI=%d dB" % (dev.addr, dev.addrType, dev.rssi))
    for (adtype, desc, value) in dev.getScanData():
        print("  %s = %s" % (desc, value))

        if value == "Desk":
            desk = dev.addr

print("Desk: %s", desk)
peripheral = Peripheral(desk, ADDR_TYPE_RANDOM)


for service in peripheral.getServices():
    print("service: %s", service.)
