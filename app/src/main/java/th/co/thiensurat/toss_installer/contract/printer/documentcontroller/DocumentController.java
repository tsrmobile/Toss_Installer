package th.co.thiensurat.toss_installer.contract.printer.documentcontroller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.DateFormateUtilities;
import th.co.thiensurat.toss_installer.utils.MyApplication;


public class DocumentController {

    //private static Context mContext = BHApplication.getContext();

    private static int LINE_SPACE = 4;
    private static int LINE_FEED = 250;
    private static int LAYOUT_WIDTH = 1080;
    private static int PRINTER_LAYOUT_WIDTH = 580;

    private static DecimalFormat df = new DecimalFormat("#,###.00");

    private static Context context;

    public DocumentController(Context context) {
        this.context = context;
    }

    private static String[] getText(String text, Paint p, float width) {
        String[] texts = text.split("\\s+");
        List<String> result = new ArrayList<String>();
        /*result.add(texts[0]);
        int ii = 1;
        while (ii < texts.length) {
            int index = result.size() - 1;
            String txt = result.get(index);
            txt += " " + texts[ii];
            Rect rect = new Rect();
            p.getTextBounds(txt, 0, txt.length(), rect);
//            Log.i("TEXT", txt + ": " + rect.width());
//            StaticLayout layout = new StaticLayout(txt, new TextPaint(p), rect.width() , Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
//            Log.i("TEXT", "Static " + txt + ": " + layout.getWidth());

            if (rect.width() < width) {
                result.set(index, txt);
            } else {
                result.add(texts[ii]);
            }

            ii++;
        }*/


        result.add("");
        int ii = 0;
        while (ii < texts.length) {
            int index = result.size() - 1;
            String txt = result.get(index);

            if (result.get(index).equals("")) {
                txt += texts[ii];
            } else {
                txt += " " + texts[ii];
            }
            //txt += " " + texts[ii];
            Rect rect = new Rect();
            p.getTextBounds(txt, 0, txt.length(), rect);
//            Log.i("TEXT", txt + ": " + rect.width());
//            StaticLayout layout = new StaticLayout(txt, new TextPaint(p), rect.width() , Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
//            Log.i("TEXT", "Static " + txt + ": " + layout.getWidth());

            if (rect.width() < width) {
                result.set(index, txt);
            } else {
                if (result.get(index).equals("")) {
                    result.set(index, txt);
                } else {
                    result.add(texts[ii]);
                }

                String newTxt = result.get(result.size() - 1);
                if (getWidth(newTxt, p) > width) {
                    int jj = newTxt.length() - 1;
                    boolean checkNewTxt = false;
                    while (checkNewTxt == false) {
                        String subTxt = newTxt.substring(0, jj);
                        if (getWidth(subTxt, p) < width) {
                            //result.add(subTxt);
                            result.set(result.size() - 1, subTxt);
                            checkNewTxt = true;
                        }
                        jj--;
                    }

                    if (jj != newTxt.length() - 1) {
                        String subTxt = newTxt.substring(jj + 1, newTxt.length());

                        if (ii + 1 < texts.length) {
                            if (getWidth(subTxt + " " + texts[ii + 1], p) < width) {
                                texts[ii + 1] = subTxt + " " + texts[ii + 1];
                            } else {
                                result.add(subTxt);
                            }
                            //texts[ii + 1] = subTxt  + " " + texts[ii + 1];
                        } else {
                            result.add(subTxt);
                        }
                    }
                } else {
                    result.set(result.size() - 1, texts[ii]);
                }
            }
            ii++;
        }

        return result.toArray(new String[result.size()]);
    }

    private static String[] getTextByPrintText(String text, int maxLength) {
        List<String> result = new ArrayList<String>();

        if(ThemalPrintController.getAlphabetOnly(text).length() <= maxLength){
            result.add(text);
        } else {
            String[] textArray = text.split("\\s+");
            result.add("");
            for(int i = 0; i < textArray.length; i++){
                int index = result.size() - 1;
                String tempText = result.get(index);

                if (tempText.equals("")) {
                    tempText += textArray[i];
                } else {
                    tempText += " " + textArray[i];
                }

                if(ThemalPrintController.getAlphabetOnly(tempText).length() <= maxLength){
                    result.set(index, tempText);
                } else {
                    String[] tempTextArray = tempText.split("\\s+");
                    String resultTempText1 = "";
                    String resultTempText2 = "";

                    if(tempTextArray.length != 1) {
                        for (int j = 0; j < tempTextArray.length; j++) {
                            if (j == (tempTextArray.length - 1)) {
                                resultTempText2 = tempTextArray[j];
                            } else {
                                if(resultTempText1.equals("")) {
                                    resultTempText1 += tempTextArray[j];
                                } else {
                                    resultTempText1 += " " + tempTextArray[j];
                                }
                            }
                        }
                        result.set(index, resultTempText1);
                        result.addAll(alignTextByLength(resultTempText2, maxLength));
                    } else {
                        List<String> tempStr = alignTextByLength(tempTextArray[0], maxLength);

                        for(int j = 0; j< tempStr.size(); j++){
                            if(j == 0 ){
                                result.set(index, tempStr.get(j));
                            } else{
                                result.add(tempStr.get(j));
                            }
                        }
                        //result.addAll(alignTextByLength(tempTextArray[0], maxLength));
                    }
                }
            }
        }

        return result.toArray(new String[result.size()]);
    }

    private static List<String> alignTextByLength(String text, int length){
        List<String> result = new ArrayList<String>();

        if(ThemalPrintController.getAlphabetOnly(text).length() <= length){
            result.add(text);
        } else {
            result.add("");
            int start = 0;
            for (int i = 0; i < text.length(); i++){
                int index = result.size() - 1;
                String tempText = text.substring(start, i + 1);

                if(ThemalPrintController.getAlphabetOnly(tempText).length() <= length){
                    result.set(index, tempText);
                } else {
                    if(i != text.length() - 1){
                        start = i;
                        result.add("");
                    }
                }
            }
        }
        return result;
    }

    private static List<PrintTextInfo> getTextAlignCenter(String text){
        List<PrintTextInfo> result = new ArrayList<>();

        String[] tempText = getTextByPrintText(text, ThemalPrintController.maxTextLength );

        for(int i = 0; i < tempText.length; i++){
            result.add(new PrintTextInfo(ThemalPrintController.calculateCenter(tempText[i])));
        }
        return result;
    }

    private static List<PrintTextInfo> getTextAlignCenter(String text, PrintTextInfo.FontType fontType){
        List<PrintTextInfo> result = new ArrayList<>();

        String[] tempText = getTextByPrintText(text, ThemalPrintController.maxTextLength );

        for(int i = 0; i < tempText.length; i++){
            result.add(new PrintTextInfo(ThemalPrintController.calculateCenter(tempText[i]), fontType));
        }
        return result;
    }

    public List<String> setTextAlignCenter(String text) {
        List<String> result = new ArrayList<>();

        String[] tempText = getTextByPrintText(text, ThemalPrintController.maxTextLength );

        for(int i = 0; i < tempText.length; i++){
            result.add(ThemalPrintController.calculateCenter(tempText[i]));
        }
        return result;
    }

    private static List<PrintTextInfo> getTextAlignLeftByOffSetLeft(String left, String right){
        List<PrintTextInfo> result = new ArrayList<>();

        int offSetLeft = 20;
        String[] tempLeft = getTextByPrintText(left, offSetLeft);
        String[] tempRight = getTextByPrintText(right, ThemalPrintController.maxTextLength - offSetLeft);

        int size = Math.max(tempLeft.length, tempRight.length);

        for(int i = 0; i < size; i++){
            result.add(new PrintTextInfo(ThemalPrintController.calculateOffSetLeft(i < tempLeft.length ? tempLeft[i] : "", i < tempRight.length ? tempRight[i] : "", offSetLeft)));
        }
        return result;
    }

    public static StringBuilder getTextAlignLeftByOffSetLeftStringBuilder(String left, String right){
        List<PrintTextInfo> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        int offSetLeft = 20;
        String[] tempLeft = getTextByPrintText(left, offSetLeft);
        String[] tempRight = getTextByPrintText(right, ThemalPrintController.maxTextLength - offSetLeft);

        int size = Math.max(tempLeft.length, tempRight.length);

        for(int i = 0; i < size; i++){
            //result.add(new PrintTextInfo(ThemalPrintController.calculateOffSetLeft(i < tempLeft.length ? tempLeft[i] : "", i < tempRight.length ? tempRight[i] : "", offSetLeft)));
            sb.append(new PrintTextInfo(ThemalPrintController.calculateOffSetLeft(i < tempLeft.length ? tempLeft[i] : "", i < tempRight.length ? tempRight[i] : "", offSetLeft)));
        }
        return sb;
    }

    private static List<PrintTextInfo> getTextAlignLeftByOffSetLeft(String left, String right, int offSetLeft){
        List<PrintTextInfo> result = new ArrayList<>();

        String[] tempLeft = getTextByPrintText(left, offSetLeft);
        String[] tempRight = getTextByPrintText(right, ThemalPrintController.maxTextLength - offSetLeft);

        int size = Math.max(tempLeft.length, tempRight.length);

        for(int i = 0; i < size; i++){
            result.add(new PrintTextInfo(ThemalPrintController.calculateOffSetLeft(i < tempLeft.length ? tempLeft[i] : "", i < tempRight.length ? tempRight[i] : "", offSetLeft)));
        }
        return result;
    }

    private static List<PrintTextInfo> getTextAlignLeftByOffSetLeft(String left, String right, PrintTextInfo.FontType fontType){
        List<PrintTextInfo> result = new ArrayList<>();

        int offSetLeft = 20;
        String[] tempLeft = getTextByPrintText(left, offSetLeft);
        String[] tempRight = getTextByPrintText(right, ThemalPrintController.maxTextLength - offSetLeft);

        int size = Math.max(tempLeft.length, tempRight.length);

        for(int i = 0; i < size; i++){
            result.add(new PrintTextInfo(ThemalPrintController.calculateOffSetLeft(i < tempLeft.length ? tempLeft[i] : "", i < tempRight.length ? tempRight[i] : "", offSetLeft), fontType));
        }
        return result;
    }

    private static List<PrintTextInfo> getTextAlignLeftByOffSetLeft(String left, String right, int offSetLeft, PrintTextInfo.FontType fontType){
        List<PrintTextInfo> result = new ArrayList<>();

        String[] tempLeft = getTextByPrintText(left, offSetLeft);
        String[] tempRight = getTextByPrintText(right, ThemalPrintController.maxTextLength - offSetLeft);

        int size = Math.max(tempLeft.length, tempRight.length);

        for(int i = 0; i < size; i++){
            result.add(new PrintTextInfo(ThemalPrintController.calculateOffSetLeft(i < tempLeft.length ? tempLeft[i] : "", i < tempRight.length ? tempRight[i] : "", offSetLeft), fontType));
        }
        return result;
    }

    private static List<PrintTextInfo> getTextAlignLeftRight(String left, String right){
        List<PrintTextInfo> result = new ArrayList<>();

        if(ThemalPrintController.getAlphabetOnly(String.format("%s %s", left, right)).length() <=  ThemalPrintController.maxTextLength){
            result.add(new PrintTextInfo(ThemalPrintController.calculateLeghtRight(left, right)));
        } else {
            String[] tempLeft = getTextByPrintText(left, (ThemalPrintController.maxTextLength / 2) - 2);
            String[] tempRight = getTextByPrintText(right, (ThemalPrintController.maxTextLength / 2) - 2);

            int size = Math.max(tempLeft.length, tempRight.length);

            for(int i = 0; i < size; i++){
                result.add(new PrintTextInfo(ThemalPrintController.calculateLeghtRight(i < tempLeft.length ? tempLeft[i] : "", i < tempRight.length ? tempRight[i] : "")));
            }
        }
        return result;
    }

    private static List<PrintTextInfo> getTextAlignLeftRight(String left, String right, PrintTextInfo.FontType fontType){
        List<PrintTextInfo> result = new ArrayList<>();

        if(ThemalPrintController.getAlphabetOnly(String.format("%s %s", left, right)).length() <=  ThemalPrintController.maxTextLength){
            result.add(new PrintTextInfo(ThemalPrintController.calculateLeghtRight(left, right), fontType));
        } else {
            String[] tempLeft = getTextByPrintText(left, (ThemalPrintController.maxTextLength / 2) - 2);
            String[] tempRight = getTextByPrintText(right, (ThemalPrintController.maxTextLength / 2) - 2);

            int size = Math.max(tempLeft.length, tempRight.length);

            for(int i = 0; i < size; i++){
                result.add(new PrintTextInfo(ThemalPrintController.calculateLeghtRight(i < tempLeft.length ? tempLeft[i] : "", i < tempRight.length ? tempRight[i] : ""), fontType));
            }
        }
        return result;
    }

    private static List<PrintTextInfo> getTextLeftRightAlignCenter(String left, String right){
        return getTextLeftRightAlignCenter( left,  right,  "TH");
    }

    private static List<PrintTextInfo> getTextLeftRightAlignCenter(String left, String right, String Language){
        List<PrintTextInfo> result = new ArrayList<>();

        String[] tempLeft = getTextByPrintText(left, (ThemalPrintController.maxTextLength / 2) - 2);
        String[] tempRight = getTextByPrintText(right, (ThemalPrintController.maxTextLength / 2) - 2);

        int size = Math.max(tempLeft.length, tempRight.length);

        for(int i = 0; i < size; i++){
            result.add(new PrintTextInfo(String.format(" %s  %s ",
                    ThemalPrintController.calculateCenterByLength(i < tempLeft.length ? tempLeft[i] : "", (ThemalPrintController.maxTextLength / 2) - 2),
                    ThemalPrintController.calculateCenterByLength(i < tempRight.length ? tempRight[i] : "", (ThemalPrintController.maxTextLength / 2) - 2))
                    , Language));
        }
        return result;
    }

    private static List<PrintTextInfo> getTextLeftRightAlignCenter(String left, String right, PrintTextInfo.FontType fontType){
        List<PrintTextInfo> result = new ArrayList<>();

        String[] tempLeft = getTextByPrintText(left, (ThemalPrintController.maxTextLength / 2) - 2);
        String[] tempRight = getTextByPrintText(right, (ThemalPrintController.maxTextLength / 2) - 2);

        int size = Math.max(tempLeft.length, tempRight.length);

        for(int i = 0; i < size; i++){
            result.add(new PrintTextInfo(String.format(" %s  %s ",
                    ThemalPrintController.calculateCenterByLength(i < tempLeft.length ? tempLeft[i] : "", (ThemalPrintController.maxTextLength / 2) - 2),
                    ThemalPrintController.calculateCenterByLength(i < tempRight.length ? tempRight[i] : "", (ThemalPrintController.maxTextLength / 2) - 2)), fontType
            ));
        }
        return result;
    }

    private static List<PrintTextInfo> getTextAlignLeft(String left){
        return getTextAlignLeft(left, "TH");
    }

    private static List<PrintTextInfo> getTextAlignLeft(String left, String language){
        List<PrintTextInfo> result = new ArrayList<>();

        if(ThemalPrintController.getAlphabetOnly(left).length() <=  ThemalPrintController.maxTextLength){
            result.add(new PrintTextInfo(left, language));
        } else {
            String[] tempLeft = getTextByPrintText(left, ThemalPrintController.maxTextLength);

            for(int i = 0; i < tempLeft.length; i++){
                result.add(new PrintTextInfo(tempLeft[i], language));
            }
        }
        return result;
    }

    private static List<PrintTextInfo> getTextAlignLeft(String left, PrintTextInfo.FontType fontType){
        List<PrintTextInfo> result = new ArrayList<>();

        if(ThemalPrintController.getAlphabetOnly(left).length() <=  ThemalPrintController.maxTextLength){
            result.add(new PrintTextInfo(left, fontType));
        } else {
            String[] tempLeft = getTextByPrintText(left, ThemalPrintController.maxTextLength);

            for(int i = 0; i < tempLeft.length; i++){
                result.add(new PrintTextInfo(tempLeft[i], fontType));
            }
        }
        return result;
    }

    private static List<PrintTextInfo> getTextForDocumentHistory(String num, String left, String right){
        List<PrintTextInfo> result = new ArrayList<>();

        if(ThemalPrintController.getAlphabetOnly(String.format("%s  %s %s", num, left, right)).length() <=  ThemalPrintController.maxTextLength){
            result.add(new PrintTextInfo(ThemalPrintController.calculateLeghtRight(String.format("%s  %s", num, left), right)));
        } else {
            String[] tempNum = getTextByPrintText(num, 3);
            String[] tempLeft = getTextByPrintText(left, (ThemalPrintController.maxTextLength / 2) - 5);
            String[] tempRight = getTextByPrintText(right, (ThemalPrintController.maxTextLength / 2) - 5);

            int size = Math.max(tempLeft.length, tempRight.length);
            size = Math.max(size, tempNum.length);

            for(int i = 0; i < size; i++){
                //result.add(new PrintTextInfo(ThemalPrintController.calculateLeghtRight(i < tempLeft.length ? tempLeft[i] : "", i < tempRight.length ? tempRight[i] : "")));

                result.add(new PrintTextInfo(ThemalPrintController.calculateLeghtRight(String.format("%s  %s", i < tempNum.length ? tempNum[i] : "   ", i < tempLeft.length ? tempLeft[i] : ""), i < tempRight.length ? tempRight[i] : "")));
            }
        }
        return result;
    }


    private static int getWidth(String text, Paint p) {
        Rect rect = new Rect();
        p.getTextBounds(text, 0, text.length(), rect);
        return rect.width();
    }

    private static String getSignatureUnderline(Paint p, int width) {
        Rect rect1 = new Rect();
        p.getTextBounds(".", 0, 1, rect1);
        Rect rect2 = new Rect();
        p.getTextBounds("..", 0, 2, rect2);
        int ww = width / ((rect2.width() - rect1.width() * 2) + rect1.width());
        char[] ch = new char[ww];
        Arrays.fill(ch, '.');
        return new String(ch);
    }

    public static void saveImage(Bitmap bitmap) {
        try {
//			img.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(new File("/sdcard/ImageAfterAddingText.png")));
            //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(File.createTempFile("PrintImage", ".jpg", new File(BHStorage.getFolder(BHStorage.FolderType.Picture)))));
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File(mContext.getCacheDir() + "/image.jpg")));
            // dest is Bitmap, if you want to preview the final image, you can
            // display it on screen also before saving
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static Bitmap scaleBitmapByWidth(Bitmap bm) {
        return scaleBitmapByWidth(bm, PRINTER_LAYOUT_WIDTH);
    }

    public static Bitmap scaleBitmapByWidth(Bitmap bm, int width) {
        float ratio = width / (float) bm.getWidth();
        int height = (int) (ratio * bm.getHeight());

        Bitmap scaledBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.postScale(ratio, ratio);
        //scaleMatrix.setScale(ratio, ratio);

        Canvas canvas = new Canvas(scaledBitmap);
        //canvas.setMatrix(scaleMatrix);
        //canvas.drawBitmap(bm, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));
        canvas.drawBitmap(bm, scaleMatrix, new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        //BitMapToString(scaledBitmap);
        return scaledBitmap;
    }

    //ไว้ดูรูปตอนพิมพ์
    /*public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }*/

    public static Bitmap scaleBitmapByHeight(Bitmap bm, int height) {
        int width = (int) (1f * height / bm.getHeight() * bm.getWidth());
        return Bitmap.createScaledBitmap(bm, width, height, true);
    }

    public static Bitmap getShortHeader() {
        Bitmap img = Bitmap.createBitmap(LAYOUT_WIDTH, 650, Config.ARGB_8888);
        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
        //cv.drawColor(Color.WHITE);
        //cv.drawColor(Color.TRANSPARENT, Mode.CLEAR);

        int logoHeight = 180;
        Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.drawable.tsr_logo_gray);
        cv.drawBitmap(scaleBitmapByHeight(logo, logoHeight), 0, 2, null);

        float x = 385;
        float y = 0;
        float fontSize = 46;

        Paint p = new Paint();

        p.setTextSize(34);
        p.setTypeface(null);
        y += fontSize + LINE_SPACE;
        cv.drawText("43/9 หมู่ 7 ซ.ชูชาติอนุสรณ์ 4 ต.บางตลาด ", x, y, p);

        y += fontSize + LINE_SPACE;
        cv.drawText("อ.ปากเกร็ด จ.นนทบุรี 11120", x, y, p);

        y += fontSize + LINE_SPACE;
        cv.drawText("เลขประจำตัวภาษี 1122331565468", x, y, p);

        return img;
    }

    public static Bitmap getHeader() {
        Bitmap img = Bitmap.createBitmap(LAYOUT_WIDTH, 650, Config.ARGB_8888);
        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
        //cv.drawColor(Color.WHITE);
        //cv.drawColor(Color.TRANSPARENT, Mode.CLEAR);

        int logoHeight = 220;
        Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.drawable.tsr_logo_gray);
        cv.drawBitmap(scaleBitmapByHeight(logo, logoHeight), 0, 2, null);

        float x = 1;
        float y = logoHeight + LINE_SPACE;
        float fontSize = 56;

        Paint p = new Paint();
        p.setTextSize(fontSize);
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        y += fontSize + LINE_SPACE;
        cv.drawText("บริษัท เธียรสุรัตน์ จำกัด (มหาชน)", x, y, p);

        p.setTextSize(44);
        p.setTypeface(null);
        y += fontSize + LINE_SPACE;
        cv.drawText("43/9 หมู่ 7 ซ.ชูชาติอนุสรณ์ 4 ต.บางตลาด", x, y, p);

        y += fontSize + LINE_SPACE;
        cv.drawText("อ.ปากเกร็ด จ.นนทบุรี 11120", x, y, p);

        y += fontSize + LINE_SPACE;
        cv.drawText("โทร. 0 2819 8888  แฟกซ์ 0 2962 6951-3", x, y, p);

        y += fontSize + LINE_SPACE;
        cv.drawText("อีเมล์. thiensurat@thiensurat.co.th", x, y, p);

        return img;
    }

    public static List<PrintTextInfo> getTextHeader() {
        List<PrintTextInfo> listHeader = new ArrayList<>();
        listHeader.addAll(getTextAlignLeft("บริษัท เธียรสุรัตน์ จำกัด (มหาชน)", PrintTextInfo.FontType.Bold));
        listHeader.addAll(getTextAlignLeft("43/9 หมู่ 7 ซ.ชูชาติอนุสรณ์ 4 ต.บางตลาด", PrintTextInfo.FontType.Bold));
        listHeader.addAll(getTextAlignLeft("อ.ปากเกร็ด จ.นนทบุรี 11120", PrintTextInfo.FontType.Bold));
        listHeader.addAll(getTextAlignLeft("โทร. 0 2819 8888  แฟกซ์ 0 2962 6951-3", PrintTextInfo.FontType.Bold));
        listHeader.addAll(getTextAlignLeft("อีเมล์. thiensurat@thiensurat.co.th", PrintTextInfo.FontType.Bold));
        listHeader.add(new PrintTextInfo("printHeader"));
        return listHeader;
    }

    public static List<PrintTextInfo> getTextShortHeader() {
        List<PrintTextInfo> listHeader = new ArrayList<>();
        listHeader.addAll(getTextAlignCenter("บริษัท เธียรสุรัตน์ จำกัด (มหาชน)", PrintTextInfo.FontType.Bold));
        listHeader.addAll(getTextAlignCenter("เลขประจำตัวผู้เสียภาษี 0107556000213", PrintTextInfo.FontType.Bold));
        listHeader.addAll(getTextAlignCenter("โทร. 1210", PrintTextInfo.FontType.Bold));
        listHeader.add(new PrintTextInfo("printShortHeader"));
        return listHeader;
    }

    public static Bitmap getImageShortHeader() {
        Bitmap img = Bitmap.createBitmap(LAYOUT_WIDTH, 650, Config.ARGB_8888);
        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
        cv.drawColor(Color.WHITE);
        cv.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        float y = 0;
        float fontSize = 44;
        float lineSpace = fontSize;

        Paint p = new Paint();
        p.setTextSize(fontSize);
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        p.setTextAlign(Align.CENTER);
        y += LINE_FEED / 2;
        cv.drawText("บริษัท เธียรสุรัตน์ จำกัด (มหาชน)", LAYOUT_WIDTH / 2, y, p);

        y += fontSize + lineSpace;
        cv.drawText("เลขประจำตัวผู้เสียภาษี 0107556000213", LAYOUT_WIDTH / 2, y, p);

        y += fontSize + lineSpace;
        cv.drawText("โทร. 1210", LAYOUT_WIDTH / 2, y, p);

        Bitmap result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) y, Config.ARGB_8888);
        cv = new Canvas(result);
        cv.drawBitmap(img, 0, 0, null);
        img.recycle();

        return result;
    }

    public static Bitmap getContract(JobItem jobItem, List<AddressItem> addressItems, List<ProductItem> productItems, String contacenumber) {
        Bitmap img = Bitmap.createBitmap(LAYOUT_WIDTH, 3000, Config.ARGB_8888);
        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
        cv.drawColor(Color.WHITE);

        float yy = 0;
        Bitmap header = getHeader();
        cv.drawBitmap(header, 0, yy, null);
        yy += header.getHeight();
        header.recycle();

        float fontSize = 90;
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        p.setTextSize(fontSize);
        p.setTextAlign(Align.CENTER);
        yy += fontSize + LINE_SPACE;
        String title = (productItems.get(0).getProductPayType().equals("2")) ? "ใบสัญญาเช่าซื้อ" : "ใบสัญญาซื้อขาย";
        cv.drawText(title, LAYOUT_WIDTH / 2, yy, p);

        fontSize = 42;
        float lineSpace = fontSize / 2;
        float xTitle = 1;
        float xValue = 400;

        Paint pTitle = new Paint(p);
        pTitle.setTypeface(Typeface.DEFAULT_BOLD);
        pTitle.setTextSize(fontSize);
        pTitle.setTextAlign(Align.LEFT);

        Paint pValue = new Paint(pTitle);
        pValue.setTypeface(null);

        yy += fontSize * 3;
        cv.drawText("วันที่ทำสัญญา", xTitle, yy, pTitle);
        cv.drawText(DateFormateUtilities.dateFormat(new Date()), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("เลขที่อ้างอิง", xTitle, yy, pTitle);
        cv.drawText(String.format("%d", jobItem.getOrderid()), xValue, yy, pValue);

        /*if (manual != null) {
            yy += fontSize + lineSpace;
            cv.drawText("เลขที่อ้างอิง", xTitle, yy, pTitle);
            //cv.drawText(String.format("%s/%4d", BHUtilities.trim(manual.ManualVolumeNo), manual.ManualRunningNo).replace(' ', '0'), xValue, yy, pValue);
            cv.drawText(String.format("%4d", manual.ManualRunningNo).replace(' ', '0'), xValue, yy, pValue);
        }*/

        yy += fontSize + lineSpace;
        cv.drawText("เลขที่สัญญา", xTitle, yy, pTitle);
        cv.drawText(contacenumber, xValue, yy, pValue);

        String[] texts = getText(String.format("%s%s %s", DateFormateUtilities.trim(jobItem.getTitle()), DateFormateUtilities.trim(jobItem.getFirstName()), DateFormateUtilities.trim(jobItem.getLastName())), pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText((productItems.get(0).getProductPayType().equals("2")) ? "ผู้เช่าซื้อ" : "ผู้ซื้อ", xTitle, yy, pTitle);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }
        yy += fontSize + lineSpace;
        cv.drawText("ผู้ซื้อ", xTitle, yy, pTitle);
        cv.drawText(String.format("%s%s %s", DateFormateUtilities.trim(jobItem.getTitle()), DateFormateUtilities.trim(jobItem.getFirstName()), DateFormateUtilities.trim(jobItem.getLastName())), xValue, yy, pValue);


        yy += fontSize + lineSpace;
        cv.drawText("เลขที่บัตร", xTitle, yy, pTitle);
        cv.drawText(DateFormateUtilities.trim(jobItem.getIDCard()), xValue, yy, pValue);

        //xValue = 250;
        /*texts = getText(defaultAddress.Address(), pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("ที่อยู่บัตร", xTitle, yy, pTitle);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }

        texts = getText(installAddress.Address(), pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("ที่ติดตั้ง", xTitle, yy, pTitle);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }

        texts = getText(BHUtilities.trim(contract.ProductName), pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("สินค้า", xTitle, yy, pTitle);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }

        yy += fontSize + lineSpace;
        cv.drawText("รุ่น", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(contract.MODEL), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("เลขเครื่อง", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(contract.ProductSerialNumber), xValue, yy, pValue);


        if (contract.TradeInDiscount > 0) {

            xValue = LAYOUT_WIDTH - 100;
            pValue.setTextAlign(Align.RIGHT);
            yy += fontSize + lineSpace;
            cv.drawText("ราคา", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(contract.SALES) + " บาท", xValue, yy, pValue);

            yy += fontSize + lineSpace;
            cv.drawText("ส่วนลดเครื่องแสดง", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(contract.TradeInDiscount) + " บาท", xValue, yy, pValue);

            yy += fontSize + lineSpace;
            cv.drawText("ราคาสุทธิ", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(contract.TotalPrice) + " บาท", xValue, yy, pValue);
        } else {

            xValue = LAYOUT_WIDTH - 100;
            pValue.setTextAlign(Align.RIGHT);
            yy += fontSize + lineSpace;
            cv.drawText("ราคาขาย", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(contract.SALES) + " บาท", xValue, yy, pValue);

        }

        if (contract.MODE > 1) {
            yy += fontSize + lineSpace;
            float sum = contract.PaymentAmount - contract.TradeInDiscount;
            cv.drawText("งวดที่ 1 ต้องชำระ", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(sum) + " บาท", xValue, yy, pValue);

            yy += fontSize + lineSpace;
            cv.drawText("งวดที่ 2 ถึงงวดที่ " + BHUtilities.numericFormat(contract.MODE) + " ต้องชำระงวดละ", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(contract.NextPaymentAmount) + " บาท", xValue, yy, pValue);

        }*/

        pValue.setTextAlign(Align.LEFT);
        Paint pSignature = new Paint(pValue);
        pSignature.setTextAlign(Align.CENTER);

        /*String customer = String.format("(%s %s)", BHUtilities.trim(contract.CustomerFullName), BHUtilities.trim(contract.CompanyName));

        if (contract.MODE == 1) {

            yy += 200;
            int Value = LAYOUT_WIDTH / 2;
            int ww = getWidth(customer, pValue) + 50;

            cv.drawText(getSignatureUnderline(pSignature, ww), Value, yy, pSignature);
            cv.drawText("ผู้ซื้อ", Value + 190, yy, pValue);

            yy += fontSize + lineSpace;
            cv.drawText(customer, Value, yy, pSignature);

            texts = getText(customer, pSignature, LAYOUT_WIDTH);
            for (int ii = 0; ii < texts.length; ii++) {
                if (ii == 0) {
                    cv.drawText(getSignatureUnderline(pSignature, ww), Value, yy, pSignature);
                    cv.drawText("ผู้ซื้อ", Value + 190, yy, pValue);
                    cv.drawText(String.format("%sผู้ซื้อ", getSignatureUnderline(pSignature, (LAYOUT_WIDTH) - (getWidth("ผู้ซื้อ", pSignature) + (LAYOUT_WIDTH / 2)))), LAYOUT_WIDTH / 2, yy, pSignature);
                }
                yy += fontSize + lineSpace;
                cv.drawText(texts[ii], Value, yy, pSignature);
            }

        } else {

            yy += 200;
            String TSR = "(นายวิรัช วงศ์นิรันดร์)";
            int xx = getWidth(TSR, pValue) + 10;
            int ww = getWidth(customer, pValue) + 50;

            int width = LAYOUT_WIDTH / 6;
            cv.drawText(getSignatureUnderline(pSignature, xx), width, yy, pSignature); // วาดจุด
            cv.drawText(String.format("ผู้ให้เช่าซื้อ"), width + 190, yy, pValue);

            xValue = LAYOUT_WIDTH - (ww / 2 + 120);
            cv.drawText("ผู้ซื้อ", LAYOUT_WIDTH - 115, yy, pValue);
            cv.drawText(getSignatureUnderline(pSignature, ww), xValue, yy, pSignature);

            yy += fontSize + lineSpace;
            cv.drawText(TSR, 230, yy, pSignature);
            cv.drawText(customer, xValue, yy, pSignature);

            //int width = LAYOUT_WIDTH / 6;
            //xValue = LAYOUT_WIDTH - (ww / 2 + 120);

            yy += 200;
            String TSR = "(นายวิรัช วงศ์นิรันดร์)";

            cv.drawText(String.format("%sผู้ให้เช่าซื้อ", getSignatureUnderline(pSignature, (LAYOUT_WIDTH / 2) - (getWidth("ผู้ให้เช่าซื้อ", pSignature) + 50))), LAYOUT_WIDTH / 4, yy, pSignature);
            cv.drawText(String.format("%sผู้เช่าซื้อ", getSignatureUnderline(pSignature, (LAYOUT_WIDTH / 2) - (getWidth("ผู้เช่าซื้อ", pSignature) + 50))), (LAYOUT_WIDTH / 4) * 3, yy, pSignature);

            String[] texts1 = getText(TSR, pSignature, LAYOUT_WIDTH / 2);
            String[] texts2 = getText(customer, pSignature, LAYOUT_WIDTH / 2);
            int num;
            if (texts1.length > texts2.length) {
                num = texts1.length;
            } else {
                num = texts2.length;
            }

            for (int ii = 0; ii < num; ii++) {
                yy += fontSize + lineSpace;
                if (texts1.length > ii) {
                    cv.drawText(texts1[ii], LAYOUT_WIDTH / 4, yy, pSignature);
                }

                if (texts2.length > ii) {
                    cv.drawText(texts2[ii], (LAYOUT_WIDTH / 4) * 3, yy, pSignature);
                }
            }

            yy += 200;
            String salename = "(" + BHUtilities.trim(contract.SaleEmployeeName) + ")";
            String salecode = "รหัส " + BHUtilities.trim(contract.SaleCode);

            String saleteamname = "(" + BHUtilities.trim(contract.upperEmployeeName) + ")";
            String saleteamcode = "" + BHUtilities.trim(contract.SaleTeamName);

            int zz = getWidth(saleteamname, pValue) + 50;
            xValue = LAYOUT_WIDTH - (zz / 2 + 120);

            int vv = getWidth(salename, pValue) + 50;
            xValue = LAYOUT_WIDTH - (vv / 2 + 120);

            int r = LAYOUT_WIDTH / 6;
            cv.drawText(getSignatureUnderline(pSignature, zz), r, yy, pSignature);
            cv.drawText("พยาน", r + 190, yy, pValue);

            cv.drawText("พยาน", LAYOUT_WIDTH - 115, yy, pValue);
            cv.drawText(getSignatureUnderline(pSignature, vv), xValue, yy, pSignature);

            yy += fontSize + lineSpace;
            cv.drawText(saleteamname, 230, yy, pSignature);
            cv.drawText(salename, xValue, yy, pSignature);
            yy += fontSize + lineSpace;
            cv.drawText(saleteamcode, 240, yy, pSignature);
            cv.drawText(salecode, xValue, yy, pSignature);

            yy += 200;
            String salename = "(" + BHUtilities.trim(contract.SaleEmployeeName != null ? contract.SaleEmployeeName : "") + ")";
            String salecode = "รหัส " + BHUtilities.trim(contract.SaleCode);

            String saleteamname = "(" + BHUtilities.trim(contract.upperEmployeeName != null ? contract.upperEmployeeName : "") + ")";
            String saleteamcode = "" + BHUtilities.trim(contract.SaleTeamName != null ? contract.SaleTeamName : "");

            cv.drawText(String.format("%sพยาน", getSignatureUnderline(pSignature, (LAYOUT_WIDTH / 2) - (getWidth("พยาน", pSignature) + 50))), LAYOUT_WIDTH / 4, yy, pSignature);
            cv.drawText(String.format("%sพยาน", getSignatureUnderline(pSignature, (LAYOUT_WIDTH / 2) - (getWidth("พยาน", pSignature) + 50))), (LAYOUT_WIDTH / 4) * 3, yy, pSignature);

            texts1 = getText(saleteamname, pSignature, LAYOUT_WIDTH / 2);
            texts2 = getText(salename, pSignature, LAYOUT_WIDTH / 2);
            if (texts1.length > texts2.length) {
                num = texts1.length;
            } else {
                num = texts2.length;
            }

            for (int ii = 0; ii < num; ii++) {
                yy += fontSize + lineSpace;
                if (texts1.length > ii) {
                    cv.drawText(texts1[ii], LAYOUT_WIDTH / 4, yy, pSignature);
                }

                if (texts2.length > ii) {
                    cv.drawText(texts2[ii], (LAYOUT_WIDTH / 4) * 3, yy, pSignature);
                }
            }

            yy += fontSize + lineSpace;
            cv.drawText(saleteamcode, LAYOUT_WIDTH / 4, yy, pSignature);
            cv.drawText(salecode, (LAYOUT_WIDTH / 4) * 3, yy, pSignature);
        }

        xValue = 1;
        yy += fontSize + lineSpace + 50;
        cv.drawText(String.format("รหัส %s %s", BHUtilities.trim(contract.SaleCode), BHUtilities.trim(contract.SaleEmployeeName)), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText(String.format("%s %s", BHUtilities.trim(contract.SaleTeamName), BHUtilities.trim(contract.upperEmployeeName)), xValue, yy, pValue);
*/
        yy += LINE_FEED;

        Bitmap result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy, Config.ARGB_8888);
        cv = new Canvas(result);
        cv.drawBitmap(img, 0, 0, null);
        img.recycle();

        return scaleBitmapByWidth(result);
    }

    public static List<PrintTextInfo> getTextContract(JobItem jobItem, List<AddressItem> addressItems, List<ProductItem> productItems, String contacenumber) {
        List<PrintTextInfo> listText = new ArrayList<>();
        String[] texts;

        listText.add(new PrintTextInfo("printHeader"));
        listText.addAll(getTextAlignCenter((productItems.get(0).getProductPayType().equals("2")) ? "ใบสัญญาเช่าซื้อ" : "ใบสัญญาซื้อขาย", PrintTextInfo.FontType.Bold));

        listText.addAll(getTextAlignLeftByOffSetLeft(" วันที่ทำสัญญา", DateFormateUtilities.dateFormat(new Date())));
        listText.addAll(getTextAlignLeftByOffSetLeft(" เลขที่สัญญา", contacenumber));
        listText.addAll(getTextAlignLeftByOffSetLeft(" เลขที่อ้างอิง", jobItem.getOrderid()));
        listText.addAll(getTextAlignLeftByOffSetLeft((productItems.get(0).getProductPayType().equals("2")) ? " ผู้เช่าซื้อ" : " ผู้ซื้อ",
                String.format("%s%s %s", DateFormateUtilities.trim(jobItem.getTitle()), DateFormateUtilities.trim(jobItem.getFirstName()), DateFormateUtilities.trim(jobItem.getLastName()))));

        listText.addAll(getTextAlignLeftByOffSetLeft(" เลขที่บัตร", DateFormateUtilities.trim(jobItem.getIDCard())));

        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        String phone1= null;
        String phone2 = null;
        String mobile1 = null;
        String mobile2 = null;
        for (AddressItem item : addressItems) {
            if (item.getAddressType().equals("AddressIDCard")) {
                sb1.append(item.getAddrDetail());
                sb1.append("\n");
                sb1.append("ต." + item.getSubdistrict() + " อ." + item.getDistrict());
                sb1.append("\n");
                sb1.append("จ." + item.getProvince() + " " + item.getZipcode());

                phone1 = (item.getPhone().isEmpty()) ? "-" : item.getPhone();
                mobile1 = (item.getMobile().isEmpty()) ? "-" : item.getMobile();
            }

            if (item.getAddressType().equals("AddressInstall")) {
                sb2.append(item.getAddrDetail());
                sb2.append("\n");
                sb2.append("ต." + item.getSubdistrict() + " อ." + item.getDistrict());
                sb2.append("\n");
                sb2.append("จ." + item.getProvince() + " " + item.getZipcode());

                phone2 = (item.getPhone().isEmpty()) ? "-" : item.getPhone();
                mobile2 = (item.getMobile().isEmpty()) ? "-" : item.getMobile();
            }
        }

        listText.addAll(getTextAlignLeftByOffSetLeft(" ที่อยู่บัตร", sb1.toString()));
        listText.addAll(getTextAlignLeftByOffSetLeft(" เบอร์โทรติดต่อ", phone1));
        listText.addAll(getTextAlignLeftByOffSetLeft(" มือถือ", mobile1));

        listText.addAll(getTextAlignLeftByOffSetLeft(" ที่ติดตั้ง", sb2.toString()));
        listText.addAll(getTextAlignLeftByOffSetLeft(" เบอร์โทรติดต่อ", phone2));
        listText.addAll(getTextAlignLeftByOffSetLeft(" มือถือ", mobile2));

        listText.add(new PrintTextInfo(""));

        String temp = "";
        String temp2 = "";

        String period = null;
        float qty = 0;
        float perPeriod = 0;
        float normalPrice = 0;
        float discountPrice = 0;
        float grandTotalPrice = 0;

        float lastnormalPrice = 0;
        float lastdiscountPrice = 0;
        float lastgrandTotal = 0;
        for (int i = 0; i < productItems.size(); i++) {
            ProductItem item = productItems.get(i);
            listText.addAll(getTextAlignLeftByOffSetLeft((i + 1) +". สินค้า", item.getProductName()));
            listText.addAll(getTextAlignLeftByOffSetLeft("   รุ่น", item.getProductModel()));
            listText.addAll(getTextAlignLeftByOffSetLeft("   เลขเครื่อง", item.getProductSerial()));

            if (item.getProductPayType().equals("2")) {
                qty += Float.parseFloat(item.getProductQty());
                normalPrice = Float.parseFloat(item.getProductPrice());
                discountPrice = Float.parseFloat(item.getProductDiscount());
                period = item.getProductPayPeriods();
                perPeriod = Float.parseFloat(item.getProductPayPerPeriods());
                grandTotalPrice = (normalPrice - discountPrice);
                listText.addAll(getTextAlignLeftRight("   ราคา", df.format(normalPrice) + " บาท"));
                listText.addAll(getTextAlignLeftRight("   ส่วนลด", df.format(discountPrice) + " บาท"));
                listText.addAll(getTextAlignLeftRight("   ราคาสุทธิ", df.format(grandTotalPrice) + " บาท"));
                listText.addAll(getTextAlignLeftRight("   จำนวนงวด", period + " งวด"));
                listText.addAll(getTextAlignLeftRight("   งวดละ", df.format(perPeriod) + " บาท"));
                listText.add(new PrintTextInfo(""));
            } else {
                normalPrice = Float.parseFloat(item.getProductPrice());
                discountPrice = Float.parseFloat(item.getProductDiscount());
                grandTotalPrice = (normalPrice - discountPrice);
                listText.addAll(getTextAlignLeftRight("   ราคา", df.format(normalPrice) + " บาท"));
                listText.addAll(getTextAlignLeftRight("   ส่วนลด", df.format(discountPrice) + " บาท"));
                listText.addAll(getTextAlignLeftRight("   ราคาสุทธิ", df.format(grandTotalPrice) + " บาท"));
                listText.add(new PrintTextInfo(""));
            }

            listText.addAll(getTextLeftRightAlignCenter("----------------------", "----------------------", "EN"));

            qty += Float.parseFloat(item.getProductQty());
            lastnormalPrice += Float.parseFloat(item.getProductPrice());
            lastdiscountPrice += Float.parseFloat(item.getProductDiscount());
            if (i == productItems.size() - 1) {
                listText.add(new PrintTextInfo(""));
                lastgrandTotal = (lastnormalPrice - lastdiscountPrice);
                listText.addAll(getTextAlignLeftRight(" ราคา", df.format(lastnormalPrice) + " บาท"));
                listText.addAll(getTextAlignLeftRight(" ส่วนลด", df.format(lastdiscountPrice) + " บาท"));
                listText.addAll(getTextAlignLeftRight(" ราคาสุทธิ", df.format(lastgrandTotal) + " บาท"));
            }
        }

        listText.add(new PrintTextInfo(""));
        String customer = String.format("(%s%s %s)", DateFormateUtilities.trim(jobItem.getTitle()), DateFormateUtilities.trim(jobItem.getFirstName()), DateFormateUtilities.trim(jobItem.getLastName()));

        if (!productItems.get(0).getProductPayType().equals("2")) {
            listText.add(new PrintTextInfo(""));
            listText.add(new PrintTextInfo("k_viruchWithCustomer"));
            listText.addAll(getTextLeftRightAlignCenter("......................", ".....................", "EN"));
            listText.addAll(getTextLeftRightAlignCenter("           ผู้ขาย         ", "          ผู้ซื้อ        "));
            String TSR = "(นายวิรัช วงศ์นิรันดร์)";
            listText.addAll(getTextLeftRightAlignCenter(TSR, customer));

        } else {
            listText.add(new PrintTextInfo(""));
            listText.add(new PrintTextInfo("k_viruchWithCustomer"));
            listText.addAll(getTextLeftRightAlignCenter("......................", ".....................", "EN"));
            listText.addAll(getTextLeftRightAlignCenter("     ผู้ให้เช่าซื้อ       ", "       ผู้เช่าซื้อ        "));
            String TSR = "(นายวิรัช วงศ์นิรันดร์)";
            listText.addAll(getTextLeftRightAlignCenter(TSR, customer));


            String installername = "" + DateFormateUtilities.trim(
                    MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_FIRSTNAME) + " " + MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_LASTNAME));
            String salename = "" + DateFormateUtilities.trim(jobItem.getPresale());

            listText.add(new PrintTextInfo(""));
            listText.add(new PrintTextInfo("signatureWitness"));
            listText.addAll(getTextLeftRightAlignCenter("......................", "......................"));
            listText.addAll(getTextLeftRightAlignCenter("     พยาน       ", "       พยาน        "));
            listText.addAll(getTextLeftRightAlignCenter("(" + installername + ")", "(" + salename + ")"));
        }

        listText.add(new PrintTextInfo(""));
        listText.add(new PrintTextInfo("endContractPage"));
        listText.add(new PrintTextInfo("selectStandardMode"));

        return listText;
    }

    public static List<PrintTextInfo> getTextInstallation(JobItem jobItem, List<AddressItem> addressItems, List<ProductItem> productItems, String contacenumber) {
        List<PrintTextInfo> listText = new ArrayList<>();
        String[] texts;

        listText.add(new PrintTextInfo("printHeader"));
        listText.addAll(getTextAlignCenter("ใบรับการติดตั้ง", PrintTextInfo.FontType.Bold));

        listText.addAll(getTextAlignLeftByOffSetLeft(" วันที่ติดตั้ง", DateFormateUtilities.dateFormat(new Date())));
        listText.addAll(getTextAlignLeftByOffSetLeft(" เลขที่สัญญา", contacenumber));
        listText.addAll(getTextAlignLeftByOffSetLeft(" เลขที่อ้างอิง", jobItem.getOrderid()));
        listText.addAll(getTextAlignLeftByOffSetLeft((productItems.get(0).getProductPayType().equals("2")) ? " ผู้เช่าซื้อ" : " ผู้ซื้อ",
                String.format("%s%s %s", DateFormateUtilities.trim(jobItem.getTitle()), DateFormateUtilities.trim(jobItem.getFirstName()), DateFormateUtilities.trim(jobItem.getLastName()))));

        listText.addAll(getTextAlignLeftByOffSetLeft(" เลขที่บัตร", DateFormateUtilities.trim(jobItem.getIDCard())));

        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        String phone1= null;
        String phone2 = null;
        String mobile1 = null;
        String mobile2 = null;
        for (AddressItem item : addressItems) {
            if (item.getAddressType().equals("AddressIDCard")) {
                sb1.append(item.getAddrDetail());
                sb1.append("\n");
                sb1.append(item.getSubdistrict() + " " + item.getDistrict());
                sb1.append("\n");
                sb1.append(item.getProvince() + " " + item.getZipcode());

                phone1 = (item.getPhone().isEmpty()) ? "-" : item.getPhone();
                mobile1 = (item.getMobile().isEmpty()) ? "-" : item.getMobile();
            }

            if (item.getAddressType().equals("AddressInstall")) {
                sb2.append(item.getAddrDetail());
                sb2.append("\n");
                sb2.append(item.getSubdistrict() + " " + item.getDistrict());
                sb2.append("\n");
                sb2.append(item.getProvince() + " " + item.getZipcode());

                phone2 = (item.getPhone().isEmpty()) ? "-" : item.getPhone();
                mobile2 = (item.getMobile().isEmpty()) ? "-" : item.getMobile();
            }
        }

        listText.addAll(getTextAlignLeftByOffSetLeft(" ที่อยู่บัตร", sb1.toString()));
        listText.addAll(getTextAlignLeftByOffSetLeft(" เบอร์โทรติดต่อ", phone1));
        listText.addAll(getTextAlignLeftByOffSetLeft(" มือถือ", mobile1));

        listText.addAll(getTextAlignLeftByOffSetLeft(" ที่ติดตั้ง", sb2.toString()));
        listText.addAll(getTextAlignLeftByOffSetLeft(" เบอร์โทรติดต่อ", phone2));
        listText.addAll(getTextAlignLeftByOffSetLeft(" มือถือ", mobile2));

        String temp = "";
        String temp2 = "";
        int j = 0;
        for (ProductItem item : productItems) {
            temp = item.getProductName();
            if (temp2.isEmpty()) {
                temp2 = temp;
                listText.addAll(getTextAlignLeftByOffSetLeft(" สินค้า", item.getProductName()));
            } else if (temp2.equals(temp)) {
                listText.addAll(getTextAlignLeftByOffSetLeft(" ", item.getProductName()));
            } else {
                listText.addAll(getTextAlignLeftByOffSetLeft(" ", item.getProductName()));
            }
            j++;
        }

        listText.add(new PrintTextInfo(""));

        temp2 = "";
        int k = 0;
        for (ProductItem item : productItems) {
            temp = item.getProductModel();
            if (temp2.isEmpty()) {
                temp2 = temp;
                listText.addAll(getTextAlignLeftByOffSetLeft(" รุ่น", item.getProductModel()));
            } else if (temp2.equals(temp)) {
                listText.addAll(getTextAlignLeftByOffSetLeft(" ", item.getProductModel()));
            } else {
                listText.addAll(getTextAlignLeftByOffSetLeft(" ", item.getProductModel()));
            }
            k++;
        }

        listText.add(new PrintTextInfo(""));

        temp2 = "";
        int l = 0;
        for (ProductItem item : productItems) {
            temp = item.getProductModel();
            if (temp2.isEmpty()) {
                temp2 = temp;
                listText.addAll(getTextAlignLeftByOffSetLeft(" เลขเครื่อง", item.getProductSerial()));
            } else if (temp2.equals(temp)) {
                listText.addAll(getTextAlignLeftByOffSetLeft(" ", item.getProductSerial()));
            } else {
                listText.addAll(getTextAlignLeftByOffSetLeft(" ", item.getProductSerial()));
            }
            l++;
        }

        listText.add(new PrintTextInfo(""));

        listText.add(new PrintTextInfo(""));
        String customer = String.format("(%s%s %s)", DateFormateUtilities.trim(jobItem.getTitle()), DateFormateUtilities.trim(jobItem.getFirstName()), DateFormateUtilities.trim(jobItem.getLastName()));

        listText.add(new PrintTextInfo(""));
        listText.add(new PrintTextInfo("customerWithInstaller"));
        listText.addAll(getTextLeftRightAlignCenter("......................", ".....................", "EN"));
        listText.addAll(getTextLeftRightAlignCenter("           ผู้รับการติดตั้ง       ", "          ผู้ติดตั้ง        "));
        String installername = "" + DateFormateUtilities.trim(
                MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_TITLE) + MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_FIRSTNAME) + " " + MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_LASTNAME));
        listText.addAll(getTextLeftRightAlignCenter(customer, installername));

        listText.add(new PrintTextInfo(""));
        listText.add(new PrintTextInfo("endContractPage"));
        listText.add(new PrintTextInfo("selectStandardMode"));

        return listText;
    }

    /*public static Bitmap getNewReceipt(PaymentInfo paymentInfo, DebtorCustomerInfo debtorCustomerInfo, AddressInfo addressInfo) {

        Bitmap img = Bitmap.createBitmap(LAYOUT_WIDTH, 3000, Config.ARGB_8888);
        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
        cv.drawColor(Color.WHITE);

        float yy = 0;
        Bitmap header = getHeader();
        cv.drawBitmap(header, 0, yy, null);
        yy += header.getHeight();
        header.recycle();

        float fontSize = 90;
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        p.setTextSize(fontSize);
        p.setTextAlign(Align.CENTER);
        yy += fontSize + LINE_SPACE;
        cv.drawText("ใบเสร็จรับเงิน", LAYOUT_WIDTH / 2, yy, p);

        fontSize = 42;
        float lineSpace = fontSize / 2;
        int xTitle = 1;
        int xValue = 480;


        Paint pTitle = new Paint(p);
        pTitle.setTypeface(Typeface.DEFAULT_BOLD);
        pTitle.setTextSize(fontSize);
        pTitle.setTextAlign(Align.LEFT);

        Paint pValue = new Paint(pTitle);
        pValue.setTypeface(null);

        String bahtLabel = " บาท";


        yy += fontSize * 3;
        cv.drawText("วันที่รับเงิน", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.dateFormat(paymentInfo.PayDate), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("เลขที่", xTitle, yy, pTitle);
        cv.drawText(paymentInfo.ReceiptCode, xValue, yy, pValue);

        if (paymentInfo.ManualVolumeNo != null && paymentInfo.ManualRunningNo > 0) {
            yy += fontSize + lineSpace;
            cv.drawText("เลขที่อ้างอิง", xTitle, yy, pTitle);

            String ManualDocumentBookRunningNo = String.format("%s/%d", BHUtilities.trim(paymentInfo.ManualVolumeNo), paymentInfo.ManualRunningNo).replace(' ', '0');
            cv.drawText(ManualDocumentBookRunningNo, xValue, yy, pValue);
        }

        yy += fontSize + lineSpace;
        cv.drawText("เลขที่สัญญา", xTitle, yy, pTitle);
        cv.drawText(paymentInfo.CONTNO, xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("วันที่ทำสัญญา", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.dateFormat(paymentInfo.EFFDATE), xValue, yy, pValue);

        String[] texts = new String[]{""};
        texts = getText(debtorCustomerInfo.CustomerFullName(), pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("ชื่อลูกค้า", xTitle, yy, pTitle);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }

        yy += fontSize + lineSpace;
        cv.drawText("ชื่อลูกค้า", xTitle, yy, pTitle);
        cv.drawText(debtorCustomerInfo.CustomerFullName(), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("เลขที่บัตร", xTitle, yy, pTitle);
        cv.drawText(debtorCustomerInfo.IDCard, xValue, yy, pValue);


        //xValue = 250;
        texts = new String[]{""};
        texts = getText(addressInfo.Address(), pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("ที่อยู่ติดตั้ง", xTitle, yy, pTitle);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }

        texts = new String[]{""};
        texts = getText(paymentInfo.ProductName, pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("ชื่อสินค้า", xTitle, yy, pTitle);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }

        yy += fontSize + lineSpace;
        cv.drawText("รุ่น", xTitle, yy, pTitle);
        cv.drawText(paymentInfo.MODEL, xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("รหัสสินค้า", xTitle, yy, pTitle);
        //-- Fixed - [BHPROJ-0025-770] :: [Data Migration] ใบเสร็จที่ไม่มี ProductSerialNumber ไม่สามารถพิมพ์ใบเสร็จได้ เนื่องจากค่า ProductSerialNumber = null
        cv.drawText(((paymentInfo.ProductSerialNumber != null) ? paymentInfo.ProductSerialNumber : "-"), xValue, yy, pValue);



        texts = new String[]{""};
        if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
            texts = getText(BHUtilities.trim(String.format("ชำระงวดที่ %d-%d", paymentInfo.PaymentPeriodNumber, paymentInfo.MODE)), pValue, LAYOUT_WIDTH / 3);

            for (int ii = 0; ii < texts.length; ii++) {
                yy += fontSize + lineSpace;
                cv.drawText(texts[ii], xTitle, yy, pTitle);
                if (ii == 0) {
                    cv.drawText(BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount) + bahtLabel, xValue, yy, pValue);
                }
            }

            yy += fontSize + lineSpace;
            cv.drawText(String.format("ชำระงวดที่ %d-%d", paymentInfo.PaymentPeriodNumber, paymentInfo.MODE), xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount) + bahtLabel, xValue, yy, pValue);

            yy += fontSize + lineSpace;
            cv.drawText("ส่วนลดตัดสด", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(paymentInfo.CloseAccountDiscountAmount) + bahtLabel, xValue, yy, pValue);
        }

        texts = new String[]{""};
        if (paymentInfo.MODE == 1) {
            if (paymentInfo.BalancesOfPeriod == 0) {
                texts = getText(BHUtilities.trim("ค่างวดเงินสด (ชำระครบ)"), pValue, LAYOUT_WIDTH / 3);
            } else {
                texts = getText(BHUtilities.trim("ค่างวดเงินสด (ชำระบางส่วน)"), pValue, LAYOUT_WIDTH / 3);
            }
        } else {
            if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
                texts = getText("จำนวนที่ชำระ", pValue, LAYOUT_WIDTH / 3);
            } else{
                if (paymentInfo.BalancesOfPeriod == 0) {
                    String txtPeriodAmountLabel = "";
                    if(paymentInfo.PaymentPeriodNumber == paymentInfo.MODE){
                        txtPeriodAmountLabel = "ค่างวดที่ %d/%d (ชำระครบ)";
                    } else {
                        txtPeriodAmountLabel = "ค่างวดที่ %d/%d";
                    }
                    texts = getText(BHUtilities.trim(String.format(txtPeriodAmountLabel, paymentInfo.PaymentPeriodNumber, paymentInfo.MODE)), pValue, LAYOUT_WIDTH / 3);
                } else {
                    texts = getText(BHUtilities.trim(String.format("ค่างวดที่ %d/%d (ชำระบางส่วน)", paymentInfo.PaymentPeriodNumber, paymentInfo.MODE)), pValue, LAYOUT_WIDTH / 3);
                }
            }

        }
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            cv.drawText(texts[ii], xTitle, yy, pTitle);
            if (ii == 0) {
                //[START] :: Fixed - [BHPROJ-0026-751] :: แก้ไขการแสดงผลในส่วนของ ยอดชำระเงิน ให้เป็นตัวสีแดง + ตัวหนา + เพิ่มขนาดตัวหนังสือมา 1 ระดับี
                if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
                    Paint pValueNew = new Paint(pValue);
                    pValueNew.setTextSize(pValueNew.getTextSize() + 1);
                    pValueNew.setTypeface(Typeface.DEFAULT_BOLD);
                    pValueNew.setColor(Color.RED);

                    //cv.drawText(BHUtilities.numericFormat(paymentInfo.Amount) + bahtLabel, xValue, yy, pTitle);
                    cv.drawText(BHUtilities.numericFormat(paymentInfo.CloseAccountNetAmount), xValue, yy, pValueNew);

                    Rect rect = new Rect();
                    pValueNew.getTextBounds(BHUtilities.numericFormat(paymentInfo.CloseAccountNetAmount), 0, BHUtilities.numericFormat(paymentInfo.CloseAccountNetAmount).length(), rect);

                    cv.drawText(bahtLabel, xValue + rect.width(), yy, pValue);

                } else {
                    Paint pValueNew = new Paint(pValue);
                    pValueNew.setTextSize(pValueNew.getTextSize() + 1);
                    pValueNew.setTypeface(Typeface.DEFAULT_BOLD);
                    pValueNew.setColor(Color.RED);

                    //cv.drawText(BHUtilities.numericFormat(paymentInfo.Amount) + bahtLabel, xValue, yy, pTitle);
                    cv.drawText(BHUtilities.numericFormat(paymentInfo.Amount), xValue, yy, pValueNew);

                    Rect rect = new Rect();
                    pValueNew.getTextBounds(BHUtilities.numericFormat(paymentInfo.Amount), 0, BHUtilities.numericFormat(paymentInfo.Amount).length(), rect);

                    cv.drawText(bahtLabel, xValue + rect.width(), yy, pValue);
                }
                // [END] :: Fixed - [BHPROJ-0026-751] :: แก้ไขการแสดงผลในส่วนของ ยอดชำระเงิน ให้เป็นตัวสีแดง + ตัวหนา + เพิ่มขนาดตัวหนังสือมา 1 ระดับ
            }
        }

        if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
            texts = getText(BHUtilities.ThaiBaht(BHUtilities.numericFormat(paymentInfo.CloseAccountNetAmount)), pValue, LAYOUT_WIDTH - xValue);
            for (int ii = 0; ii < texts.length; ii++) {
                yy += fontSize + lineSpace;
                cv.drawText(texts[ii], xValue, yy, pValue);
            }
        } else {
            texts = getText(BHUtilities.ThaiBaht(BHUtilities.numericFormat(paymentInfo.Amount)), pValue, LAYOUT_WIDTH - xValue);
            for (int ii = 0; ii < texts.length; ii++) {
                yy += fontSize + lineSpace;
                cv.drawText(texts[ii], xValue, yy, pValue);
            }
        }


        yy += fontSize + lineSpace;
        cv.drawText(BHUtilities.ThaiBaht(BHUtilities.numericFormat(paymentInfo.Amount)), xValue, yy, pValue);

        if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {

        } else{
            if (paymentInfo.BalancesOfPeriod != 0) {
                yy += fontSize + lineSpace;
                if (paymentInfo.MODE == 1) {
                    cv.drawText("คงเหลือเงินสด", xTitle, yy, pTitle);
                } else {
                    cv.drawText(String.format("คงเหลืองวดที่ %d", paymentInfo.PaymentPeriodNumber), xTitle, yy, pTitle);
                }
                cv.drawText(BHUtilities.numericFormat(paymentInfo.BalancesOfPeriod) + bahtLabel, xValue, yy, pValue);
            }

            if (paymentInfo.Balances - paymentInfo.BalancesOfPeriod != 0) {
                yy += fontSize + lineSpace;

                if (paymentInfo.MODE == 1) {
                    cv.drawText("คงเหลือเงินสด", xTitle, yy, pTitle);
                } else {
                if (paymentInfo.BalancesOfPeriod == 0) {
                    if ((paymentInfo.PaymentPeriodNumber + 1) == paymentInfo.MODE) {
                        cv.drawText(String.format("คงเหลืองวดที่ %d", paymentInfo.MODE), xTitle, yy, pTitle);
                    } else {
                        cv.drawText(String.format("คงเหลืองวดที่ %d - %d", paymentInfo.PaymentPeriodNumber + 1, paymentInfo.MODE), xTitle, yy, pTitle);
                    }
                } else {
                    if (paymentInfo.PaymentPeriodNumber == paymentInfo.MODE) {
                        cv.drawText(String.format("คงเหลืองวดที่ %d", paymentInfo.MODE), xTitle, yy, pTitle);
                    } else {
                        cv.drawText(String.format("คงเหลืองวดที่ %d - %d", paymentInfo.PaymentPeriodNumber, paymentInfo.MODE), xTitle, yy, pTitle);
                    }
                }
                    cv.drawText(String.format("คงเหลืองวดที่ %d - %d", paymentInfo.PaymentPeriodNumber + 1, paymentInfo.MODE), xTitle, yy, pTitle);
                }
                cv.drawText(BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + bahtLabel, xValue, yy, pValue);
            }
        }

        switch (Enum.valueOf(PaymentInfo.PaymentType1.class, paymentInfo.PaymentType)) {
            case Cash:
                break;
            case Credit:
                yy += fontSize + lineSpace;
                cv.drawText("ชำระโดยบัตรเครดิต", xTitle, yy, pTitle);
                cv.drawText(BHUtilities.trim(paymentInfo.BankName), xValue, yy, pValue);

                yy += fontSize + lineSpace;
                cv.drawText("เลขที่บัตร", xTitle, yy, pTitle);
                cv.drawText(BHUtilities.trim(paymentInfo.CreditCardNumber), xValue, yy, pValue);
                break;
            case Cheque:
                yy += fontSize + lineSpace;
                cv.drawText("ชำระโดยเช็ค", xTitle, yy, pTitle);
                cv.drawText(BHUtilities.trim(paymentInfo.BankName), xValue, yy, pValue);


                yy += fontSize + lineSpace;
                cv.drawText("สาขา", xTitle, yy, pTitle);
                cv.drawText(BHUtilities.trim(paymentInfo.ChequeBankBranch), xValue, yy, pValue);

                yy += fontSize + lineSpace;
                cv.drawText("เลขที่เช็ค", xTitle, yy, pTitle);
                cv.drawText(BHUtilities.trim(paymentInfo.ChequeNumber), xValue, yy, pValue);

                yy += fontSize + lineSpace;
                cv.drawText("ลงวันที่", xTitle, yy, pTitle);

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date date = formatter.parse(paymentInfo.ChequeDate);
                    cv.drawText(BHUtilities.dateFormat(date), xValue, yy, pValue);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
            default:
                break;
        }

        pValue.setTextAlign(Align.LEFT);
        Paint pSignature = new Paint(pValue);
        pSignature.setTextAlign(Align.CENTER);


        yy += 200;
        String sale = String.format("(%s)", paymentInfo.SaleEmployeeName != null ? paymentInfo.SaleEmployeeName : "");
        String team = String.format("(%s)", paymentInfo.TeamCode != null ? paymentInfo.TeamCode : "");
        int ww = getWidth(sale, pValue) + 50;

        cv.drawText(getSignatureUnderline(pSignature, ww), xValue, yy, pSignature);
        cv.drawText("ผู้รับเงิน", xValue + 190, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText(sale, xValue, yy, pSignature);
        yy += fontSize + lineSpace;
        cv.drawText(team, xValue, yy, pSignature);

        yy += LINE_FEED;

        Bitmap result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy, Config.ARGB_8888);
        cv = new Canvas(result);
        cv.drawBitmap(img, 0, 0, null);
        img.recycle();

        return scaleBitmapByWidth(result);
    }*/


    /*public static ShortReceiptInfo getShortReceipt(PaymentInfo paymentInfo, DebtorCustomerInfo debtorCustomerInfo, AddressInfo addressInfo) {

        ShortReceiptInfo shortReceiptInfo = new ShortReceiptInfo();

        Bitmap receiptHeader = Bitmap.createBitmap(LAYOUT_WIDTH, 3000, Config.ARGB_8888);
        receiptHeader.setHasAlpha(true);

        Canvas cv = new Canvas(receiptHeader);
        cv.drawColor(Color.WHITE);

        float yy = 0;
        Bitmap header = getShortHeader();
        cv.drawBitmap(header, 0, yy, null);
        yy += 210;
        header.recycle();

        float fontSize = 60;
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        p.setTextSize(fontSize);
        p.setTextAlign(Align.CENTER);
        yy += fontSize + LINE_SPACE;
        cv.drawText("ใบเสร็จรับเงิน/ใบกำกับภาษีอย่างย่อ", LAYOUT_WIDTH / 2, yy, p);

        Bitmap resultHeader = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy, Config.ARGB_8888);
        cv = new Canvas(resultHeader);
        cv.drawBitmap(receiptHeader, 0, 0, null);
        receiptHeader.recycle();

        shortReceiptInfo.receiptHeader =  scaleBitmapByWidth(resultHeader);

        //zone detail
        String detail="\n";
        detail += "Receipt No. "+paymentInfo.ReceiptCode+" (VAT Included)\n";
        detail += "Receipt Date. "+BHUtilities.dateFormat(paymentInfo.PayDate)+"\n";
        detail += "Contract No. "+paymentInfo.CONTNO;

        if (paymentInfo.ManualVolumeNo != null && paymentInfo.ManualRunningNo > 0) {
            String ManualDocumentBookRunningNo = String.format("%s/%d", BHUtilities.trim(paymentInfo.ManualVolumeNo), paymentInfo.ManualRunningNo).replace(' ', '0');
            detail += "Ref No. "+ManualDocumentBookRunningNo;
        }
        detail += "\n";
        //detail += paymentInfo.ProductName;

        shortReceiptInfo.receiptDetail1 = detail;

        //zone detail bitmap
        Bitmap receiptDetail = Bitmap.createBitmap(LAYOUT_WIDTH, 3000, Config.ARGB_8888);
        receiptDetail.setHasAlpha(true);

        cv = new Canvas(receiptDetail);
        cv.drawColor(Color.WHITE);

        yy = 21;

        fontSize = 42;
        float lineSpace = fontSize / 2;
        int xTitle = 1;

        yy += lineSpace;
        cv.drawColor(Color.WHITE);

        Paint pTitle = new Paint(p);
        pTitle.setTypeface(Typeface.DEFAULT_BOLD);
        pTitle.setTextSize(fontSize);
        pTitle.setTextAlign(Align.LEFT);

        Paint pValue = new Paint(pTitle);
        pValue.setTypeface(null);


        cv.drawText(paymentInfo.ProductName, xTitle, yy, pValue);

        if (paymentInfo.MODE == 1) {
            if (paymentInfo.BalancesOfPeriod == 0) {
               // detail +=BHUtilities.trim("งวด 1");

                cv.drawText(BHUtilities.trim("งวด 1"), 650, yy, pValue);
            }
        } else {
            if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
            } else{
                if (paymentInfo.BalancesOfPeriod == 0) {
                    //detail += BHUtilities.trim(String.format("งวด %d/%d", paymentInfo.PaymentPeriodNumber, paymentInfo.MODE))+"\n";
                    cv.drawText(BHUtilities.trim(String.format("งวด %d/%d", paymentInfo.PaymentPeriodNumber, paymentInfo.MODE)), 650, yy, pValue);
                }
            }

        }
        yy += lineSpace;

        Bitmap resultDetail = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy, Config.ARGB_8888);
        cv = new Canvas(resultDetail);
        cv.drawBitmap(receiptDetail, 0, 0, null);
        receiptDetail.recycle();

        shortReceiptInfo.receiptDetail = scaleBitmapByWidth(resultDetail);

        detail = "";    //clear

        detail += ThemalPrintController.calculateLeght("", "ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท\n");

        detail += ThemalPrintController.calculateLeght("", "รวม " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท\n");


        if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {

        } else{
            if (paymentInfo.BalancesOfPeriod != 0) {
                if (paymentInfo.MODE == 1) {

                } else {
                    detail += String.format("คงขาด งวด %d", paymentInfo.PaymentPeriodNumber) + " "+BHUtilities.numericFormat(paymentInfo.BalancesOfPeriod) + " บาท";

                }

            }

            if (paymentInfo.Balances - paymentInfo.BalancesOfPeriod != 0) {

                if (paymentInfo.MODE == 1) {
                } else {
                    detail +=String.format("คงขาด งวด %d-%d", paymentInfo.PaymentPeriodNumber + 1, paymentInfo.MODE) + " "+BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod)+" บาท";
                }

            }
        }

        shortReceiptInfo.receiptDetail2 = detail;


        //zone tailer
        Bitmap receiptTailer = Bitmap.createBitmap(LAYOUT_WIDTH, 3000, Config.ARGB_8888);
        receiptTailer.setHasAlpha(true);

        cv = new Canvas(receiptTailer);
        cv.drawColor(Color.WHITE);

        yy = 0;

        fontSize = 42;

        int xValue = 200;

        String bahtLabel = " บาท";

        pValue.setTextAlign(Align.LEFT);
        Paint pSignature = new Paint(pValue);
        pSignature.setTextAlign(Align.LEFT);

        String sale = String.format("%s", paymentInfo.SaleEmployeeName != null ? paymentInfo.SaleEmployeeName : "");
        String team = String.format("%s", paymentInfo.TeamCode != null ? paymentInfo.TeamCode : "");

        yy += fontSize + lineSpace;
        cv.drawText("ผุ้รับเงิน", xTitle, yy, pValue);
        cv.drawText(sale, xValue, yy, pValue);

        yy += lineSpace;

        String[] texts = new String[]{""};
        texts = getText(debtorCustomerInfo.CustomerFullName(), pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("ชื่อลูกค้า", xTitle, yy, pValue);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }

        yy += LINE_FEED;


        Bitmap resultTailer = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy, Config.ARGB_8888);
        cv = new Canvas(resultTailer);
        cv.drawBitmap(receiptTailer, 0, 0, null);
        receiptTailer.recycle();

        shortReceiptInfo.receiptTailer =  scaleBitmapByWidth(resultTailer);

        return shortReceiptInfo;

    }*/

    /*public static ShortReceiptInfo getTextReceipt(PaymentInfo paymentInfo, DebtorCustomerInfo debtorCustomerInfo, AddressInfo addressInfo) {
        ShortReceiptInfo shortReceiptInfo = new ShortReceiptInfo();


        shortReceiptInfo.listTxt.add("บริษัท เธียรสุรัตน์ จำกัด (มหาชน)");
        shortReceiptInfo.listTxt.add("เลขประจำตัวผู้เสียภาษี 0107556000213");
        shortReceiptInfo.listTxt.add("ใบเสร็จรับเงิน/ใบกำกับภาษีอย่างย่อ");


        shortReceiptInfo.listTxt.add(ThemalPrintController.calculateCenter("บริษัท เธียรสุรัตน์ จำกัด (มหาชน)"));
        shortReceiptInfo.listTxt.add(ThemalPrintController.calculateCenter("เลขประจำตัวผู้เสียภาษี 0107556000213"));
        shortReceiptInfo.listTxt.add(ThemalPrintController.calculateCenter("ใบเสร็จรับเงิน/ใบกำกับภาษีอย่างย่อ"));

        shortReceiptInfo.listTxt.add(ThemalPrintController.getBetweenSpace("เลขที่ใบเสร็จ", paymentInfo.ReceiptCode + " (รวม Vat)"));
        shortReceiptInfo.listTxt.add(ThemalPrintController.getBetweenSpace("วันที่รับเงิน", (BHUtilities.dateFormat(paymentInfo.PayDate) + "")));
        shortReceiptInfo.listTxt.add(ThemalPrintController.getBetweenSpace("เลขที่สัญญา", paymentInfo.CONTNO));

        if (paymentInfo.ManualVolumeNo != null && paymentInfo.ManualRunningNo > 0) {
            String ManualDocumentBookRunningNo = String.format("%s/%d", BHUtilities.trim(paymentInfo.ManualVolumeNo), paymentInfo.ManualRunningNo).replace(' ', '0');
            shortReceiptInfo.listTxt.add(ThemalPrintController.getBetweenSpace("อ้างอิงสัญญาเลขที่", ManualDocumentBookRunningNo + ""));
        }


        ThemalPrintController.addLongText(shortReceiptInfo.listTxt, paymentInfo.ProductName);

        if (paymentInfo.MODE == 1) {
            if (paymentInfo.BalancesOfPeriod == 0) {
                // detail +=BHUtilities.trim("งวด 1");

                shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght(BHUtilities.trim("งวด 1 (ชำระครบ)"), "ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
                //shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght("    ", "รวม " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
            } else {
                shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght(BHUtilities.trim("งวด 1 (ชำระบางส่วน)"), "ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
            }
            shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght("        ", "รวม " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
        } else {
            if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
                if(paymentInfo.PaymentPeriodNumber == paymentInfo.MODE) {
                    shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght(BHUtilities.trim(String.format("งวด %d", paymentInfo.MODE)), BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount) + " บาท"));
                } else {
                    shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght(BHUtilities.trim(String.format("งวด %d-%d", paymentInfo.PaymentPeriodNumber, paymentInfo.MODE)), BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount) + " บาท"));
                }
            } else{
                String txtPeriodAmountLabel = "";
                if (paymentInfo.BalancesOfPeriod == 0) {
                    //detail += BHUtilities.trim(String.format("งวด %d/%d", paymentInfo.PaymentPeriodNumber, paymentInfo.MODE))+"\n";

                    if(paymentInfo.PaymentPeriodNumber == paymentInfo.MODE){
                        txtPeriodAmountLabel = "งวด %d/%d (ชำระครบ)";
                    } else {
                        txtPeriodAmountLabel = "งวด %d/%d";
                    }
                    shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght(BHUtilities.trim(String.format(txtPeriodAmountLabel, paymentInfo.PaymentPeriodNumber, paymentInfo.MODE)), "ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
                    //shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght("        ", "รวม " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
                } else {
                    txtPeriodAmountLabel = "งวด %d (ชำระบางส่วน)";
                    shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght(BHUtilities.trim(String.format(txtPeriodAmountLabel, paymentInfo.PaymentPeriodNumber)), "ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
                }
                shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght("        ", "รวม " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
            }
        }

        if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
            shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght("ส่วนลดตัดสด", BHUtilities.numericFormat(paymentInfo.CloseAccountDiscountAmount) + " บาท"));
            shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght("จำนวนที่ชำระ", BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount - paymentInfo.CloseAccountDiscountAmount) + " บาท"));

        } else{
            if (paymentInfo.BalancesOfPeriod != 0) {
                shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght(String.format("คงเหลืองวดที่ %d", paymentInfo.PaymentPeriodNumber), "ราคา " + BHUtilities.numericFormat(paymentInfo.BalancesOfPeriod) + " บาท"));
                shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght("วันนัดชำระ", BHUtilities.dateFormat(paymentInfo.PaymentAppointmentDate)));
            }

            if (paymentInfo.Balances - paymentInfo.BalancesOfPeriod != 0) {
                if (paymentInfo.MODE == 1) {
                } else {
                    if((paymentInfo.PaymentPeriodNumber + 1) == paymentInfo.MODE){
                        shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght(String.format("คงขาด งวด %d", paymentInfo.MODE), BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + " บาท"));
                    }else {
                        shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght(String.format("คงขาด งวด %d-%d", paymentInfo.PaymentPeriodNumber + 1, paymentInfo.MODE), BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + " บาท"));
                    }
                }
            }

            /*if (paymentInfo.Balances != 0) {
                if (paymentInfo.MODE == 1) {
                    shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght(String.format("คงขาด งวด %d", paymentInfo.PaymentPeriodNumber), BHUtilities.numericFormat(paymentInfo.Balances) + " บาท"));
                } else {
                    if(paymentInfo.PaymentPeriodNumber == paymentInfo.MODE){
                        shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght(String.format("คงขาด งวด %d", paymentInfo.PaymentPeriodNumber), BHUtilities.numericFormat(paymentInfo.Balances) + " บาท"));
                    } else{
                        int paymentPeriodNumber = paymentInfo.PaymentPeriodNumber;
                        if(paymentInfo.BalancesOfPeriod == 0){
                            paymentPeriodNumber++;
                        }
                        shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght(String.format("คงขาด งวด %d-%d", paymentPeriodNumber, paymentInfo.MODE), BHUtilities.numericFormat(paymentInfo.Balances) + " บาท"));
                    }
                }
            }
        }

        String sale = String.format("%s", paymentInfo.SaleEmployeeName != null ? paymentInfo.SaleEmployeeName : "");
        String team = String.format("%s", paymentInfo.TeamCode != null ? paymentInfo.TeamCode : "");


        ThemalPrintController.addLongText(shortReceiptInfo.listTxt, "ผู้รับเงิน " + sale);
        ThemalPrintController.addLongText(shortReceiptInfo.listTxt, "ชื่อลูกค้า " + debtorCustomerInfo.CustomerFullName());



        return shortReceiptInfo;
    }*/


    public static List<PrintTextInfo> getTextReceiptNew(/*PaymentInfo paymentInfo, DebtorCustomerInfo debtorCustomerInfo, AddressInfo addressInfo*/) {
        List<PrintTextInfo> listText = new ArrayList<>();

        listText.addAll(getTextShortHeader());
        listText.add(new PrintTextInfo("selectPageMode"));
        listText.add(new PrintTextInfo("setPageRegion(0, 0, 570, 650 ,alignLeft)"));
        listText.add(new PrintTextInfo("beginPage(0, 4)"));
        listText.addAll(getTextAlignCenter("ใบเสร็จรับเงิน/ใบกำกับภาษีอย่างย่อ", PrintTextInfo.FontType.Bold));
        listText.add(new PrintTextInfo("printTitleBackground(0, 0, 570, 64)"));
        listText.add(new PrintTextInfo("beginPage(0, 66)"));
        listText.add(new PrintTextInfo("printFrame(0, 0, 570, 650)"));
        // [Start] Fixed - [BHPROJ-0026-3267][Android-ใบเสร็จรับเงินที่พิมพ์ออกมา] ปรับรูปแบบใบเสร็จตามรายละเอียดที่แจ้งมาในรูป
        /*listText.addAll(getTextAlignLeftByOffSetLeft(" วันที่รับเงิน", BHUtilities.dateFormat(paymentInfo.PayDate) + " เวลา " + BHUtilities.dateFormat(paymentInfo.PayDate, "HH:mm") + " น."));
        listText.addAll(getTextAlignLeftByOffSetLeft(" เลขที่", paymentInfo.ReceiptCode));
        listText.addAll(getTextAlignLeftByOffSetLeft(" เลขที่สัญญา", paymentInfo.CONTNO));
        listText.addAll(getTextAlignLeft(" ชื่อลูกค้า " + debtorCustomerInfo.CustomerFullName()));*/


        /*if (paymentInfo.ManualVolumeNo != null && paymentInfo.ManualRunningNo > 0) {
            String ManualDocumentBookRunningNo = String.format("%s/%d", BHUtilities.trim(paymentInfo.ManualVolumeNo), paymentInfo.ManualRunningNo).replace(' ', '0');
            listText.addAll(getTextAlignLeftByOffSetLeft(" อ้างอิงสัญญาเลขที่", ManualDocumentBookRunningNo));
        }*/

        //listText.addAll(getTextAlignLeft(" " + paymentInfo.ProductName));

        /*if (paymentInfo.MODE == 1) {
            if (paymentInfo.BalancesOfPeriod == 0) {
                listText.addAll(getTextAlignLeftRight(BHUtilities.trim(" งวด 1 (ชำระครบ)"), "ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
            } else {
                listText.addAll(getTextAlignLeftRight(BHUtilities.trim(" งวด 1 (ชำระบางส่วน)"), "ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
            }
            listText.addAll(getTextAlignLeftRight(" ", "รวม " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
        } else {
            if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
                if(paymentInfo.PaymentPeriodNumber == paymentInfo.MODE) {
                    listText.addAll(getTextAlignLeftRight(BHUtilities.trim(String.format(" งวด %d", paymentInfo.MODE)), BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount) + " บาท"));
                } else {
                    listText.addAll(getTextAlignLeftRight(BHUtilities.trim(String.format(" งวด %d-%d", paymentInfo.PaymentPeriodNumber, paymentInfo.MODE)), BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount) + " บาท"));
                }
            } else{
                String txtPeriodAmountLabel = "";
                if (paymentInfo.BalancesOfPeriod == 0) {
                    if(paymentInfo.PaymentPeriodNumber == paymentInfo.MODE){
                        txtPeriodAmountLabel = "ชำระงวดที่ %d (ชำระครบ)";
                    } else {
                        txtPeriodAmountLabel = "ชำระงวดที่ %d";
                    }
                    listText.addAll(getTextAlignLeftRight(" " + BHUtilities.trim(String.format(txtPeriodAmountLabel, paymentInfo.PaymentPeriodNumber)), BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
                } else {
                    txtPeriodAmountLabel = " งวด %d (ชำระบางส่วน)";
                    listText.addAll(getTextAlignLeftRight(" " + BHUtilities.trim(String.format(txtPeriodAmountLabel, paymentInfo.PaymentPeriodNumber)), BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
                }
                //listText.addAll(getTextAlignLeftRight("", "รวม " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
            }
        }*/

        /*if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
            listText.addAll(getTextAlignLeftRight(" ส่วนลดตัดสด", BHUtilities.numericFormat(paymentInfo.CloseAccountDiscountAmount) + " บาท"));
            listText.addAll(getTextAlignLeftRight(" จำนวนที่ชำระ", BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount - paymentInfo.CloseAccountDiscountAmount) + " บาท"));
        } else{
            if (paymentInfo.BalancesOfPeriod != 0) {
                listText.addAll(getTextAlignLeftRight(String.format(" คงเหลืองวดที่ %d", paymentInfo.PaymentPeriodNumber), "ราคา " + BHUtilities.numericFormat(paymentInfo.BalancesOfPeriod) + " บาท"));
                listText.addAll(getTextAlignLeftRight(" วันนัดชำระ", BHUtilities.dateFormat(paymentInfo.PaymentAppointmentDate)));
            }

            if (paymentInfo.Balances - paymentInfo.BalancesOfPeriod != 0) {
                if (paymentInfo.MODE == 1) {
                } else {
                    if((paymentInfo.PaymentPeriodNumber + 1) == paymentInfo.MODE){
                        listText.addAll(getTextAlignLeftRight(String.format(" คงเหลือ งวดที่ %d เป็นเงิน", paymentInfo.MODE), BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + " บาท"));
                    }else {
                        listText.addAll(getTextAlignLeftRight(String.format(" คงเหลือ งวดที่ %d ถึง %d เป็นเงิน", paymentInfo.PaymentPeriodNumber + 1, paymentInfo.MODE), BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + " บาท"));
                    }
                }
            }
        }*/

        //String sale = String.format("%s", paymentInfo.SaleEmployeeName != null ? paymentInfo.SaleEmployeeName : "");
        //String team = String.format("%s", paymentInfo.TeamCode != null ? paymentInfo.TeamCode : "");

        listText.addAll(getTextAlignCenter("  "));
        listText.addAll(getTextAlignCenter(" ____________________________ผู้รับเงิน"));
        //listText.addAll(getTextAlignCenter(String.format(" (%s) %s", sale, paymentInfo.CashCode)));
        // [End] Fixed - [BHPROJ-0026-3267][Android-ใบเสร็จรับเงินที่พิมพ์ออกมา] ปรับรูปแบบใบเสร็จตามรายละเอียดที่แจ้งมาในรูป

        listText.add(new PrintTextInfo("endPage"));
        listText.add(new PrintTextInfo("selectStandardMode"));
        return listText;
    }

    /*public static Bitmap getImageReceiptNew(PaymentInfo paymentInfo, DebtorCustomerInfo debtorCustomerInfo, AddressInfo addressInfo) {

        Bitmap img = Bitmap.createBitmap(LAYOUT_WIDTH, 3000, Config.ARGB_8888);
        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
        cv.drawColor(Color.WHITE);

        float yy = 0;
        float fontSize = 44;

        Bitmap header = getImageShortHeader();
        cv.drawBitmap(header, 0, yy, null);
        yy += header.getHeight();
        header.recycle();


        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        p.setTextSize(fontSize);
        p.setTextAlign(Align.CENTER);

        yy += fontSize * 2;
        cv.drawText("ใบเสร็จรับเงิน/ใบกำกับภาษีอย่างย่อ", LAYOUT_WIDTH / 2, yy, p);


        float lineSpace = fontSize;//fontSize / 2;
        int xTitle = 1;
        int xValue = 480;

        Paint pTitle = new Paint(p);
        pTitle.setTypeface(Typeface.DEFAULT);
        pTitle.setTextSize(fontSize);
        pTitle.setTextAlign(Align.LEFT);

        Paint pValue = new Paint(pTitle);

        String[] texts = new String[]{""};

        // [Start] Fixed - [BHPROJ-0026-3267][Android-ใบเสร็จรับเงินที่พิมพ์ออกมา] ปรับรูปแบบใบเสร็จตามรายละเอียดที่แจ้งมาในรูป **
        yy += fontSize + lineSpace;
        cv.drawText("วันที่รับเงิน", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.dateFormat(paymentInfo.PayDate) + " เวลา " + BHUtilities.dateFormat(paymentInfo.PayDate, "HH:mm") + " น.", xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("เลขที่", xTitle, yy, pTitle);
        cv.drawText(paymentInfo.ReceiptCode, xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("เลขที่สัญญา", xTitle, yy, pTitle);
        cv.drawText(paymentInfo.CONTNO, xValue, yy, pValue);

        texts = getText(debtorCustomerInfo.CustomerFullName(), pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("ชื่อลูกค้า", xTitle, yy, pTitle);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }

        if (paymentInfo.ManualVolumeNo != null && paymentInfo.ManualRunningNo > 0) {
            String ManualDocumentBookRunningNo = String.format("%s/%d", BHUtilities.trim(paymentInfo.ManualVolumeNo), paymentInfo.ManualRunningNo).replace(' ', '0');
            yy += fontSize + lineSpace;
            cv.drawText("อ้างอิงสัญญาเลขที่", xTitle, yy, pTitle);
            cv.drawText(ManualDocumentBookRunningNo, xValue, yy, pValue);
        }

        texts = getText(paymentInfo.ProductName, pTitle, LAYOUT_WIDTH - xTitle);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            cv.drawText(texts[ii], xTitle, yy, pTitle);
        }


        pValue.setTextAlign(Align.RIGHT);
        int xValueAlignRIGHT = LAYOUT_WIDTH - 5;

        if (paymentInfo.MODE == 1) {
            if (paymentInfo.BalancesOfPeriod == 0) {
                yy += fontSize + lineSpace;
                cv.drawText(BHUtilities.trim("งวด 1 (ชำระครบ)"), xTitle, yy, pTitle);
                cv.drawText("ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", xValueAlignRIGHT, yy, pValue);
            } else {
                yy += fontSize + lineSpace;
                cv.drawText(BHUtilities.trim("งวด 1 (ชำระบางส่วน)"), xTitle, yy, pTitle);
                cv.drawText("ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", xValueAlignRIGHT, yy, pValue);
            }

            yy += fontSize + lineSpace;
            cv.drawText("รวม " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", xValueAlignRIGHT, yy, pValue);
        } else {
            if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
                if (paymentInfo.PaymentPeriodNumber == paymentInfo.MODE) {
                    yy += fontSize + lineSpace;
                    cv.drawText(BHUtilities.trim(String.format("งวด %d", paymentInfo.MODE)), xTitle, yy, pTitle);
                    cv.drawText(BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount) + " บาท", xValueAlignRIGHT, yy, pValue);
                } else {
                    yy += fontSize + lineSpace;
                    cv.drawText(BHUtilities.trim(String.format("งวด %d-%d", paymentInfo.PaymentPeriodNumber, paymentInfo.MODE)), xTitle, yy, pTitle);
                    cv.drawText(BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount) + " บาท", xValueAlignRIGHT, yy, pValue);
                }
            } else{
                String txtPeriodAmountLabel = "";
                if (paymentInfo.BalancesOfPeriod == 0) {
                    if(paymentInfo.PaymentPeriodNumber == paymentInfo.MODE){
                        txtPeriodAmountLabel = "ชำระงวดที่ %d (ชำระครบ)";
                    } else {
                        txtPeriodAmountLabel = "ชำระงวดที่ %d";
                    }

                    yy += fontSize + lineSpace;
                    cv.drawText(BHUtilities.trim(String.format(txtPeriodAmountLabel, paymentInfo.PaymentPeriodNumber)), xTitle, yy, pTitle);
                    cv.drawText(BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", xValueAlignRIGHT, yy, pValue);
                } else {
                    txtPeriodAmountLabel = "งวด %d (ชำระบางส่วน)";

                    yy += fontSize + lineSpace;
                    cv.drawText(BHUtilities.trim(String.format(txtPeriodAmountLabel, paymentInfo.PaymentPeriodNumber)), xTitle, yy, pTitle);
                    cv.drawText(BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", xValueAlignRIGHT, yy, pValue);
                }
                //listText.addAll(getTextAlignLeftRight("", "รวม " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
            }
        }

        if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
            yy += fontSize + lineSpace;
            cv.drawText("ส่วนลดตัดสด", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(paymentInfo.CloseAccountDiscountAmount) + " บาท", xValueAlignRIGHT, yy, pValue);

            yy += fontSize + lineSpace;
            cv.drawText("จำนวนที่ชำระ", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount - paymentInfo.CloseAccountDiscountAmount) + " บาท", xValueAlignRIGHT, yy, pValue);
        } else{

            if (paymentInfo.BalancesOfPeriod != 0) {
                yy += fontSize + lineSpace;
                cv.drawText(String.format("คงเหลืองวดที่ %d", paymentInfo.PaymentPeriodNumber), xTitle, yy, pTitle);
                cv.drawText("ราคา " + BHUtilities.numericFormat(paymentInfo.BalancesOfPeriod) + " บาท", xValueAlignRIGHT, yy, pValue);

                yy += fontSize + lineSpace;
                cv.drawText("วันนัดชำระ", xTitle, yy, pTitle);
                cv.drawText(BHUtilities.dateFormat(paymentInfo.PaymentAppointmentDate), xValueAlignRIGHT, yy, pValue);

            }


            if (paymentInfo.Balances - paymentInfo.BalancesOfPeriod != 0) {
                if (paymentInfo.MODE == 1) {
                } else {
                    if((paymentInfo.PaymentPeriodNumber + 1) == paymentInfo.MODE){
                        yy += fontSize + lineSpace;
                        cv.drawText(String.format("คงเหลือ งวดที่ %d เป็นเงิน", paymentInfo.MODE), xTitle, yy, pTitle);
                        cv.drawText(BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + " บาท", xValueAlignRIGHT, yy, pValue);
                    }else {
                        yy += fontSize + lineSpace;
                        cv.drawText(String.format("คงเหลือ งวดที่ %d ถึง %d เป็นเงิน", paymentInfo.PaymentPeriodNumber + 1, paymentInfo.MODE), xTitle, yy, pTitle);
                        cv.drawText(BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + " บาท", xValueAlignRIGHT, yy, pValue);
                    }
                }
            }
        }


        Paint pSignature = new Paint(pValue);
        pSignature.setTextAlign(Align.CENTER);

        String sale = String.format("%s", paymentInfo.SaleEmployeeName != null ? paymentInfo.SaleEmployeeName : "");
        //String team = String.format("%s", paymentInfo.TeamCode != null ? paymentInfo.TeamCode : "");

        yy += fontSize * 4;
        cv.drawText("____________________________ผู้รับเงิน", LAYOUT_WIDTH / 2, yy, pSignature);

        yy += fontSize + lineSpace;
        cv.drawText(String.format("(%s) %s", sale, paymentInfo.CashCode), LAYOUT_WIDTH / 2, yy, pSignature);
        // [End] Fixed - [BHPROJ-0026-3267][Android-ใบเสร็จรับเงินที่พิมพ์ออกมา] ปรับรูปแบบใบเสร็จตามรายละเอียดที่แจ้งมาในรูป

        yy += LINE_FEED;

        Bitmap result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy, Config.ARGB_8888);
        cv = new Canvas(result);
        cv.drawBitmap(img, 0, 0, null);
        img.recycle();

        return scaleBitmapByWidth(result);

    }*/


    /*public static Bitmap getReturnProduct(List<ReturnProductDetailInfo> returnList, int newHeight) {

        int defaultHeight = 3000;
        Bitmap img;
        if (newHeight == 0) {
            img = Bitmap.createBitmap(LAYOUT_WIDTH, defaultHeight, Config.ARGB_8888);
        } else {
            img = Bitmap.createBitmap(LAYOUT_WIDTH, newHeight, Config.ARGB_8888);
        }

        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
        cv.drawColor(Color.WHITE);

        float yy = 0;
        Bitmap header = getHeader();
        cv.drawBitmap(header, 0, yy, null);
        yy += header.getHeight();
        header.recycle();

        float fontSize = 90;
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        p.setTextSize(fontSize);
        p.setTextAlign(Align.CENTER);
        yy += fontSize + LINE_SPACE;
        cv.drawText("ใบส่งคืนสินค้า", LAYOUT_WIDTH / 2, yy, p);

        fontSize = 42;
        float lineSpace = fontSize / 2;
        int xTitle = 1;
        int xValue = 480;


        Paint pTitle = new Paint(p);
        pTitle.setTypeface(Typeface.DEFAULT_BOLD);
        pTitle.setTextSize(fontSize);
        pTitle.setTextAlign(Align.LEFT);

        Paint pValue = new Paint(pTitle);
        pValue.setTypeface(null);

        yy += fontSize * 3;
        cv.drawText("เลขที่ใบส่งคืนสินค้า", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(returnList.get(0).ReturnProductID), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("วันที่ส่งคืนสินค้า", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.dateFormat(returnList.get(0).ReturnDate), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("พนักงานที่ส่งคืนสินค้า", xTitle, yy, pTitle);
        cv.drawText(String.format("%s %s", BHUtilities.trim(returnList.get(0).EmpID), BHUtilities.trim(returnList.get(0).EmployeeName)), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("ทีมงานที่คืนสินค้า", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(returnList.get(0).TeamCode), xValue, yy, pValue);

        String status = returnList.get(0).Status;

        if (status.equals(ReturnProductStatus.REQUEST.toString())) {
            status = "รอการตรวจสอบสินค้า";
        } else if (status.equals(ReturnProductStatus.REJECT.toString())) {
            status = "ไม่ผ่านการตรวจสอบสินค้า";
        } else if (status.equals(ReturnProductStatus.PASS.toString())) {
            status = "ผ่านการตรวจสอบสินค้า";
        }

        yy += fontSize + lineSpace;
        cv.drawText("สถานะการรับคืนสินค้า", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(status), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("หมายเหตุการรับคืนสินค้า", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(returnList.get(0).Remark), xValue, yy, pValue);

        yy += fontSize * 2;
        cv.drawText("รายละเอียดสินค้าที่ส่งคืน (" + returnList.size() + " รายการ)", xTitle, yy, pTitle);

        for (ReturnProductDetailInfo info : returnList) {

            yy += fontSize + lineSpace;
            cv.drawText("รหัสสินค้า", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.trim(info.ProductSerialNumber), xValue, yy, pValue);

        }

        yy += LINE_FEED;

        Bitmap result;
        if (defaultHeight > yy && newHeight == 0) {
            result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy, Config.ARGB_8888);
            cv = new Canvas(result);
            cv.drawBitmap(img, 0, 0, null);
            img.recycle();

            return scaleBitmapByWidth(result);
        } else if (newHeight != 0) {
            result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy, Config.ARGB_8888);
            cv = new Canvas(result);
            cv.drawBitmap(img, 0, 0, null);
            img.recycle();

            return scaleBitmapByWidth(result);
        } else {
            img.recycle();
            return getReturnProduct(returnList, Math.round(yy));
        }
    }*/

    /*public static List<PrintTextInfo> getTextReturnProduct(List<ReturnProductDetailInfo> returnList) {
        List<PrintTextInfo> listText = new ArrayList<>();

        listText.addAll(getTextHeader());
        listText.addAll(getTextAlignCenter("ใบส่งคืนสินค้า", PrintTextInfo.FontType.Bold));

        listText.addAll(getTextAlignLeftByOffSetLeft("เลขที่ใบส่งคืนสินค้า", BHUtilities.trim(returnList.get(0).ReturnProductID)));
        listText.addAll(getTextAlignLeftByOffSetLeft("วันที่ส่งคืนสินค้า", BHUtilities.dateFormat(returnList.get(0).ReturnDate)));
        listText.addAll(getTextAlignLeftByOffSetLeft("พนักงานที่ส่งคืนสินค้า", String.format("%s %s", BHUtilities.trim(returnList.get(0).EmpID), BHUtilities.trim(returnList.get(0).EmployeeName))));
        listText.addAll(getTextAlignLeftByOffSetLeft("ทีมงานที่คืนสินค้า", BHUtilities.trim(returnList.get(0).TeamCode)));

        String status = returnList.get(0).Status;
        if (status.equals(ReturnProductStatus.REQUEST.toString())) {
            status = "รอการตรวจสอบสินค้า";
        } else if (status.equals(ReturnProductStatus.REJECT.toString())) {
            status = "ไม่ผ่านการตรวจสอบสินค้า";
        } else if (status.equals(ReturnProductStatus.PASS.toString())) {
            status = "ผ่านการตรวจสอบสินค้า";
        }

        listText.addAll(getTextAlignLeftByOffSetLeft("สถานะการรับคืนสินค้า", BHUtilities.trim(status)));
        listText.addAll(getTextAlignLeftByOffSetLeft("หมายเหตุการรับคืนสินค้า", BHUtilities.trim(returnList.get(0).Remark)));

        listText.addAll(getTextAlignLeft("รายละเอียดสินค้าที่ส่งคืน (" + returnList.size() + " รายการ)"));
        for (ReturnProductDetailInfo info : returnList) {
            listText.addAll(getTextAlignLeftByOffSetLeft("รหัสสินค้า", BHUtilities.trim(info.ProductSerialNumber)));
        }

        // [START] :: Fixed-[BHPROJ-0024-3214] :: [LINE-25/07/2016][Android-ระบบส่งคืนสินค้าเข้าระบบ] เพิ่มเติมส่วนที่ให้ผู้ส่งคืนเซ็นชื่อในตอนพิมพ์
        listText.addAll(getTextAlignCenter("...............ผู้ส่ง", PrintTextInfo.FontType.Normal));
        listText.addAll(getTextAlignCenter(String.format("( %s )", BHUtilities.trim(returnList.get(0).EmployeeName))));
        // [END] :: Fixed-[BHPROJ-0024-3214] :: [LINE-25/07/2016][Android-ระบบส่งคืนสินค้าเข้าระบบ] เพิ่มเติมส่วนที่ให้ผู้ส่งคืนเซ็นชื่อในตอนพิมพ์

        return listText;
    }*/

    /*public static Bitmap getSendMoney(SendMoneyInfo sendMoney) {
        Bitmap img = Bitmap.createBitmap(LAYOUT_WIDTH, 3000, Config.ARGB_8888);
        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
        cv.drawColor(Color.WHITE);

        float yy = 0;
        Bitmap header = getHeader();
        cv.drawBitmap(header, 0, yy, null);
        yy += header.getHeight();
        header.recycle();

        float fontSize = 90;
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        p.setTextSize(fontSize);
        p.setTextAlign(Align.CENTER);

        yy += fontSize + LINE_SPACE;
        cv.drawText(String.format("ใบนำส่ง%s", sendMoney.PaymentTypeName), LAYOUT_WIDTH / 2, yy, p);
        yy += fontSize + LINE_SPACE * 3;

        Paint SubTitle = new Paint(p);
        SubTitle.setTextSize(65);
        cv.drawText(String.format("(%s)", sendMoney.ChannelItemName), LAYOUT_WIDTH / 2, yy, SubTitle);

        yy += 50;

        Bitmap barcode;
        String barcodeNum;

        // [START] :: Fixed - [BHPROJ-0024-2061] :: [Android-พิมพ์ใบนำส่งเงิน] กรณีพิมพ์ใบนำส่งเงินออกเครื่อง Printer มีการส่งค่า Barcode ออกมาไม่ครบถ้วน
        if (sendMoney.ChannelCode.equals("HeadOffice")) {
            barcode = BHBarcode.generateCode128(sendMoney.Reference2, 850, 300);
            barcodeNum = sendMoney.Reference2;
        } else {
            barcode = BHBarcode.generateCode128(sendMoney.AccountCode1, 850, 300);
            barcodeNum = sendMoney.AccountCode1;
        }

        barcode = BHBarcode.generateCode128(sendMoney.Reference1, 850, 300);
        barcodeNum = sendMoney.Reference1;
        // [END] :: Fixed - [BHPROJ-0024-2061] :: [Android-พิมพ์ใบนำส่งเงิน] กรณีพิมพ์ใบนำส่งเงินออกเครื่อง Printer มีการส่งค่า Barcode ออกมาไม่ครบถ้วน

        if (barcode != null) {
            cv.drawBitmap(barcode, (LAYOUT_WIDTH - barcode.getWidth()) / 2, yy, null);
            yy += barcode.getHeight();
        }

        fontSize = 42;
        Paint pTitle = new Paint(p);
        pTitle.setTypeface(Typeface.DEFAULT_BOLD);
        pTitle.setTextSize(fontSize);

        yy += fontSize + 10;
        cv.drawText(barcodeNum, LAYOUT_WIDTH / 2, yy, pTitle);
        pTitle.setTextAlign(Align.LEFT);
        yy += fontSize * 2;

        float lineSpace = fontSize / 2;
        float xTitle = 1;
        float xValue = 400;

        Paint pValue = new Paint(pTitle);
        pValue.setTypeface(null);

        cv.drawText("ชื่อผู้ส่งเงิน :", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(sendMoney.FirstName + " " + sendMoney.LastName), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("รหัสผู้ส่ง :", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(sendMoney.CreateBy), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("วันที่ส่ง :", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.dateFormat(sendMoney.SendDate, BHUtilities.DEFAULT_DATE_FORMAT), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("เลขที่อ้างอิง :", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(sendMoney.Reference2), xValue, yy, pValue);

//        if (sendMoney.PaymentType.equals("Credit")) {
//
//            yy += fontSize + lineSpace;
//            cv.drawText("ธนาคาร :", xTitle, yy, pTitle);
//            cv.drawText(BHUtilities.trim(sendMoney.PayeeName), xValue, yy,
//                    pValue);
//        }

        yy += fontSize + lineSpace;
        cv.drawText("ชื่อผู้รับเงิน/สาขา :", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(sendMoney.PayeeName), xValue, yy, pValue);

        if (sendMoney.PaymentType.toLowerCase().equals("credit")) {
            yy += fontSize + lineSpace;
            cv.drawText("เชลล์สลิป :", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(sendMoney.SendAmount) + " บาท", xValue, yy, pValue);
        } else if (sendMoney.PaymentType.toLowerCase().equals("cheque")) {
            yy += fontSize + lineSpace;
            cv.drawText("เช็ค :", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(sendMoney.SendAmount) + " บาท", xValue, yy, pValue);
        }

        yy += fontSize + lineSpace;
        cv.drawText("จำนวนเงิน :", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.numericFormat(sendMoney.SendAmount) + " บาท", xValue, yy, pValue);

        yy += LINE_FEED;

        Bitmap result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy, Config.ARGB_8888);
        cv = new Canvas(result);
        cv.drawBitmap(img, 0, 0, null);
        img.recycle();

        return scaleBitmapByWidth(result);
    }*/

    /*public static List<PrintTextInfo> getTextSendMoney(SendMoneyInfo sendMoney) {

        List<PrintTextInfo> listText = new ArrayList<>();

        listText.addAll(getTextHeader());
        listText.add(new PrintTextInfo("selectPageMode"));
        listText.add(new PrintTextInfo("setPageRegion(0, 0, 570, 1000 ,alignLeft)"));
        listText.add(new PrintTextInfo("beginPage(0, 66)"));
        listText.addAll(getTextAlignCenter(String.format("ใบนำส่ง%s", sendMoney.PaymentTypeName), PrintTextInfo.FontType.Bold));

        listText.addAll(getTextAlignCenter(String.format("(%s)", sendMoney.ChannelItemName), PrintTextInfo.FontType.Bold));
        if(sendMoney.Reference2.length() > 8){
            String strBarcodeNo = sendMoney.Reference2;
            listText.add(new PrintTextInfo(strBarcodeNo, PrintTextInfo.FontType.Normal, true));
        }

        listText.addAll(getTextAlignLeftByOffSetLeft(" ชื่อผู้ส่งเงิน :", BHUtilities.trim(sendMoney.FirstName + " " + sendMoney.LastName)));
        listText.addAll(getTextAlignLeftByOffSetLeft(" รหัสผู้ส่ง :", BHUtilities.trim(sendMoney.CreateBy)));
        listText.addAll(getTextAlignLeftByOffSetLeft(" วันที่ส่ง :", BHUtilities.dateFormat(sendMoney.SendDate, BHUtilities.DEFAULT_DATE_FORMAT)));
        listText.addAll(getTextAlignLeftByOffSetLeft(" Ref. 1 :", BHUtilities.trim(sendMoney.Reference1)));
        listText.addAll(getTextAlignLeftByOffSetLeft(" Ref. 2 :", BHUtilities.trim(sendMoney.Reference2)));
        listText.addAll(getTextAlignLeftByOffSetLeft(" ชื่อผู้รับเงิน/สาขา :", BHUtilities.trim(sendMoney.PayeeName)));

        if (sendMoney.PaymentType.toLowerCase().equals("credit")) {
            listText.addAll(getTextAlignLeftByOffSetLeft(" เชลล์สลิป :", BHUtilities.numericFormat(sendMoney.SendAmount) + " บาท"));
        } else if (sendMoney.PaymentType.toLowerCase().equals("cheque")) {
            listText.addAll(getTextAlignLeftByOffSetLeft(" เช็ค :", BHUtilities.numericFormat(sendMoney.SendAmount) + " บาท"));
        }
        listText.addAll(getTextAlignLeftByOffSetLeft(" จำนวนเงิน :", BHUtilities.numericFormat(sendMoney.SendAmount) + " บาท"));
//
//        listText.add(new PrintTextInfo("setPageRegion(470, 0, 100, 1000 ,alignBottom)"));
//        listText.add(new PrintTextInfo("beginPage(0, 4)"));
//        String SendMoneyBarcode = String.format("%s|%s|%s", sendMoney.Reference1, "", BHUtilities.numericFormat(sendMoney.SendAmount).replace(",", "").replace(".", ""));
//        listText.add(new PrintTextInfo(SendMoneyBarcode, PrintTextInfo.FontType.Normal, true, true, 2));
//        listText.addAll(getTextAlignLeft(String.format(" | 010755600021300 %s %s", sendMoney.Reference1, BHUtilities.numericFormat(sendMoney.SendAmount).replace(",", "").replace(".", "")), "EN"));
//        //listText.addAll(getTextAlignCenter("", PrintTextInfo.FontType.Normal));

        listText.add(new PrintTextInfo("endPage"));
        listText.add(new PrintTextInfo("selectStandardMode"));

        if(sendMoney.Reference2.length() == 8) {

            listText.addAll(getTextAlignLeft("----------------------------------------", "EN"));
            listText.addAll(getTextAlignLeft(" สำหรับธนาคาร", PrintTextInfo.FontType.Normal));
            String SendMoneyBarcode = String.format("%s|%s|%s", sendMoney.Reference1, "", BHUtilities.numericFormat(sendMoney.SendAmount).replace(",", "").replace(".", ""));
            listText.add(new PrintTextInfo(SendMoneyBarcode, PrintTextInfo.FontType.Normal, true, true));
            listText.addAll(getTextAlignLeft(String.format(" | 010755600021300 %s %s", sendMoney.Reference1, BHUtilities.numericFormat(sendMoney.SendAmount).replace(",", "").replace(".", "")), "EN"));
            listText.addAll(getTextAlignCenter("", PrintTextInfo.FontType.Normal));
            listText.addAll(getTextAlignLeft(String.format(" เพื่อเข้าบัญชี %s",sendMoney.ChannelItemName)));
            listText.addAll(getTextAlignLeft(String.format(" EMPID(Ref.1): %s", BHUtilities.trim(sendMoney.Reference1)), "EN"));
            listText.addAll(getTextAlignLeft(" ", "EN"));
            listText.addAll(getTextAlignLeft(String.format(" TNSNO(Ref.2): %s", BHUtilities.trim(sendMoney.Reference2)), "EN"));
            listText.addAll(getTextAlignLeft(String.format(" จำนวนเงิน %s บาท", BHUtilities.numericFormat(sendMoney.SendAmount))));

            //listText.addAll(getTextAlignLeft(String.format("%s %s", sendMoney.Reference2, BHUtilities.numericFormat(sendMoney.SendAmount).replace(",", "").replace(".", ""))));
            //listText.add(new PrintTextInfo("selectStandardMode"));
        }


        return listText;
    }*/

    /*public static Bitmap getSendDocument(SendDocumentInfo sendDocument) {
        Bitmap img = Bitmap.createBitmap(LAYOUT_WIDTH, 3000, Config.ARGB_8888);
        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
        cv.drawColor(Color.WHITE);

        float yy = 0;
        Bitmap header = getHeader();
        cv.drawBitmap(header, 0, yy, null);
        yy += header.getHeight();
        header.recycle();

        float fontSize = 90;
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        p.setTextSize(fontSize);
        p.setTextAlign(Align.CENTER);
        yy += fontSize + LINE_SPACE;
        cv.drawText("ใบส่งคืนเอกสาร", LAYOUT_WIDTH / 2, yy, p);

        fontSize = 42;
        float lineSpace = fontSize / 2;
        int xTitle = 1;
        int xValue = 480;

        Paint pTitle = new Paint(p);
        pTitle.setTypeface(Typeface.DEFAULT_BOLD);
        pTitle.setTextSize(fontSize);
        pTitle.setTextAlign(Align.LEFT);

        Paint pValue = new Paint(pTitle);
        pValue.setTypeface(null);

        yy += LINE_FEED;

        Bitmap result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy,
                Config.ARGB_8888);
        cv = new Canvas(result);
        cv.drawBitmap(img, 0, 0, null);
        img.recycle();

        return scaleBitmapByWidth(result);
    }*/


    /*public static Bitmap getDocumentHistory(List<DocumentHistoryInfo> outputContract,
                                            List<DocumentHistoryInfo> outputReceipt,
                                            List<DocumentHistoryInfo> outputChangeProduct,
                                            List<DocumentHistoryInfo> outputImpoundProduct,
                                            List<DocumentHistoryInfo> outputChangeContract,
                                            List<DocumentHistoryInfo> outputManualDocument,
                                            List<DocumentHistoryInfo> outputPayInSlipBank,
                                            List<DocumentHistoryInfo> outputPayInSlipPayPoint,
                                            int newHeight) {
        int num = 0;
        int defaultHeight = 3000;
        Bitmap img;
        if (newHeight == 0) {
            img = Bitmap.createBitmap(LAYOUT_WIDTH, defaultHeight, Config.ARGB_8888);
        } else {
            img = Bitmap.createBitmap(LAYOUT_WIDTH, newHeight, Config.ARGB_8888);
        }

        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
        cv.drawColor(Color.WHITE);

        float yy = 0;
        Bitmap header = getHeader();
        cv.drawBitmap(header, 0, yy, null);
        yy += header.getHeight();
        header.recycle();

        float fontSize = 90;
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        p.setTextSize(fontSize);
        p.setTextAlign(Align.CENTER);
        yy += fontSize + LINE_SPACE;
        cv.drawText("ใบส่งเอกสาร", LAYOUT_WIDTH / 2, yy, p);

        fontSize = 42;
        float lineSpace = fontSize / 2;
        float xTitle = 1;
        float xValue = 400;

        Paint pTitleL = new Paint(p);
        pTitleL.setTypeface(Typeface.DEFAULT_BOLD);
        pTitleL.setTextSize(fontSize);
        pTitleL.setTextAlign(Align.LEFT);

        Paint pTitleR = new Paint(p);
        pTitleR.setTypeface(Typeface.DEFAULT_BOLD);
        pTitleR.setTextSize(fontSize);
        pTitleR.setTextAlign(Align.RIGHT);

        Paint pValue = new Paint(p);
        pValue.setTypeface(null);
        pValue.setTextSize(fontSize);
        pValue.setTextAlign(Align.RIGHT);

        yy += fontSize * 3;
        cv.drawText("รายการส่งเอกสาร ประจำวันที่", xTitle, yy, pTitleL);
        cv.drawText(BHUtilities.dateFormat(new Date()), LAYOUT_WIDTH - 10, yy, pValue);


        EmployeeInfo outputEmp = TSRController.getEmployeeByID(BHPreference.employeeID());
        if (outputEmp != null) {
            yy += fontSize + lineSpace;
            cv.drawText("พนักงาน", xTitle, yy, pTitleL);
            cv.drawText(outputEmp.FullName(), LAYOUT_WIDTH - 10, yy, pValue);
        }

        Paint pListView1 = new Paint(p);
        pListView1.setTypeface(null);
        pListView1.setTextSize(fontSize);
        pListView1.setTextAlign(Align.CENTER);

        Paint pListView2 = new Paint(p);
        pListView2.setTypeface(null);
        pListView2.setTextSize(fontSize);
        pListView2.setTextAlign(Align.LEFT);

        Paint pListView3 = new Paint(p);
        pListView3.setTypeface(null);
        pListView3.setTextSize(fontSize);
        pListView3.setTextAlign(Align.RIGHT);

        String[] texts = new String[]{""};
        if (outputContract != null) {
            yy += fontSize + fontSize;
            cv.drawText("เอกสารใบสัญญา", xTitle, yy, pTitleL);
            cv.drawText(String.format("%d ใบ", outputContract.size()), LAYOUT_WIDTH - 10, yy, pTitleR);
            for (DocumentHistoryInfo info : outputContract) {
                num++;
                texts = getText(info.CustomerFullName, pListView3, LAYOUT_WIDTH - 650);
                for (int ii = 0; ii < texts.length; ii++) {
                    yy += fontSize + lineSpace;
                    if (ii == 0) {
                        cv.drawText(info.No, xTitle + 50, yy, pListView1);
                        cv.drawText(info.DocumentNo, xTitle + 100, yy, pListView2);
                    }
                    cv.drawText(texts[ii], LAYOUT_WIDTH - 10, yy, pListView3);
                }
            }
        }

        if (outputReceipt != null) {
            yy += fontSize + fontSize;
            cv.drawText("เอกสารใบเสร็จ", xTitle, yy, pTitleL);
            cv.drawText(String.format("%d ใบ", outputReceipt.size()), LAYOUT_WIDTH - 10, yy, pTitleR);
            for (DocumentHistoryInfo info : outputReceipt) {
                num++;
                texts = getText(info.CustomerFullName, pListView3, LAYOUT_WIDTH - 650);
                for (int ii = 0; ii < texts.length; ii++) {
                    yy += fontSize + lineSpace;
                    if (ii == 0) {
                        cv.drawText(info.No, xTitle + 50, yy, pListView1);
                        cv.drawText(info.DocumentNo, xTitle + 100, yy, pListView2);
                    }
                    cv.drawText(texts[ii], LAYOUT_WIDTH - 10, yy, pListView3);
                }
            }
        }

        if (outputChangeProduct != null) {
            yy += fontSize + fontSize;
            cv.drawText("เอกสารใบเปลี่ยนเครื่อง", xTitle, yy, pTitleL);
            cv.drawText(String.format("%d ใบ", outputChangeProduct.size()), LAYOUT_WIDTH - 10, yy, pTitleR);
            for (DocumentHistoryInfo info : outputChangeProduct) {
                num++;
                texts = getText(info.CustomerFullName, pListView3, LAYOUT_WIDTH - 650);
                for (int ii = 0; ii < texts.length; ii++) {
                    yy += fontSize + lineSpace;
                    if (ii == 0) {
                        cv.drawText(info.No, xTitle + 50, yy, pListView1);
                        cv.drawText(info.DocumentNo, xTitle + 100, yy, pListView2);
                    }
                    cv.drawText(texts[ii], LAYOUT_WIDTH - 10, yy, pListView3);
                }
            }
        }

        if (outputImpoundProduct != null) {
            yy += fontSize + fontSize;
            cv.drawText("เอกสารใบถอดเครื่อง", xTitle, yy, pTitleL);
            cv.drawText(String.format("%d ใบ", outputImpoundProduct.size()), LAYOUT_WIDTH - 10, yy, pTitleR);
            for (DocumentHistoryInfo info : outputImpoundProduct) {
                num++;
                texts = getText(info.CustomerFullName, pListView3, LAYOUT_WIDTH - 650);
                for (int ii = 0; ii < texts.length; ii++) {
                    yy += fontSize + lineSpace;
                    if (ii == 0) {
                        cv.drawText(info.No, xTitle + 50, yy, pListView1);
                        cv.drawText(info.DocumentNo, xTitle + 100, yy, pListView2);
                    }
                    cv.drawText(texts[ii], LAYOUT_WIDTH - 10, yy, pListView3);
                }
            }
        }

        if (outputChangeContract != null) {
            yy += fontSize + fontSize;
            cv.drawText("เอกสารใบเปลี่ยนสัญญา", xTitle, yy, pTitleL);
            cv.drawText(String.format("%d ใบ", outputChangeContract.size()), LAYOUT_WIDTH - 10, yy, pTitleR);
            for (DocumentHistoryInfo info : outputChangeContract) {
                num++;
                texts = getText(info.CustomerFullName, pListView3, LAYOUT_WIDTH - 650);
                for (int ii = 0; ii < texts.length; ii++) {
                    yy += fontSize + lineSpace;
                    if (ii == 0) {
                        cv.drawText(info.No, xTitle + 50, yy, pListView1);
                        cv.drawText(info.DocumentNo, xTitle + 100, yy, pListView2);
                    }
                    cv.drawText(texts[ii], LAYOUT_WIDTH - 10, yy, pListView3);
                }
            }
        }

        if (outputManualDocument != null) {
            yy += fontSize + fontSize;
            cv.drawText("เอกสารมือ", xTitle, yy, pTitleL);
            cv.drawText(String.format("%d ใบ", outputManualDocument.size()), LAYOUT_WIDTH - 10, yy, pTitleR);
            for (DocumentHistoryInfo info : outputManualDocument) {
                num++;
                if (info.ManualDocumentTypeID.equals("0")) {
                    texts = getText(info.dc0CustomerFullName, pListView3, LAYOUT_WIDTH - 650);
                    for (int ii = 0; ii < texts.length; ii++) {
                        yy += fontSize + lineSpace;
                        if (ii == 0) {
                            cv.drawText(info.No, xTitle + 50, yy, pListView1);
                            cv.drawText(info.con0CONTNO, xTitle + 100, yy, pListView2);
                        }
                        cv.drawText(texts[ii], LAYOUT_WIDTH - 10, yy, pListView3);
                    }
                } else if (info.ManualDocumentTypeID.equals("1")) {
                    texts = getText(info.dc1CustomerFullName, pListView3, LAYOUT_WIDTH - 650);
                    for (int ii = 0; ii < texts.length; ii++) {
                        yy += fontSize + lineSpace;
                        if (ii == 0) {
                            cv.drawText(info.No, xTitle + 50, yy, pListView1);
                            cv.drawText(info.re1ReceiptCode, xTitle + 100, yy, pListView2);
                        }
                        cv.drawText(texts[ii], LAYOUT_WIDTH - 10, yy, pListView3);
                    }
                }
            }
        }

        if (outputPayInSlipBank != null) {
            yy += fontSize + fontSize;
            cv.drawText("เอกสาร Slip ธนาคาร", xTitle, yy, pTitleL);
            cv.drawText(String.format("%d ใบ", outputPayInSlipBank.size()), LAYOUT_WIDTH - 10, yy, pTitleR);
            for (DocumentHistoryInfo info : outputPayInSlipBank) {
                num++;
                texts = getText(info.employeeFullName, pListView3, LAYOUT_WIDTH - 650);
                for (int ii = 0; ii < texts.length; ii++) {
                    yy += fontSize + lineSpace;
                    if (ii == 0) {
                        cv.drawText(info.No, xTitle + 50, yy, pListView1);
                        cv.drawText(info.TransactionNo, xTitle + 100, yy, pListView2);
                    }
                    cv.drawText(texts[ii], LAYOUT_WIDTH - 10, yy, pListView3);
                }
            }
        }

        if (outputPayInSlipPayPoint != null) {
            yy += fontSize + fontSize;
            cv.drawText("เอกสาร Slip เพย์พอยท์", xTitle, yy, pTitleL);
            cv.drawText(String.format("%d ใบ", outputPayInSlipPayPoint.size()), LAYOUT_WIDTH - 10, yy, pTitleR);
            for (DocumentHistoryInfo info : outputPayInSlipPayPoint) {
                num++;
                texts = getText(info.employeeFullName, pListView3, LAYOUT_WIDTH - 650);
                for (int ii = 0; ii < texts.length; ii++) {
                    yy += fontSize + lineSpace;
                    if (ii == 0) {
                        cv.drawText(info.No, xTitle + 50, yy, pListView1);
                        cv.drawText(info.TransactionNo, xTitle + 100, yy, pListView2);
                    }
                    cv.drawText(texts[ii], LAYOUT_WIDTH - 10, yy, pListView3);
                }
            }
        }

        yy += fontSize + fontSize;
        cv.drawText("จำนวนเอกสารทั้งหมด", xTitle, yy, pTitleL);
        cv.drawText(String.format("%d ใบ", num), LAYOUT_WIDTH - 10, yy, pTitleR);

        yy += fontSize + fontSize;

        Bitmap barcode;
        String strBarcodeNo = BHPreference.teamCode() + "|" + BHPreference.SubTeamCode() + "|" + BHPreference.saleCode();
        barcode = BHBarcode.generateCode128(strBarcodeNo, 1080, 200);
        if (barcode != null) {
            cv.drawBitmap(barcode, (LAYOUT_WIDTH - barcode.getWidth()) / 2, yy, null);
            yy += barcode.getHeight();
        }

        fontSize = 42;
        Paint pBarcodeNo = new Paint(p);
        pBarcodeNo.setTypeface(Typeface.DEFAULT_BOLD);
        pBarcodeNo.setTextSize(fontSize);
        pBarcodeNo.setTextAlign(Align.CENTER);

        yy += fontSize + 10;
        cv.drawText(strBarcodeNo, LAYOUT_WIDTH / 2, yy, pBarcodeNo);


        yy += LINE_FEED;

        Bitmap result;
        if (defaultHeight > yy && newHeight == 0) {
            result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy, Config.ARGB_8888);
            cv = new Canvas(result);
            cv.drawBitmap(img, 0, 0, null);
            img.recycle();

            return scaleBitmapByWidth(result);
        } else if (newHeight != 0) {
            result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy, Config.ARGB_8888);
            cv = new Canvas(result);
            cv.drawBitmap(img, 0, 0, null);
            img.recycle();

            return scaleBitmapByWidth(result);
        } else {
            img.recycle();
            return getDocumentHistory(outputContract,
                    outputReceipt,
                    outputChangeProduct,
                    outputImpoundProduct,
                    outputChangeContract,
                    outputManualDocument,
                    outputPayInSlipBank,
                    outputPayInSlipPayPoint,
                    Math.round(yy));
        }
    }*/

    /*public static List<PrintTextInfo> getTextDocumentHistory(List<DocumentHistoryInfo> outputContract,
                                            List<DocumentHistoryInfo> outputReceipt,
                                            List<DocumentHistoryInfo> outputChangeProduct,
                                            List<DocumentHistoryInfo> outputImpoundProduct,
                                            List<DocumentHistoryInfo> outputChangeContract,
                                            List<DocumentHistoryInfo> outputManualDocument,
                                            List<DocumentHistoryInfo> outputPayInSlipBank,
                                            List<DocumentHistoryInfo> outputPayInSlipPayPoint) {
        List<PrintTextInfo> listText = new ArrayList<>();
        int num = 0;

        listText.addAll(getTextHeader());
        listText.addAll(getTextAlignCenter("ใบส่งเอกสาร", PrintTextInfo.FontType.Bold));
        listText.addAll(getTextAlignLeftRight("รายการส่งเอกสาร ประจำวันที่", BHUtilities.dateFormat(new Date())));

        EmployeeInfo outputEmp = TSRController.getEmployeeByID(BHPreference.employeeID());
        if (outputEmp != null) {
            listText.addAll(getTextAlignLeftRight("พนักงาน", outputEmp.FullName()));
        }

        if (outputContract != null) {
            num +=  outputContract.size();
            listText.addAll(getTextAlignLeftRight("เอกสารใบสัญญา", String.format("%d ใบ", outputContract.size())));
            for (int i = 0; i < outputContract.size(); i++) {
                listText.addAll(getTextForDocumentHistory(String.format("%3d", i + 1), outputContract.get(i).DocumentNo, outputContract.get(i).CustomerFullName));
            }
        }

        if (outputReceipt != null) {
            num +=  outputReceipt.size();
            listText.addAll(getTextAlignLeftRight("เอกสารใบเสร็จ", String.format("%d ใบ", outputReceipt.size())));
            for (int i = 0; i < outputReceipt.size(); i++) {
                listText.addAll(getTextForDocumentHistory(String.format("%3d", i + 1), outputReceipt.get(i).DocumentNo, outputReceipt.get(i).CustomerFullName));
            }
        }

        if (outputChangeProduct != null) {
            num +=  outputChangeProduct.size();
            listText.addAll(getTextAlignLeftRight("เอกสารใบเปลี่ยนเครื่อง", String.format("%d ใบ", outputChangeProduct.size())));
            for (int i = 0; i < outputChangeProduct.size(); i++) {
                listText.addAll(getTextForDocumentHistory(String.format("%3d", i + 1), outputChangeProduct.get(i).DocumentNo, outputChangeProduct.get(i).CustomerFullName));
            }
        }

        if (outputImpoundProduct != null) {
            num +=  outputImpoundProduct.size();
            listText.addAll(getTextAlignLeftRight("เอกสารใบถอดเครื่อง", String.format("%d ใบ", outputImpoundProduct.size())));
            for (int i = 0; i < outputImpoundProduct.size(); i++) {
                listText.addAll(getTextForDocumentHistory(String.format("%3d", i + 1), outputImpoundProduct.get(i).DocumentNo, outputImpoundProduct.get(i).CustomerFullName));
            }
        }

        if (outputChangeContract != null) {
            num +=  outputChangeContract.size();
            listText.addAll(getTextAlignLeftRight("เอกสารใบเปลี่ยนสัญญา", String.format("%d ใบ", outputChangeContract.size())));
            for (int i = 0; i < outputChangeContract.size(); i++) {
                listText.addAll(getTextForDocumentHistory(String.format("%3d", i + 1), outputChangeContract.get(i).DocumentNo, outputChangeContract.get(i).CustomerFullName));
            }
        }

        if (outputManualDocument != null) {
            num +=  outputManualDocument.size();
            listText.addAll(getTextAlignLeftRight("เอกสารมือ", String.format("%d ใบ", outputManualDocument.size())));
            for (int i = 0; i < outputManualDocument.size(); i++) {
                if (outputManualDocument.get(i).ManualDocumentTypeID.equals("0")) {
                    listText.addAll(getTextForDocumentHistory(String.format("%3d",  i + 1), outputManualDocument.get(i).con0CONTNO, outputManualDocument.get(i).dc0CustomerFullName));
                } else if (outputManualDocument.get(i).ManualDocumentTypeID.equals("1")) {
                    listText.addAll(getTextForDocumentHistory(String.format("%3d", i + 1), outputManualDocument.get(i).re1ReceiptCode, outputManualDocument.get(i).dc1CustomerFullName));
                }
            }
        }

        if (outputPayInSlipBank != null) {
            num +=  outputPayInSlipBank.size();
            listText.addAll(getTextAlignLeftRight("เอกสาร Slip ธนาคาร", String.format("%d ใบ", outputPayInSlipBank.size())));
            for (int i = 0; i < outputPayInSlipBank.size(); i++) {
                listText.addAll(getTextForDocumentHistory(String.format("%3d", i + 1), outputPayInSlipBank.get(i).TransactionNo, outputPayInSlipBank.get(i).employeeFullName));
            }
        }

        if (outputPayInSlipPayPoint != null) {
            num +=  outputPayInSlipPayPoint.size();
            listText.addAll(getTextAlignLeftRight("เอกสาร Slip เพย์พอยท์", String.format("%d ใบ", outputPayInSlipPayPoint.size())));
            for (int i = 0; i < outputPayInSlipPayPoint.size(); i++) {
                listText.addAll(getTextForDocumentHistory(String.format("%3d", i + 1), outputPayInSlipPayPoint.get(i).TransactionNo, outputPayInSlipPayPoint.get(i).employeeFullName));
            }
        }

        listText.addAll(getTextAlignLeftRight("จำนวนเอกสารทั้งหมด", String.format("%d ใบ", num)));

        //listText.add(new PrintTextInfo(""));

        // [START] :: Fixed-[BHPROJ-0024-3215] :: [LINE-25/07/2016][Android-ระบบส่งเอกสาร] กรณีที่ รหัสทีม/รหัสหน่วย/SaleCode ยาวเกินไป ทำให้ Barcode ที่ใช้ส่งเอกสารมีความยาวเกิน 23 ตัวอักษร เครื่องพิมพ์จะไม่สามารถพิมพ์ Barcode ได้
//        String strBarcodeNo = BHPreference.teamCode() + "|" + BHPreference.SubTeamCode() + "|" + BHPreference.saleCode();
        String strBarcodeNo = BHPreference.SubTeamCode() + "|" + BHPreference.saleCode();
        // [END] :: Fixed-[BHPROJ-0024-3215] :: [LINE-25/07/2016][Android-ระบบส่งเอกสาร] กรณีที่ รหัสทีม/รหัสหน่วย/SaleCode ยาวเกินไป ทำให้ Barcode ที่ใช้ส่งเอกสารมีความยาวเกิน 23 ตัวอักษร เครื่องพิมพ์จะไม่สามารถพิมพ์ Barcode ได้

        listText.add(new PrintTextInfo(strBarcodeNo, PrintTextInfo.FontType.Normal, true));
        listText.addAll(getTextAlignCenter(strBarcodeNo, PrintTextInfo.FontType.Normal));

        return listText;
    }*/

    /*public static Bitmap getChangeProduct(ChangeProductInfo changeProduct, ContractInfo contract, AddressInfo address) {
        int num = 0;
        Bitmap img = Bitmap.createBitmap(LAYOUT_WIDTH, 3000, Config.ARGB_8888);
        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
        cv.drawColor(Color.WHITE);

        float yy = 0;
        Bitmap header = getHeader();
        cv.drawBitmap(header, 0, yy, null);
        yy += header.getHeight();
        header.recycle();

        float fontSize = 90;
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        p.setTextSize(fontSize);
        p.setTextAlign(Align.CENTER);
        yy += fontSize + LINE_SPACE;
        cv.drawText("ใบเปลี่ยนเครื่อง/รับเครื่อง", LAYOUT_WIDTH / 2, yy, p);

        fontSize = 42;
        float lineSpace = fontSize / 2;
        float xTitle = 1;
        float xValue = 400;

        Paint pTitleL = new Paint(p);
        pTitleL.setTypeface(Typeface.DEFAULT_BOLD);
        pTitleL.setTextSize(fontSize);
        pTitleL.setTextAlign(Align.LEFT);

        Paint pValueL = new Paint(p);
        pValueL.setTypeface(null);
        pValueL.setTextSize(fontSize);
        pValueL.setTextAlign(Align.LEFT);

        Paint pTitleR = new Paint(p);
        pTitleR.setTypeface(Typeface.DEFAULT_BOLD);
        pTitleR.setTextSize(fontSize);
        pTitleR.setTextAlign(Align.RIGHT);

        Paint pValueR = new Paint(p);
        pValueR.setTypeface(null);
        pValueR.setTextSize(fontSize);
        pValueR.setTextAlign(Align.RIGHT);

        yy += fontSize * 3;
        cv.drawText("เลขที่", xTitle, yy, pTitleL);
        cv.drawText(changeProduct.ChangeProductPaperID, xTitle + 125, yy, pValueL);
        cv.drawText("วันที่", LAYOUT_WIDTH - 250, yy, pTitleR);
        cv.drawText(BHUtilities.dateFormat(changeProduct.EffectiveDate, "dd/MM/yyyy"), LAYOUT_WIDTH - 10, yy, pValueR);


        yy += fontSize + lineSpace;
        cv.drawText(changeProduct.ProductName, xTitle, yy, pValueL);

        yy += fontSize + lineSpace;
        cv.drawText("หมายเลขเครื่องเก่า", xTitle, yy, pTitleL);
        cv.drawText(changeProduct.OldProductSerialNumber, xValue, yy, pValueL);

        yy += fontSize + lineSpace;
        cv.drawText("หมายเลขเครื่องใหม่", xTitle, yy, pTitleL);
        cv.drawText(changeProduct.NewProductSerialNumber, xValue, yy, pValueL);

        yy += fontSize + lineSpace;
        cv.drawText("เลขที่สัญญา", xTitle, yy, pTitleL);
        cv.drawText(contract.CONTNO, xValue, yy, pValueL);

        String[] texts = getText(String.format("%s", BHUtilities.trim(changeProduct.CustomerFullName)), pValueL, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("ผู้ซื้อ", xTitle, yy, pTitleL);
            }
            cv.drawText(texts[ii], xValue, yy, pValueL);
        }

        texts = getText(address.Address(), pValueL, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("ที่อยู่", xTitle, yy, pTitleL);
            }
            cv.drawText(texts[ii], xValue, yy, pValueL);
        }

        texts = getText(address.Telephone(), pValueL, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("เบอร์โทร", xTitle, yy, pTitleL);
            }
            cv.drawText(texts[ii], xValue, yy, pValueL);
        }

        yy += fontSize + lineSpace;
        cv.drawText("วันที่ติดตั้ง", xTitle, yy, pTitleL);
        cv.drawText(BHUtilities.dateFormat(changeProduct.InstallDate, "dd/MM/yyyy"), xValue, yy, pValueL);


        Paint pTitleC = new Paint(p);
        pTitleC.setTypeface(Typeface.DEFAULT_BOLD);
        pTitleC.setTextSize(fontSize);
        pTitleC.setTextAlign(Align.CENTER);

        Paint pValueC = new Paint(p);
        pValueC.setTypeface(null);
        pValueC.setTextSize(fontSize);
        pValueC.setTextAlign(Align.CENTER);


        yy += fontSize + fontSize + fontSize;
        cv.drawText("บริษัทได้เปลี่ยนเครื่องใหม่และรับคืนเครื่องเก่าเรียบร้อยแล้ว", LAYOUT_WIDTH / 2, yy, pTitleC);

        yy += LINE_FEED + (LINE_FEED / 2);
        cv.drawText("____________________", (LAYOUT_WIDTH / 2) / 2, yy, pValueC);
        cv.drawText("____________________", ((LAYOUT_WIDTH / 2) / 2) + (LAYOUT_WIDTH / 2), yy, pValueC);

        yy += fontSize + fontSize;
        cv.drawText("ผู้รับเปลี่ยนเครื่อง", (LAYOUT_WIDTH / 2) / 2, yy, pValueC);
        cv.drawText("ลูกค้าผู้เปลี่ยนเครื่อง", ((LAYOUT_WIDTH / 2) / 2) + (LAYOUT_WIDTH / 2), yy, pValueC);


        yy += fontSize + fontSize + fontSize;
        cv.drawText(String.format("รหัสพนักงานขาย %s : %s", contract.SaleCode, contract.SaleEmployeeName), xTitle, yy, pValueL);

        yy += fontSize + fontSize;
        cv.drawText(String.format("ทีม %s : %s", contract.SaleTeamCode, contract.upperEmployeeName), xTitle, yy, pValueL);

        yy += LINE_FEED;

        Bitmap result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy,
                Config.ARGB_8888);
        cv = new Canvas(result);
        cv.drawBitmap(img, 0, 0, null);
        img.recycle();

        return scaleBitmapByWidth(result);
    }*/

    /*public static List<PrintTextInfo> getTextChangeProduct(ChangeProductInfo changeProduct, ContractInfo contract, AddressInfo address) {
        List<PrintTextInfo> listText = new ArrayList<>();

        listText.addAll(getTextHeader());
        listText.addAll(getTextAlignCenter("ใบเปลี่ยนเครื่อง/รับเครื่อง", PrintTextInfo.FontType.Bold));
        listText.addAll(getTextAlignLeftRight(String.format("%s %s", "เลขที่", changeProduct.ChangeProductPaperID), String.format("%s %s", "วันที่", BHUtilities.dateFormat(changeProduct.EffectiveDate, "dd/MM/yyyy"))));

        listText.addAll(getTextAlignLeft(changeProduct.ProductName));

        listText.addAll(getTextAlignLeftByOffSetLeft("หมายเลขเครื่องเก่า", changeProduct.OldProductSerialNumber));
        listText.addAll(getTextAlignLeftByOffSetLeft("หมายเลขเครื่องใหม่", changeProduct.NewProductSerialNumber));
        listText.addAll(getTextAlignLeftByOffSetLeft("เลขที่สัญญา", contract.CONTNO));
        listText.addAll(getTextAlignLeftByOffSetLeft("ผู้ซื้อ", String.format("%s", BHUtilities.trim(changeProduct.CustomerFullName))));
        listText.addAll(getTextAlignLeftByOffSetLeft("ที่อยู่", address.Address()));
        listText.addAll(getTextAlignLeftByOffSetLeft("เบอร์โทร", address.Telephone()));
        listText.addAll(getTextAlignLeftByOffSetLeft("วันที่ติดตั้ง", BHUtilities.dateFormat(changeProduct.InstallDate, "dd/MM/yyyy")));

        listText.addAll(getTextAlignCenter("บริษัทได้เปลี่ยนเครื่องใหม่และรับคืนเครื่องเก่าเรียบร้อยแล้ว"));

        listText.add(new PrintTextInfo(""));

        listText.addAll(getTextLeftRightAlignCenter("______________________", "______________________"));
        listText.addAll(getTextLeftRightAlignCenter("ผู้รับเปลี่ยนเครื่อง", "ลูกค้าผู้เปลี่ยนเครื่อง"));

        listText.addAll(getTextAlignLeft(String.format("รหัสพนักงานขาย %s : %s", contract.SaleCode, contract.SaleEmployeeName)));
        listText.addAll(getTextAlignLeft(String.format("ทีม %s : %s", contract.SaleTeamCode, contract.upperEmployeeName)));

        return listText;
    }*/

    /*public static Bitmap getImpoundProduct(ImpoundProductInfo impoundProduct, AddressInfo address) {
        int num = 0;
        Bitmap img = Bitmap.createBitmap(LAYOUT_WIDTH, 3000, Config.ARGB_8888);
        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
        cv.drawColor(Color.WHITE);

        float yy = 0;
        Bitmap header = getHeader();
        cv.drawBitmap(header, 0, yy, null);
        yy += header.getHeight();
        header.recycle();

        float fontSize = 90;
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        p.setTextSize(fontSize);
        p.setTextAlign(Align.CENTER);
        yy += fontSize + LINE_SPACE;
//        cv.drawText("ใบเปลี่ยนเครื่อง/รับเครื่อง", LAYOUT_WIDTH / 2, yy, p);
        cv.drawText("ใบรับคืนเครื่อง", LAYOUT_WIDTH / 2, yy, p);

        fontSize = 42;
        float lineSpace = fontSize / 2;
        float xTitle = 1;
        float xValue = 400;

        Paint pTitleL = new Paint(p);
        pTitleL.setTypeface(Typeface.DEFAULT_BOLD);
        pTitleL.setTextSize(fontSize);
        pTitleL.setTextAlign(Align.LEFT);

        Paint pValueL = new Paint(p);
        pValueL.setTypeface(null);
        pValueL.setTextSize(fontSize);
        pValueL.setTextAlign(Align.LEFT);

        Paint pTitleR = new Paint(p);
        pTitleR.setTypeface(Typeface.DEFAULT_BOLD);
        pTitleR.setTextSize(fontSize);
        pTitleR.setTextAlign(Align.RIGHT);

        Paint pValueR = new Paint(p);
        pValueR.setTypeface(null);
        pValueR.setTextSize(fontSize);
        pValueR.setTextAlign(Align.RIGHT);

        yy += fontSize * 3;

        cv.drawText("เลขที่", xTitle, yy, pTitleL);
        cv.drawText(impoundProduct.ImpoundProductPaperID, xTitle + 125, yy, pValueL);
        cv.drawText("วันที่", LAYOUT_WIDTH - 250, yy, pTitleR);
        cv.drawText(BHUtilities.dateFormat(impoundProduct.EffectiveDate, "dd/MM/yyyy"), LAYOUT_WIDTH - 10, yy, pValueR);


        yy += fontSize + lineSpace;
        cv.drawText(impoundProduct.ProductName, xTitle, yy, pValueL);


        yy += fontSize + lineSpace;
        cv.drawText("หมายเลขเครื่อง", xTitle, yy, pTitleL);
        cv.drawText(impoundProduct.ProductSerialNumber, xValue, yy, pValueL);

        yy += fontSize + lineSpace;
        cv.drawText("เลขที่สัญญา", xTitle, yy, pTitleL);
        cv.drawText(impoundProduct.CONTNO, xValue, yy, pValueL);

        yy += fontSize + lineSpace;
        cv.drawText("ผู้ซื้อ", xTitle, yy, pTitleL);
        cv.drawText(String.format("%s %s", BHUtilities.trim(impoundProduct.CustomerFullName), BHUtilities.trim(impoundProduct.CompanyName)), xValue, yy, pValueL);


        String[] texts = getText(address.Address(), pValueL, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("ที่อยู่", xTitle, yy, pTitleL);
            }
            cv.drawText(texts[ii], xValue, yy, pValueL);
        }

        texts = getText(address.Telephone(), pValueL, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("เบอร์โทร", xTitle, yy, pTitleL);
            }
            cv.drawText(texts[ii], xValue, yy, pValueL);
        }

        yy += fontSize + lineSpace;
        cv.drawText("วันที่ติดตั้ง", xTitle, yy, pTitleL);
        cv.drawText(BHUtilities.dateFormat(impoundProduct.InstallDate, "dd/MM/yyyy"), xValue, yy, pValueL);


        Paint pTitleC = new Paint(p);
        pTitleC.setTypeface(Typeface.DEFAULT_BOLD);
        pTitleC.setTextSize(fontSize);
        pTitleC.setTextAlign(Align.CENTER);

        Paint pValueC = new Paint(p);
        pValueC.setTypeface(null);
        pValueC.setTextSize(fontSize);
        pValueC.setTextAlign(Align.CENTER);


        yy += fontSize + fontSize + fontSize;
//        cv.drawText("บริษัทได้เปลี่ยนเครื่องใหม่และรับคืนเครื่องเก่าเรียบร้อยแล้ว", LAYOUT_WIDTH / 2, yy, pTitleC);
        cv.drawText("บริษัทได้รับคืนเครื่องดังกล่าวเรียบร้อยแล้ว", LAYOUT_WIDTH / 2, yy, pTitleC);

        yy += LINE_FEED + (LINE_FEED / 2);
        cv.drawText("____________________", (LAYOUT_WIDTH / 2) / 2, yy, pValueC);
        cv.drawText("____________________", ((LAYOUT_WIDTH / 2) / 2) + (LAYOUT_WIDTH / 2), yy, pValueC);

        yy += fontSize + fontSize;
        cv.drawText("ผู้รับคืนเครื่อง", (LAYOUT_WIDTH / 2) / 2, yy, pValueC);
        cv.drawText("ลูกค้าผู้คืนเครื่อง", ((LAYOUT_WIDTH / 2) / 2) + (LAYOUT_WIDTH / 2), yy, pValueC);


        yy += fontSize + fontSize + fontSize;
        cv.drawText(String.format("รหัสพนักงานขาย %s : %s", impoundProduct.SaleCode, impoundProduct.SaleEmployeeName), xTitle, yy, pValueL);

        yy += fontSize + fontSize;
        cv.drawText(String.format("ทีม %s : %s", impoundProduct.SaleTeamCode, impoundProduct.TeamHeadName), xTitle, yy, pValueL);

        yy += LINE_FEED;

        Bitmap result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy,
                Config.ARGB_8888);
        cv = new Canvas(result);
        cv.drawBitmap(img, 0, 0, null);
        img.recycle();

        return scaleBitmapByWidth(result);
    }*/

    /*public static List<PrintTextInfo> getTextImpoundProduct(ImpoundProductInfo impoundProduct, AddressInfo address) {
        List<PrintTextInfo> listText = new ArrayList<>();

        listText.addAll(getTextHeader());
        listText.addAll(getTextAlignCenter("ใบรับคืนเครื่อง", PrintTextInfo.FontType.Bold));
        listText.addAll(getTextAlignLeftRight(String.format("%s %s", "เลขที่", impoundProduct.ImpoundProductPaperID), String.format("%s %s", "วันที่", BHUtilities.dateFormat(impoundProduct.EffectiveDate, "dd/MM/yyyy"))));

        listText.addAll(getTextAlignLeft(impoundProduct.ProductName));

        listText.addAll(getTextAlignLeftByOffSetLeft("หมายเลขเครื่อง", impoundProduct.ProductSerialNumber));
        listText.addAll(getTextAlignLeftByOffSetLeft("เลขที่สัญญา", impoundProduct.CONTNO));
        listText.addAll(getTextAlignLeftByOffSetLeft("ผู้ซื้อ", String.format("%s %s", BHUtilities.trim(impoundProduct.CustomerFullName), BHUtilities.trim(impoundProduct.CompanyName))));
        listText.addAll(getTextAlignLeftByOffSetLeft("ที่อยู่", address.Address()));
        listText.addAll(getTextAlignLeftByOffSetLeft("เบอร์โทร", address.Telephone()));
        listText.addAll(getTextAlignLeftByOffSetLeft("วันที่ติดตั้ง", BHUtilities.dateFormat(impoundProduct.InstallDate, "dd/MM/yyyy")));

        listText.addAll(getTextAlignCenter("บริษัทได้รับคืนเครื่องดังกล่าวเรียบร้อยแล้ว"));

        listText.add(new PrintTextInfo(""));

        listText.addAll(getTextLeftRightAlignCenter("______________________", "______________________"));
        listText.addAll(getTextLeftRightAlignCenter("ผู้รับคืนเครื่อง", "ลูกค้าผู้คืนเครื่อง"));

        listText.addAll(getTextAlignLeft(String.format("รหัสพนักงานขาย %s : %s", impoundProduct.SaleCode, impoundProduct.SaleEmployeeName)));
        listText.addAll(getTextAlignLeft(String.format("ทีม %s : %s", impoundProduct.SaleTeamCode, impoundProduct.TeamHeadName)));

        return listText;
    }*/

    /*public static Bitmap getChangeContract(ChangeContractInfo changeContract, ContractInfo oldContract, ContractInfo newContract, AddressInfo defaultAddress, AddressInfo installAddress, List<SalePaymentPeriodInfo> sspList, ManualDocumentInfo manual) {
        Bitmap img = Bitmap.createBitmap(LAYOUT_WIDTH, 3000, Config.ARGB_8888);
        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
        cv.drawColor(Color.WHITE);

        float yy = 0;
        Bitmap header = getHeader();
        cv.drawBitmap(header, 0, yy, null);
        yy += header.getHeight();
        header.recycle();

        float fontSize = 90;
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        p.setTextSize(fontSize);
        p.setTextAlign(Align.CENTER);
        yy += fontSize + LINE_SPACE;
        String title = newContract.MODE > 1 ? "ใบสัญญาเช่าซื้อ" : "ใบสัญญาซื้อขาย";
        cv.drawText(title, LAYOUT_WIDTH / 2, yy, p);

        fontSize = 42;
        float lineSpace = fontSize / 2;
        float xTitle = 1;
        float xValue = 400;

        Paint pTitle = new Paint(p);
        pTitle.setTypeface(Typeface.DEFAULT_BOLD);
        pTitle.setTextSize(fontSize);
        pTitle.setTextAlign(Align.LEFT);

        Paint pValue = new Paint(pTitle);
        pValue.setTypeface(null);

        yy += fontSize * 3;
        cv.drawText("วันที่ทำสัญญา", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.dateFormat(newContract.EFFDATE), xValue, yy, pValue);

        if (manual != null) {
            yy += fontSize + lineSpace;
            cv.drawText("เลขที่อ้างอิง", xTitle, yy, pTitle);
            //cv.drawText(String.format("%s/%4d", BHUtilities.trim(manual.ManualVolumeNo), manual.ManualRunningNo).replace(' ', '0'), xValue, yy, pValue);
            cv.drawText(String.format("%4d", manual.ManualRunningNo).replace(' ', '0'), xValue, yy, pValue);
        }

        yy += fontSize + lineSpace;
        cv.drawText("เลขที่สัญญา", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(newContract.CONTNO) + " (" + changeContract.ProblemName.substring(0, 2) + ")", xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("วันที่เปลี่ยนสัญญา", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.dateFormat(changeContract.EffectiveDate), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("ผู้ซื้อ", xTitle, yy, pTitle);
        cv.drawText(String.format("%s %s", BHUtilities.trim(newContract.CustomerFullName), BHUtilities.trim(newContract.CompanyName)), xValue, yy, pValue);

        String[] texts = getText(String.format("%s %s", BHUtilities.trim(newContract.CustomerFullName), BHUtilities.trim(newContract.CompanyName)), pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText(newContract.MODE > 1 ? "ผู้เช่าซื้อ" : "ผู้ซื้อ", xTitle, yy, pTitle);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }


        yy += fontSize + lineSpace;
        cv.drawText("เลขที่บัตร", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(newContract.IDCard), xValue, yy, pValue);

        //xValue = 250;
        texts = getText(defaultAddress.Address(), pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("ที่อยู่บัตร", xTitle, yy, pTitle);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }

        texts = getText(installAddress.Address(), pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("ที่ติดตั้ง", xTitle, yy, pTitle);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }

        texts = getText(BHUtilities.trim(newContract.ProductName), pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("สินค้า", xTitle, yy, pTitle);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }

        yy += fontSize + lineSpace;
        cv.drawText("รุ่น", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(newContract.MODEL), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("เลขเครื่อง", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(newContract.ProductSerialNumber), xValue, yy, pValue);


        if (newContract.TradeInDiscount > 0) {

            xValue = LAYOUT_WIDTH - 100;
            pValue.setTextAlign(Align.RIGHT);
            yy += fontSize + lineSpace;
            cv.drawText("ราคา", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(newContract.SALES) + " บาท", xValue, yy, pValue);

            yy += fontSize + lineSpace;
            cv.drawText("ส่วนลดเครื่องแสดง", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(newContract.TradeInDiscount) + " บาท", xValue, yy, pValue);

            yy += fontSize + lineSpace;
            cv.drawText("ราคาสุทธิ", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(newContract.TotalPrice) + " บาท", xValue, yy, pValue);
        } else {

            xValue = LAYOUT_WIDTH - 100;
            pValue.setTextAlign(Align.RIGHT);
            yy += fontSize + lineSpace;
            cv.drawText("ราคาขาย", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(newContract.SALES) + " บาท", xValue, yy, pValue);

        }

        if (sspList.size() > 0) {
            yy += fontSize + lineSpace;
            float sum = newContract.PaymentAmount - newContract.TradeInDiscount;
            cv.drawText("งวดที่ 1 ต้องชำระ", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(sum) + " บาท", xValue, yy, pValue);

            yy += fontSize + lineSpace;
            cv.drawText("งวดที่ 2 ถึงงวดที่ " + BHUtilities.numericFormat(newContract.MODE) + " ต้องชำระงวดละ", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(sspList.get(1).PaymentAmount) + " บาท", xValue, yy, pValue);

            yy += fontSize + lineSpace;
            cv.drawText("งวดที่ 1 ต้องชำระ", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(sspList.get(0).NetAmount) + " บาท", xValue, yy, pValue);

            if (sspList.size() != 1) {
                if (sspList.size() == 2) {
                    yy += fontSize + lineSpace;
                    cv.drawText("งวดที่ 2 ต้องชำระ", xTitle, yy, pTitle);
                    cv.drawText(BHUtilities.numericFormat(sspList.get(1).NetAmount) + " บาท", xValue, yy, pValue);
                } else {
                    if (sspList.get(1).NetAmount == sspList.get(2).NetAmount) {
                        yy += fontSize + lineSpace;
                        cv.drawText("งวดที่ 2 ถึงงวดที่ " + BHUtilities.numericFormat(sspList.size()) + " ต้องชำระงวดละ", xTitle, yy, pTitle);
                        cv.drawText(BHUtilities.numericFormat(sspList.get(1).NetAmount) + " บาท", xValue, yy, pValue);

                    } else {
                        if (sspList.size() == 3) {
                            yy += fontSize + lineSpace;
                            cv.drawText("งวดที่ 2 ต้องชำระ", xTitle, yy, pTitle);
                            cv.drawText(BHUtilities.numericFormat(sspList.get(1).NetAmount) + " บาท", xValue, yy, pValue);

                            yy += fontSize + lineSpace;
                            cv.drawText("งวดที่ 3 ต้องชำระ", xTitle, yy, pTitle);
                            cv.drawText(BHUtilities.numericFormat(sspList.get(2).NetAmount) + " บาท", xValue, yy, pValue);
                        } else {
                            yy += fontSize + lineSpace;
                            cv.drawText("งวดที่ 2 ต้องชำระ", xTitle, yy, pTitle);
                            cv.drawText(BHUtilities.numericFormat(sspList.get(1).NetAmount) + " บาท", xValue, yy, pValue);

                            yy += fontSize + lineSpace;
                            cv.drawText("งวดที่ 3 ถึงงวดที่ " + BHUtilities.numericFormat(sspList.size()) + " ต้องชำระงวดละ", xTitle, yy, pTitle);
                            cv.drawText(BHUtilities.numericFormat(sspList.get(2).NetAmount) + " บาท", xValue, yy, pValue);
                        }
                    }
                }
            }
        }

        pValue.setTextAlign(Align.LEFT);
        Paint pSignature = new Paint(pValue);
        pSignature.setTextAlign(Align.CENTER);


        String customer = String.format("(%s %s)", BHUtilities.trim(newContract.CustomerFullName), BHUtilities.trim(newContract.CompanyName));

        if (newContract.MODE == 1) {
            yy += 200;
            int Value = LAYOUT_WIDTH / 2;
            int ww = getWidth(customer, pValue) + 50;

            texts = getText(customer, pSignature, LAYOUT_WIDTH);
            for (int ii = 0; ii < texts.length; ii++) {
                if (ii == 0) {
                    cv.drawText(getSignatureUnderline(pSignature, ww), Value, yy, pSignature);
                    cv.drawText("ผู้ซื้อ", Value + 190, yy, pValue);
                    cv.drawText(String.format("%sผู้ซื้อ", getSignatureUnderline(pSignature, (LAYOUT_WIDTH) - (getWidth("ผู้ซื้อ", pSignature) + (LAYOUT_WIDTH / 2)))), LAYOUT_WIDTH / 2, yy, pSignature);
                }
                yy += fontSize + lineSpace;
                cv.drawText(texts[ii], Value, yy, pSignature);
            }

        } else {

            yy += 200;
            String TSR = "(นายวิรัช วงศ์นิรันดร์)";
            int xx = getWidth(TSR, pValue) + 10;
            int ww = getWidth(customer, pValue) + 50;

            int width = LAYOUT_WIDTH / 6;
            cv.drawText(getSignatureUnderline(pSignature, xx), width, yy, pSignature); // วาดจุด
            cv.drawText(String.format("ผู้ให้เช่าซื้อ"), width + 190, yy, pValue);

            xValue = LAYOUT_WIDTH - (ww / 2 + 120);
            cv.drawText("ผู้ซื้อ", LAYOUT_WIDTH - 115, yy, pValue);
            cv.drawText(getSignatureUnderline(pSignature, ww), xValue, yy, pSignature);

            yy += fontSize + lineSpace;
            cv.drawText(TSR, 230, yy, pSignature);
            cv.drawText(customer, xValue, yy, pSignature);

            yy += 200;
            String TSR = "(นายวิรัช วงศ์นิรันดร์)";

            cv.drawText(String.format("%sผู้ให้เช่าซื้อ", getSignatureUnderline(pSignature, (LAYOUT_WIDTH / 2) - (getWidth("ผู้ให้เช่าซื้อ", pSignature) + 50))), LAYOUT_WIDTH / 4, yy, pSignature);
            cv.drawText(String.format("%sผู้เช่าซื้อ", getSignatureUnderline(pSignature, (LAYOUT_WIDTH / 2) - (getWidth("ผู้เช่าซื้อ", pSignature) + 50))), (LAYOUT_WIDTH / 4) * 3, yy, pSignature);

            String[] texts1 = getText(TSR, pSignature, LAYOUT_WIDTH / 2);
            String[] texts2 = getText(customer, pSignature, LAYOUT_WIDTH / 2);
            int num;
            if (texts1.length > texts2.length) {
                num = texts1.length;
            } else {
                num = texts2.length;
            }

            for (int ii = 0; ii < num; ii++) {
                yy += fontSize + lineSpace;
                if (texts1.length > ii) {
                    cv.drawText(texts1[ii], LAYOUT_WIDTH / 4, yy, pSignature);
                }

                if (texts2.length > ii) {
                    cv.drawText(texts2[ii], (LAYOUT_WIDTH / 4) * 3, yy, pSignature);
                }
            }



            yy += 200;
            String salename = "(" + BHUtilities.trim(changeContract.EffectiveByEmployeeName) + ")";
            String salecode = "รหัส " + BHUtilities.trim(changeContract.EffectiveBySaleCode != null ? changeContract.EffectiveBySaleCode : changeContract.EffectiveBy);

            String saleteamname = "(" + BHUtilities.trim(changeContract.EffectiveByUpperEmployeeName) + ")";
            String saleteamcode = "" + BHUtilities.trim(changeContract.EffectiveBySaleTeamName);

            int zz = getWidth(saleteamname, pValue) + 50;
            xValue = LAYOUT_WIDTH - (zz / 2 + 120);

            int vv = getWidth(salename, pValue) + 50;
            xValue = LAYOUT_WIDTH - (vv / 2 + 120);

            int r = LAYOUT_WIDTH / 6;
            cv.drawText(getSignatureUnderline(pSignature, zz), r, yy, pSignature);
            cv.drawText("พยาน", r + 190, yy, pValue);

            cv.drawText("พยาน", LAYOUT_WIDTH - 115, yy, pValue);
            cv.drawText(getSignatureUnderline(pSignature, vv), xValue, yy, pSignature);

            yy += fontSize + lineSpace;
            cv.drawText(saleteamname, 230, yy, pSignature);
            cv.drawText(salename, xValue, yy, pSignature);
            yy += fontSize + lineSpace;
            cv.drawText(saleteamcode, 240, yy, pSignature);
            cv.drawText(salecode, xValue, yy, pSignature);


            String salename = "(" + BHUtilities.trim(changeContract.EffectiveByEmployeeName) + ")";
            String salecode = "รหัส " + BHUtilities.trim(changeContract.EffectiveBySaleCode != null ? changeContract.EffectiveBySaleCode : changeContract.EffectiveBy);

            String saleteamname = "(" + BHUtilities.trim(changeContract.EffectiveByUpperEmployeeName) + ")";
            String saleteamcode = "" + BHUtilities.trim(changeContract.EffectiveBySaleTeamName);

            cv.drawText(String.format("%sพยาน", getSignatureUnderline(pSignature, (LAYOUT_WIDTH / 2) - (getWidth("พยาน", pSignature) + 50))), LAYOUT_WIDTH / 4, yy, pSignature);
            cv.drawText(String.format("%sพยาน", getSignatureUnderline(pSignature, (LAYOUT_WIDTH / 2) - (getWidth("พยาน", pSignature) + 50))), (LAYOUT_WIDTH / 4) * 3, yy, pSignature);

            texts1 = getText(saleteamname, pSignature, LAYOUT_WIDTH / 2);
            texts2 = getText(salename, pSignature, LAYOUT_WIDTH / 2);
            if (texts1.length > texts2.length) {
                num = texts1.length;
            } else {
                num = texts2.length;
            }

            for (int ii = 0; ii < num; ii++) {
                yy += fontSize + lineSpace;
                if (texts1.length > ii) {
                    cv.drawText(texts1[ii], LAYOUT_WIDTH / 4, yy, pSignature);
                }

                if (texts2.length > ii) {
                    cv.drawText(texts2[ii], (LAYOUT_WIDTH / 4) * 3, yy, pSignature);
                }
            }

            yy += fontSize + lineSpace;
            cv.drawText(saleteamcode, LAYOUT_WIDTH / 4, yy, pSignature);
            cv.drawText(salecode, (LAYOUT_WIDTH / 4) * 3, yy, pSignature);


        }

        xValue = 1;
        yy += fontSize + lineSpace + 50;
        cv.drawText(String.format("รหัส %s %s", BHUtilities.trim(newContract.SaleCode), BHUtilities.trim(newContract.SaleEmployeeName)), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText(String.format("%s %s", BHUtilities.trim(newContract.SaleTeamName), BHUtilities.trim(newContract.upperEmployeeName)), xValue, yy, pValue);

        yy += LINE_FEED;

        Bitmap result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy, Config.ARGB_8888);
        cv = new Canvas(result);
        cv.drawBitmap(img, 0, 0, null);
        img.recycle();

        return scaleBitmapByWidth(result);
    }*/

    //public static List<PrintTextInfo> getTextChangeContract(ChangeContractInfo changeContract, ContractInfo oldContract, ContractInfo newContract, AddressInfo defaultAddress, AddressInfo installAddress, List<SalePaymentPeriodInfo> sspList, ManualDocumentInfo manual) {
    /*public static List<PrintTextInfo> getTextChangeContract(ChangeContractInfo changeContract, ContractInfo oldContract, ContractInfo newContract, AddressInfo defaultAddress, AddressInfo installAddress, List<SalePaymentPeriodInfo> sspList) {
        List<PrintTextInfo> listText = new ArrayList<>();

        listText.addAll(getTextHeader());

        String title = newContract.MODE > 1 ? "ใบสัญญาเช่าซื้อ" : "ใบสัญญาซื้อขาย";
        listText.addAll(getTextAlignCenter(title, PrintTextInfo.FontType.Bold));

        listText.addAll(getTextAlignLeftByOffSetLeft("วันที่ทำสัญญา", BHUtilities.dateFormat(newContract.EFFDATE)));

        //[START] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน
        if (manual != null) {
            listText.addAll(getTextAlignLeftByOffSetLeft("เลขที่อ้างอิง", String.format("%4d", manual.ManualRunningNo).replace(' ', '0')));
        }
        listText.addAll(getTextAlignLeftByOffSetLeft("เลขที่อ้างอิง", newContract.ContractReferenceNo != null ? newContract.ContractReferenceNo : ""));
        // [END] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน

        listText.addAll(getTextAlignLeftByOffSetLeft("เลขที่สัญญา", BHUtilities.trim(newContract.CONTNO) + " (" + changeContract.ProblemName.substring(0, 2) + ")"));
        listText.addAll(getTextAlignLeftByOffSetLeft("วันที่เปลี่ยนสัญญา", BHUtilities.dateFormat(changeContract.EffectiveDate)));
        listText.addAll(getTextAlignLeftByOffSetLeft(newContract.MODE > 1 ? "ผู้เช่าซื้อ" : "ผู้ซื้อ", String.format("%s %s", BHUtilities.trim(newContract.CustomerFullName), BHUtilities.trim(newContract.CompanyName))));
        listText.addAll(getTextAlignLeftByOffSetLeft("เลขที่บัตร", BHUtilities.trim(newContract.IDCard)));
        listText.addAll(getTextAlignLeftByOffSetLeft("ที่อยู่บัตร", defaultAddress.Address()));
        listText.addAll(getTextAlignLeftByOffSetLeft("ที่ติดตั้ง", installAddress.Address()));
        listText.addAll(getTextAlignLeftByOffSetLeft("สินค้า", BHUtilities.trim(newContract.ProductName)));
        listText.addAll(getTextAlignLeftByOffSetLeft("รุ่น", BHUtilities.trim(newContract.MODEL)));
        listText.addAll(getTextAlignLeftByOffSetLeft("เลขเครื่อง", BHUtilities.trim(newContract.ProductSerialNumber)));

        if (newContract.TradeInDiscount > 0) {
            listText.addAll(getTextAlignLeftByOffSetLeft("ราคา", BHUtilities.numericFormat(newContract.SALES) + " บาท"));
            listText.addAll(getTextAlignLeftByOffSetLeft("ส่วนลดเครื่องแสดง", BHUtilities.numericFormat(newContract.TradeInDiscount) + " บาท"));
            listText.addAll(getTextAlignLeftByOffSetLeft("ราคาสุทธิ", BHUtilities.numericFormat(newContract.TotalPrice) + " บาท"));
        } else {
            listText.addAll(getTextAlignLeftByOffSetLeft("ราคาขาย", BHUtilities.numericFormat(newContract.SALES) + " บาท"));
        }

        if (sspList.size() > 0) {
            listText.addAll(getTextAlignLeftByOffSetLeft("งวดที่ 1 ต้องชำระ", BHUtilities.numericFormat(sspList.get(0).NetAmount) + " บาท"));
            if (sspList.size() != 1) {
                if (sspList.size() == 2) {
                    listText.addAll(getTextAlignLeftByOffSetLeft("งวดที่ 2 ต้องชำระ", BHUtilities.numericFormat(sspList.get(1).NetAmount) + " บาท"));
                } else {
                    if (sspList.get(1).NetAmount == sspList.get(2).NetAmount) {
                        listText.addAll(getTextAlignLeftByOffSetLeft("งวดที่ 2 ถึงงวดที่ " + BHUtilities.numericFormat(sspList.size()) + " ต้องชำระงวดละ", BHUtilities.numericFormat(sspList.get(1).NetAmount) + " บาท"));
                    } else {
                        if (sspList.size() == 3) {
                            listText.addAll(getTextAlignLeftByOffSetLeft("งวดที่ 2 ต้องชำระ", BHUtilities.numericFormat(sspList.get(1).NetAmount) + " บาท"));
                            listText.addAll(getTextAlignLeftByOffSetLeft("งวดที่ 3 ต้องชำระ", BHUtilities.numericFormat(sspList.get(2).NetAmount) + " บาท"));
                        } else {
                            listText.addAll(getTextAlignLeftByOffSetLeft("งวดที่ 2 ต้องชำระ", BHUtilities.numericFormat(sspList.get(1).NetAmount) + " บาท"));
                            listText.addAll(getTextAlignLeftByOffSetLeft("งวดที่ 3 ถึงงวดที่ " + BHUtilities.numericFormat(sspList.size()) + " ต้องชำระงวดละ", BHUtilities.numericFormat(sspList.get(2).NetAmount) + " บาท"));
                        }
                    }
                }
            }
        }

        listText.add(new PrintTextInfo(""));

        String customer = String.format("(%s%s)", BHUtilities.trim(newContract.CustomerFullName), BHUtilities.trim(newContract.CompanyName));
        if (newContract.MODE == 1) {
            listText.addAll(getTextAlignCenter("...........................ผู้ซื้อ"));
            listText.addAll(getTextAlignCenter(customer));
        } else {

            listText.add(new PrintTextInfo(""));
            listText.add(new PrintTextInfo("signature"));
            listText.addAll(getTextLeftRightAlignCenter("......................", ".....................", "EN"));
            listText.addAll(getTextLeftRightAlignCenter("       ผู้ให้เช่าซื้อ       ", "       ผู้เช่าซื้อ        "));
            String TSR = "(นายวิรัช วงศ์นิรันดร์)";
            listText.addAll(getTextLeftRightAlignCenter(TSR, customer));

            String salename = "(" + BHUtilities.trim(changeContract.EffectiveByEmployeeName) + ")";
            String salecode = "รหัส " + BHUtilities.trim(changeContract.EffectiveBySaleCode != null ? changeContract.EffectiveBySaleCode : changeContract.EffectiveBy);

            String saleteamname = "(" + BHUtilities.trim(changeContract.EffectiveByUpperEmployeeName) + ")";
            String saleteamcode = "" + BHUtilities.trim(changeContract.EffectiveBySaleTeamName);

            listText.addAll(getTextLeftRightAlignCenter("..................พยาน", "..................พยาน"));
            listText.addAll(getTextLeftRightAlignCenter(saleteamname, salename));
            listText.addAll(getTextLeftRightAlignCenter(saleteamcode, salecode));
        }

        listText.add(new PrintTextInfo(""));
        listText.addAll(getTextAlignLeft(String.format("รหัส %s %s", BHUtilities.trim(newContract.SaleCode), BHUtilities.trim(newContract.SaleEmployeeName))));
        listText.addAll(getTextAlignLeft(String.format("%s %s", BHUtilities.trim(newContract.SaleTeamName), BHUtilities.trim(newContract.upperEmployeeName))));

        return listText;
    }*/
}
