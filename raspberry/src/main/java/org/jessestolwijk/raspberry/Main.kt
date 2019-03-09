package org.jessestolwijk.raspberry

import com.pi4j.io.gpio.GpioController
import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.RaspiPin

fun main() {
    println("Hello World")
    runtime.onShutdown {
        if(!gpio.isShutdown) {
            gpio.shutdown()
        }
        println("shutdown")
    }

    gpioController {

        provisionDigitalOutputPin(RaspiPin.GPIO_00, "PinLED").also { pin ->
            repeat(20) {
                pin.low()
                Thread.sleep(100)
                pin.high()
                Thread.sleep(100)
            }
        }

        shutdown()
    }


    println("Finished")
}

private val gpio: GpioController = GpioFactory.getInstance()
fun gpioController(block: GpioController.() -> Unit) = gpio.block()

private val runtime = Runtime.getRuntime()
private fun Runtime.onShutdown(block: () -> Unit) = addShutdownHook(Thread { block() })
