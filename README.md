# PubSub
[![@Tony沈哲 on weibo](https://img.shields.io/badge/weibo-%40Tony%E6%B2%88%E5%93%B2-blue.svg)](http://www.weibo.com/fengzhizi715)
[![License](https://img.shields.io/badge/license-Apache%202-lightgrey.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![](https://jitpack.io/v/fengzhizi715/PubSub.svg)](https://jitpack.io/#fengzhizi715/PubSub)

使用 Kotlin Coroutines 实现的 Local Pub/Sub、Event Bus、Message Bus

## 下载

将它添加到项目的 root build.gradle 中：

```groovy
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

然后在项目或者在 module 中添加：

```groovy
implementation 'com.github.fengzhizi715.PubSub:core:v1.0.0'
```

## Feature

* 支持事件发布和订阅
* 支持 Sticky Event/Retained Event
* 线程安全，可以在任何线程中发布/订阅事件，支持使用指定的线程进行事件订阅
* 支持延时发送事件
* 支持事件发送的异常处理

## Usage

1. 定义 EventBus

可以定义全局的 EventBus 或者定义多个 EventBus

```kotlin
val eventBus: Broker by lazy {
    Broker(Dispatchers.IO)
}
```

2. 定义事件

```kotlin
class XXXEvent
```

3. 发送事件

```kotlin
eventBus.publish(XXXEvent())
```

发送 Sticky Event/Retained Event

```kotlin
eventBus.publish(XXXEvent(),true)
```

延时发送事件

```kotlin
GlobalScope.launch {
    eventBus.publish(XXXEvent(),false,2000)
}
```

4. 订阅事件

```kotlin
runBlocking{
    eventBus.subscribe<XXXEvent>("subscriber name", this, Dispatchers.IO) {
        ......
    }
}
```

订阅 Sticky Event/Retained Event

```kotlin
runBlocking{
    eventBus.subscribe<XXXEvent>("subscriber name", this, Dispatchers.IO,true) {
        ......
    }
}
```

5. 取消订阅

```kotlin
broker.unsubscribe("subscriber name")
```

取消所有的订阅

```kotlin
broker.unsubscribeAll()
```