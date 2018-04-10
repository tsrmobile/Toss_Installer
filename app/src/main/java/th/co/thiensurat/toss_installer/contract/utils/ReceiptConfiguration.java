package th.co.thiensurat.toss_installer.contract.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.centerm.smartpos.aidl.printer.AidlPrinter;
import com.centerm.smartpos.constant.Constant;
import com.datecs.api.printer.Printer;
import com.github.danielfelgar.drawreceiptlib.ReceiptBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.contract.printer.documentcontroller.PrintTextInfo;
import th.co.thiensurat.toss_installer.contract.printer.documentcontroller.ThemalPrintController;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.DateFormateUtilities;
import th.co.thiensurat.toss_installer.utils.ImageConfiguration;
import th.co.thiensurat.toss_installer.utils.MyApplication;

import static com.thefinestartist.utils.content.ContextUtil.getExternalFilesDir;

/**
 * Created by teerayut.k on 2/12/2018.
 */

public class ReceiptConfiguration {

    private String TSR;
    private String customer;
    private String salename;
    private String installername;

    private String type = null;
    private String phone = null;
    private String period = null;
    private String headerType = null;
    private String customerType = null;
    private String printerNameOrMac = null;

    private String empid;
    private File empSign;

    private String kViruchPath;
    private String empSignPath;
    private String witnessPath;
    private String customerPath;
    private String contactPaht;

    private float qty = 0;
    private float perPeriod = 0;
    private float normalPrice = 0;
    private float discountPrice = 0;
    private float grandTotalPrice = 0;

    private float lastgrandTotal = 0;
    private float lastnormalPrice = 0;
    private float lastdiscountPrice = 0;

    private File installationPath;
    private File contactWitnessFile;

    private Bitmap bmp = null;
    private Bitmap bmpContact = null;
    private Bitmap bmpWitness = null;
    private Bitmap bmpCustomer = null;
    private Bitmap bmpEmployee = null;
    private Bitmap bmpContactWitness = null;
    private Bitmap bmpInstalltion = null;

    private Context context;
    private JobItem jobItem;
    private String contactNumber;
    private List<ProductItem> productItemList;
    private List<AddressItem> addressItemList;

    private InputStream ins = null;
    private BitmapFactory.Options bmOptions;
    //private ReceiptBuilder receiptBuilder;
    private DecimalFormat df = new DecimalFormat("#,###.00");

    private ImageConfiguration imgConfig;

    public ReceiptConfiguration(Context context) {
        this.context = context;
        bmOptions = new BitmapFactory.Options();
        bmOptions.inScaled = true;
        bmOptions.inSampleSize = 4;
        bmOptions.inDither=false;
        bmOptions.inPurgeable=true;
        bmOptions.inInputShareable=true;
        empid = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID);
        imgConfig = new ImageConfiguration(context);
    }

    public void setReceiptInfoActivity(String printerNameOrMac, JobItem job, String number, List<ProductItem> productList, List<AddressItem> addressItem) {
        this.printerNameOrMac = printerNameOrMac;
        this.jobItem = job;
        this.contactNumber = number;
        this.productItemList = productList;
        this.addressItemList = addressItem;
        //imgConfig = new ImageConfiguration(context);
    }

    private void CEOSignature() {
        InputStream ins;
        InputStream ins2;
        try {
            ins = context.getResources().getAssets().open("k_viruch.png");
            ins2 = context.getResources().getAssets().open("witness.png");
            bmp = BitmapFactory.decodeStream(ins);
            bmpWitness = BitmapFactory.decodeStream(ins2);
            ins.close();
            ins2.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IOExceiption", e.getLocalizedMessage());
        }
    }

    public void createSignForDatecPrinter(String path1, String path2) {
        this.customerPath = path1;
        this.contactPaht = path2;

        contactWitnessFile = new File(imgConfig.getAlbumStorageDir(empid), String.format("signature_witness.jpg"));
        installationPath = new File(imgConfig.getAlbumStorageDir(jobItem.getOrderid()), String.format("signature_%s_install.jpg", jobItem.getOrderid()));
        bmpInstalltion = BitmapFactory.decodeFile(installationPath.getAbsolutePath(), bmOptions);
        bmpContact = BitmapFactory.decodeFile(contactPaht, bmOptions);
        bmpContactWitness = BitmapFactory.decodeFile(contactWitnessFile.getAbsolutePath(), bmOptions);
    }

    public void setPathSignature(String path1, String path2) {
        this.empSignPath = path1;
        this.customerPath = path2;
        bmpEmployee = BitmapFactory.decodeFile(empSignPath, bmOptions);
        bmpCustomer = BitmapFactory.decodeFile(customerPath, bmOptions);
    }

    public Bitmap print(String printType) {
        this.type = printType;
        CEOSignature();
        TSR = "นายวิรัช วงศ์นิรันดร์";
        customer = String.format("%s%s %s", DateFormateUtilities.trim(jobItem.getTitle()), DateFormateUtilities.trim(jobItem.getFirstName()), DateFormateUtilities.trim(jobItem.getLastName()));
        installername = "" + DateFormateUtilities.trim( MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_FIRSTNAME) + " " + MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_LASTNAME));
        salename = "" + DateFormateUtilities.trim(jobItem.getPresale());
        if (printerNameOrMac.equals("DPP-350")) {
            return datecPrint();
        } else if (printerNameOrMac.equals("Virtual Bluetooth Printer")) {
            return ePosPrint();
        } else {
            return anotherPrint();
        }
    }

    public Bitmap printReceipt() {
        /*TSR = "นายวิรัช วงศ์นิรันดร์";
        customer = String.format("%s%s %s", DateFormateUtilities.trim(jobItem.getTitle()), DateFormateUtilities.trim(jobItem.getFirstName()), DateFormateUtilities.trim(jobItem.getLastName()));

        salename = "" + DateFormateUtilities.trim(jobItem.getPresale());*/
        File filEmp = new File(imgConfig.getAlbumStorageDir(empid), String.format("signature_%s.jpg", empid));
        bmpEmployee = BitmapFactory.decodeFile(filEmp.getAbsolutePath(), bmOptions);
        installername = "" + DateFormateUtilities.trim( MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_FIRSTNAME) + " " + MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_LASTNAME));
        /*if (printerNameOrMac.equals("DPP-350")) {
            return datecPrintReceipt();
        } else if (printerNameOrMac.equals("Virtual Bluetooth Printer")) {
            return ePosPrintReceipt();
        } else {
            return anotherPrintReceipt();
        }*/
        return ePosPrintReceipt();
    }

    public Bitmap headerPrint() {
        Bitmap bmp = null;
        try {
            ins = context.getResources().getAssets().open("tsr_header.png");
            bmp = BitmapFactory.decodeStream(ins);
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmp;
    }

    private Bitmap ePosPrint() {
        try {
            if (type.equals("contact")) {
                for (ProductItem item : productItemList) {
                    if (item.getProductPayType().equals("1")) {
                        customerType = "ผู้ซื้อ";
                        headerType = "ใบสัญญาซื้อขาย";
                    } else {
                        customerType = "ผู้เช่าซื้อ";
                        headerType = "ใบสัญญาเช่าซื้อ";
                    }
                }
            } else if (type.equals("installreceipt")) {
                headerType = "ใบรับการติดตั้ง";
            }

            ReceiptBuilder receiptBuilder = new ReceiptBuilder(1650);
            receiptBuilder.setAlign(Paint.Align.CENTER);
            receiptBuilder.setMargin(15, 0);
            receiptBuilder.setAlign(Paint.Align.CENTER);
            receiptBuilder.setColor(Color.BLACK);
            receiptBuilder.setTextSize(80);
            receiptBuilder.addParagraph();
            receiptBuilder.addText(headerType);
            receiptBuilder.addBlankSpace(30);
            receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("วันที่ทำสัญญา", false);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addText(DateFormateUtilities.dateFormat(new Date()));
            receiptBuilder.addBlankSpace(60);
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("เลขที่สัญญา", false);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addText(contactNumber);
            receiptBuilder.addBlankSpace(60);
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("เลขที่อ้างอิง", false);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addText(jobItem.getOrderid());
            receiptBuilder.addBlankSpace(60);
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText(customerType, false);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addText(customer);
            receiptBuilder.addBlankSpace(60);
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("เลขที่บัตรฯ", false);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addText(jobItem.getIDCard());
            receiptBuilder.addBlankSpace(60);
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("ที่อยู่บัตรฯ", false);

        for (AddressItem item : addressItemList) {
            if (item.getAddressType().equals("AddressIDCard")) {
                receiptBuilder.setAlign(Paint.Align.RIGHT);
                receiptBuilder.addText(item.getAddrDetail());
                receiptBuilder.addBlankSpace(60);
                receiptBuilder.addText("ต." + item.getSubdistrict() + " อ." + item.getDistrict());
                receiptBuilder.addBlankSpace(60);
                receiptBuilder.addText("จ." + item.getProvince() + " " + item.getZipcode());
                receiptBuilder.addBlankSpace(60);
            }

            if (!item.getPhone().isEmpty()) {
                phone = item.getPhone();
            } else if (!item.getMobile().isEmpty()) {
                phone = item.getMobile();
            } else if (!item.getOffice().isEmpty()) {
                phone = item.getOffice();
            }
        }

        receiptBuilder.addBlankSpace(60);
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("ที่อยู่ติดตั้ง", false);

        for (AddressItem item : addressItemList) {
            if (item.getAddressType().equals("AddressInstall")) {
                receiptBuilder.setAlign(Paint.Align.RIGHT);
                receiptBuilder.addText(item.getAddrDetail());
                receiptBuilder.addBlankSpace(60);
                receiptBuilder.addText("ต." + item.getSubdistrict() + " อ." + item.getDistrict());
                receiptBuilder.addBlankSpace(60);
                receiptBuilder.addText("จ." + item.getProvince() + " " + item.getZipcode());
                receiptBuilder.addBlankSpace(60);
            }

            if (!item.getPhone().isEmpty()) {
                phone = item.getPhone();
            } else if (!item.getMobile().isEmpty()) {
                phone = item.getMobile();
            } else if (!item.getOffice().isEmpty()) {
                phone = item.getOffice();
            }
        }

        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("เบอร์โทรติดต่อ", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText(phone);
        receiptBuilder.addBlankSpace(60);

        receiptBuilder.addParagraph();
        for (int i = 0; i < productItemList.size(); i++) {
            ProductItem item = productItemList.get(i);
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("สินค้า", false);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addText(item.getProductName());
            receiptBuilder.addBlankSpace(60);
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("รุ่น", false);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addText(item.getProductModel());
            receiptBuilder.addBlankSpace(60);
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("เลขเครื่อง", false);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addText(item.getProductSerial());
            receiptBuilder.addBlankSpace(60);

            if (type.equals("contact")) {
                if (item.getProductPayType().equals("2")) {
                    qty += Float.parseFloat(item.getProductQty());
                    normalPrice = Float.parseFloat(item.getProductPrice());
                    discountPrice = Float.parseFloat(item.getProductDiscount());
                    period = item.getProductPayPeriods();
                    perPeriod = Float.parseFloat(item.getProductPayPerPeriods());
                    grandTotalPrice = (normalPrice - discountPrice);
                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("ราคา", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(df.format(normalPrice) + " บาท");
                    receiptBuilder.addBlankSpace(60);
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("ส่วนลด", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(df.format(normalPrice) + " บาท");
                    receiptBuilder.addBlankSpace(60);
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("ราคาสุทธิ", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(df.format(grandTotalPrice) + " บาท");
                    receiptBuilder.addBlankSpace(60);
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("จำนวนงวด", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(period + " งวด");
                    receiptBuilder.addBlankSpace(60);
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("งวดละ", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(df.format(perPeriod) + " บาท");
                    receiptBuilder.addBlankSpace(60);
                } else {
                    normalPrice = Float.parseFloat(item.getProductPrice());
                    discountPrice = Float.parseFloat(item.getProductDiscount());
                    grandTotalPrice = (normalPrice - discountPrice);
                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("ราคา", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(df.format(normalPrice) + " บาท");
                    receiptBuilder.addBlankSpace(60);
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("ส่วนลด", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(df.format(discountPrice) + " บาท");
                    receiptBuilder.addBlankSpace(60);
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("ราคาสุทธิ", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(df.format(grandTotalPrice) + " บาท");
                }

                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Paint.Align.CENTER);
                    receiptBuilder.addLine();
                    receiptBuilder.addBlankSpace(60);
                    qty += Float.parseFloat(item.getProductQty());
                    lastnormalPrice += Float.parseFloat(item.getProductPrice());
                    lastdiscountPrice += Float.parseFloat(item.getProductDiscount());
                    if (i == productItemList.size() - 1) {
                        receiptBuilder.addParagraph();
                        receiptBuilder.setAlign(Paint.Align.LEFT);
                        receiptBuilder.addText("รวมส่วนลด", false);
                        receiptBuilder.setAlign(Paint.Align.RIGHT);
                        receiptBuilder.addText(df.format(discountPrice) + " บาท");
                        receiptBuilder.addBlankSpace(60);
                        receiptBuilder.setAlign(Paint.Align.LEFT);
                        receiptBuilder.addText("รวมเป็นเงิน", false);
                        receiptBuilder.setAlign(Paint.Align.RIGHT);
                        receiptBuilder.addText(df.format(grandTotalPrice) + " บาท");
                    }
            } else if (type.equals("installreceipt")) {

            }
        }

            if (type.equals("contact")) {
                if (!productItemList.get(0).getProductPayType().equals("2")) {
                    receiptBuilder.addParagraph();
                    receiptBuilder.addBlankSpace(280);
                    receiptBuilder.setAlign(Paint.Align.CENTER);
                    receiptBuilder.addImage(imgConfig.getResizedBitmap(bmp, 640, 240));
                    receiptBuilder.setAlign(Paint.Align.CENTER);
                    receiptBuilder.addText("________________________________________________");
                    receiptBuilder.addBlankSpace(60);
                    receiptBuilder.setAlign(Paint.Align.CENTER);
                    receiptBuilder.addText("ผู้ขาย");
                    receiptBuilder.addBlankSpace(60);
                    receiptBuilder.setAlign(Paint.Align.CENTER);
                    receiptBuilder.addText(TSR);

                    receiptBuilder.addParagraph();
                    receiptBuilder.addBlankSpace(100);
                    receiptBuilder.setAlign(Paint.Align.CENTER);
                    receiptBuilder.addImage(imgConfig.getResizedBitmap(bmpCustomer, 640, 240));
                    receiptBuilder.setAlign(Paint.Align.CENTER);
                    receiptBuilder.addText("________________________________________________");
                    receiptBuilder.addBlankSpace(60);
                    receiptBuilder.setAlign(Paint.Align.CENTER);
                    receiptBuilder.addText("ผู้ซื้อ");
                    receiptBuilder.addBlankSpace(60);
                    receiptBuilder.setAlign(Paint.Align.CENTER);
                    receiptBuilder.addText(customer);
                } else {
                    receiptBuilder.addParagraph();
                    receiptBuilder.addBlankSpace(280);
                    receiptBuilder.setAlign(Paint.Align.CENTER);
                    receiptBuilder.addImage(imgConfig.getResizedBitmap(bmp, 640, 240));
                    receiptBuilder.setAlign(Paint.Align.CENTER);
                    receiptBuilder.addText("________________________________________________");
                    receiptBuilder.addBlankSpace(60);
                    receiptBuilder.setAlign(Paint.Align.CENTER);
                    receiptBuilder.addText("ผู้ให้เช่าซื้อ");
                    receiptBuilder.addBlankSpace(60);
                    receiptBuilder.setAlign(Paint.Align.CENTER);
                    receiptBuilder.addText(TSR);

                    receiptBuilder.addParagraph();
                    receiptBuilder.addBlankSpace(100);
                    receiptBuilder.setAlign(Paint.Align.CENTER);
                    receiptBuilder.addImage(imgConfig.getResizedBitmap(bmpCustomer, 680, 240));
                    receiptBuilder.setAlign(Paint.Align.CENTER);
                    receiptBuilder.addText("________________________________________________");
                    receiptBuilder.addBlankSpace(60);
                    receiptBuilder.setAlign(Paint.Align.CENTER);
                    receiptBuilder.addText("ผู้เช่าซื้อ");
                    receiptBuilder.addBlankSpace(60);
                    receiptBuilder.setAlign(Paint.Align.CENTER);
                    receiptBuilder.addText(customer);

                    receiptBuilder.addParagraph();
                    receiptBuilder.addBlankSpace(100);
                    receiptBuilder.setAlign(Paint.Align.CENTER);
                    receiptBuilder.addImage(imgConfig.getResizedBitmap(bmpEmployee, 680, 240));
                    receiptBuilder.setAlign(Paint.Align.CENTER);
                    receiptBuilder.addText("________________________________________________");
                    receiptBuilder.addBlankSpace(60);
                    receiptBuilder.setAlign(Paint.Align.CENTER);
                    receiptBuilder.addText("พยาน");
                    receiptBuilder.addBlankSpace(60);
                    receiptBuilder.setAlign(Paint.Align.CENTER);
                    receiptBuilder.addText("(" + installername + ")");

                    receiptBuilder.addBlankSpace(100);
                    receiptBuilder.setAlign(Paint.Align.CENTER);
                    receiptBuilder.addImage(imgConfig.getResizedBitmap(bmpWitness, 640, 240));
                    receiptBuilder.setAlign(Paint.Align.CENTER);
                    receiptBuilder.addText("________________________________________________");
                    receiptBuilder.addBlankSpace(60);
                    receiptBuilder.setAlign(Paint.Align.CENTER);
                    receiptBuilder.addText("พยาน");
                    receiptBuilder.addBlankSpace(60);
                    receiptBuilder.setAlign(Paint.Align.CENTER);
                    receiptBuilder.addText("(" + salename + ")");
                }
            } else if (type.equals("installreceipt")) {
                receiptBuilder.addParagraph();
                receiptBuilder.addBlankSpace(280);
                receiptBuilder.setAlign(Paint.Align.CENTER);
                receiptBuilder.addImage(imgConfig.getResizedBitmap(bmpCustomer, 640, 240));
                receiptBuilder.setAlign(Paint.Align.CENTER);
                receiptBuilder.addText("________________________________________________");
                receiptBuilder.addBlankSpace(60);
                receiptBuilder.setAlign(Paint.Align.CENTER);
                receiptBuilder.addText("ผู้รับการติดตั้ง");
                receiptBuilder.addBlankSpace(60);
                receiptBuilder.setAlign(Paint.Align.CENTER);
                receiptBuilder.addText("(" + customer + ")");

                receiptBuilder.addParagraph();
                receiptBuilder.addBlankSpace(100);
                receiptBuilder.setAlign(Paint.Align.CENTER);
                receiptBuilder.addImage(imgConfig.getResizedBitmap(bmpEmployee, 640, 240));
                receiptBuilder.setAlign(Paint.Align.CENTER);
                receiptBuilder.addText("________________________________________________");
                receiptBuilder.addBlankSpace(60);
                receiptBuilder.setAlign(Paint.Align.CENTER);
                receiptBuilder.addText("ผู้ติดตั้ง");
                receiptBuilder.addBlankSpace(60);
                receiptBuilder.addBlankSpace(60);
                receiptBuilder.setAlign(Paint.Align.CENTER);
                receiptBuilder.addText("(" + installername + ")");
            }
            return receiptBuilder.build();
        } catch (Exception e) {
            Log.e("Draw receipt", e.getLocalizedMessage());
        }
        return null;
    }

    private Bitmap datecPrint() {
        if (type.equals("contact")) {
            for (ProductItem item : this.productItemList) {
                if (item.getProductPayType().equals("1")) {
                    customerType = "ผู้ซื้อ";
                    headerType = "ใบสัญญาซื้อขาย";
                } else {
                    customerType = "ผู้เช่าซื้อ";
                    headerType = "ใบสัญญาเช่าซื้อ";
                }
            }
        } else if (type.equals("installreceipt")) {
            headerType = "ใบรับการติดตั้ง";
        }

        ReceiptBuilder receiptBuilder = new ReceiptBuilder(574);
        receiptBuilder.setMargin(5, 0);
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.setColor(Color.BLACK);
        receiptBuilder.setTextSize(22);
        receiptBuilder.addImage(headerPrint());
        receiptBuilder.addBlankSpace(20);
        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Paint.Align.CENTER);
        receiptBuilder.addText(headerType);
        receiptBuilder.addBlankSpace(20);
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("วันที่ทำสัญญา", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText(DateFormateUtilities.dateFormat(new Date()));
        receiptBuilder.addBlankSpace(20);
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("เลขที่สัญญา", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText(contactNumber);
        receiptBuilder.addBlankSpace(20);
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("เลขที่อ้างอิง", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText(jobItem.getOrderid());
        receiptBuilder.addBlankSpace(20);
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText(customerType, false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText(customer);
        receiptBuilder.addBlankSpace(20);
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("เลขที่บัตรฯ", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText(jobItem.getIDCard());
        receiptBuilder.addBlankSpace(20);


        for (AddressItem item : addressItemList) {
            if (item.getAddressType().equals("AddressIDCard")) {
                receiptBuilder.setAlign(Paint.Align.LEFT);
                receiptBuilder.addText("ที่อยู่บัตรฯ", false);

                receiptBuilder.setAlign(Paint.Align.RIGHT);
                receiptBuilder.addText(item.getAddrDetail());
                receiptBuilder.addBlankSpace(20);
                receiptBuilder.setAlign(Paint.Align.RIGHT);
                receiptBuilder.addText("ต." + item.getSubdistrict() + " อ." + item.getDistrict());
                receiptBuilder.addBlankSpace(20);
                receiptBuilder.setAlign(Paint.Align.RIGHT);
                receiptBuilder.addText("จ." + item.getProvince() + " " + item.getZipcode());
                receiptBuilder.addBlankSpace(20);
            }

            if (!item.getPhone().isEmpty()) {
                phone = item.getPhone();
            } else if (!item.getMobile().isEmpty()) {
                phone = item.getMobile();
            } else if (!item.getOffice().isEmpty()) {
                phone = item.getOffice();
            }

            if (item.getAddressType().equals("AddressInstall")) {
                receiptBuilder.setAlign(Paint.Align.LEFT);
                receiptBuilder.addText("ที่อยู่ติดตั้ง", false);

                receiptBuilder.setAlign(Paint.Align.RIGHT);
                receiptBuilder.addText(item.getAddrDetail());
                receiptBuilder.addBlankSpace(20);
                receiptBuilder.setAlign(Paint.Align.RIGHT);
                receiptBuilder.addText("ต." + item.getSubdistrict() + " อ." + item.getDistrict());
                receiptBuilder.addBlankSpace(20);
                receiptBuilder.setAlign(Paint.Align.RIGHT);
                receiptBuilder.addText("จ." + item.getProvince() + " " + item.getZipcode());
                receiptBuilder.addBlankSpace(20);
            }
        }

        receiptBuilder.addBlankSpace(20);
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("ที่อยู่ติดตั้ง", false);

        for (AddressItem item : addressItemList) {
            if (item.getAddressType().equals("AddressInstall")) {
                receiptBuilder.setAlign(Paint.Align.RIGHT);
                receiptBuilder.addText(item.getAddrDetail());
                receiptBuilder.addBlankSpace(20);
                receiptBuilder.setAlign(Paint.Align.RIGHT);
                receiptBuilder.addText("ต." + item.getSubdistrict() + " อ." + item.getDistrict());
                receiptBuilder.addBlankSpace(20);
                receiptBuilder.setAlign(Paint.Align.RIGHT);
                receiptBuilder.addText("จ." + item.getProvince() + " " + item.getZipcode());
                receiptBuilder.addBlankSpace(20);
            }

            if (!item.getPhone().isEmpty()) {
                phone = item.getPhone();
            } else if (!item.getMobile().isEmpty()) {
                phone = item.getMobile();
            } else if (!item.getOffice().isEmpty()) {
                phone = item.getOffice();
            }
        }

        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("เบอร์โทรติดต่อ", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText(phone);
        receiptBuilder.addBlankSpace(20);

        for (int i = 0; i < productItemList.size(); i++) {
            ProductItem item = productItemList.get(i);
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("สินค้า", false);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addText(item.getProductName());
            receiptBuilder.addBlankSpace(20);
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("รุ่น", false);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addText(item.getProductModel());
            receiptBuilder.addBlankSpace(20);
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("เลขเครื่อง", false);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addText(item.getProductSerial());
            receiptBuilder.addBlankSpace(20);

            if (type.equals("contact")) {

                if (item.getProductPayType().equals("2")) {
                    qty += Float.parseFloat(item.getProductQty());
                    normalPrice = Float.parseFloat(item.getProductPrice());
                    discountPrice = Float.parseFloat(item.getProductDiscount());
                    period = item.getProductPayPeriods();
                    perPeriod = Float.parseFloat(item.getProductPayPerPeriods());
                    grandTotalPrice = (normalPrice - discountPrice);

                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("ราคา", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(df.format(normalPrice) + " บาท");
                    receiptBuilder.addBlankSpace(20);
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("ส่วนลด", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(df.format(discountPrice) + " บาท");
                    receiptBuilder.addBlankSpace(20);
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("ราคาสุทธิ", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(df.format(grandTotalPrice) + " บาท");
                    receiptBuilder.addBlankSpace(20);
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("จำนวนงวด", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(period + " งวด");
                    receiptBuilder.addBlankSpace(20);
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("งวดละ", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(df.format(perPeriod) + " บาท");
                    receiptBuilder.addBlankSpace(20);
                } else {
                    normalPrice = Float.parseFloat(item.getProductPrice());
                    discountPrice = Float.parseFloat(item.getProductDiscount());
                    grandTotalPrice = (normalPrice - discountPrice);

                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("ราคา", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(df.format(normalPrice) + " บาท");
                    receiptBuilder.addBlankSpace(20);
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("ส่วนลด", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(df.format(discountPrice) + " บาท");
                    receiptBuilder.addBlankSpace(20);
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("ราคาสุทธิ", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(df.format(grandTotalPrice) + " บาท");
                }

                receiptBuilder.setAlign(Paint.Align.LEFT);
                receiptBuilder.addLine();
                receiptBuilder.addBlankSpace(20);

                qty += Float.parseFloat(item.getProductQty());
                lastnormalPrice += Float.parseFloat(item.getProductPrice());
                lastdiscountPrice += Float.parseFloat(item.getProductDiscount());
                if (i == productItemList.size() - 1) {
                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("รวมส่วนลด", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(df.format(discountPrice) + " บาท");
                    receiptBuilder.addBlankSpace(20);
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("รวมเป็นเงิน", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(df.format(grandTotalPrice) + " บาท");
                    receiptBuilder.addBlankSpace(20);
                }
            } else if (type.equals("installreceipt")) {

            }
        }

        if (type.equals("contact")) {
            if (!productItemList.get(0).getProductPayType().equals("2")) {
                receiptBuilder.addParagraph();
                receiptBuilder.setAlign(Paint.Align.CENTER);
                receiptBuilder.addImage(bmpContact);
                receiptBuilder.setAlign(Paint.Align.LEFT);
                receiptBuilder.addLine(260);
                receiptBuilder.setAlign(Paint.Align.RIGHT);
                receiptBuilder.addLine(260);

                receiptBuilder.addParagraph();
                receiptBuilder.setAlign(Paint.Align.LEFT);
                receiptBuilder.addText("                    ผู้ขาย", false);
                receiptBuilder.setAlign(Paint.Align.RIGHT);
                receiptBuilder.addText("ผู้ซื้อ                         ");
            } else {
                receiptBuilder.addParagraph();
                receiptBuilder.setAlign(Paint.Align.CENTER);
                receiptBuilder.addImage(bmpContact);

                receiptBuilder.addParagraph();
                receiptBuilder.setAlign(Paint.Align.LEFT);
                receiptBuilder.addLine(260);
                receiptBuilder.setAlign(Paint.Align.RIGHT);
                receiptBuilder.addLine(260);

                receiptBuilder.addParagraph();
                receiptBuilder.setAlign(Paint.Align.LEFT);
                receiptBuilder.addText("                 ผู้ให้เช่าซื้อ", false);
                receiptBuilder.setAlign(Paint.Align.RIGHT);
                receiptBuilder.addText("ผู้เช่าซื้อ                       ");
                receiptBuilder.addBlankSpace(20);

                receiptBuilder.addParagraph();
                receiptBuilder.setAlign(Paint.Align.CENTER);
                receiptBuilder.addImage(bmpContactWitness);

                receiptBuilder.addParagraph();
                receiptBuilder.setAlign(Paint.Align.LEFT);
                receiptBuilder.addLine(260);
                receiptBuilder.setAlign(Paint.Align.RIGHT);
                receiptBuilder.addLine(260);

                receiptBuilder.addParagraph();
                receiptBuilder.setAlign(Paint.Align.LEFT);
                receiptBuilder.addText("                    พยาน", false);
                receiptBuilder.setAlign(Paint.Align.RIGHT);
                receiptBuilder.addText("พยาน                        ");

                receiptBuilder.addBlankSpace(20);
                receiptBuilder.setAlign(Paint.Align.LEFT);
                receiptBuilder.addText("          (" + installername + ")", false);
                receiptBuilder.setAlign(Paint.Align.RIGHT);
                receiptBuilder.addText("(" + salename + ")         ");
            }
        } else if (type.equals("installreceipt")) {
            receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Paint.Align.CENTER);
            receiptBuilder.addImage(bmpInstalltion);

            receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addLine(260);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addLine(260);

            receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("             ผู้รับการติดตั้ง", false);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addText("ผู้ติดตั้ง                    ");

            receiptBuilder.addBlankSpace(20);
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("          (" + customer + ")", false);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addText("(" + installername + ")         ");
        }

        return receiptBuilder.build();
    }

    private Bitmap anotherPrint() {
        return null;
    }


    /**
     * Print Receipt
     * @return
     */
    private Bitmap ePosPrintReceipt() {
        ReceiptBuilder receiptBuilder = new ReceiptBuilder(1650);
        receiptBuilder.setMargin(15, 0);
        receiptBuilder.setAlign(Paint.Align.CENTER);
        receiptBuilder.setColor(Color.BLACK);
        receiptBuilder.setTextSize(96);
        receiptBuilder.addText("บริษัท เธียรสุรัตน์ จำกัด (มหาชน)");
        receiptBuilder.addBlankSpace(60);
        receiptBuilder.addText("เลขประจำตัวผู้เสียภาษี 0107556000213");
        receiptBuilder.addBlankSpace(60);
        receiptBuilder.addText("โทร.1210");
        receiptBuilder.addBlankSpace(60);
        receiptBuilder.addParagraph();
        receiptBuilder.setTextSize(92);
        receiptBuilder.addText("ใบเสร็จรับเงิน/ใบกำกับภาษีอย่างย่อ");
        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("วันที่รับเงิน", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText("09/04/2561 11:04");
        receiptBuilder.addBlankSpace(60);
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("เลขที่", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText("TSR0000001");
        receiptBuilder.addBlankSpace(60);
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("เลขที่สัญญา", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText("6104000001");
        receiptBuilder.addBlankSpace(60);
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("ชื่อลูกค้า", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText("Thiensurat.co.th");
        receiptBuilder.addBlankSpace(60);
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("เครื่องกรองน้ำเซฟ (Salf Alkaline)");
        receiptBuilder.addBlankSpace(60);
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("ชำระงวดที่ 1", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText("1,000.00");
        receiptBuilder.addBlankSpace(60);
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("คงเหลือ งวดที่ 2" + " ถึง 6", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText("5,000.00");
        receiptBuilder.addParagraph();
        receiptBuilder.addBlankSpace(80);
        receiptBuilder.setAlign(Paint.Align.CENTER);
        receiptBuilder.addImage(imgConfig.getResizedBitmap(bmpEmployee, 700, 240));
        receiptBuilder.setAlign(Paint.Align.CENTER);
        receiptBuilder.addText("________________________________________________");
        receiptBuilder.addBlankSpace(60);
        receiptBuilder.setAlign(Paint.Align.CENTER);
        receiptBuilder.addText("(" + installername + ")");
        receiptBuilder.addBlankSpace(60);
        receiptBuilder.setAlign(Paint.Align.CENTER);
        receiptBuilder.addText("ผู้รับเงิน");

        return receiptBuilder.build();
    }

    private Bitmap datecPrintReceipt() {
        ReceiptBuilder receiptBuilder = new ReceiptBuilder(574);
        receiptBuilder.setMargin(5, 0);
        receiptBuilder.setAlign(Paint.Align.CENTER);
        receiptBuilder.setColor(Color.BLACK);
        receiptBuilder.setTextSize(80);
        receiptBuilder.addText("บริษัท เธียรสุรัตน์ จำกัด (มหาชน)");
        receiptBuilder.addText("เลขประจำตัวผู้เสียภาษี 0107556000213");
        receiptBuilder.addText("โทร.1210");
        receiptBuilder.addParagraph();
        receiptBuilder.addText("ใบเสร็จรับเงิน/ใบกำกับภาษีอย่างย่อ");
        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("วันที่รับเงิน", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText("");
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("เลขที่", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText("");
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("เลขที่สัญญา", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText("");
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("ชื่อลูกค้า", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText("");
        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("เครื่องกรองน้ำเซฟ (Salf Alkaline)");
        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("ชำระงวดที่", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText("");
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("คงเหลือ งวดที่" + "ถึง" + "เป็นเงิน", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText("");
        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Paint.Align.CENTER);
        receiptBuilder.addImage(imgConfig.getResizedBitmap(bmpEmployee, 640, 240));
        receiptBuilder.setAlign(Paint.Align.CENTER);
        receiptBuilder.addText("________________________________________________");
        receiptBuilder.addBlankSpace(60);
        receiptBuilder.setAlign(Paint.Align.CENTER);
        receiptBuilder.addText("(" + installername + ")");
        receiptBuilder.addBlankSpace(60);
        receiptBuilder.setAlign(Paint.Align.CENTER);
        receiptBuilder.addText("ผู้รับเงิน");


        return receiptBuilder.build();
    }

    private Bitmap anotherPrintReceipt() {

        return null;
    }

}
