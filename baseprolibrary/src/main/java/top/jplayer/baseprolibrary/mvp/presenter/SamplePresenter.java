package top.jplayer.baseprolibrary.mvp.presenter;

import android.text.TextUtils;

import java.util.Locale;

import io.reactivex.disposables.Disposable;
import top.jplayer.baseprolibrary.mvp.contract.BasePresenter;
import top.jplayer.baseprolibrary.mvp.contract.SampleContract;
import top.jplayer.baseprolibrary.mvp.model.SampleModel;
import top.jplayer.baseprolibrary.mvp.model.bean.LoginBean;
import top.jplayer.baseprolibrary.ui.SampleActivity;
import top.jplayer.baseprolibrary.utils.SharePreUtil;
import top.jplayer.baseprolibrary.utils.ToastUtils;

/**
 * Created by Administrator on 2018/1/27.
 * 抢红包功能
 */

public class SamplePresenter extends BasePresenter<SampleActivity> implements SampleContract.ISamplePresenter {
    private SampleModel sampleModel;

    public SamplePresenter(SampleActivity iView) {
        super(iView);
        sampleModel = new SampleModel();
    }

    public void requestSign(String no, String token) {
        sampleModel.requestSign(no, token).subscribe(gradBean -> {
            if (TextUtils.equals("0000", gradBean.errorCode)) {
                ToastUtils.init().showSuccessToast(mIView, "抢到了，关闭该界面吧");
            } else if (TextUtils.equals("9999", gradBean.errorCode)) {
                ToastUtils.init().showSuccessToast(mIView, "已签到");
            } else if (TextUtils.equals("1005", gradBean.errorCode)) {
                ToastUtils.init().showSuccessToast(mIView, "登录失效,请重新登录");
            } else {
                ToastUtils.init().showSuccessToast(mIView, "先看看是否已签到" + gradBean.errorCode);
            }
        });
    }

    @Override
    public void requestHBList(String no, String token) {
        Disposable disposable = sampleModel.requestHBList(no, token)
                .map(sampleBean -> {
                    if (TextUtils.equals("0000", sampleBean.errorCode)) {
                        if (sampleBean.data != null && sampleBean.data.list != null) {
                            return sampleBean;
                        } else return null;
                    }
                    return null;
                })
                .subscribe(sampleBean ->
                {

                    if (sampleBean.data.list.size() < 1) {
                        mIView.showEmpty();
                    } else {

                        mIView.setHBList(sampleBean);
                    }
                }, throwable -> mIView.showError());
        addSubscription(disposable);
    }

    public void requestHasHBList(String no, String token) {
        Disposable disposable = sampleModel.requestHasHBList(no, token)
                .map(sampleBean -> {
                    if (TextUtils.equals("0000", sampleBean.errorCode)) {
                        if (sampleBean.data != null && sampleBean.data.list != null) {
                            return sampleBean;
                        } else return null;
                    }
                    return null;
                })
                .subscribe(sampleBean ->
                {

                    if (sampleBean.data.list.size() < 1) {
                    } else {
                        mIView.setHBOne(sampleBean);
                    }
                }, throwable -> {
                });
        addSubscription(disposable);
    }

    @Override
    public void requestGrad(String id, String userNo, String accessToken) {
        Disposable subscribe = sampleModel.requestGrad(id, userNo, accessToken).subscribe(gradBean ->
        {
            if (TextUtils.equals("0000", gradBean.errorCode)) {
                requestGet(id, userNo, accessToken);
            } else if (!TextUtils.equals("0009", gradBean.errorCode)) {
                requestGrad(id, userNo, accessToken);
            }
        });
        addSubscription(subscribe);
    }

    @Override
    public void requestGet(String id, String userNo, String accessToken) {
        Disposable disposable = sampleModel.requestGet(id, userNo, accessToken).subscribe(gradBean -> {
            if (TextUtils.equals("0000", gradBean.errorCode)) {
                ToastUtils.init().showSuccessToast(mIView, "抢到了，关闭该界面吧");
            } else if (!TextUtils.equals("0009", gradBean.errorCode)) {
                requestGet(id, userNo, accessToken);
            }
        }, throwable -> requestGet(id, userNo, accessToken));
        addSubscription(disposable);
    }

    @Override
    public void addAccount(String phone, String password) {
        Disposable disposable = sampleModel.login(phone, password).subscribe(loginBean -> {
                    LoginBean.ResultBean result = loginBean.result;
                    if (result != null) {
                        ToastUtils.init().showSuccessToast(mIView, String.format(Locale.CHINA, "%s-登陆成功", result.name));
                        String userNo = (String) SharePreUtil.getData(mIView, "userNo", "");
                        String name = (String) SharePreUtil.getData(mIView, "name", "");
                        String accessToken = (String) SharePreUtil.getData(mIView, "accessToken", "");
                        if (TextUtils.equals("", userNo)) {
                            userNo = result.userNo;
                        } else {
                            userNo = userNo + "," + result.userNo;
                        }
                        if (TextUtils.equals("", name)) {
                            name = result.name;
                        } else {
                            name = name + "," + result.name;
                        }
                        if (TextUtils.equals("", accessToken)) {
                            accessToken = result.accessToken;
                        } else {
                            accessToken = accessToken + "," + result.accessToken;
                        }
                        SharePreUtil.saveData(mIView, "userNo", userNo);
                        SharePreUtil.saveData(mIView, "name", name);
                        SharePreUtil.saveData(mIView, "accessToken", accessToken);
                        mIView.loginSuccess();
                    }
                }
                , throwable -> ToastUtils.init().showErrorToast(mIView, "登陆失败"));
        addSubscription(disposable);
    }


    public void requestTotalMoney(String no) {
        Disposable disposable = sampleModel.requestTotalMoney(no).subscribe(gradBean -> {
            if (TextUtils.equals("0000", gradBean.errorCode)) {
                SharePreUtil.saveData(mIView, no, gradBean.data.totalMoney);
            }
        }, throwable -> {
        });
        addSubscription(disposable);
    }
}
