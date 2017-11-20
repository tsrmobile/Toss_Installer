package th.co.thiensurat.toss_installer.base;

/**
 * Created by TheKhaeng on 12/18/2016.
 */

public interface BaseMvpInterface{
    interface View{
        Presenter getPresenter();
    }


    interface Presenter<V extends View>{
        void attachView(V mvpView);

        void detachView();

        V getView();

        void onViewCreate();

        void onViewDestroy();

        void onViewStart();

        void onViewStop();

    }
}
