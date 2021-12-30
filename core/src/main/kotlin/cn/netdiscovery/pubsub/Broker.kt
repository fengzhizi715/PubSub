package cn.netdiscovery.pubsub

import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart

/**
 *
 * @FileName:
 *          cn.netdiscovery.pubsub.Broker
 * @author: Tony Shen
 * @date: 2021/12/29 3:46 PM
 * @version: V1.0 <描述当前版本功能>
 */
open class Broker(
    private val dispatcher: CoroutineContext = Dispatchers.Default
) : BrokerPublisher, BrokerSubscriber {

    private val eventFlow by lazy { MutableSharedFlow<Any>(extraBufferCapacity = Int.MAX_VALUE) }
    private val retainedEvents by lazy { ConcurrentHashMap<KClass<out Any>, Any>() }
    private val subscriberJobs by lazy { ConcurrentHashMap<Any, Set<Job>>() }

    override fun publish(event: Any, retain: Boolean) {
        if (retain) retainedEvents[event::class] = event
        eventFlow.tryEmit(event)
    }

    suspend fun publish(event: Any, retain: Boolean,timeMillis: Long) {
        delay(timeMillis)
        publish(event, retain)
    }

    @OptIn(FlowPreview::class)
    override fun <T : Any> subscribe(
        subscriber: Any,
        eventClass: KClass<T>,
        scope: CoroutineScope,
        eventDispatcher: CoroutineContext,
        emitRetained: Boolean,
        onEvent: suspend (T) -> Unit
    ) {
        val newJob = eventFlow
            .asSharedFlow()
            .onStart { if (emitRetained) emitRetainedEvents() }
            .filter { event -> event::class == eventClass }
            .flatMapConcat { event ->
                flow { emit(onEvent(event as T)) }
                    .catch { error -> publish(ExceptionEvent(subscriber, event, error)) }
                    .flowOn(eventDispatcher)
            }
            .flowOn(dispatcher)
            .launchIn(scope)

        subscriberJobs[subscriber] = getJobs(subscriber) + newJob
    }

    inline fun <reified T : Any> subscribe(
        subscriber: Any,
        scope: CoroutineScope,
        dispatcher: CoroutineContext = Dispatchers.Main,
        emitRetained: Boolean = false,
        noinline onEvent: suspend (T) -> Unit
    ) = subscribe(subscriber, T::class, scope, dispatcher, emitRetained, onEvent)

    override fun unsubscribe(subscriber: Any) {
        getJobs(subscriber).forEach { job -> job.cancel() }
        subscriberJobs.remove(subscriber)
    }

    override fun <T : Any> getRetained(eventClass: KClass<T>): T? =
        retainedEvents[eventClass] as? T

    override fun <T : Any> removeRetained(eventClass: KClass<T>): T? =
        retainedEvents.remove(eventClass) as? T

    private fun getJobs(subscriber: Any): Set<Job> =
        subscriberJobs[subscriber] ?: emptySet()

    private suspend fun FlowCollector<Any>.emitRetainedEvents() =
        emitAll(retainedEvents.values.asFlow())
}