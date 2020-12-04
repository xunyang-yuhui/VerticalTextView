# VerticalTextView
一款显示垂直文字的View
[![](https://jitpack.io/v/xunyang-yuhui/VerticalTextView.svg)](https://jitpack.io/#xunyang-yuhui/VerticalTextView)

效果展示

![捕获v2.PNG](https://i.loli.net/2020/12/04/9iy6pnrqHeaAKwL.png)
![捕获v1.PNG](https://i.loli.net/2020/12/04/2BKXvO19bmYUfr7.png)

# 使用方法
## xml添加

```

<com.yu.verticaltextview.VerticalTextView
            android:id="@+id/view_vertical"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="#80000000"
            android:padding="10dp"
            app:text="显示的文字（手动换行添加 \n ）"
            app:textColor="@android:color/white"
            app:textSize="20sp"
            app:textAlign="left"
            app:isRtl="false"
            app:rowSpan="10dp"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintTop_toTopOf="parent"/>
            
```

## 代码中动态设置
```

  view_vertical.setText(" 使至塞上\n  |王维\n\n大漠孤烟直，\n长河落日圆。")
  view_vertical.setRowSpan(10)
  view_vertical.setTextAlign(VerticalTextView.TextAlignType.center)
  view_vertical.setTextColor(Color.WHITE)
  view_vertical.setTextSize(20)
  
```
