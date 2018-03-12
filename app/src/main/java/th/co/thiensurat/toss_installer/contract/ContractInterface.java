package th.co.thiensurat.toss_installer.contract;

import android.content.Context;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import th.co.thiensurat.toss_installer.api.result.ContactResultGroup;
import th.co.thiensurat.toss_installer.api.result.JobFinishItem;
import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.contract.item.ContactItem;
import th.co.thiensurat.toss_installer.contract.item.ObjectImage;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;

/**
 * Created by teerayut.k on 11/20/2017.
 */

public class ContractInterface {

    public interface View extends BaseMvpInterface.View {
        void setAddessFromSQLite(List<AddressItem> itemList);
        void setProductFromSQLite(List<ProductItem> productItemList);
        void setContactNumber(String number);
        void setImageToContactBody(List<ObjectImage> objectImages);
        void onLoad();
        void onLongLoad();
        void onDismiss();
        void onFail(String fail);
        void onSuccess(String success);
        void onUploadSuccess(String updated);
        void onGetImage(String orderid);
        void uploadData();
        void onJobClosed();
    }

    public interface Presenter extends BaseMvpInterface.Presenter<View> {
        void getAddressFromSQLite(String orderid);
        void getProductFromSQLite(String orderid);
        void getContactNumber(String orderid);
        void updateContactNumber(String orderid, String contactnumber);
        void updateStep(String orderid, String step);
        void getAllImage(String orderid);
        String getInstallDate(String orderid);
        String getInstallEnd(String orderid);
        void updatePrintStatus(String orderid, String print);
        void uploadImageToServer(RequestBody requestBody, List<MultipartBody.Part> body);
        void uploadDataToServer(RequestBody requestBody);
    }
}
