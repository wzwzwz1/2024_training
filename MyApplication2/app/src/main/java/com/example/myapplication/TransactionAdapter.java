package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

import java.util.List;

public class TransactionAdapter extends ArrayAdapter<Transaction> {

    private Context context;
    private List<Transaction> transactionList;
    private DatabaseHelper databaseHelper;
    private EditTransactionFragment.OnTransactionUpdatedListener onTransactionUpdatedListener;

    public TransactionAdapter(Context context, List<Transaction> transactionList,
                              EditTransactionFragment.OnTransactionUpdatedListener listener) {
        super(context, 0, transactionList);
        this.context = context;
        this.transactionList = transactionList;
        this.databaseHelper = new DatabaseHelper(context);
        this.onTransactionUpdatedListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false);
        }

        Transaction transaction = transactionList.get(position);

        TextView tvAmount = convertView.findViewById(R.id.tvAmount);
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvCategory = convertView.findViewById(R.id.tvCategory);
        TextView tvNote = convertView.findViewById(R.id.tvNote);
        CardView cardView = (CardView) convertView;

        double amount = Double.parseDouble(transaction.getAmount());

        // Add a plus sign for income and retain negative sign for expense
        if (amount < 0) {
            tvAmount.setText(String.format("-%.2f", -amount));
            tvAmount.setTextColor(context.getResources().getColor(android.R.color.holo_orange_light));

        } else {
            tvAmount.setText(String.format("+%.2f", amount));
            tvAmount.setTextColor(context.getResources().getColor(android.R.color.black));

        }

        tvDate.setText(transaction.getDate());
        tvCategory.setText(transaction.getCategory());
        tvNote.setText(transaction.getNote());

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showEditDeleteDialog(transaction);
                return true;
            }
        });

        return convertView;
    }

    private void showEditDeleteDialog(Transaction transaction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("编辑或删除账单")
                .setItems(new String[]{"编辑", "删除"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            showEditTransactionDialog(transaction);
                        } else {
                            databaseHelper.deleteTransaction(transaction.getId());
                            transactionList.remove(transaction);
                            notifyDataSetChanged();
                        }
                    }
                })
                .show();
    }

    private void showEditTransactionDialog(Transaction transaction) {
        EditTransactionFragment editTransactionFragment = new EditTransactionFragment();
        Bundle args = new Bundle();
        args.putSerializable("transaction", transaction);
        editTransactionFragment.setArguments(args);
        editTransactionFragment.setOnTransactionUpdatedListener(onTransactionUpdatedListener);
        editTransactionFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "EditTransactionFragment");
    }

    public void updateData(List<Transaction> newTransactionList) {
        //this.transactionList = newTransactionList;
        //notifyDataSetChanged();
    }

}
