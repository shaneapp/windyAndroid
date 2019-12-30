package com.example.windyandroid.View.Custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.example.windyandroid.R

class TriangleShapeView : View {

    private var color = 0
    private val p = Paint()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setup(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        setup(attrs)
    }

    fun setup(attrs: AttributeSet) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.TriangleShapeView, 0, 0)

        try {
            color = typedArray.getColor(R.styleable.TriangleShapeView_triangleBackgroundColor, 0)

            p.color = color
            p.setShadowLayer(20f, 0f, 0f, Color.BLACK)
        } finally {
            typedArray.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val path = Path()
        path.moveTo(0f, height.toFloat())
        path.lineTo(0f, 0f)
        path.lineTo(width.toFloat(), height.toFloat())
        path.lineTo(0f, height.toFloat())
        path.close()

        canvas.drawPath(path, p)
    }
}