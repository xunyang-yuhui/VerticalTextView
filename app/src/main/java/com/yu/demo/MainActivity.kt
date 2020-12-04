package com.yu.demo

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.yu.verticaltextview.VerticalTextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        view_vertical.setText(" 使至塞上\n  |王维\n单车欲问边，\n属国过居延。\n征蓬出汉塞，\n归雁入胡天。\n大漠孤烟直，\n长河落日圆。\n萧关逢候骑，\n都护在燕然。")
        view_vertical.setRowSpan(10)
        view_vertical.setTextAlign(VerticalTextView.TextAlignType.center)
        view_vertical.setTextColor(Color.WHITE)
        view_vertical.setTextSize(20)
        view_vertical.setOnClickListener{
            view_vertical.isRtl = !view_vertical.isRtl
        }
    }
}
