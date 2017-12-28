package com.zhurui.bunnymall.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.zhurui.bunnymall.common.BaseApplication;

import cn.xiaoneng.coreapi.ChatParamsBody;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.utils.CoreData;

/**
 * Created by zhaopf on 2017/10/11 0011.
 */

public class BintutuUtils {

    /**
     * 联系卖家
     * @param productId
     * @param productName
     * @param productPrice
     * @param productImage
     * @param productXnSupplierID
     */
    public static void connectionSeller(Activity activity,String productId, String productName, String productPrice, String productImage, String productXnSupplierID) {
        String[] permissions = {
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };
        Ntalker.getExtendInstance().ntalkerSystem().requestPermissions(activity, permissions);
        String shareUrl = Contants.PRODUCT_SHARE_URL + productId + ".html";
        ChatParamsBody chatparams = new ChatParamsBody();
        // 咨询发起页（专有参数）
        chatparams.startPageTitle = productName;
        chatparams.startPageUrl = shareUrl;
        // erp参数
        chatparams.erpParam = "";
        // 此参数不传就默认在sdk外部打开，即在onClickUrlorEmailorNumber方法中打开
        chatparams.clickurltoshow_type = CoreData.CLICK_TO_SDK_EXPLORER;
        // 商品展示（专有参数）
        chatparams.itemparams.appgoodsinfo_type = CoreData.SHOW_GOODS_BY_WIDGET;
//        chatparams.itemparams.clientgoodsinfo_type = CoreData.SHOW_GOODS_BY_WIDGET;
        chatparams.itemparams.clicktoshow_type = CoreData.CLICK_TO_SDK_EXPLORER;
        chatparams.itemparams.goods_image = Contants.BASE_IMGURL + productImage;
        chatparams.itemparams.goods_price = productPrice;
        chatparams.itemparams.goods_name = productName;
        chatparams.itemparams.goods_url = shareUrl;
        chatparams.itemparams.itemparam = "";
        //使用id方式，（SHOW_GOODS_BY_ID）
        chatparams.itemparams.goods_id = productId;//ntalker_test au_1000_9999
        int startChat = Ntalker.getBaseInstance().startChat(BaseApplication.getInstance(), productXnSupplierID + "_9999", null, chatparams);
        if (0 == startChat) {
            Log.e("startChat", "打开聊窗成功");
        } else {
            Log.e("startChat", "打开聊窗失败，错误码:" + startChat);
        }
    }
}
