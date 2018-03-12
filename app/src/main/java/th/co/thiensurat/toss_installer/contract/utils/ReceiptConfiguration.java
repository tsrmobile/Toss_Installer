package th.co.thiensurat.toss_installer.contract.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.datecs.api.printer.Printer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import th.co.thiensurat.toss_installer.contract.printer.documentcontroller.PrintTextInfo;
import th.co.thiensurat.toss_installer.contract.printer.documentcontroller.ThemalPrintController;

/**
 * Created by teerayut.k on 2/12/2018.
 */

public class ReceiptConfiguration {

    private Context context;
    private Printer printer;
    public static final int maxTextLength = 47;

    private static String[] lowerChar = new String[]{"ุ","ู" };
    private static String[] topUpperChar = new String[]{"่","้","๊","๋","์","ํ"};
    private static String[] upperChar = new String[]{"ิ","ี","ึ","ื","ั","่","้","๊","๋","์","็","ํ"};

    public ReceiptConfiguration(Context context, Printer printer) {
        this.context = context;
        this.printer = printer;
    }

    public void printHeaderImage() {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final AssetManager assetManager = context.getAssets();
        final Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(assetManager.open("tsr_header.png"),null, options);
            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();
            final int[] argb = new int[width * height];
            bitmap.getPixels(argb, 0, width, 0, 0, width, height);
            bitmap.recycle();
            printer.printCompressedImage(argb, width, height, Printer.ALIGN_CENTER, true);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("print", "header");
        }
    }

    private static Dictionary getSpecialChar(){
        Dictionary SpecialChar = new Hashtable();

        SpecialChar.put("-24-19","140");//หยาดน้ำค้าง+เอก
        SpecialChar.put("-23-19","141");//หยาดน้ำค้าง+โทร
        SpecialChar.put("-22-19","142");//หยาดน้ำค้าง+ตรี
        SpecialChar.put("-21-19","143");//หยาดน้ำค้าง+จัตวา

        SpecialChar.put("-19-24","140");//หยาดน้ำค้าง+เอก
        SpecialChar.put("-19-23","141");//หยาดน้ำค้าง+โทร
        SpecialChar.put("-19-22","142");//หยาดน้ำค้าง+ตรี
        SpecialChar.put("-19-21","143");//หยาดน้ำค้าง+จัตวา

        SpecialChar.put("-47-24","146");//ไม้หันอากาศ+เอก
        SpecialChar.put("-47-23","147");//ไม้หันอากาศ+โทร
        SpecialChar.put("-47-22","148");//ไม้หันอากาศ+ตรี
        SpecialChar.put("-47-21","149");//ไม้หันอากาศ+จัตวา

        SpecialChar.put("-44-24","150");//อิ+เอก
        SpecialChar.put("-44-23","151");//อิ+โทร
        SpecialChar.put("-44-22","152");//อิ+ตรี
        SpecialChar.put("-44-21","153");//อิ+จัตวา
        SpecialChar.put("-44-20","154");//อิ+การันต์

        SpecialChar.put("-43-24","155");//อี+เอก
        SpecialChar.put("-43-23","156");//อี+โทร
        SpecialChar.put("-43-22","157");//อี+ตรี
        SpecialChar.put("-43-21","158");//อี+จัตวา

        SpecialChar.put("-42-24","219");//อึ+เอก
        SpecialChar.put("-42-23","220");//อึ+โทร
        SpecialChar.put("-42-22","221");//อึ+ตรี
        SpecialChar.put("-42-21","222");//อึ+จัตวา

        SpecialChar.put("-41-24","251");//อื+เอก
        SpecialChar.put("-41-23","252");//อื+โทร
        SpecialChar.put("-41-22","253");//อื+ตรี
        SpecialChar.put("-41-21","254");//อื+จัตวา

        return SpecialChar;
    }

    public void printThaiCharacter(String message)  throws IOException {
        //message = message.replaceAll("ำ", "ํา");
        byte[] cmd = new byte[3];
        cmd[0] = 0x1b;
        cmd[1] = 0x33;
        cmd[2] = 0x1;
        printCommand(cmd);

        Dictionary SpecialChar = getSpecialChar();
        String msg1 = "";
        String msg2 = "";
        String msg3 = "";
        String ch = "";
        Boolean _found = false;
        Boolean isLevel3 = false;
        int spaceCount = 0;

        // ค้นหาสระ และวรรณยุกต์ ด้านบน
        for (int i = 0; i < message.length(); i++) {
            for (int j = 0; j < upperChar.length; j++) {
                ch = message.substring(i, i+1);
                if (ch.equals(upperChar[j])) {
                    _found = true;
                    break;
                } else {
                    _found = false;
                }
            }

            String nextCh = "";
            if (_found) {
                //ตรวจสอบตัวอักษรถัดไปเป็นวรรณยุกต์หรือไม่
                if (i < message.length() - 1) {
                    for (int l = 0; l < topUpperChar.length; l++) {
                        nextCh = message.substring(i + 1, i + 2);
                        if (nextCh.equals(topUpperChar[l])) {
                            _found = true;
                            break;
                        } else {
                            _found = false;
                        }
                    }
                } else {
                    _found = false;
                }

                if (_found) {
                    i++;
                    spaceCount--;
                    for (int sp = 0; sp < spaceCount; sp++) {
                        byte[] spaceByte = new byte[]{(byte) 32};
                        printCommand(spaceByte);
                    }

                    spaceCount = 0;
                    String SpecialString = "";
                    try {
                        byte[] chbyte = ch.getBytes("TIS-620");
                        Byte chByte = chbyte[0];

                        byte[] nextChbyte = nextCh.getBytes("TIS-620");
                        Byte nextChByte = nextChbyte[0];

                        SpecialString = String.format("%d%d", chByte.intValue(), nextChByte.intValue());
                    } catch (UnsupportedEncodingException var5) {
                        byte[] chbyte = ch.getBytes();
                        Byte chByte = chbyte[0];

                        byte[] nextChbyte = nextCh.getBytes();
                        Byte nextChByte = nextChbyte[0];

                        SpecialString = String.format("%d%d", chByte.intValue(), nextChByte.intValue());
                    }

                    String spString =  SpecialChar.get(SpecialString).toString();
                    printCommand(new byte[]{(byte) Integer.parseInt(spString)});

                } else {
                    spaceCount--;
                    for (int sp = 0; sp < spaceCount; sp++) {
                        byte[] spaceByte = new byte[]{(byte) 32};
                        printCommand(spaceByte);
                    }

                    spaceCount = 0;
                    try {
                        printCommand(ch.getBytes("TIS-620"));
                    } catch (UnsupportedEncodingException var5) {
                        printCommand(ch.getBytes());
                    }
                }
            } else {
                for (int k = 0; k < lowerChar.length; k++) {
                    ch = message.substring(i, i+1);
                    if (ch.equals(lowerChar[k])) {
                        _found = true;
                        break;
                    } else {
                        _found = false;
                    }
                }

                if(!_found)
                    spaceCount++;
            }
        }

        //  ค้นหาตัวอักษร
        for (int i = 0; i < message.length(); i++) {
            _found = false;
            for (int j = 0; j < upperChar.length; j++) {
                if (_found) break;
                ch = message.substring(i, i + 1);
                if (ch.equals(upperChar[j])) {
                    _found = true;
                    break;
                } else {
                    for (int k = 0; k < lowerChar.length; k++) {
                        ch = message.substring(i, i + 1);
                        if (ch.equals(lowerChar[k])) {
                            _found = true;
                            break;
                        } else {
                            _found = false;
                        }
                    }
                }
            }

            if (!_found) {
                msg2 = msg2 + message.substring(i, i + 1);
            }
        }

        // ค้นหาสระด้านล่าง
        for (int i = 0; i < message.length(); i++) {
            for (int j = 0; j < lowerChar.length; j++) {
                ch = message.substring(i, i + 1);
                if (ch.equals(lowerChar[j])) {
                    _found = true;
                    break;
                } else {
                    _found = false;
                }
            }

            if (_found) {
                msg3 = msg3.substring(0, msg3.length() - 1);
                msg3 = msg3 + message.substring(i, i + 1);
            } else {
                for(int k = 0; k < upperChar.length; k++) {
                    ch = message.substring(i, i + 1);
                    if (ch.equals(upperChar[k])) {
                        _found = true;
                        break;
                    } else {
                        _found = false;
                    }
                }

                if (!_found)
                    msg3 = msg3 + " ";
            }
        }

        byte[] send;
        try {
            send = msg1.getBytes("TIS-620");
        } catch (UnsupportedEncodingException var5) {
            send = msg1.getBytes();
        }

        printCommand(send);
        commitPrint();

        try {
            send = msg2.getBytes("TIS-620");
        } catch (UnsupportedEncodingException var5) {
            send = msg2.getBytes();
        }

        printCommand(send);
        commitPrint();

        if (!msg3.replace(" ","").isEmpty()) {
            try {
                send = msg3.getBytes("TIS-620");
            } catch (UnsupportedEncodingException var5) {
                send = msg3.getBytes();
            }
            printCommand(send);
            commitPrint();
        }
    }

    public StringBuilder setTextAlignCenter(String text) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.delete(0, stringBuilder.length());
        String[] tempText = getTextByPrintText(text, ThemalPrintController.maxTextLength );

        for(int i = 0; i < tempText.length; i++){
            stringBuilder.append(calculateCenter(tempText[i]));
        }
        return stringBuilder;
    }

    public StringBuilder setTextAlignLeftWithTitle(String title, String right){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.delete(0, stringBuilder.length());

        int offSetLeft = 20;
        String[] tempLeft = getTextByPrintText(title, offSetLeft);
        String[] tempRight = getTextByPrintText(right, maxTextLength - offSetLeft);

        int size = Math.max(tempLeft.length, tempRight.length);
        for(int i = 0; i < size; i++){
            stringBuilder.append(calculateOffSetLeft(i < tempLeft.length ? tempLeft[i] : "", i < tempRight.length ? tempRight[i] : "", offSetLeft));
        }

        return stringBuilder;
    }

    public List<PrintTextInfo> getTextAlignLeftByOffSetLeft(String left, String right){
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

    public List<PrintTextInfo> getTextAlignLeftRight(String left, String right){
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

    private String[] getTextByPrintText(String text, int maxLength) {
        List<String> result = new ArrayList<String>();
        if (getAlphabetOnly(text).length() <= maxLength){
            result.add(text);
        } else {
            String[] textArray = text.split("\\s+");
            result.add("");
            for (int i = 0; i < textArray.length; i++){
                int index = result.size() - 1;
                String tempText = result.get(index);

                if (tempText.equals("")) {
                    tempText += textArray[i];
                } else {
                    tempText += " " + textArray[i];
                }

                if(getAlphabetOnly(tempText).length() <= maxLength){
                    result.set(index, tempText);
                } else {
                    String[] tempTextArray = tempText.split("\\s+");
                    String resultTempText1 = "";
                    String resultTempText2 = "";

                    if (tempTextArray.length != 1) {
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
                    }
                }
            }
        }
        return result.toArray(new String[result.size()]);
    }

    public String getAlphabetOnly(String message) {
        //  ค้นหาตัวอักษร
        String result="";
        boolean _found = false;
        String ch = "";
        for (int i = 0; i < message.length(); i++) {
            _found = false;
            for (int j = 0; j < upperChar.length; j++) {
                if (_found) break;
                ch = message.substring(i,i+1);
                if (ch.equals(upperChar[j])) {
                    _found = true;
                    break;
                } else {
                    for (int k = 0; k<lowerChar.length; k++) {
                        ch = message.substring(i, i + 1);
                        if (ch.equals(lowerChar[k])) {
                            _found = true;
                            break;
                        } else {
                            _found = false;
                        }
                    }
                }
            }
            if (!_found) {
                result = result + message.substring(i, i + 1);
            }
        }
        return result;
    }

    public List<String> alignTextByLength(String text, int length){
        List<String> result = new ArrayList<String>();
        if (getAlphabetOnly(text).length() <= length){
            result.add(text);
        } else {
            result.add("");
            int start = 0;
            for (int i = 0; i < text.length(); i++){
                int index = result.size() - 1;
                String tempText = text.substring(start, i + 1);

                if (getAlphabetOnly(tempText).length() <= length){
                    result.set(index, tempText);
                } else {
                    if (i != text.length() - 1){
                        start = i;
                        result.add("");
                    }
                }
            }
        }
        return result;
    }

    public String calculateCenter(String center) {
        int maxLength = maxTextLength - getAlphabetOnly(center).length();
        String result = center;
        for (int i = 0; i < maxLength; i++) {
            if ((maxLength / 2) < i) {
                result = " " + result;
            } else {
                result += " ";
            }
        }
        return  result;
    }

    public String calculateOffSetLeft(String left, String right, int offSetLeft) {
        int totalSpace = offSetLeft;
        String result = left;
        int _aplabetOnly = getAlphabetOnly(left).length();
        for (int i =_aplabetOnly; i < totalSpace; i++) {
            result += " ";
        }
        result += right;
        return result;
    }

    public String calculateLeghtRight(String left, String right) {
        int maxLength = maxTextLength - getAlphabetOnly(left).length();
        int resultLegth = maxLength - getAlphabetOnly(right).length();
        String result = left;
        for (int i = 0; i < resultLegth; i++) {
            result += " ";
        }
        result += right;
        return  result;
    }

    public void commitPrint() throws IOException{
        printer.feedPaper(10);
    }

    private void printCommand(byte[] cmd) throws IOException {
        printer.printText(cmd);
    }

}
