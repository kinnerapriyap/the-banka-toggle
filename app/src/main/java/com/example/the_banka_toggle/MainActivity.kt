package com.example.the_banka_toggle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.DragEvent
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

        root.setOnDragListener { v, event ->
            val view = event.localState as StretchableSquare
            when (event?.action) {
                DragEvent.ACTION_DRAG_LOCATION -> {
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    view.isVisible = true
                    view.y = 0f
                }
            }
            true
        }
    }
}