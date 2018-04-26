package com.ins.smartapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ins.smartapp.R;
import com.ins.smartapp.rest.ApiClient;
import com.ins.smartapp.rest.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ${user} on 18/04/2018.
 */

public class SignupActivity extends AppCompatActivity {
    @BindView(R.id.etusername)
    EditText etUsername;

    @BindView(R.id.etpassword)
    EditText etPassword;

    @BindView(R.id.etnama)
    EditText etNama;

    @BindView(R.id.etemail)
    EditText etEmail;

    @BindView(R.id.ethp)
    EditText ethp;

    @BindView(R.id.btnsave)
    Button btnSave;

    ApiInterface apiService;
    ProgressDialog pd;
    private static final String TAG = SignupActivity.class.getSimpleName();


    protected void onCreate (@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registeruser);
        ButterKnife.bind(this);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();

            }
        });

    }
    //@OnClick(R.id.btnsave)

    public void signup() {
        Log.d(TAG,"Menjalankan method Signup");
        pd = ProgressDialog.show(this,null,"Sedang mendaftarkan account",true,false);
        String strUsername = etUsername.getText().toString();
        String strPassword = etPassword.getText().toString();
        String strNamaLengkap = etNama.getText().toString();
        String strEmail = etEmail.getText().toString();
        String strNoHp = ethp.getText().toString();

        Log.d(TAG,"mendapatkan data dari edit text");

        apiService.signup(strUsername,strPassword,strNamaLengkap,strEmail,strNoHp).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG,"berhasil memanggil api");
                if (response.isSuccessful()){
                    pd.dismiss();
                    Log.d(TAG,"succes mendapatkan api");
                    Intent i = new Intent(SignupActivity.this,MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                Log.e(TAG,t.getLocalizedMessage());
                Toast.makeText(getApplicationContext(),"Gagal",Toast.LENGTH_SHORT).show();

            }
        });


    }
}
