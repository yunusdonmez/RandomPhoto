package com.genesis.randomphoto.framework

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet


class ATextView : AppCompatTextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}