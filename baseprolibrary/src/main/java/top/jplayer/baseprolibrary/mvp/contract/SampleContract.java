package top.jplayer.baseprolibrary.mvp.contract;

import top.jplayer.baseprolibrary.mvp.model.bean.SampleBean;

/**
 * Created by Administrator on 2018/1/27.
 * 抢啊抢红包
 */

public interface SampleContract {
    interface ISampleView extends IContract.IView {
        void setHBList(SampleBean sampleBean);
    }

    interface ISamplePresenter extends IContract.IPresenter {
        void requestHBList(String no,String token);

        void requestGrad(String id, String userNo,String accessToken);

        void requestGet(String id, String userNo,String accessToken);

        void addAccount(String phone, String password);
    }
}
