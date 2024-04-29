package me.lucyydotp.cheeselib.module

/**
 * Emits custom events.
 */
class EventEmitter<T : Any> {

    /**
     * A subscription to an event emitter.
     */
    data class Subscription<T : Any>(
        /**
         * The emitter this subscription is for.
         */
        val emitter: EventEmitter<out T>,
        /**
         * The handler callback.
         */
        val handler: (T) -> Unit,
        /**
         * The subscription's priority. Lower values are called first.
         */
        val priority: Int,
    ) {
        /**
         * Unsubscribes this subscription.
         */
        fun unsubscribe() = emitter.unsubscribe(this)
    }

    private var handlers = listOf<Subscription<in T>>()

    /**
     * Removes a subscription. It will no longer be invoked for events.
     */
    fun unsubscribe(subscription: Subscription<in T>) {
        handlers -= subscription
    }

    /**
     * Subscribes to this event emitter.
     * @param handler a callback function
     * @param priority the subscription's priority. Lower values are called first
     * @return a subscription object that can be used to cancel the subscription later
     */
    fun subscribe(handler: (T) -> Unit, priority: Int = 100): Subscription<T> = Subscription(this, handler, priority)
        .also { handlers = (handlers + it).sortedBy(Subscription<in T>::priority) }

    /**
     * Emits an event.
     */
    fun emit(event: T): T = event.also {
        handlers.forEach { it.handler(event) }
    }
}
