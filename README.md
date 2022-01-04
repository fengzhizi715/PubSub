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
* 线程安全，可以在任何线程中发布/订阅事件
* 支持延时发送事件