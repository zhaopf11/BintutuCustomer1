package com.zhurui.bunnymall.viewutils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.mine.bean.CityModel;
import com.zhurui.bunnymall.mine.bean.DistrictModel;
import com.zhurui.bunnymall.mine.bean.ProvinceModel;
import com.zhurui.bunnymall.utils.GetProvinceUtil;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by zhoux on 2017/7/26.
 */

public class ProvinceDialog extends Dialog {

    private View view;
    private RadioGroup radiobtngroup_province;
    private RadioButton radio_province;
    private RadioButton radio_city;
    private RadioButton radio_county;
    private Button btn_sure;
    private RecyclerView recycler_province;
    private ProvinceAdapter provinceAdapter;
    private List<ProvinceModel> provinceList = null;
    private List<CityModel> cityModelList = null;
    private List<DistrictModel> districtModels = null;
    private Context context;
    private int chooseposition = -1;
    private BtnOnClick btnOnClick;


    public ProvinceDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view= inflater.inflate(R.layout.dialog_province,null);
        radiobtngroup_province =(RadioGroup)view.findViewById(R.id.radiobtngroup_province);
        radio_province=(RadioButton) view.findViewById(R.id.radio_province);
        radio_city = (RadioButton)view.findViewById(R.id.radio_city);
        radio_county=(RadioButton)view.findViewById(R.id.radio_county);
        btn_sure=(Button)view.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String province = radio_province.getText().toString();
                String city = radio_city.getText().toString();
                String county = radio_county.getText().toString();
                String addressinfo = "";
//                if(!province.equals("省份")){
//                    addressinfo=addressinfo+province;
//                }
//                if(!city.equals("城市")){
//                    addressinfo = addressinfo+" "+city;
//                }
//                if(!county.equals("区县")){
//                    addressinfo=addressinfo+" "+county;
//                }
                btnOnClick.btnOnClick(province,city,county);
                dismiss();
            }
        });
        recycler_province = (RecyclerView)view.findViewById(R.id.recycler_province);
        radiobtngroup_province.setOnCheckedChangeListener(onCheckedChangeListener);
        initData();
        setView();

    }

    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId){
                case R.id.radio_province:
                    radio_city.setText("城市");
                    radio_county.setText("区县");
                    provinceAdapter.state=1;
                    provinceAdapter.notifyDataSetChanged();

                    break;
                case R.id.radio_city:
                    radio_county.setText("区县");
                    if(null ==cityModelList || cityModelList.size()==0){
                        cityModelList = provinceList.get(0).getCityList();
                        radio_province.setText( provinceList.get(0).getName());
                    }
                    provinceAdapter.cityList = cityModelList;
                    provinceAdapter.state=2;
                    provinceAdapter.notifyDataSetChanged();
                    break;
                case R.id.radio_county:
                    if(null ==districtModels || districtModels.size()==0){
                        cityModelList = provinceList.get(0).getCityList();
                        districtModels = cityModelList.get(0).getDistrictList();
                        radio_province.setText( provinceList.get(0).getName());
                        radio_city.setText(cityModelList.get(0).getName());
                    }
                    provinceAdapter.state=3;
                    provinceAdapter.districtList = districtModels;
                    provinceAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private void initData(){
        provinceList= GetProvinceUtil.getProvince(context);
        provinceAdapter = new ProvinceAdapter(context);
        provinceAdapter.state = 1;
        provinceAdapter.provinceList= provinceList;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recycler_province.setLayoutManager(linearLayoutManager);
        recycler_province.setAdapter(provinceAdapter);
        provinceAdapter.setOnItemClickLitener(new ProvinceAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                chooseposition = position;
                if(1==provinceAdapter.state){
                    cityModelList = provinceList.get(position).getCityList();
                    radio_province.setText(provinceList.get(position).getName());
                    radiobtngroup_province.check(R.id.radio_city);
                    provinceAdapter.cityList = cityModelList;
                    provinceAdapter.state=2;
                    provinceAdapter.notifyDataSetChanged();
                }else if(2== provinceAdapter.state){
                    districtModels = cityModelList.get(position).getDistrictList();
                    radio_city.setText(cityModelList.get(position).getName());
                    radiobtngroup_province.check(R.id.radio_county);
                    provinceAdapter.districtList = districtModels;
                    provinceAdapter.state = 3;
                    provinceAdapter.notifyDataSetChanged();
                }else if(3== provinceAdapter.state){
                    radio_county.setText(districtModels.get(position).getName());
                }


            }
        });
    }

    private void setView(){

        view.setAlpha(1.0f);
        this.setContentView(view);
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.AnimBottom);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int)context.getResources().getDisplayMetrics().widthPixels; // 宽度
        dialogWindow.setAttributes(lp);
        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    public interface BtnOnClick{
        void btnOnClick(String province,String city,String county);
    }

    public void setBtnOnClice(BtnOnClick btnOnClick){
        this.btnOnClick  = btnOnClick;

    }
}
