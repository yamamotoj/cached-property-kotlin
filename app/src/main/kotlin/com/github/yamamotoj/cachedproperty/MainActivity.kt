package com.github.yamamotoj.cachedproperty

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private val cache = CachedProperty(Random()::nextInt)
    val data by cache

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cache.invalidate()
    }
}
