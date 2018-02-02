object Log {
    val STATUS_DEBUG = 1
    val STATUS_MESSAGE = 2

    var min_level = STATUS_DEBUG

    fun log(message: String, importance: Int) {
        if (importance < min_level) return
        console.log(message)
    }
}