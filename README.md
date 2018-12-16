# koactor

Lightweight `Actor model` for Kotlin

## Usages

```
  val context = ActorContext()
  val helloActor = context.spawn<Message>(Creator(HelloActor::class, "Actor", 3))
  helloActor.tell(Say("say"))
```
