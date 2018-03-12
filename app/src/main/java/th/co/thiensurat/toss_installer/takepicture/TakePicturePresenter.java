package th.co.thiensurat.toss_installer.takepicture;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.hwangjr.rxbus.RxBus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.api.request.UploadImage;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItemGroup;
import th.co.thiensurat.toss_installer.takepicture.item.ImageItem;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

/**
 * Created by teerayut.k on 11/12/2017.
 */

public class TakePicturePresenter extends BaseMvpPresenter<TakePictureInterface.View> implements TakePictureInterface.Presenter {

    private DBHelper dbHelper;
    private static Context context;
    private List<ImageItem> imageItemList;
    private ProductItemGroup productItemGroup;

    private ServiceManager serviceManager;

    public static TakePictureInterface.Presenter create(Context activity) {
        context = activity;
        return new TakePicturePresenter();
    }

    public TakePicturePresenter() {
        serviceManager = ServiceManager.getInstance();
    }

    public void setManager( ServiceManager manager ){
        serviceManager = manager;
    }

    @Override
    public void onViewCreate() {
        RxBus.get().register( this );
    }

    @Override
    public void onViewDestroy() {
        RxBus.get().unregister( this );
    }

    @Override
    public void saveImageUrl(String orderid, String serial, String type, String url, String productcode) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        dbHelper.addImage(orderid, serial, type, url, productcode);
        getView().refresh();
    }

    @Override
    public void getImage(String orderid, String type) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        this.imageItemList = dbHelper.getImage(orderid, type);
        getView().setImageToAdapter(imageItemList);
    }

    @Override
    public void editImageUrl(String id, String url) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        dbHelper.editImage(id, url);
        getView().refresh();
    }

    @Override
    public void delImage(String id) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        dbHelper.deleteImage(id);
        getView().refresh();
    }

    @Override
    public boolean getItemInstalled(String orderid) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        return dbHelper.getProductNotInstall(orderid);
    }

    @Override
    public List<ProductItem> getAllItem(String orderid) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        productItemGroup = dbHelper.getProductByID(orderid);
        return productItemGroup.getProduct();
    }

    @Override
    public void updateStep(String orderid, String step) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        dbHelper.updateStep(orderid, step);
    }

    /*@Override
    public void uploadImageToServer(String action, String orderid, String image64, String imageType, String productcode) {
        List<UploadImage.uploadBody> uploadBodies = new ArrayList<>();
        uploadBodies.add(new UploadImage.uploadBody()
                .setAction(action)
                .setOrderid(orderid)
                .setImage64(image64)
                .setImageType(imageType)
                .setProductcode(productcode)
        );
        getView().onLoading();
        serviceManager.requestUpload(uploadBodies, new ServiceManager.ServiceManagerCallback() {
            @Override
            public void onSuccess(Object result) {
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(result));
                    if ("SUCCESS".equals(jsonObject.getString("status"))) {
                        getView().onDismiss();
                        getView().onSuccess(jsonObject.getString("message"));
                    } else if ("FAIL".equals(jsonObject.getString("status"))) {
                        getView().onDismiss();
                        getView().onFail(jsonObject.getString("message"));
                    } else {
                        getView().onDismiss();
                        getView().onFail(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    Log.e("json obj", e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("upload failure", t.getLocalizedMessage());
            }
        });
    }*/
}
