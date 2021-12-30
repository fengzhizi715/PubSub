package cn.netdiscovery.pubsub.example

import cn.netdiscovery.pubsub.Broker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 *
 * @FileName:
 *          cn.netdiscovery.pubsub.example.Test
 * @author: Tony Shen
 * @date: 2021/12/29 4:40 PM
 * @version: V1.0 <描述当前版本功能>
 */
data class Test(val message:String)

fun main() {

    val broker = Broker()

    GlobalScope.launch {
        broker.subscribe<Test>("1", this, Dispatchers.IO,true) {
            println(it.message)
        }
    }

    broker.publish(Test("test1"),true)

    GlobalScope.launch {
        broker.publish(Test("test2"),true,2000)
    }

    Thread.sleep(5000)
}