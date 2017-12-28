package com.zhurui.bunnymall.cart.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.cart.bean.Cart;
import com.zhurui.bunnymall.cart.bean.CartBean;
import com.zhurui.bunnymall.cart.bean.CartProductBean;
import com.zhurui.bunnymall.common.BaseApplication;
import com.zhurui.bunnymall.common.BaseViewHolder;
import com.zhurui.bunnymall.home.activity.ProductNewDetailActivity;
import com.zhurui.bunnymall.mine.bean.UserFootDataDetailBean;
import com.zhurui.bunnymall.utils.Contants;
import com.zhurui.bunnymall.viewutils.AmountView;
import com.zhurui.bunnymall.viewutils.bean.ProductAttrbuteSxstr;

import java.text.DecimalFormat;
import java.util.List;


/**
 * Created by zhoux on 2017/7/14.
 */

public class ShopCartAdapter extends RecyclerView.Adapter<ShopCartAdapter.MyViewHolder> {

    private Context context;
    private int group = -1;
    public boolean isEdit = false;
    public List<CartProductBean> cartProductBeen;
    public List<CartBean> cartBeanList;
    OnItemClick onItemClick;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private ImageLoader imageLoader;

    public ShopCartAdapter(Context context) {
        this.context = context;
        this.imageLoader = BaseApplication.getInstance().getImageLoader();

    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.adapter_cart, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.itemView.setTag(position);
        group = position;
        CartBean cartBean = cartBeanList.get(position);
        CartProductBean cartProductBean = cartProductBeen.get(position);
        if (cartProductBean.getSupplierID().equals(cartProductBean.getProductID())) {
            holder.img_group.setVisibility(View.VISIBLE);
            if (cartBean.isCheck()) {
                holder.img_group.setImageResource(R.drawable.checkbox_checked);
            } else {
                holder.img_group.setImageResource(R.drawable.checkbox_normal);
            }
            holder.lin_child.setVisibility(View.GONE);
            holder.lin_product.setVisibility(View.GONE);
            holder.checkbox_group.setText(cartProductBean.getSupplierName());
        } else {
            holder.lin_group.setVisibility(View.GONE);
            holder.lin_child.setVisibility(View.VISIBLE);
            holder.lin_product.setVisibility(View.VISIBLE);
            if (cartBean.isCheck()) {
                holder.checkbox_child.setImageResource(R.drawable.checkbox_checked);
            } else {
                holder.checkbox_child.setImageResource(R.drawable.checkbox_normal);
            }
//            holder.img_product.setImageUrl(Contants.BASE_IMGURL + cartProductBean.getProductMainImage(), imageLoader);
            if (!(Contants.BASE_IMGURL +cartProductBean.getProductMainImage()).equals(holder.img_product.getTag(R.id.imageloader_uri))) {
                Glide.with(context).load(Contants.BASE_IMGURL + cartProductBean.getProductMainImage())
                        .placeholder(R.drawable.list_normal)
                        .error(R.drawable.list_normal)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)//是将图片原尺寸缓存到本地。
                        .into(holder.img_product);
                holder.img_product.setTag(R.id.imageloader_uri, Contants.BASE_IMGURL + cartProductBean.getProductMainImage());
            }

//            holder.img_product.setErrorImageResId(R.drawable.list_normal);
//            holder.img_product.setDefaultImageResId(R.drawable.list_normal);
            if ("2".equals(cartProductBean.getCustomFlag())) {
                holder.text_price.setText("议价");
                holder.text_fare.setVisibility(View.GONE);
            } else {
                float totalPrice = Float.parseFloat(cartProductBean.getPrice()) + Float.parseFloat(cartProductBean.getShuxingPrice());
                holder.text_price.setText("¥" + decimalFormat.format(totalPrice) + "");
                holder.productId.setText(cartProductBean.getProductID());
                holder.product_brand.setText(cartProductBean.getBrandName());
                ProductAttrbuteSxstr producSxstr = cartProductBean.getDingZhiShuXing();
                UserFootDataDetailBean footDataDetail = cartProductBean.getFootDataDetail();
                showProductAttrSxstr(holder, producSxstr, footDataDetail);//展示选中的定制属性
            }
            if (isEdit) {
                holder.lin_produce_detail.setVisibility(View.GONE);
                holder.amountView.setVisibility(View.VISIBLE);
            } else {
                holder.lin_produce_detail.setVisibility(View.VISIBLE);
                holder.amountView.setVisibility(View.GONE);
            }

            holder.text_productintroduce.setText(cartProductBean.getProductName());

            holder.text_count.setText("x" + cartProductBean.getNumber());
            if (null == cartProductBean.getFee() || "".equals(cartProductBean.getFee()) || "0".equals(cartProductBean.getFee())) {
                holder.text_fare.setText("免运费");
            } else {
                holder.text_fare.setText(cartProductBean.getFee());
            }
            holder.text_count.setText("x" + cartProductBean.getNumber() + "");

            holder.amountView.setAmount(Integer.parseInt(cartProductBean.getNumber()));
            holder.amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
                @Override
                public void onAmountChange(View view, int amount) {
                    holder.text_count.setText("x" + amount);
                    onItemClick.numChange(position, amount);
                }

                @Override
                public void toMax() {

                }
            });
        }

        if (null != onItemClick) {
            holder.img_group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.groupCheck(!cartBean.isCheck(), position);
                }
            });
            holder.checkbox_child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.childCheck(!cartBean.isCheck(), position);
                }
            });

            holder.lin_child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.itemClick(position);
                }
            });
        }
    }

    /**
     * 展示选中的定制属性
     *
     * @param producSxstr
     */
    private void showProductAttrSxstr(MyViewHolder holder, ProductAttrbuteSxstr producSxstr, UserFootDataDetailBean footDataDetail) {
        String checkmaterial = "";
        String personalityrequest = "";
        String customizesize = "";
        String yanseImage = "";

        if (producSxstr != null) {//定制属性
            if (producSxstr.getMianliaoList() != null && producSxstr.getMianliaoList().size() > 0) {//面料
                checkmaterial = checkmaterial + producSxstr.getMianliaoList().get(0).getName() + "  ";
            }
            if (producSxstr.getDicaiList() != null && producSxstr.getDicaiList().size() > 0) {//底材
                checkmaterial = checkmaterial + producSxstr.getDicaiList().get(0).getName() + "  ";
            }
            if (producSxstr.getXiegenList() != null && producSxstr.getXiegenList().size() > 0) {//鞋跟
                checkmaterial = checkmaterial + producSxstr.getXiegenList().get(0).getName() + "  ";
            }

            if (producSxstr.getDipeiList() != null && producSxstr.getDipeiList().size() > 0) {//底配  底配为多选,循环取出选中的属性
                for (int i = 0; i < producSxstr.getDipeiList().size(); i++) {
                    personalityrequest = personalityrequest + producSxstr.getDipeiList().get(0).getName() + "  ";
                }
            }
            if (producSxstr.getGexingList() != null && producSxstr.getGexingList().size() > 0) {//个性
                personalityrequest = personalityrequest + producSxstr.getGexingList().get(0).getName() + "  ";
            }

            if (producSxstr.getChimaList() != null && producSxstr.getChimaList().size() > 0) {//尺码
                if (footDataDetail != null) {//选中的一条足型数据
                    if(footDataDetail.getShoeSize().equals(producSxstr.getChimaList().get(0).getName())){
                        //如果选中足型数据的尺码和标准尺码一致，显示根据谁谁的足型数据定制
                        customizesize = "根据  " + customizesize + footDataDetail.getName() + "的足型数据  ";
                    }else{
                        //如果选中足型数据的尺码和标准尺码不一致，显示根据 标准尺码 45定制
                        customizesize = "根据  标准尺码";
                    }
                }
                customizesize = customizesize + producSxstr.getChimaList().get(0).getName() + "码  定制  ";
            }

            if (producSxstr.getYanseList() != null && producSxstr.getYanseList().size() > 0) {//选中颜色的图片
                yanseImage = producSxstr.getYanseList().get(0).getCustomPropertiesValueImageUrl();
            }
        }
        if (!TextUtils.isEmpty(checkmaterial)) {
            holder.lin_check_material.setVisibility(View.VISIBLE);
            holder.check_material_text.setText(checkmaterial);
        }
        if (!TextUtils.isEmpty(personalityrequest)) {
            holder.lin_personality_request.setVisibility(View.VISIBLE);
            holder.personality_request_text.setText(personalityrequest);
        }
        if (!TextUtils.isEmpty(customizesize)) {
            holder.lin_customize_size.setVisibility(View.VISIBLE);
            holder.customize_size_text.setText(customizesize);
        }
        if (!TextUtils.isEmpty(yanseImage)) {
            holder.img_product.setImageUrl(Contants.BASE_IMGURL + yanseImage, imageLoader);
        }
    }


    @Override
    public int getItemCount() {
        return null == cartProductBeen ? 0 : cartProductBeen.size();
    }


    public interface OnItemClick {
        void groupCheck(boolean isCheck, int position);

        void childCheck(boolean isCheck, int postion);

        void itemClick(int postion);

        void numChange(int position, int amount);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView checkbox_group;
        private LinearLayout lin_group;
        private ImageView img_group;
        private ImageView checkbox_child;
        private LinearLayout lin_child;
        private LinearLayout lin_produce_detail;
        private AmountView amountView;
        private TextView text_count;
        private TextView text_unit;
        private TextView text_price;
        private NetworkImageView img_product;
        private TextView text_productintroduce;
        private TextView text_fare;
        private RelativeLayout rel_colorsize;
        private LinearLayout lin_check_material;
        private LinearLayout lin_personality_request;
        private LinearLayout lin_customize_size;
        private TextView personality_request_text;
        private TextView check_material_text;
        private TextView customize_size_text;
        private TextView productId;
        private TextView product_brand;
        private LinearLayout lin_product;

        public MyViewHolder(View view) {
            super(view);
            checkbox_group = (TextView) view.findViewById(R.id.checkbox_group);
            lin_group = (LinearLayout) view.findViewById(R.id.lin_group);
            img_group = (ImageView) view.findViewById(R.id.img_group);
            checkbox_child = (ImageView) view.findViewById(R.id.checkbox_child);
            lin_child = (LinearLayout) view.findViewById(R.id.lin_child);
            amountView = (AmountView) view.findViewById(R.id.amountView);
            lin_produce_detail = (LinearLayout) view.findViewById(R.id.lin_produce_detail);
            text_count = (TextView) view.findViewById(R.id.text_count);
            text_price = (TextView) view.findViewById(R.id.text_price);
            img_product = (NetworkImageView) view.findViewById(R.id.img_product);
            text_productintroduce = (TextView) view.findViewById(R.id.text_productintroduce);
            text_fare = (TextView) view.findViewById(R.id.text_fare);
            rel_colorsize = (RelativeLayout) view.findViewById(R.id.rel_colorsize);
            text_unit = (TextView) view.findViewById(R.id.text_unit);
            lin_check_material = (LinearLayout) view.findViewById(R.id.lin_check_material);
            lin_personality_request = (LinearLayout) view.findViewById(R.id.lin_personality_request);
            lin_customize_size = (LinearLayout) view.findViewById(R.id.lin_customize_size);
            personality_request_text = (TextView) view.findViewById(R.id.personality_request_text);
            check_material_text = (TextView) view.findViewById(R.id.check_material_text);
            customize_size_text = (TextView) view.findViewById(R.id.customize_size_text);
            productId = (TextView) view.findViewById(R.id.productId);
            product_brand = (TextView) view.findViewById(R.id.product_brand);
            lin_product = (LinearLayout) view.findViewById(R.id.lin_product);
        }
    }
}
