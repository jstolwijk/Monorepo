
fun main() {
    val numberOfQueries = readLine()!!.toInt()

    repeat(numberOfQueries) {
        val query = readLine()!!.toDouble()

        println((query / 7) + 1)
    }
}