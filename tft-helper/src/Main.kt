package com.jesse

import java.awt.Toolkit.getDefaultToolkit
import java.awt.Dimension
import java.awt.Toolkit

fun main() {
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val width = screenSize.getWidth()
    val height = screenSize.getHeight()

    println("$width $height")
}
