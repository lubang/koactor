package koactor

interface Actor<T> {
    fun tell(msg: T)
}
