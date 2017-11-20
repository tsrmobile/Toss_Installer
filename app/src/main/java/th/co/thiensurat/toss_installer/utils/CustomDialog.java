package th.co.thiensurat.toss_installer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.EditText;

import cn.pedant.SweetAlert.SweetAlertDialog;
import th.co.thiensurat.toss_installer.R;

import static th.co.thiensurat.toss_installer.utils.Constance.REQUEST_SETTINGS;

/**
 * Created by teerayut.k on 9/26/2017.
 */

public class CustomDialog {

    private static Context context;
    private static SweetAlertDialog sweetAlertDialog;

    public CustomDialog(Context mcontext) {
        this.context = mcontext;
        sweetAlertDialog = new SweetAlertDialog(context);
    }

    public CustomDialog(final FragmentActivity mcontext) {
        this.context = mcontext;
        sweetAlertDialog = new SweetAlertDialog(context);
    }

    public static void dialogLoading() {
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.colorPrimaryDark));
        sweetAlertDialog.setTitleText("Loading...");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
    }

    public static void dialogDimiss() {
        if (sweetAlertDialog.isShowing()) {
            sweetAlertDialog.dismiss();
        }
    }

    public static void dialogFail(String fail) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Error")
                .setContentText(fail)
                .showCancelButton(false)
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public static void dialogSuccess(String success) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success")
                .setContentText(success)
                .showCancelButton(false)
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog dialog) {
                        dialog.dismiss();
                        /*if (context instanceof DetailActivity) {
                            ((DetailActivity)context).clearForm();
                        }*/
                    }
                }).show();
    }

    public static void dialogNetworkError() {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("ไม่ได้เชื่อมต่ออินเตอร์เน็ต!")
                .setContentText("กรุณาตั้งค่าการเชื่อมต่อ")
                .showCancelButton(true)
                .setCancelText("ยกเลิก")
                .setConfirmText("ตั้งค่า")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.setClassName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                        ((Activity) context).startActivityForResult(intent, REQUEST_SETTINGS);
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        ((Activity) context).finish();
                    }
                })
                .show();
    }

    public static void dialogChooser(final Intent intent1, final int result1, final Intent intent2, final int result2) {
        final PermissionUtil permissionUtil = new PermissionUtil();
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("เลือกรูปภาพ")
                .setContentText("เลือกจากแกลลอรี่หรือถ่ายรูป")
                .setCancelText("แกลเลอรี่")
                .setConfirmText("กล้อง")
                .showCancelButton(false)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        if (permissionUtil.checkMarshMellowPermission()) {
                            if (permissionUtil.verifyPermissions(context, permissionUtil.getCameraPermissions()))
                                ((Activity) context).startActivityForResult(intent1, result1);
                            else
                                ActivityCompat.requestPermissions((Activity) context, permissionUtil.getCameraPermissions(), result1);
                        } else
                            ((Activity) context).startActivityForResult(intent1, result1);
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        if (permissionUtil.checkMarshMellowPermission()) {
                            if (permissionUtil.verifyPermissions(context, permissionUtil.getGalleryPermissions()))
                                ((Activity) context).startActivityForResult(intent2, result2);
                            else
                                ActivityCompat.requestPermissions((Activity) context, permissionUtil.getGalleryPermissions(), result2);
                        } else
                            ((Activity) context).startActivityForResult(intent2, result2);
                    }
                })
                .show();
    }
}
