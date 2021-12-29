package cn.netdiscovery.pubsub

/**
 *
 * @FileName:
 *          cn.netdiscovery.pubsub.BrokerPublisher
 * @author: Tony Shen
 * @date: 2021/12/29 3:38 PM
 * @version: V1.0 <描述当前版本功能>
 */
interface BrokerPublisher {

    fun publish(event: Any, retain: Boolean = false)
}