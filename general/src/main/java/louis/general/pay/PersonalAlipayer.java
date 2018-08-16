package louis.general.pay;

import android.app.Activity;

import louis.general.util.IntentUtil;

/**
 * Created by Louis on 2018/5/5.
 *For Xp快译
 * 描述:针对个人的支付宝付款处理类，支持1，2，3，6，8，18，28元的支付金额，不支持自定义金额
 */

public class PersonalAlipayer {

   public static void pay(Activity activity,int price){
       String payUrl;
       switch (price){
           case 1:
               payUrl="HTTPS://QR.ALIPAY.COM/FKX02079IRBDJHNGCYX8B7?t=1531145830451";
               break;
           case 2:
               payUrl="HTTPS://QR.ALIPAY.COM/FKX03289E8VA3DOQMXLQ77?t=1531145869666";
               break;
           case 3:
               payUrl="HTTPS://QR.ALIPAY.COM/FKX03249PH45NZQXI1G530?t=1531145894843";
               break;
           case 6:
               payUrl="HTTPS://QR.ALIPAY.COM/FKX04095KCDG2PZHF7HUDA?t=1531145926585";
               break;
           case 8:
               payUrl="HTTPS://QR.ALIPAY.COM/FKX04444MEK35XSNRBBL52?t=1531145955695";
               break;
           case 18:
               payUrl="HTTPS://QR.ALIPAY.COM/FKX06849NZIZ9SSMUP1QAC?t=1531145974366";
               break;
           case 28:
               payUrl="HTTPS://QR.ALIPAY.COM/FKX03072NGH3REAIBP41A0?t=1531145986703";
               break;
           default:
               payUrl="HTTPS://QR.ALIPAY.COM/FKX03289E8VA3DOQMXLQ77?t=1531145869666";
               break;
       }
       IntentUtil.openAlipay(activity,payUrl);
   }
}
