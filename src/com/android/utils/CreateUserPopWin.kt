package com.android.utils

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.RelativeLayout
import com.android.antiexplosionphone.R


/**
 * Created by Administrator on 2018/1/4.
 */
class CreateUserPopWin(mContext: Activity, itemsOnClick: View.OnClickListener) : PopupWindow() {

    private val mContext: Context

    private val view: View

    private val btn_save_pop: Button

    var text_mobile: EditText

    init {

        this.mContext = mContext

        this.view = LayoutInflater.from(mContext).inflate(R.layout.create_user_dialog, null)

        text_mobile = view.findViewById(R.id.text_mobile) as EditText

        btn_save_pop = view.findViewById(R.id.btn_save_pop) as Button

        // 设置按钮监听
        btn_save_pop.setOnClickListener(itemsOnClick)

        // 设置外部可点击
        this.isOutsideTouchable = true


        /* 设置弹出窗口特征 */
        // 设置视图
        this.contentView = this.view

        // 设置弹出窗体的宽和高
        /*
   * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
   * 对象,这样这可以以同样的方式改变这个Activity的属性.
   */
        val dialogWindow = mContext.window

        val m = mContext.windowManager
        val d = m.defaultDisplay // 获取屏幕宽、高用
        val p = dialogWindow.attributes // 获取对话框当前的参数值

        this.height = RelativeLayout.LayoutParams.WRAP_CONTENT
        this.width = (d.width * 0.8).toInt()

        // 设置弹出窗体可点击
        this.isFocusable = true

    }
}
