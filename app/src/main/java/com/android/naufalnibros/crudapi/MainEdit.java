package com.android.naufalnibros.crudapi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.naufalnibros.crudapi.API.ApiService;
import com.android.naufalnibros.crudapi.Model.ModelData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainEdit extends AppCompatActivity{

    String ID_MAHASISWA;
    EditText et_id, et_nama, et_kelas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        ID_MAHASISWA = getIntent().getStringExtra(ModelData.id_mahasiswa);

        et_id       = (EditText) findViewById(R.id.edit_id);
        et_nama     = (EditText) findViewById(R.id.edit_nama);
        et_kelas    = (EditText) findViewById(R.id.edit_kelas);

        Log.d("DEBUG ID ", ID_MAHASISWA);

        bindData();



        //deleted
        Button btnDel = (Button) findViewById(R.id.hapus);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hapusData(ID_MAHASISWA);
            }
        });


        //batal
        Button btnBatal = (Button) findViewById(R.id.batal);
        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void bindData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        Call<List<ModelData>> call = service.getSingleData(ID_MAHASISWA);
        call.enqueue(new Callback<List<ModelData>>() {
            @Override
            public void onResponse(Call<List<ModelData>> call, Response<List<ModelData>> response) {
                if (response.isSuccessful()){
                    for (int i = 0; i < response.body().size(); i++){
                        et_id.setText(response.body().get(i).getidMahasiswa());
                        et_nama.setText(response.body().get(i).getNama());
                        et_kelas.setText(response.body().get(i).getKelas_mhs());
                    }
                } else {
                    Toast.makeText(MainEdit.this, "DETAIL FAILED", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<ModelData>> call, Throwable t) {

            }
        });
    }

    public void hapusData(String id_mhs){

        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(MainActivity.ROOT_URL)
                                .addConverterFactory(new StringConverter())
                                .build();

        ApiService service = retrofit.create(ApiService.class);


        Call<ResponseBody> call = service.hapusData(id_mhs);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                BufferedReader reader = null;
                String respon = "";

                try {
                    reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                    respon = reader.readLine();
                } catch (IOException e){
                    e.printStackTrace();
                }

                Toast.makeText(MainEdit.this, respon, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }


}
