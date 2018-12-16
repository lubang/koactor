package koactor

class ActorContext {
    fun <M> spawn(creator: Creator): Actor<M> {
        return creator.newActor()
    }
}
