package com.example.easywallet;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.easywallet.adapter.WalletListAdapter;
import com.example.easywallet.db.WalletDbHelper;
import com.example.easywallet.model.WalletItem;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView mTextResult;
    private ListView mListWallet;
    private Button mBtnIncome;
    private Button mBtnExpense;

    private WalletDbHelper mHelper;
    private SQLiteDatabase mDb;

    private ArrayList<WalletItem> mWalletItemList = new ArrayList<>();
    private WalletListAdapter mAdapter;

    private int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new WalletDbHelper(this);
        mDb = mHelper.getReadableDatabase();

        loadDataFromDb();

        mAdapter = new WalletListAdapter(
                this,
                R.layout.item,
                mWalletItemList
        );

        mListWallet = findViewById(R.id.list_wallet);
        mListWallet.setAdapter(mAdapter);
        computeTotal();

        mListWallet.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                final WalletItem item = mWalletItemList.get(i);

                String des = item.des;
                DecimalFormat form = new DecimalFormat("#,###");
                String cost = form.format(item.cost);

                String message = "ยืนยันลบรายการ \""+des+" "+cost+" บาท\" ?";
                dialog.setMessage(message);

                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int walletId = item.id;
                        mDb.delete(
                                WalletDbHelper.TABLE_NAME,
                                WalletDbHelper.COL_ID + "=?",
                                new String[]{String.valueOf(walletId)}
                        );
                        loadDataFromDb();
                        mAdapter.notifyDataSetChanged();
                        computeTotal();
                    }
                });
                dialog.setNegativeButton("No",null);

                dialog.show();
                return true;
            }
        });

        mBtnIncome = findViewById(R.id.btn_income);
        mBtnIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, IncomeActivity.class);
                intent.putExtra("status", "income");
                startActivityForResult(intent, 123);
            }
        });

        mBtnExpense = findViewById(R.id.btn_expense);
        mBtnExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ExpenseActivity.class);
                intent.putExtra("status", "expense");
                startActivityForResult(intent, 123);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (resultCode == RESULT_OK) {
                loadDataFromDb();
                mAdapter.notifyDataSetChanged();
                computeTotal();
            }
        }
    }

    private void computeTotal() {
        DecimalFormat form = new DecimalFormat("#,###");
        mTextResult = findViewById(R.id.text_result);
        String text = "คงเหลือ "+form.format(total)+" บาท";
        mTextResult.setText(text);
    }

    private void loadDataFromDb() {
        Cursor cursor = mDb.query(
                WalletDbHelper.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        mWalletItemList.clear();
        total = 0;

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(WalletDbHelper.COL_ID));
            String picture = cursor.getString(cursor.getColumnIndex(WalletDbHelper.COL_PICTURE));
            String des = cursor.getString(cursor.getColumnIndex(WalletDbHelper.COL_DES));
            int cost = cursor.getInt(cursor.getColumnIndex(WalletDbHelper.COL_COST));

            if(picture.indexOf("income") != -1){
                total += cost;
            }
            else {
                total -= cost;
            }

            WalletItem item = new WalletItem(id, picture, des, cost);
            mWalletItemList.add(item);
        }
    }

}
