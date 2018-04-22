package th.co.thiensurat.toss_installer.setting.backupandrestore;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.api.ApiURL;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.setting.backupandrestore.utils.Download;
import th.co.thiensurat.toss_installer.setting.backupandrestore.utils.DownloadService;
import th.co.thiensurat.toss_installer.setting.backupandrestore.utils.UnZipper;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.MyApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class BackupAndRestoreActivity extends BaseMvpActivity<BackupAndRestoreInterface.Presenter>
        implements BackupAndRestoreInterface.View, Observer {

    private ProgressDialog dialog;
    private CustomDialog customDialog;
    private TextView textViewTitle;

    private static String PACKAGE_NAME;

    private String empid;
    private File fileDB;
    private String DB_PATH;
    private String PICTURE_PATH;
    private String FILE_BACKUP_ZIP;
    private String FILE_BACKUP_PATH;
    private String FILE_BACKUP_PATH_UNZIP;

    public static final String MESSAGE_PROGRESS = "message_progress";

    private Uri fileUri;

    @Override
    public BackupAndRestoreInterface.Presenter createPresenter() {
        return BackupAndRestorePresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_backup_and_restore;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.progressText) TextView textViewProgress;
    @BindView(R.id.progress) ProgressBar progressBar;
    @BindView(R.id.button_backup) Button buttonBackup;
    @BindView(R.id.button_restore) Button buttonRestore;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
        buttonBackup.setOnClickListener( onBackup() );
        buttonRestore.setOnClickListener( onRestore() );
    }

    @Override
    public void setupInstance() {
        customDialog = new CustomDialog(this);
    }

    @Override
    public void setupView() {
        setToolbar();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void initialize() {
        registerReceiver();
        PACKAGE_NAME = getPackageName();
        File extStore = Environment.getExternalStorageDirectory();
        FILE_BACKUP_PATH = extStore.getAbsolutePath() + "/Android/data/" + PACKAGE_NAME + "/files";
        FILE_BACKUP_ZIP = extStore.getAbsolutePath() + "/Android/data/" + PACKAGE_NAME + "/files.zip";
        FILE_BACKUP_PATH_UNZIP = extStore.getAbsolutePath() + "/Android/data/" + PACKAGE_NAME + "";

        DB_PATH = "/data/data/" + PACKAGE_NAME + "/databases/" + Constance.DBNAME;
        fileDB = new File(DB_PATH);

        empid = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID);

        File fileImage = new File(FILE_BACKUP_PATH + "/Pictures/" + empid);
        File[] files = fileImage.listFiles();
        try {
            if (files.length < 0) {
                buttonBackup.setEnabled(false);
            } else {
                buttonBackup.setEnabled(true);
            }
        } catch (Exception e) {
            buttonBackup.setEnabled(false);
        }

        getPresenter().checkBackup(empid);
    }

    private void setToolbar() {
        toolbar.setTitle("");
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_clear_white_24dp));
        textViewTitle.setText("สำรอง และกู้คือข้อมูล");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_OK);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNotBackup() {
        buttonRestore.setEnabled(false);
    }

    @Override
    public void onBackupExist() {
        buttonRestore.setEnabled(true);
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
        /*if (dialog.isShowing()) {
            dialog.dismiss();
        }*/
    }

    @Override
    public void onSuccess(String success) {
        /*if (dialog.isShowing()) {
            dialog.dismiss();
        }*/
        customDialog.dialogSuccess(success);
    }

    @Override
    public void onDeleteFileZip() {
        File file = new File(FILE_BACKUP_ZIP);
        if (file.delete()) {

        } else {
            customDialog.dialogFail("ลบไฟล์ผิดพลาด");
        }
    }

    private View.OnClickListener onBackup() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonBackup.startAnimation(new AnimateButton().animbutton());
                new uploadBackup().execute(FILE_BACKUP_PATH);
                //zipFileAtPath(FILE_BACKUP_PATH, FILE_BACKUP_ZIP);
                //onBackUpDB();
                //zipSubFolder(FILE_BACKUP_PATH, FILE_BACKUP_ZIP);
            }
        };
    }

    private View.OnClickListener onRestore() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonRestore.startAnimation(new AnimateButton().animbutton());
                buttonRestore.setEnabled(false);
                getPresenter().restore(ApiURL.BASE_URL + ApiURL.BASE_FOLDER + empid + "/" + Constance.DBNAME);
            }
        };
    }

    private void onBackUpDB() {
        Uri uri = Uri.fromFile(fileDB);
        File file = FileUtils.getFile(this, uri);

        File f2 = new File(FILE_BACKUP_ZIP);
        Uri uri2 = Uri.fromFile(f2);
        File file2 = FileUtils.getFile(this, uri2);

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("application/x-sqlite3"), file);

        RequestBody requestFile2 =
                RequestBody.create(MediaType.parse("application/zip-compressed"), file2);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("database_file", file.getName(), requestFile);

        MultipartBody.Part fileZip =
                MultipartBody.Part.createFormData("zip_file", file2.getName(), requestFile2);

        String descriptionString = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID);
        RequestBody description =
                RequestBody.create(MediaType.parse("text/plain"), descriptionString);

        getPresenter().backup(description, body, fileZip);
    }

    @Override
    public boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
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

    @Override
    public void extractFileToDevice() {
        UnZipper unzipper = new UnZipper(FILE_BACKUP_ZIP, FILE_BACKUP_PATH_UNZIP);
        unzipper.addObserver(this);
        unzipper.unzip();
    }

    public boolean zipFileAtPath(final String sourcePath, final String toLocation) {
        byte data[];
        final int BUFFER = 2048;
        File sourceFile = new File(sourcePath);
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(toLocation);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            if (sourceFile.isDirectory()) {
                zipSubFolder(out, sourceFile, sourceFile.getParent().length());
            } else {
                data = new byte[BUFFER];
                FileInputStream fi = new FileInputStream(sourcePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(getLastPathComponent(sourcePath));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void zipSubFolder(ZipOutputStream out, File folder, int basePathLength) throws IOException {
        final int BUFFER = 2048;
        File[] fileList = folder.listFiles();
        BufferedInputStream origin = null;
        for (File file : fileList) {
            if (file.isDirectory()) {
                zipSubFolder(out, file, basePathLength);
            } else {
                byte data[] = new byte[BUFFER];
                String unmodifiedFilePath = file.getPath();
                String relativePath = unmodifiedFilePath .substring(basePathLength);
                FileInputStream fi = new FileInputStream(unmodifiedFilePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(relativePath);
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
        }
    }

    public String getLastPathComponent(String filePath) {
        String[] segments = filePath.split("/");
        if (segments.length == 0)
            return "";
        String lastPathComponent = segments[segments.length - 1];
        return lastPathComponent;
    }

    @Override
    public void update(Observable o, Object arg) {
        //Log.e("observer", o.countObservers() + ", " + arg.toString());
        //customDialog.dialogSuccess("กู้คืนข้อมูลสำเร็จ..");
    }

    public class uploadBackup extends AsyncTask<String, Integer, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {
            try {
                for (String path : strings) {
                    if (zipFileAtPath(path, FILE_BACKUP_ZIP)) {
                        Log.e("Compressed", new File(FILE_BACKUP_ZIP).exists() + "");
                    } else {
                        Log.e("Can not compress file", new File(FILE_BACKUP_ZIP).exists() + "");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customDialog.dialogLongLoading();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            customDialog.dialogDimiss();
            onBackUpDB();
        }
    }

    @Override
    public void downloadFile() {
        Intent intent = new Intent(BackupAndRestoreActivity.this, DownloadService.class);
        startService(intent);
    }

    private void registerReceiver(){
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(BackupAndRestoreActivity.this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(MESSAGE_PROGRESS)){
                progressBar.setVisibility(View.VISIBLE);
                textViewProgress.setVisibility(View.VISIBLE);
                Download download = intent.getParcelableExtra("download");
                progressBar.setProgress(download.getProgress());
                if(download.getProgress() == 100){
                    textViewProgress.setText("กู้คืนข้อมูลสำเร็จ");
                    textViewProgress.setTextColor(getResources().getColor(R.color.LimeGreen));
                    buttonRestore.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    //textViewProgress.setVisibility(View.GONE);
                    getPresenter().extractFile();
                } else {
                    textViewProgress.setText(String.format("Downloaded (%d/%d) MB",download.getCurrentFileSize(),download.getTotalFileSize()));

                }
            }
        }
    };
}
