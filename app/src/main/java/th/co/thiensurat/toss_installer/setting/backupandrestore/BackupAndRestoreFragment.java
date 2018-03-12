package th.co.thiensurat.toss_installer.setting.backupandrestore;


import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;

import com.basel.ProgressStatusBar.ProgressStatusBar;
import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.lizhangqu.coreprogress.ProgressHelper;
import io.github.lizhangqu.coreprogress.ProgressUIListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.api.ApiURL;
import th.co.thiensurat.toss_installer.base.BaseMvpFragment;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.MyApplication;
import th.co.thiensurat.toss_installer.utils.ProgressRequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class BackupAndRestoreFragment extends BaseMvpFragment<BackupAndRestoreInterface.Presenter>
        implements BackupAndRestoreInterface.View {

    private ProgressDialog dialog;
    private CustomDialog customDialog;

    private static String PACKAGE_NAME;

    private String DB_PATH;
    private String DB_BACKUP_ZIP;
    private String DB_BACKUP_PATH;

    private Uri fileUri;

    public BackupAndRestoreFragment() {
        // Required empty public constructor
    }

    public static BackupAndRestoreFragment getInstance() {
        return new BackupAndRestoreFragment();
    }

    @Override
    public BackupAndRestoreInterface.Presenter createPresenter() {
        return BackupAndRestorePresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.fragment_backup_and_restore;
    }

    @BindView(R.id.button_backup) Button buttonBackup;
    @BindView(R.id.button_restore) Button buttonRestore;
    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
        buttonBackup.setOnClickListener( onBackup() );
        buttonRestore.setOnClickListener( onRestore() );
    }

    @Override
    public void setupInstance() {
        customDialog = new CustomDialog(getActivity());
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("กำลังกู้คืนข้อมูล...");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setIndeterminate(true);
    }

    @Override
    public void setupView() {

    }

    @Override
    public void initialize() {
        PACKAGE_NAME = getActivity().getPackageName();
    }

    @Override
    public void onLoad() {
        customDialog.dialogLongLoading();
    }

    @Override
    public void onDismiss() {
        customDialog.dialogDimiss();
    }

    @Override
    public void onFail(String fail) {
        customDialog.dialogFail(fail);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onSuccess(String success) {
        if (dialog.isShowing()) {
            //dialog.setProgress(100);
            dialog.dismiss();
        }
        customDialog.dialogSuccess(success);
    }

    private View.OnClickListener onBackup() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonBackup.startAnimation(new AnimateButton().animbutton());
                onBackUpDB();
            }
        };
    }

    private View.OnClickListener onRestore() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonRestore.startAnimation(new AnimateButton().animbutton());
                dialog.show();
                //restoreDB();
                getPresenter().restore(ApiURL.BASE_URL + ApiURL.BASE_FOLDER
                        + MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID) + "/" + Constance.DBNAME);
            }
        };
    }

    private void onBackUpDB() {
        DB_PATH = "/data/data/" + PACKAGE_NAME + "/databases/" + Constance.DBNAME;

        File f = new File(DB_PATH);
        Uri uri = Uri.fromFile(f);
        File file = FileUtils.getFile(getActivity(), uri);

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("application/x-sqlite3"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("database_file", file.getName(), requestFile);

        String descriptionString = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID);
        RequestBody description =
                RequestBody.create(MediaType.parse("text/plain"), descriptionString);


        getPresenter().backup(description, body);
    }

    @Override
    public boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File fileDownload = new File("/data/data/" + PACKAGE_NAME + "/databases/" + Constance.DBNAME);
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(fileDownload);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    dialog.setProgress((int)(100 * fileSizeDownloaded / fileSize));
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private void restoreDB() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(ApiURL.BASE_URL + ApiURL.BASE_FOLDER
                + MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID) + "/" + Constance.DBNAME);
        builder.get();
        Call call = okHttpClient.newCall(builder.build());

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "=============onFailure===============");
                e.printStackTrace();
                onFail("กู้คืนข้อมูลไม่สำเร็จ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = ProgressHelper.withProgress(response.body(), new ProgressUIListener() {

                    //if you don't need this method, don't override this methd. It isn't an abstract method, just an empty method.
                    @Override
                    public void onUIProgressStart(long totalBytes) {
                        super.onUIProgressStart(totalBytes);
                        dialog.show();
                    }

                    @Override
                    public void onUIProgressChanged(long numBytes, long totalBytes, float percent, float speed) {
                        dialog.setProgress((int) (100 * percent));
                    }

                    //if you don't need this method, don't override this methd. It isn't an abstract method, just an empty method.
                    @Override
                    public void onUIProgressFinish() {
                        super.onUIProgressFinish();
                        dialog.dismiss();
                        onSuccess("กู้คืนข้อมูลสำเร็จ");
                    }
                });

                BufferedSource source = responseBody.source();

                File outFile = new File("/data/data/" + PACKAGE_NAME + "/databases/" + Constance.DBNAME);
                outFile.delete();
                outFile.getParentFile().mkdirs();
                outFile.createNewFile();

                BufferedSink sink = Okio.buffer(Okio.sink(outFile));
                source.readAll(sink);
                sink.flush();
                source.close();
            }
        });
    }
}
