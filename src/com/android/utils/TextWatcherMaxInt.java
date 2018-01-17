package com.android.utils;

import java.io.UnsupportedEncodingException;

import com.android.antiexplosionphone.R;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 输入int类型限制类
 * 
 * @author TY
 * 
 */
public class TextWatcherMaxInt implements TextWatcher {

	private Context mContext;
	private EditText mEditText = null;
	private int mnMaxbytes = 0; // 最大字节数
	private int mnMaxvalue = 0; // 最大值(为-1时不做判断)

	/**
	 * TextWatcher用于监听输入
	 * 
	 * @param context
	 * @param editText
	 * @param maxbytes
	 *            --限制最大字节数(-1为忽略此限制)
	 * @param maxvalue
	 *            --限制最大数值(-1为忽略此限制)
	 */
	public TextWatcherMaxInt(Context context, EditText editText, int maxbytes,
			int maxvalue) {
		mEditText = editText;
		mContext = context;
		mnMaxbytes = maxbytes;
		mnMaxvalue = maxvalue;
	}

	public void setMaxParams(int maxbytes, int maxvalue) {
		mnMaxbytes = maxbytes;
		mnMaxvalue = maxvalue;
	}

	public void afterTextChanged(Editable arg0) {

	}

	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {

	}

	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

		Editable editable = mEditText.getText();
		int byteLen = -1;
		try {
			byteLen = editable.toString().getBytes("gb2312").length;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("mnMaxvalue = "+mnMaxvalue+" mnMaxbytes = "+mnMaxbytes);
		if ((mnMaxvalue != -1) && (byteLen != 0)) {
			int value = -1;
			value = Integer.parseInt(editable.toString());
			// System.out.println("str = "+arg0+" = "+arg1+" = "+arg2+" = "+arg3+" value = "+value);
			if (value > mnMaxvalue) {
				int selEndIndex = Selection.getSelectionEnd(editable);
				String str = editable.toString();
				// 截取新字符串
				String newStr = str.substring(0, arg1);
				mEditText.setText(newStr);
				editable = mEditText.getText();

				// 新字符串的长度
				int newLen = editable.length();
				// 旧光标位置超过字符串长度
				if (selEndIndex > newLen) {
					selEndIndex = editable.length();
				}
				// 设置新光标所在的位置
				Selection.setSelection(editable, selEndIndex);

				Utils.toast(mContext, "输入数值超过" + mnMaxvalue);
				return;
			}
		}
		
		// System.out.println("str = "+arg0+" = "+arg1+" = "+arg2+" = "+arg3+" byteLen = "+byteLen);
		if ((mnMaxbytes != -1) && (byteLen > mnMaxbytes)) {
			int selEndIndex = Selection.getSelectionEnd(editable);
			String str = editable.toString();
			// 截取新字符串
			String newStr = str.substring(0, arg1);
			mEditText.setText(newStr);
			editable = mEditText.getText();

			// 新字符串的长度
			int newLen = editable.length();
			// 旧光标位置超过字符串长度
			if (selEndIndex > newLen) {
				selEndIndex = editable.length();
			}
			// 设置新光标所在的位置
			Selection.setSelection(editable, selEndIndex);

			Utils.toast(mContext, "输入字符超过" + mnMaxbytes + "字节");
			return;
		}
		if(mEditText.getId()==R.id.et_delaytime){
			if(!"".equals(editable.toString())&&"0".equals(editable.toString())){
				Utils.toast(mContext, "输入不得小于1");
				return;
			}
		}
		 if( mEditText.getId()==R.id.et_intervaltime ){
			if(!"".equals(editable.toString())&&"1".equals(editable.toString())||"0".equals(editable.toString())){
				Utils.toast(mContext, "输入不得小于2");
				return;
					}
		}
			
			
			
	}
		

}
