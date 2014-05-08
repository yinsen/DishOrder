/**
 * Copyright 2012 Taketoma
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.infolands.dishorder;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

public class NumberPickerDialog extends Dialog implements OnClickListener{
	Button btn_ok;
	Button btn_cancel;
	TextView txt_input;
	TextView txt_rangeMin;
	TextView txt_rangeMax;
	Button btn_1;
	Button btn_2;
	Button btn_3;
	Button btn_4;
	Button btn_5;
	Button btn_6;
	Button btn_7;
	Button btn_8;
	Button btn_9;
	Button btn_0;
	Button btn_clear;
	Button btn_back;
	Context context;
	int initNumber;
	int maxNumber;
	int minNumber;
	int mode; //用于标识调用者
	
	public interface OnNumberChangedListener {
		void numberChanged(int number, int mode);
	}
	private OnNumberChangedListener mListener;
	public NumberPickerDialog(Context context, OnNumberChangedListener listener, int number,
			int maxNum, int minNum, int mode) {
		super(context);
		this.context = context;
		this.mListener = listener;
		this.initNumber = number;
		this.mode = mode;
		this.maxNumber = maxNum;
		this.minNumber = minNum;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.number_picker_layout);
		
//		setTitle("Number Picker");
		
		txt_input = (TextView) findViewById(R.id.txt_inputNumber);
		txt_input.setText(String.valueOf(initNumber));
//		txt_rangeMin = (TextView) findViewById(R.id.txt_numberMIN);
//		txt_rangeMax = (TextView) findViewById(R.id.txt_numberMAX);
//		txt_rangeMin.setText("MIN : " + String.valueOf(minNumber));
//		txt_rangeMax.setText("MAX : " + String.valueOf(maxNumber));
		btn_1 = (Button) findViewById(R.id.btn_1);
		btn_2 = (Button) findViewById(R.id.btn_2);
		btn_3 = (Button) findViewById(R.id.btn_3);
		btn_4 = (Button) findViewById(R.id.btn_4);
		btn_5 = (Button) findViewById(R.id.btn_5);
		btn_6 = (Button) findViewById(R.id.btn_6);
		btn_7 = (Button) findViewById(R.id.btn_7);
		btn_8 = (Button) findViewById(R.id.btn_8);
		btn_9 = (Button) findViewById(R.id.btn_9);
		btn_0 = (Button) findViewById(R.id.btn_0);
		btn_clear = (Button) findViewById(R.id.btn_clear);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_ok = (Button) findViewById(R.id.ok);
		btn_cancel = (Button) findViewById(R.id.cancel);
		btn_1.setOnClickListener(this);
		btn_2.setOnClickListener(this);
		btn_3.setOnClickListener(this);
		btn_4.setOnClickListener(this);
		btn_5.setOnClickListener(this);
		btn_6.setOnClickListener(this);
		btn_7.setOnClickListener(this);
		btn_8.setOnClickListener(this);
		btn_9.setOnClickListener(this);
		btn_0.setOnClickListener(this);
		btn_clear.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		setCancelable(false);
	}
	private void setText(int num){
		String nowNumber = txt_input.getText().toString();
		String newNumber = "";
		if (nowNumber.equals("") || nowNumber.equals("0")){
			newNumber = String.valueOf(num);
		}else{
			newNumber = nowNumber.concat(String.valueOf(num));
			if (Integer.parseInt(newNumber) > maxNumber ){
				newNumber = nowNumber;
				TranslateAnimation translate = new TranslateAnimation(0, 10, 0, 0);
				translate.setDuration(1000);
				translate.setInterpolator(new CycleInterpolator(7));
				txt_input.startAnimation(translate);
			}else if (Integer.parseInt(nowNumber)< minNumber){
				newNumber = nowNumber;
				TranslateAnimation translate = new TranslateAnimation(0, 10, 0, 0);
				translate.setDuration(1000);
				translate.setInterpolator(new CycleInterpolator(7));
				txt_input.startAnimation(translate);
			}
		}
		txt_input.setText(String.valueOf(newNumber));
	}
	private void deleteText(){
		txt_input.setText("");
	}
	private void backDeleteText(){
		String nowNumber = txt_input.getText().toString();
		int length =  nowNumber.length();
		if (!(length == 0)){
			nowNumber = nowNumber.substring(0,length - 1);
		}
		txt_input.setText(nowNumber);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ok:
			int resultNumber = 0;
			if (!txt_input.getText().equals("")){
				resultNumber = Integer.parseInt(txt_input.getText().toString());
			}
			mListener.numberChanged(resultNumber, mode);
			dismiss();
		break;
		
		case R.id.cancel:
			dismiss();
		break;
		
		case R.id.btn_0:
			setText(0);
		break;
		
		case R.id.btn_1:
			setText(1);
		break;
		
		case R.id.btn_2:
			setText(2);
		break;
		
		case R.id.btn_3:
			setText(3);
		break;
		
		case R.id.btn_4:
			setText(4);
		break;
		
		case R.id.btn_5:
			setText(5);
		break;
		
		case R.id.btn_6:
			setText(6);
		break;
		
		case R.id.btn_7:
			setText(7);
		break;
		
		case R.id.btn_8:
			setText(8);
		break;
		
		case R.id.btn_9:
			setText(9);
		break;

		case R.id.btn_clear:
			deleteText();
		break;

		case R.id.btn_back:
			backDeleteText();
		break;
		}
	}
}
