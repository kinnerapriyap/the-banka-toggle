package com.example.the_banka_toggle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private val root: ConstraintLayout
        get() = findViewById(R.id.root)

    private val stretchableSquare: StretchableSquare
        get() = findViewById(R.id.stretchable_square)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stretchableSquare.params = StretchableSquareParams(
            stretchFactor = 1f,
            size = 300,
            isTop = true,
            isDebug = true
        )
        stretchableSquare.invalidate()
    }
}