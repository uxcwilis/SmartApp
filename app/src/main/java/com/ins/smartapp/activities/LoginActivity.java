package com.ins.smartapp.activities;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ins.smartapp.R;
import com.ins.smartapp.model.User;
import com.ins.smartapp.response.ResponseLogin;
import com.ins.smartapp.rest.ApiClient;
import com.ins.smartapp.rest.ApiInterface;
import com.ins.smartapp.utils.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ${user} on 25/04/2018.
 */
public class LoginActivity extends AppCompatActivity{
    @BindView(R.id.username)
    EditText etUsername;

    @BindView(R.id.password)
    EditText etPassword;

    @BindView(R.id.btnmasuk)
    Button tombolLogin;

    @BindView(R.id.createUser)
    TextView createUser;

    ApiInterface apiservice;
    SessionManager sessionManager;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        apiservice = ApiClient.getClient().create(ApiInterface.class);
        sessionManager = new SessionManager(this);


        tombolLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    private void loginUser() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        apiservice.login(username,password).enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                if (response.isSuccessful())
                {
                    User userLoggedIn = response.body().getUser();
                    sessionManager.createLoginSession(userLoggedIn);
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    Toast.makeText(LoginActivity.this,"Berhasil Login",Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();

                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getLocalizedMessage() );
                Toast.makeText(LoginActivity.this,"Gagal Terhubung ke server",Toast.LENGTH_SHORT).show();

            }
        });
    }

}
