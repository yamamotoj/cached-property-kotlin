# Kotlin Cached Property 
Lazy cache using Kotlin delegated property.


```kotlin
class SomeClass {
    private val cache = CachedProperty(Random()::nextInt)
    val data by cache

    // remove cached value and value is loaded 
    // when the data is called on next time
    fun revoke() = cache.invalidate()
}
```


Download
---
[![](https://jitpack.io/v/yamamotoj/cached-property-kotlin.svg)](https://jitpack.io/#yamamotoj/cached-property-kotlin)

```gradle
repositories {
  maven { url "https://jitpack.io" }
}
```

```gradle
dependencies {
  compile 'com.github.yamamotoj:cached-property-kotlin:0.1.0'
}
```





Licence
----

```
Copyright 2017 Jumpei Yamamoto

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

