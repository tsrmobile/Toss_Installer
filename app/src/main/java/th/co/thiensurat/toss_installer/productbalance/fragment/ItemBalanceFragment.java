package th.co.thiensurat.toss_installer.productbalance.fragment;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpFragment;
import th.co.thiensurat.toss_installer.productwithdraw.adapter.InstallItemAdapter;
import th.co.thiensurat.toss_installer.productwithdraw.item.InstallItem;
import th.co.thiensurat.toss_installer.utils.CustomDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemBalanceFragment extends BaseMvpFragment<ItemBalanceInterface.Presenter> implements ItemBalanceInterface.View {

    private CustomDialog customDialog;
    private InstallItemAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<InstallItem> installItemList;

    public ItemBalanceFragment() {
        // Required empty public constructor
    }

    public static ItemBalanceFragment getInstance() {
        return new ItemBalanceFragment();
    }

    @Override
    public ItemBalanceInterface.Presenter createPresenter() {
        return ItemBalancePresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.fragment_item_balance;
    }

    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.textview_fail) TextView textViewFail;
    @BindView(R.id.layout_fail) RelativeLayout relativeLayoutFail;
    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void setupInstance() {
        adapter = new InstallItemAdapter(getActivity());
        customDialog = new CustomDialog(getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
    }

    @Override
    public void setupView() {
        setRecyclerView();
        relativeLayoutFail.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void initialize() {
        getPresenter().getItemBalance(getActivity());
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void setItemBalanceToAdapter(List<InstallItem> installItemList) {
        recyclerView.setVisibility(View.VISIBLE);
        relativeLayoutFail.setVisibility(View.GONE);
        adapter.setInstallItemList(installItemList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (installItemList.size() == 0) {
            relativeLayoutFail.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
}
