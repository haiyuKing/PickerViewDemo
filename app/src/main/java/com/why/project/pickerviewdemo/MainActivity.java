package com.why.project.pickerviewdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.why.project.pickerviewdemo.bean.SpinnearBean;
import com.why.project.pickerviewdemo.util.DateTimeHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

	private TextView hobbyTv;//选择爱好
	/**爱好列表集合*/
	private ArrayList<SpinnearBean> mHobbyList;
	private ArrayList<String> mHobbyNameList;//用于选择器显示
	private OptionsPickerView mHobbyPickerView;//选择器

	private TextView addressTv;//选择地址
	/**地址列表集合*/
	private ArrayList<SpinnearBean> mAddressList;
	private ArrayList<String> mAddressNameList;//用于选择器显示
	private OptionsPickerView mAddressPickerView;//选择器

	private TextView startdateTv;//开始日期
	private TimePickerView mStartDatePickerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initViews();
		initDatas();
		initEvents();
	}

	private void initViews() {
		hobbyTv = findViewById(R.id.hobbyTv);
		addressTv = (TextView) findViewById(R.id.addressTv);
		//实现文本区域可滑动的效果，用于当显示的文本过长时
		addressTv.setMovementMethod(ScrollingMovementMethod.getInstance());//实现可滑动，但是和ScrollView滑动冲突，需要处理下
		//https://blog.csdn.net/qq_36070190/article/details/70053228
		addressTv.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					//通知父控件不要干扰
					v.getParent().requestDisallowInterceptTouchEvent(true);
				}
				if(event.getAction()==MotionEvent.ACTION_MOVE){
					//通知父控件不要干扰
					v.getParent().requestDisallowInterceptTouchEvent(true);
				}
				if(event.getAction()==MotionEvent.ACTION_UP){
					v.getParent().requestDisallowInterceptTouchEvent(false);
				}
				return false;
			}
		});
		startdateTv = findViewById(R.id.startdateTv);
	}

	private void initDatas() {
		//========================================初始化爱好列表集合========================================
		mHobbyList = new ArrayList<SpinnearBean>();
		mHobbyNameList = new ArrayList<String>();

		//模拟获取数据集合
		try{
			mHobbyList = parseJsonArray("spinners.txt");
		}catch (Exception e) {
			e.printStackTrace();
		}
		for(SpinnearBean spinnearBean : mHobbyList){
			mHobbyNameList.add(spinnearBean.getParaName());
		}

		//============初始化选择器============
		initHobbyOptionPicker();
		//如果想要直接赋值的话，这样写
		/*if(mHobbyNameList.size() > 0){
			hobbyTv.setText(mHobbyNameList.get(0));//默认展现第一个
		}*/

		//========================================初始化地址列表集合========================================
		mAddressList = new ArrayList<SpinnearBean>();
		mAddressNameList = new ArrayList<String>();
		//模拟获取数据集合
		try{
			mAddressList = parseJsonArray("spinners2.txt");
		}catch (Exception e) {
			e.printStackTrace();
		}
		for(SpinnearBean spinnearBean : mAddressList){
			mAddressNameList.add(spinnearBean.getParaName());
		}
		//============初始化选择器============
		initAddressOptionPicker();
		//当选择器列表项文本过长时，直接赋值的话，会有问题：高度变小了。不太明白什么原因，所以需要延迟设置文本，实际过程中因为有网络请求，所以一般没有问题
		/*new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if(mAddressNameList.size() > 0){
					addressTv.setText(mAddressNameList.get(0));//默认展现第一个
				}
			}
		}, 500);*/

		//========================================初始化开始日期选择器控件========================================
		initStartTimePicker();//初始化开始日期选择器控件
	}

	//初始化爱好选择器
	private void initHobbyOptionPicker() {
		mHobbyPickerView = new OptionsPickerBuilder(MainActivity.this, new OnOptionsSelectListener() {
				@Override
				public void onOptionsSelect(int options1, int option2, int options3, View v) {
						//返回的分别是三个级别的选中位置
						String tx = mHobbyNameList.get(options1);
						hobbyTv.setText(tx);
					}
				})
				.setDecorView((RelativeLayout)findViewById(R.id.activity_rootview))//必须是RelativeLayout，不设置setDecorView的话，底部虚拟导航栏会显示在弹出的选择器区域
				.setTitleText("选择爱好")//标题文字
				.setTitleSize(20)//标题文字大小
				.setTitleColor(getResources().getColor(R.color.pickerview_title_text_color))//标题文字颜色
				.setCancelText("取消")//取消按钮文字
				.setCancelColor(getResources().getColor(R.color.pickerview_cancel_text_color))//取消按钮文字颜色
				.setSubmitText("确定")//确认按钮文字
				.setSubmitColor(getResources().getColor(R.color.pickerview_submit_text_color))//确定按钮文字颜色
				.setContentTextSize(20)//滚轮文字大小
				.setTextColorCenter(getResources().getColor(R.color.pickerview_center_text_color))//设置选中文本的颜色值
				.setLineSpacingMultiplier(1.8f)//行间距
				.setDividerColor(getResources().getColor(R.color.pickerview_divider_color))//设置分割线的颜色
				.setSelectOptions(0)//设置选择的值
				.build();

		mHobbyPickerView.setPicker(mHobbyNameList);//添加数据
	}

	//初始化地址选择器
	private void initAddressOptionPicker() {
		mAddressPickerView = new OptionsPickerBuilder(MainActivity.this, new OnOptionsSelectListener() {
			@Override
			public void onOptionsSelect(int options1, int option2, int options3, View v) {
				//返回的分别是三个级别的选中位置
				String tx = mAddressNameList.get(options1);
				addressTv.setText(tx);
			}
		})
				.setDecorView((RelativeLayout)findViewById(R.id.activity_rootview))//必须是RelativeLayout，不设置setDecorView的话，底部虚拟导航栏会显示在弹出的选择器区域
				.setTitleText("选择地址")//标题文字
				.setTitleSize(20)//标题文字大小
				.setTitleColor(getResources().getColor(R.color.pickerview_title_text_color))//标题文字颜色
				.setCancelText("取消")//取消按钮文字
				.setCancelColor(getResources().getColor(R.color.pickerview_cancel_text_color))//取消按钮文字颜色
				.setSubmitText("确定")//确认按钮文字
				.setSubmitColor(getResources().getColor(R.color.pickerview_submit_text_color))//确定按钮文字颜色
				.setContentTextSize(20)//滚轮文字大小
				.setTextColorCenter(getResources().getColor(R.color.pickerview_center_text_color))//设置选中文本的颜色值
				.setLineSpacingMultiplier(1.8f)//行间距
				.setDividerColor(getResources().getColor(R.color.pickerview_divider_color))//设置分割线的颜色
				.setSelectOptions(0)//设置选择的值
				.build();

		mAddressPickerView.setPicker(mAddressNameList);//添加数据
	}

	/**初始化开始日期选择器控件*/
	private void initStartTimePicker() {
		//控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
		//因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
		Calendar selectedDate = Calendar.getInstance();
		//设置最小日期和最大日期
		Calendar startDate = Calendar.getInstance();
		try {
			startDate.setTime(DateTimeHelper.parseStringToDate("1970-01-01"));//设置为2006年4月28日
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar endDate = Calendar.getInstance();//最大日期是今天

		//时间选择器
		mStartDatePickerView = new TimePickerBuilder(MainActivity.this, new OnTimeSelectListener() {
					@Override
					public void onTimeSelect(Date date, View v) {//选中事件回调
						// 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
						startdateTv.setText(DateTimeHelper.formatToString(date,"yyyy-MM-dd"));
					}
				})
				.setDecorView((RelativeLayout)findViewById(R.id.activity_rootview))//必须是RelativeLayout，不设置setDecorView的话，底部虚拟导航栏会显示在弹出的选择器区域
				//年月日时分秒 的显示与否，不设置则默认全部显示
				.setType(new boolean[]{true, true, true, false, false, false})
				.setLabel("", "", "", "", "", "")
				.isCenterLabel(false)//是否只显示中间选中项的label文字，false则每项item全部都带有label。
				.setTitleText("开始日期")//标题文字
				.setTitleSize(20)//标题文字大小
				.setTitleColor(getResources().getColor(R.color.pickerview_title_text_color))//标题文字颜色
				.setCancelText("取消")//取消按钮文字
				.setCancelColor(getResources().getColor(R.color.pickerview_cancel_text_color))//取消按钮文字颜色
				.setSubmitText("确定")//确认按钮文字
				.setSubmitColor(getResources().getColor(R.color.pickerview_submit_text_color))//确定按钮文字颜色
				.setContentTextSize(20)//滚轮文字大小
				.setTextColorCenter(getResources().getColor(R.color.pickerview_center_text_color))//设置选中文本的颜色值
				.setLineSpacingMultiplier(1.8f)//行间距
				.setDividerColor(getResources().getColor(R.color.pickerview_divider_color))//设置分割线的颜色
				.setRangDate(startDate, endDate)//设置最小和最大日期
				.setDate(selectedDate)//设置选中的日期
				.build();
	}

	private void initEvents() {
		//选择爱好的下拉菜单点击事件
		hobbyTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mHobbyPickerView.show();
			}
		});

		//选择地址的下拉菜单点击事件
		addressTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mAddressPickerView.show();
			}
		});

		//开始日期的下拉菜单点击事件
		startdateTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mStartDatePickerView.show();
			}
		});
	}





	/*===========读取assets目录下的js字符串文件（js数组和js对象），然后生成List集合===========*/
	public static final String LISTROOTNODE = "spinnerList";
	public static final String KEY_LISTITEM_NAME = "paraName";
	public static final String KEY_LISTITEM_VALUE = "paraValue";
	public static final String KEY_LISTITEM_CHECKCOLOR = "checkColor";

	/**
	 * 解析JSON文件的简单数组
	 */
	private ArrayList<SpinnearBean> parseJsonArray(String fileName) throws Exception{

		ArrayList<SpinnearBean> itemsList = new ArrayList<SpinnearBean>();

		String jsonStr = getStringFromAssert(MainActivity.this, fileName);
		if(jsonStr.equals("")){
			return null;
		}
		JSONObject allData = new JSONObject(jsonStr);  //全部内容变为一个项
		JSONArray jsonArr = allData.getJSONArray(LISTROOTNODE); //取出数组
		for(int x = 0;x<jsonArr.length();x++){
			SpinnearBean model = new SpinnearBean();
			JSONObject jsonobj = jsonArr.getJSONObject(x);
			model.setParaName(jsonobj.getString(KEY_LISTITEM_NAME));
			model.setParaValue(jsonobj.getString(KEY_LISTITEM_VALUE));
			if(jsonobj.has(KEY_LISTITEM_CHECKCOLOR)){
				model.setCheckColor(jsonobj.getString(KEY_LISTITEM_CHECKCOLOR));
			}
			model.setSelectedState(false);
			itemsList.add(model);
			model = null;
		}
		return itemsList;
	}

	/**
	 * 访问assets目录下的资源文件，获取文件中的字符串
	 * @param filePath - 文件的相对路径，例如："listdata.txt"或者"/www/listdata.txt"
	 * @return 内容字符串
	 * */
	public String getStringFromAssert(Context mContext, String filePath) {

		String content = ""; // 结果字符串
		try {
			InputStream is = mContext.getResources().getAssets().open(filePath);// 打开文件
			int ch = 0;
			ByteArrayOutputStream out = new ByteArrayOutputStream(); // 实现了一个输出流
			while ((ch = is.read()) != -1) {
				out.write(ch); // 将指定的字节写入此 byte 数组输出流
			}
			byte[] buff = out.toByteArray();// 以 byte 数组的形式返回此输出流的当前内容
			out.close(); // 关闭流
			is.close(); // 关闭流
			content = new String(buff, "UTF-8"); // 设置字符串编码
		} catch (Exception e) {
			Toast.makeText(mContext, "对不起，没有找到指定文件！", Toast.LENGTH_SHORT)
					.show();
		}
		return content;
	}
}
