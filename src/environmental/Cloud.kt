package environmental

data class Cloud(private var id: Int, private val location: Int, private val duration: Int, private val amount: Int) {
    fun getId() = id
    fun getLocation() = location
    fun getDuration() = duration
    fun getAmount() = amount

    fun setId(newId: Int) {
        id = newId
    }
}