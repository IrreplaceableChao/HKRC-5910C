package com.android.MPFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.MP.Data;
import com.android.antiexplosionphone.R;
import com.android.utils.FormulaUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.android.MP.Data.ks;
import static com.android.MP.Data.zy;
import static com.android.utils.FormulaUtil.$C$2;
import static com.android.utils.FormulaUtil.$E$2;
import static com.android.utils.FormulaUtil.$F$1;
import static com.android.utils.FormulaUtil.kongshen;


public class Horizontal extends Fragment {

	private LineChart mLineChart;
private View view;
	private int[] x;
	private int[] y;
	int sj_x; //设计曲线X轴坐标
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_horizontal, container, false);

		findview();

		init();

		return view;
	}
	private void findview(){
		mLineChart = (LineChart) view.findViewById(R.id.chart);
	}

	private void init() {

		sj_x = (int)(kongshen*Math.abs(Math.cos($C$2*$F$1)));
//      mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Bold.ttf");
		XAxis xAxis = mLineChart.getXAxis();
//      YAxis leftAxis = mLineChart.getAxisLeft();
		//x轴间距	xAxis.setSpaceBetweenLabels(4);
		mLineChart.getAxisRight().setEnabled(false);//设置y轴的右侧是否显示坐标
		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴的位置
//        xAxis.setTypeface(mTf); // 设置字体
		xAxis.setEnabled(true);
		// 上面第一行代码设置了false,所以下面第一行即使设置为true也不会绘制AxisLine
		xAxis.setDrawAxisLine(true);
		// 前面xAxis.setEnabled(false);
		// 则下面绘制的Grid不会有"竖的线"（与X轴有关）
//        xAxis.setDrawGridLines(true);
		LineData mLineData = getLineData();
		showChart(mLineChart, mLineData, Color.rgb(114, 188, 223));//这里调背景颜色
		YAxis leftAxis = mLineChart.getAxisLeft();
		leftAxis.setValueFormatter(new MyYAxisValueFormatter());


//		xAxis.setValueFormatter(new MyXAxisValueFormatter());
	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
	};
	// 设置显示的样式
	private void showChart(LineChart lineChart, LineData lineData, int color) {
		lineChart.setDrawBorders(false);  //是否在折线图上添加边框
		// no description text
//		lineChart.setDescription("");// 数据描述
		// 如果没有数据的时候，会显示这个，类似listview的emtpyview
//		lineChart.setNoDataTextDescription("You need to provide data for the chart.");
		// enable / disable grid background
		lineChart.setDrawGridBackground(false); // 是否显示表格颜色
		lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度
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

		// get the legend (only possible after setting data)
		Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的

		// modify the legend ...
		// mLegend.setPosition(LegendPosition.LEFT_OF_CHART);
		mLegend.setForm(Legend.LegendForm.LINE);// 样式circle
		mLegend.setFormSize(6f);// 字体
		mLegend.setTextColor(Color.WHITE);// 颜色
//      mLegend.setTypeface(mTf);// 字体
		lineChart.animateX(2500); // 立即执行的动画,x轴
	}

	/**
	 * 生成一个数据
	 */
	private LineData getLineData() {
		int length = ks.length;
		FormulaUtil formula = new FormulaUtil(ks, Data.qj, Data.fw,length);
		x = new int[length];
		y = new int[length];
		int min = 0;
		for (int i = 0; i <length; i++) {
			x[i] = (int)((formula.y_()[i] * Math.cos((270 + $E$2 + 0) * $F$1) - formula.x_()[i] * Math.sin((270 + $E$2 + 0) * $F$1)));//2017-7-12 16:00:50 为单旭升单独更改  在X轴*100  所以在这个位置需要除以100
			y[i] = (int)(zy[i]);
			if (x[i] < min) min = x[i];
//			Log.e("Horizontal:i===>","第"+i+"次"+"    x:"+x[i]+"        y:"+y[i]);
		}
//		ArrayList<String> xValues = new ArrayList<String>();
//
//		if (min<0){
//			for (int i = 0;i<length;i++){
//				x[i] = x[i]+(0-min);
//			}
//			for (int i = 0; i < Data.x_num*100 + 5; i++) {/*填写图标中有多少个坐标点，填写孔深*/
//				// x轴显示的数据，这里默认使用数字下标显示
//				xValues.add("" +(i+min)/100);//2017-7-12 16:00:50 为单旭升单独更改  在X轴*100  所以在这个位置需要除以100
//			}
//		}else {
//			for (int i = 0; i < Data.x_num*100 + 5; i++) {/*填写图标中有多少个坐标点，填写孔深*/
//				// x轴显示的数据，这里默认使用数字下标显示
//				xValues.add("" + i/100);//2017-7-12 16:00:50 为单旭升单独更改  在X轴*100  所以在这个位置需要除以100
//			}
//		}

		/*********开始*****/
		ArrayList<Entry> yValues = new ArrayList<Entry>();
		yValues.add(new Entry(0, 0));
		yValues.add(new Entry(0,sj_x));
		LineDataSet lineDataSet = new LineDataSet(yValues, "设计曲线" /*显示在比例图上*/);
		lineDataSet.setLineWidth(1.75f); // 线宽
		lineDataSet.setDrawCircles(false);//取消小圆圈
		lineDataSet.setColor(Color.WHITE);// 显示颜色
		lineDataSet.setDrawValues(false);//设置折线图中的坐标点
		lineDataSet.setDrawHighlightIndicators(false);
		lineDataSet.setDrawVerticalHighlightIndicator(false);

		/*********结束*****/

		/*********Y轴画一条透明线，针对坐标比例*****/
		ArrayList<Entry> yValues_1 = new ArrayList<Entry>();
		yValues_1.add(new Entry(0,0));
		yValues_1.add(new Entry((sj_x/2),0));
		LineDataSet lineDataSet_1 = new LineDataSet(yValues_1, "" /*显示在比例图上*/);
		lineDataSet_1.setLineWidth(1.75f); // 线宽
		lineDataSet_1.setDrawCircles(false);//取消小圆圈

		int color = Color.argb(0,0,0,0);// 半透明的紫色
		lineDataSet_1.setColor(color);// 显示颜色
		lineDataSet_1.setDrawValues(false);//设置折线图中的坐标点
		lineDataSet_1.setDrawHighlightIndicators(false);
		lineDataSet_1.setDrawVerticalHighlightIndicator(false);
		/*********结束*****/

		/*********画一个原点*****/
		ArrayList<Entry> y_QD = new ArrayList<Entry>();
		y_QD.add(new Entry(y[0], x[0]));
		LineDataSet lineDataSet_QD = new LineDataSet(y_QD, "  起点" /*显示在比例图上*/);
		lineDataSet_QD.setLineWidth(2f); // 线宽
		lineDataSet_QD.setDrawCircles(true);//取消小圆圈
		lineDataSet_QD.setDrawCircleHole(false);
		int color1 = Color.argb(254,30,144,255);// 半透明的紫色
		lineDataSet_QD.setCircleColor(color1);
		lineDataSet_QD.setColor(color1);// 显示颜色
		lineDataSet_QD.setCircleSize(6);
		lineDataSet_QD.setDrawValues(false);//设置折线图中的坐标点
		lineDataSet_QD.setDrawHighlightIndicators(false);
		lineDataSet_QD.setDrawVerticalHighlightIndicator(false);
		/*********结束*****/


		/*********开始画实钻设计曲线*****/
		ArrayList<Entry> yValues1 = new ArrayList<Entry>();
		for (int i = 0; i < length; i++) {
			yValues1.add(new Entry(y[i], x[i]));
		}
		ArrayList<ILineDataSet> lineDataSets = new ArrayList<ILineDataSet>();
//		lineDataSets.add(lineDataSet); // add the datasets
		LineDataSet lineDataSet1 = new LineDataSet(yValues1, "钻孔曲线    横轴（设计方位线）,纵轴（左右偏差）" /*显示在比例图上*/);
//         mLineDataSet.setFillAlpha(110);
//         mLineDataSet.setFillColor(Color.RED);
		//用y轴的集合来设置参数
		lineDataSet1.setLineWidth(1.75f); // 线宽
		lineDataSet1.setDrawCircles(false);//取消小圆圈
//		lineDataSet1.setCircleSize(1f);// 显示的圆形大小
		lineDataSet1.setColor(Color.RED);// 显示颜色
		lineDataSet1.setDrawValues(false);//设置折线图中的坐标点
//		lineDataSet1.setCircleColor(Color.WHITE);// 圆形的颜色
//		lineDataSet1.setHighLightColor(Color.WHITE); // 高亮的线的颜色
//		lineDataSet1.setDrawCubic(true); //设置是否允许曲线平滑
//		lineDataSet1.setCubicIntensity(0.2f);//平滑度
		/*********结束*****/

		lineDataSets.add(lineDataSet_1);
		lineDataSets.add(lineDataSet);
		lineDataSets.add(lineDataSet1);
		lineDataSets.add(lineDataSet_QD);
		// create a data object with the datasets
		LineData lineData = new LineData(lineDataSets);
		return lineData;
	}
	public class MyYAxisValueFormatter implements IAxisValueFormatter {

		private DecimalFormat mFormat;

		public MyYAxisValueFormatter () {
			mFormat = new DecimalFormat("###,###,##0"); // use one decimal
		}

//		@Override
//		public String getFormattedValue(float value, YAxis yAxis) {
//			// write your logic here
//			// access the YAxis object to get more information
//			return mFormat.format(value/100); // e.g. append a dollar-sign
//		}

		@Override
		public String getFormattedValue(float value, AxisBase axis) {
			return mFormat.format(value); // e.g. append a dollar-sign
		}
	}
}
