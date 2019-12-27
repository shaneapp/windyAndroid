package com.example.windyandroid.View.Custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class TriangleShapeView : View {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val path = Path()
        path.moveTo(0f, height.toFloat())
        path.lineTo(0f, 0f)
        path.lineTo(width.toFloat(), height.toFloat())
        path.lineTo(0f, height.toFloat())
        path.close()

        val p = Paint()
        p.color = Color.WHITE
        p.setShadowLayer(20f, 0f, 0f, Color.BLACK)

        canvas.drawPath(path, p)
    }
}