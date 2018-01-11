package com.testtask.fb.presenter.facebook;

import com.testtask.fb.model.UserInfo;
import com.testtask.fb.view.EditView;

public class FBUpdateInfo implements FBUpdateInfoPresenter{
    EditView editView;
    UserInfo userModel;

    public FBUpdateInfo(EditView editView) {
        this.editView = editView;
        //Get singleton with updating data
        userModel = UserInfo.getInstance();
    }


    @Override
    public void updateRequest (){
        // Can't to change user information with request.
        // You need to manually use the Official Facebook app

    }


    @Override
    public void onDestroy() {
        editView = null;
    }

}
