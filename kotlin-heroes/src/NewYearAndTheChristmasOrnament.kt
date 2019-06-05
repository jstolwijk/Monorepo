
fun main() {
    val (yellow, blue, red) = readLine()!!.split(" ").map { it.toInt() }

    listOf(yellow, blue - 1, red - 2).min()?.also {
        println(it * 3 + 3)
    }
}

