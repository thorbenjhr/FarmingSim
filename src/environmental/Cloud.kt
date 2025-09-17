package environmental

data class Cloud(private var id: Int, private var location: Int, private var duration: Int, private var amount: Int) {
    fun getId() = id
    fun getLocation() = location
    fun getDuration() = duration
    fun getAmount() = amount

    fun setLocation(newLocation: Int) {
        location = newLocation
    }

    fun setId(newId: Int) {
        id = newId
    }

    fun setDuration(newDuration: Int) {
        duration = newDuration
    }

    fun setAmount(newAmount: Int) {
        amount = newAmount
    }
}