package th.co.thiensurat.toss_installer.installation.step;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.job.item.JobItem;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.ChangeTintColor;
import th.co.thiensurat.toss_installer.utils.Constance;

public class StepViewActivity extends BaseMvpActivity<StepViewInterface.Presenter> implements StepViewInterface.View {

    private JobItem jobItem;
    private TextView textViewTitle;
    private ChangeTintColor changeTintColor;

    @Override
    public StepViewInterface.Presenter createPresenter() {
        return StepViewPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_step_view;
    }

    @BindView(R.id.step1_status) TextView textViewStatus1;
    @BindView(R.id.step2_status) TextView textViewStatus2;
    @BindView(R.id.step3_status) TextView textViewStatus3;
    @BindView(R.id.step4_status) TextView textViewStatus4;
    @BindView(R.id.step5_status) TextView textViewStatus5;
    @BindView(R.id.step6_status) TextView textViewStatus6;
    @BindView(R.id.step7_status) TextView textViewStatus7;
    @BindView(R.id.step8_status) TextView textViewStatus8;
    @BindView(R.id.step1) LinearLayout linearLayoutStep1;
    @BindView(R.id.step2) LinearLayout linearLayoutStep2;
    @BindView(R.id.step3) LinearLayout linearLayoutStep3;
    @BindView(R.id.step4) LinearLayout linearLayoutStep4;
    @BindView(R.id.step5) LinearLayout linearLayoutStep5;
    @BindView(R.id.step6) LinearLayout linearLayoutStep6;
    @BindView(R.id.step7) LinearLayout linearLayoutStep7;
    @BindView(R.id.step8) LinearLayout linearLayoutStep8;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
        linearLayoutStep1.setOnClickListener( onStep1() );
        linearLayoutStep2.setOnClickListener( onStep2() );
        linearLayoutStep3.setOnClickListener( onStep3() );
        linearLayoutStep4.setOnClickListener( onStep4() );
        linearLayoutStep5.setOnClickListener( onStep5() );
        linearLayoutStep6.setOnClickListener( onStep6() );
        linearLayoutStep7.setOnClickListener( onStep7() );
        linearLayoutStep8.setOnClickListener( onStep8() );
    }

    @Override
    public void setupInstance() {
        changeTintColor = new ChangeTintColor(StepViewActivity.this);
    }

    @Override
    public void setupView() {
        setToolbar();
    }

    @Override
    public void initialize() {
        getDataFromIntent();

    }

    private void getDataFromIntent() {
        jobItem = getIntent().getParcelableExtra(Constance.KEY_JOB_ITEM);
    }

    private void setToolbar() {
        toolbar.setTitle("");
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textViewTitle.setText("ขั้นตอนการทำงาน");
        setSupportActionBar(toolbar);
    }

    private View.OnClickListener onStep1() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutStep1.startAnimation(new AnimateButton().animbutton());

            }
        };
    }

    private View.OnClickListener onStep2() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutStep2.startAnimation(new AnimateButton().animbutton());
            }
        };
    }

    private View.OnClickListener onStep3() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutStep3.startAnimation(new AnimateButton().animbutton());
            }
        };
    }

    private View.OnClickListener onStep4() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutStep4.startAnimation(new AnimateButton().animbutton());
            }
        };
    }

    private View.OnClickListener onStep5() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutStep5.startAnimation(new AnimateButton().animbutton());
            }
        };
    }

    private View.OnClickListener onStep6() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutStep6.startAnimation(new AnimateButton().animbutton());
            }
        };
    }

    private View.OnClickListener onStep7() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutStep7.startAnimation(new AnimateButton().animbutton());
            }
        };
    }

    private View.OnClickListener onStep8() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutStep8.startAnimation(new AnimateButton().animbutton());
            }
        };
    }

    @Override
    public void setStatusStep1() {
        textViewStatus1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_18dp, 0, 0, 0);
        changeTintColor.setTextViewDrawableColor(textViewStatus1, R.color.LimeGreen);
    }

    @Override
    public void setStatusStep2() {
        textViewStatus2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_18dp, 0, 0, 0);
        changeTintColor.setTextViewDrawableColor(textViewStatus2, R.color.LimeGreen);
    }

    @Override
    public void setStatusStep3() {
        textViewStatus3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_18dp, 0, 0, 0);
        changeTintColor.setTextViewDrawableColor(textViewStatus3, R.color.LimeGreen);
    }

    @Override
    public void setStatusStep4() {
        textViewStatus4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_18dp, 0, 0, 0);
        changeTintColor.setTextViewDrawableColor(textViewStatus4, R.color.LimeGreen);
    }

    @Override
    public void setStatusStep5() {
        textViewStatus5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_18dp, 0, 0, 0);
        changeTintColor.setTextViewDrawableColor(textViewStatus5, R.color.LimeGreen);
    }

    @Override
    public void setStatusStep6() {
        textViewStatus6.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_18dp, 0, 0, 0);
        changeTintColor.setTextViewDrawableColor(textViewStatus6, R.color.LimeGreen);
    }

    @Override
    public void setStatusStep7() {
        textViewStatus7.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_18dp, 0, 0, 0);
        changeTintColor.setTextViewDrawableColor(textViewStatus7, R.color.LimeGreen);
    }

    @Override
    public void setStatusStep8() {
        textViewStatus8.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_18dp, 0, 0, 0);
        changeTintColor.setTextViewDrawableColor(textViewStatus8, R.color.LimeGreen);
    }
}
