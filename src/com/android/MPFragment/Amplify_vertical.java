package com.android.MPFragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.android.MP.Data;
import com.android.antiexplosionphone.R;
import com.android.utils.FormulaUtil;
import com.android.utils.Utils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import static com.android.MP.Data.ks;
import static com.android.MP.Data.sx;
import static com.android.utils.FormulaUtil.$C$2;
import static com.android.utils.FormulaUtil.$E$2;
import static com.android.utils.FormulaUtil.$F$1;
import static com.android.utils.FormulaUtil.kongshen;
import static com.android.utils.FormulaUtil.shang;
/**
 * Created by Administrator on 2017/1/5.
 */

public class Amplify_vertical extends Activity{
    private LineChart mLineChart;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amplify_v);
        findview();
        setTopBottom((float) shang, getSX_Max() + 5);
        setonClick();
        init();
    }

    private void setonClick() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void findview() {
        mLineChart = (LineChart) findViewById(R.id.chart);
        button = (Button) findViewById(R.id.button4);
    }

    private void init() {
        XAxis xAxis = mLineChart.getXAxis();
        mLineChart.getAxisRight().setEnabled(false);//设置y轴的右侧是否显示坐标
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴坐标显示的位置
        xAxis.setEnabled(true);        // xAxis.setEnabled(true);设置了false,即使下一行设置为true也不会绘制AxisLine
        xAxis.setDrawAxisLine(true);
        LineData mLineData = getLineData();
        showChart(mLineChart, mLineData, Color.rgb(114, 188, 223));//这里调背景颜色
    }

    // 设置显示的样式
    private void showChart(LineChart lineChart, LineData lineData, int color) {
        lineChart.setDrawBorders(false);  //是否在折线图上添加边框
        // enable / disable grid background
        lineChart.setDrawGridBackground(false); // 是否显示表格颜色
        lineChart.setGridBackgroundColor(0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度
        // enable touch gestures
        lineChart.setTouchEnabled(true); // 设置是否可以触摸
        // enable scaling and dragging
        lineChart.setDragEnabled(true);// 是否可以拖拽
        lineChart.setScaleEnabled(true);// 是否可以缩放
        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(true);//  xy轴是否同时缩放
        lineChart.setBackgroundColor(color);// 设置背景
        // add data
        lineChart.setData(lineData);  // 设置数据
    }

    /**
     * 生成一个数据
     *
     * @param //count 表示图表中有多少个坐标点
     * @return
     */
    private LineData getLineData() {
        int length = ks.length;
        FormulaUtil formula = new FormulaUtil(ks, Data.qj, Data.fw,length);
        float[] x = new float[length];
        float[] y = new float[length];
        double x_min = 0;
        double y_min = 0;
        for (int i = 0; i <x.length; i++) {
            x[i] =(float) ((formula.y_()[i] * Math.cos((270 + $E$2 + 0) * $F$1) - formula.x_()[i] * Math.sin((270 + $E$2 + 0) * $F$1)));
            y[i]= (float) (sx[i]);
            if (x[i] < x_min) x_min = x[i];
            if (y[i] < y_min) y_min = y[i];
        }

        /*********开始*****/
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        yValues.add(new Entry(0, 0));
        int sj_x = (int)(kongshen*Math.abs(Math.cos($C$2*$F$1)));
        int sj_y = (int)(kongshen*Math.sin($C$2*$F$1));
        yValues.add(new Entry(sj_x,0));
        LineDataSet lineDataSet = new LineDataSet(yValues, "设计曲线" /*显示在比例图上*/);
        lineDataSet.setLineWidth(1.25f); // 线宽
        lineDataSet.setDrawCircles(false);//取消小圆圈
        lineDataSet.setColor(Color.WHITE);// 显示颜色
        lineDataSet.setDrawValues(false);//设置折线图中的坐标点
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawVerticalHighlightIndicator(false);
        /*********结束*****/

        /*********画一个原点*****/
        ArrayList<Entry> y_QD = new ArrayList<Entry>();
        y_QD.add(new Entry(y[0], x[0]));
        LineDataSet lineDataSet_QD = new LineDataSet(y_QD, "起点" /*显示在比例图上*/);
        lineDataSet_QD.setLineWidth(1f); // 线宽
        lineDataSet_QD.setDrawCircles(true);//取消小圆圈
        lineDataSet_QD.setDrawCircleHole(false);
        int color1 = Color.argb(254,30,144,255);//
        lineDataSet_QD.setCircleColor(color1);
        lineDataSet_QD.setColor(color1);// 显示颜色
        lineDataSet_QD.setDrawValues(false);//设置折线图中的坐标点
        lineDataSet_QD.setDrawHighlightIndicators(false);
        lineDataSet_QD.setDrawVerticalHighlightIndicator(false);
        /*********结束*****/
        ArrayList<Entry> yValues1 = new ArrayList<Entry>();
        for (int i = 0; i < length; i++) {
            yValues1.add(new Entry(x[i],y[i] ));
        }


        LineDataSet lineDataSet1 = new LineDataSet(yValues1, "钻孔曲线    横轴（设计方位线）,纵轴（上下偏差）" /*显示在比例图上*/);
        //用y轴的集合来设置参数
        lineDataSet1.setLineWidth(1.75f); // 线宽
        lineDataSet1.setDrawCircles(false);//取消小圆圈
//		lineDataSet1.setCircleSize(1f);// 显示的圆形大小
        lineDataSet1.setColor(Color.RED);// 显示颜色
        lineDataSet1.setDrawValues(false);//设置折线图中的坐标点
		lineDataSet1.setCubicIntensity(0.1f);//平滑度
        lineDataSet1.setDrawHighlightIndicators(false);
        lineDataSet1.setDrawVerticalHighlightIndicator(false);
        ArrayList lineDataSets = new ArrayList();
        lineDataSets.add(lineDataSet);
        lineDataSets.add(lineDataSet1);
        lineDataSets.add(lineDataSet_QD);
        // create a data object with the datasets
        LineData lineData = new LineData(lineDataSets);
        return lineData;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void setTopBottom(float line, float maximum) {
//        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        if (line == 0) line = 3;
        if (maximum == 0) maximum = 50;
        LimitLine ll1 = new LimitLine(line, "上偏差参考线");
        ll1.setLineWidth(1f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);
//      ll1.setTypeface(tf);

        LimitLine ll2 = new LimitLine(-line, "下偏差参考线");
        ll2.setLineWidth(1f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);
//      ll2.setTypeface(tf);
        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaximum(maximum);
        leftAxis.setAxisMinimum(-maximum);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);//虚线绘制
        leftAxis.setDrawZeroLine(false);   //绘制0的线
        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);
    }

    public float getSX_Max() {
        double max;
        max = Data.sx[0];
        for (int i = 0; i < Data.sx.length; i++) {
            if (Math.abs(Data.sx[i]) > max) max = Math.abs(Data.sx[i]);
        }
        return (float) Utils.BD(max);
    }
}
