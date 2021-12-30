package cn.netdiscovery.pubsub

/**
 *
 * @FileName:
 *          cn.netdiscovery.pubsub.ExceptionEvent
 * @author: Tony Shen
 * @date: 2021/12/29 4:36 PM
 * @version: V1.0 <描述当前版本功能>
 */
data class ExceptionEvent(
    val subscriber: Any,
    val event: Any,
    val error: Throwable
)