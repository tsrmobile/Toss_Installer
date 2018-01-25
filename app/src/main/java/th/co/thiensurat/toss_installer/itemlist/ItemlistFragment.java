package th.co.thiensurat.toss_installer.itemlist;


import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.MainActivity;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpFragment;
import th.co.thiensurat.toss_installer.itemlist.adapter.InstallItemAdapter;
import th.co.thiensurat.toss_installer.itemlist.item.InstallItem;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.CustomDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemlistFragment extends BaseMvpFragment<ItemlistInterface.Presenter> implements ItemlistInterface.View {

    private CustomDialog customDialog;
    private InstallItemAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<InstallItem> installItemList;

    public ItemlistFragment() {
        // Required empty public constructor
    }

    public static ItemlistFragment getInstance() {
        return new ItemlistFragment();
    }

    @Override
    public ItemlistInterface.Presenter createPresenter() {
        return ItemlistPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.fragment_itemlist;
    }

    @BindView(R.id.edittext_search) EditText editTextSearch;
    @BindView(R.id.button_search) ImageView buttonSearch;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.button_confirm) Button buttonConfirm;
    @BindView(R.id.textview_fail) TextView textViewFail;
    @BindView(R.id.layout_fail) RelativeLayout relativeLayoutFail;
    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
        buttonConfirm.setOnClickListener( onConfirm() );
        buttonSearch.setOnClickListener( onSearch() );

        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editTextSearch.getWindowToken(), 0);
                    if (!editTextSearch.getText().toString().isEmpty()) {
                        buttonSearch.performClick();
                        return true;
                    } else {
                        //onFail("กรุณากรอกเลขที่ใบเบิกสินค้า");
                        return false;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void setupInstance() {
        adapter = new InstallItemAdapter(getActivity());
        customDialog = new CustomDialog(getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
    }

    @Override
    public void setupView() {
        ((MainActivity) getActivity()).setTitle("รายการเบิกสินค้า");
        setRecyclerView();
        relativeLayoutFail.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        buttonConfirm.setVisibility(View.GONE);
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onLoad() {
        customDialog.dialogLoading();
    }

    @Override
    public void onDismiss() {
        customDialog.dialogDimiss();
    }

    @Override
    public void onFail(String fail) {
        relativeLayoutFail.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        textViewFail.setText(fail);
    }

    @Override
    public void onSuccess(String success) {

    }

    @Override
    public void onApply() {
        getPresenter().requestInstallItem(editTextSearch.getText().toString(), "load");
        new createDb().execute();
    }

    @Override
    public void setInstallItemToAdapter(List<InstallItem> installItem) {
        this.installItemList = installItem;
        adapter.setInstallItemList(installItemList);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        relativeLayoutFail.setVisibility(View.GONE);

        if (installItemList.get(0).getAStockStatus().equals("T")) {
            buttonConfirm.setVisibility(View.VISIBLE);
        } else {
            new createDb().execute();
        }
    }

    private View.OnClickListener onConfirm() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonConfirm.startAnimation(new AnimateButton().animbutton());
                getPresenter().requestApplyItem(installItemList.get(0).getPrintTakeStockID(), "insert");
            }
        };
    }

    private View.OnClickListener onSearch() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSearch.startAnimation(new AnimateButton().animbutton());
                if (!editTextSearch.getText().toString().isEmpty()) {
                    if (!getPresenter().checkStockID(getActivity(), editTextSearch.getText().toString())) {
                        getPresenter().requestInstallItem(editTextSearch.getText().toString(), "load");
                    } else {
                        customDialog.dialogFail("เบิกสินค้าแล้ว");
                    }
                }
            }
        };
    }

    public class createDb extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getPresenter().insertDataToSQLite(getActivity(), installItemList);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
