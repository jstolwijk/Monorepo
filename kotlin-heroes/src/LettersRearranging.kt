
fun main() {
    val numberOfQueries = readLine()!!.toInt()

    repeat(numberOfQueries) {
        val query = readLine()!!

        when {
            query.isPalindrome() -> println("-1")
            query.
        }
    }
}

private fun String.isPalindrome() = this == reversed()