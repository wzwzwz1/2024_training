package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {

    private Button btnAddTransaction;
    private TransactionListFragment transactionListFragment;
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);

        // 设置自定义工具栏
        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("账单管理");

        // 初始化头像按钮
        profileImage = findViewById(R.id.profile_image);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        // 加载TransactionListFragment
        transactionListFragment = new TransactionListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, transactionListFragment)
                .commit();

        btnAddTransaction = findViewById(R.id.btnAddTransaction);
        btnAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTransactionDialog();
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_settings) {
                    Toast.makeText(MainActivity.this, "Settings selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.action_logout) {
                    Toast.makeText(MainActivity.this, "Logout selected", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        popupMenu.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // 处理设置选项
            Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_logout) {
            // 处理登出选项
            Toast.makeText(this, "Logout selected", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            transactionListFragment.updateTransactions();
        }
    }

    private void showAddTransactionDialog() {
        AddTransactionFragment addTransactionFragment = new AddTransactionFragment();
        addTransactionFragment.setOnTransactionSavedListener(new AddTransactionFragment.OnTransactionSavedListener() {
            @Override
            public void onTransactionSaved() {
                transactionListFragment.updateTransactions();
            }
        });
        addTransactionFragment.show(getSupportFragmentManager(), "AddTransactionFragment");
    }
}
