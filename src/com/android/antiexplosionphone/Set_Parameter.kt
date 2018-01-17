package com.android.antiexplosionphone

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.android.bean.SetParameterBean
import org.xutils.DbManager
import org.xutils.common.util.LogUtil
import org.xutils.x


/**
 * Created by Administrator on 2017/12/26.
 */
class Set_Parameter : Activity(), View.OnClickListener {

    val Longziwu: EditText by lazy {
        findViewById(R.id.ziwu) as EditText
    }
    val Longcipian: EditText by lazy {
        findViewById(R.id.zipian) as EditText
    }
    val save: Button by lazy {
        findViewById(R.id.save) as Button
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_parameter)
        save.setOnClickListener(this)
        SetDbinfo(2)
    }

    var ziwuxian: Double = 0.0
    var cipianjiao: Double = 0.0
    fun setParameter() {

        if (Longziwu.getText() == null || Longziwu.getText().toString().length == 0) run {
            editBuilder("请填写当地子午线收敛角！")
            return
        } else {
            ziwuxian = Longziwu.getText().toString().trim().toDouble()
            if (ziwuxian > 1.5 || ziwuxian < -1.5) {
                editBuilder("当地子午线收敛角应在1.5°~-1.5°范围内！")
                return
            }
        }
        if (Longcipian.getText() == null || Longcipian.getText().toString().length == 0) run {
            editBuilder("请填写磁偏角！")
            return
        } else {
            cipianjiao = Longcipian.getText().toString().trim().toDouble()
            if (cipianjiao > 5 || cipianjiao < -12) {
                editBuilder("磁偏角应在5°~-12°范围内！")
                return
            }
        }
        SetDbinfo(1)
    }
    /***
     * 添加当地子午线收敛角、磁偏角到数据库
     * int 1 为存数据
     * int 2 为取数据
     */
    private fun SetDbinfo(int: Int) {
        val daoConfig = DbManager.DaoConfig().setDbName("SetParameter") // 数据库的名字
                // 保存到指定路径 .setDbDir(newFile(Environment.getExternalStorageDirectory().getAbsolutePath()))
                .setDbVersion(1)// 数据库的版本号
                .setDbUpgradeListener { arg0, arg1, arg2 ->
                    LogUtil.e("数据库版本更新了！，此版本不联网发布不涉及数据库更新问题。")  // 数据库版本更新监听
                }
        val manager = x.getDb(daoConfig)
        try {
            if (int == 1) {
                val parameter = SetParameterBean()
                parameter.declination = cipianjiao
                parameter.convergenceOfMeridians = ziwuxian
                manager.saveOrUpdate(parameter)
            } else if (int == 2) {
                val findAll = manager.findAll(SetParameterBean::class.java)
                for (i in findAll.indices) {
                    Log.e("find", findAll[i].declination.toString() + ":convergenceOfMeridians=" + findAll[i].convergenceOfMeridians)
                    cipianjiao = findAll[i].declination
                    ziwuxian = findAll[i].convergenceOfMeridians
                    Longziwu.setText(ziwuxian.toString())
                    Longcipian.setText(cipianjiao.toString())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun editBuilder(str: String) {
        AlertDialog.Builder(this).setTitle("提示").setMessage(str).setNegativeButton("确定") { dialog, which -> }.setCancelable(false).show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.save -> {
                setParameter()
            }
        }
    }
}
