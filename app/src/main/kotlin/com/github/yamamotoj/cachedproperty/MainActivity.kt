package com.github.yamamotoj.cachedproperty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private val intValue by cached { Random().nextInt() }

    private val booleanValue by CachedProperty { Random().nextBoolean() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // After this call, subsequent access to intValue will trigger the initializer computation
        ::intValue.invalidateCache()

        // After this call, subsequent access to booleanValue will trigger the initializer computation
        ::booleanValue.invalidateCache()
    }
}
