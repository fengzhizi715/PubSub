package cn.netdiscovery.pubsub

import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 *
 * @FileName:
 *          cn.netdiscovery.pubsub.BrokerSubscriber
 * @author: Tony Shen
 * @date: 2021/12/29 3:39 PM
 * @version: V1.0 <描述当前版本功能>
 */
interface BrokerSubscriber {

    fun <T : Any> subscribe(
        subscriber: Any,
        eventClass: KClass<T>,
        scope: CoroutineScope,
        eventDispatcher: CoroutineContext = Dispatchers.Main,
        emitRetained: Boolean = false,
        onEvent: suspend (T) -> Unit
    )

    fun unsubscribe(subscriber: Any)

    fun <T : Any> getRetained(eventClass: KClass<T>): T?

    fun <T : Any> removeRetained(eventClass: KClass<T>): T?
}