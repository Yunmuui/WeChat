package com.jsut.wechat.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<String> mLoginStatusLiveData = new MutableLiveData<>();

    public void setLoginStatus(String loginStatus) {
        mLoginStatusLiveData.setValue(loginStatus);
    }

    public LiveData<String> getLoginStatus() {
        return mLoginStatusLiveData;
    }

}