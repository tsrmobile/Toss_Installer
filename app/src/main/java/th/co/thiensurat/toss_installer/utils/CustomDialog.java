package th.co.thiensurat.toss_installer.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;
import th.co.thiensurat.toss_installer.MainActivity;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.contract.ContractActivity;
import th.co.thiensurat.toss_installer.installation.InstallationActivity;

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
        sweetAlertDialog.setTitleText("กำลังโหลด...");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
    }

    public static void dialogLongLoading() {
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.colorPrimaryDark));
        sweetAlertDialog.setTitleText("กำลังอัพโหลดข้อมูล...");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
    }

    public static void dialogLoadingWithCancel() {
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.colorPrimaryDark));
        sweetAlertDialog.setTitleText("กำลังโหลด...");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setConfirmText("หยุด");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });
        sweetAlertDialog.show();
    }

    public static void dialogInitialData() {
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.colorPrimaryDark));
        sweetAlertDialog.setTitleText("กำลังเตรียมข้อมูล...");
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
                .setConfirmText("ตกลง")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public static void dialogSuccess(String success) {
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText("Success");
        sweetAlertDialog.setContentText(success);
        sweetAlertDialog.showCancelButton(false);
        sweetAlertDialog.setConfirmText("ตกลง");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog dialog) {
                        dialog.dismiss();
                        if (context instanceof ContractActivity) {
                            ((ContractActivity)context).addStep();
                        }
                    }
                }).show();
    }

    public static void dialogNetworkError() {
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText("ไม่ได้เชื่อมต่ออินเตอร์เน็ต!");
        sweetAlertDialog.setContentText("กรุณาตั้งค่าการเชื่อมต่อ");
        sweetAlertDialog.showCancelButton(true);
        sweetAlertDialog.setCancelText("ยกเลิก");
        sweetAlertDialog.setConfirmText("ตั้งค่า");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.setClassName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                        ((Activity) context).startActivityForResult(intent, REQUEST_SETTINGS);
                        sDialog.dismiss();
                    }
                });
        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        ((Activity) context).finish();
                    }
                });
        sweetAlertDialog.show();
    }

    public static void dialogBluetooth() {
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText("ไม่พบปริ้นเตอร์");
        sweetAlertDialog.setContentText("กรุณาเปิดบลูทูธ");
        sweetAlertDialog.showCancelButton(true);
        sweetAlertDialog.setCancelText("ยกเลิก");
        sweetAlertDialog.setConfirmText("ตั้งค่า");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        ((Activity) context).startActivityForResult(intent, Constance.REQUEST_BLUETOOTH_SETTINGS);
                    }
                });
        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });
        sweetAlertDialog.show();
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

    public static void dialogWarning(String warning) {
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText("คำเตือน");
        sweetAlertDialog.setContentText(warning);
        sweetAlertDialog.showCancelButton(false);
        sweetAlertDialog.setConfirmText("ตกลง");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog dialog) {
                        dialog.dismiss();
                        if (context instanceof InstallationActivity) {
                            Intent intent = new Intent(context, MainActivity.class);
                            ((InstallationActivity)context).startActivity(intent);
                        }
                    }
                }).show();
    }

    public static boolean dialogShowing() {
        if (sweetAlertDialog.isShowing()) {
            return true;
        } else {
            return false;
        }
    }
}
