package cn.ucai.live.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.live.R;
import cn.ucai.live.data.model.IUserModel;
import cn.ucai.live.data.model.OnCompleteListener;
import cn.ucai.live.data.model.UserModel;
import cn.ucai.live.utils.CommonUtils;
import cn.ucai.live.utils.I;
import cn.ucai.live.utils.MD5;
import cn.ucai.live.utils.MFGT;
import cn.ucai.live.utils.Result;
import cn.ucai.live.utils.ResultUtils;

public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity";
    @BindView(R.id.email)
    EditText mUsername;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.register)
    Button register;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nick)
    EditText mNick;
    @BindView(R.id.config_password)
    EditText mConfigPassword;
    String username,password,nickname;
    ProgressDialog pd;
    IUserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        userModel = new UserModel();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.register)
    public void registerOnClick() {
        if (inputCheck()){
            showDialog();
            register();
        }
    }

    private void register() {
        userModel.register(RegisterActivity.this, username, nickname, MD5.getMessageDigest(password),
                new OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        boolean success = false;
                        if (result != null ){
                            Result res = ResultUtils.getResultFromJson(result, String.class);
                            if (res != null) {
                                if (res.isRetMsg()) {//注册成功
                                    Log.e(TAG, "register,res = " + res);
                                    CommonUtils.showShortToast(R.string.Registered_successfully);
                                    success = true;
                                    registerEmClient();
                                } else if (res.getRetCode() == I.MSG_REGISTER_USERNAME_EXISTS) {
                                    CommonUtils.showShortToast(R.string.User_already_exists);
                                } else {
                                    CommonUtils.showShortToast(R.string.Registration_failed);
                                }
                            }
                        }
                        if (!success){
                            pd.dismiss();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.e(TAG,"register,error = " + error);
                        pd.dismiss();
                        CommonUtils.showShortToast(R.string.Registration_failed);
                    }
                });
    }

    private void registerEmClient() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(username, MD5.getMessageDigest(password));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            showToast("注册成功");
                            /*startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();*/
                            MFGT.gotoLogin(RegisterActivity.this);
                        }
                    });
                } catch (final HyphenateException e) {
                    unregister();
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            showLongToast("注册失败：" + e.getMessage());
                        }
                    });
                }
            }
        }).start();
    }

    private void unregister() {
        userModel.unregister(RegisterActivity.this, username, new OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {
                pd.dismiss();
                Log.e(TAG,"result = " + result);
            }
            @Override
            public void onError(String error) {
                pd.dismiss();
                Log.e(TAG,"error = " + error);
            }
        });
    }

    private void showDialog() {
        pd = new ProgressDialog(RegisterActivity.this);
        pd.setMessage("正在注册...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
    }


    private boolean inputCheck() {
        username = mUsername.getText().toString().trim();
        nickname = mNick.getText().toString().trim();
        password = mPassword.getText().toString().trim();
        String confirm_pwd = mConfigPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, getResources().getString(R.string.User_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
            mUsername.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(nickname)) {
            Toast.makeText(this, getResources().getString(R.string.User_nick_cannot_be_empty), Toast.LENGTH_SHORT).show();
            mNick.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, getResources().getString(R.string.Password_cannot_be_empty), Toast.LENGTH_SHORT).show();
            mPassword.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(confirm_pwd)) {
            Toast.makeText(this, getResources().getString(R.string.Confirm_password_cannot_be_empty), Toast.LENGTH_SHORT).show();
            mConfigPassword.requestFocus();
            return false;
        } else if (!password.equals(confirm_pwd)) {
            Toast.makeText(this, getResources().getString(R.string.Two_input_password), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
