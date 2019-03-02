package org.jessestolwijk.raspberry

import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.PinState
import com.pi4j.io.gpio.RaspiPin
import sun.nio.cs.Surrogate.low

fun main(args: Array<String>) {
    val gpio = GpioFactory.getInstance()

    val pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_17, "PinLED", PinState.HIGH)
    println("light is: ON")

    Thread.sleep(2000)

    pin.low()
    println("light is: OFF")
    Thread.sleep(1000)
    println("light is: ON for 1 second")
    pin.pulse(1000, true)
    gpio.shutdown()
}