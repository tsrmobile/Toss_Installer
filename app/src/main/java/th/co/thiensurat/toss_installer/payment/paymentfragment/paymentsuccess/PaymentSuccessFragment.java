package th.co.thiensurat.toss_installer.payment.paymentfragment.paymentsuccess;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpFragment;
import th.co.thiensurat.toss_installer.payment.paymentitem.PaymentItemPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentSuccessFragment extends BaseMvpFragment<PaymentSuccessInterface.Presenter>
        implements PaymentSuccessInterface.View {


    public PaymentSuccessFragment() {
        // Required empty public constructor
    }

    public static PaymentSuccessFragment getInstance() {
        return new PaymentSuccessFragment();
    }

    @Override
    public PaymentSuccessInterface.Presenter createPresenter() {
        return PaymentSuccessPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.fragment_payment_success;
    }

    @Override
    public void bindView(View view) {

    }

    @Override
    public void setupInstance() {

    }

    @Override
    public void setupView() {

    }

    @Override
    public void initialize() {

    }
}
