package com.example.easywallet;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.easywallet.db.WalletDbHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class IncomeActivity extends AppCompatActivity {

    private ImageView mImageAdd;
    private EditText mEditDes;
    private EditText mEditCost;
    private Button mBtnOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        Intent intent = getIntent();
        String status = intent.getStringExtra("status");
        final String pictureFileName = "ic_income.png";
        mImageAdd = findViewById(R.id.image_add);

        AssetManager am = this.getAssets();
        try {
            InputStream stream = am.open(pictureFileName);
            Drawable drawable = Drawable.createFromStream(stream, null);
            mImageAdd.setImageDrawable(drawable);

        } catch (IOException e) {
            e.printStackTrace();

            File pictureFile = new File(this.getFilesDir(), pictureFileName);
            Drawable drawable = Drawable.createFromPath(pictureFile.getAbsolutePath());
            mImageAdd.setImageDrawable(drawable);
        }

        mEditDes = findViewById(R.id.edit_des);
        mEditCost = findViewById(R.id.edit_cost);
        mBtnOK = findViewById(R.id.btn_ok);

        mBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String des = String.valueOf(mEditDes.getText());
                String cost = String.valueOf(mEditCost.getText());
                if(des.length() == 0){
                    mEditDes.setError("กรุณาป้อนรายละเอียด");
                }
                if(cost.length() == 0){
                    mEditCost.setError("กรุณาป้อนจำนวนเงิน");
                }
                if(cost.length() > 0 && des.length() > 0){
                    WalletDbHelper dbHelper = new WalletDbHelper(IncomeActivity.this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    ContentValues cv = new ContentValues();
                    cv.put(WalletDbHelper.COL_PICTURE, pictureFileName);
                    cv.put(WalletDbHelper.COL_DES, des);
                    cv.put(WalletDbHelper.COL_COST, cost);
                    db.insert(WalletDbHelper.TABLE_NAME,null,cv);

                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        getSupportActionBar().setTitle("บันทึกรายรับ");
    }
}
