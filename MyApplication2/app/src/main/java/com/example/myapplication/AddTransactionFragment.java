package com.example.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;



import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddTransactionFragment extends DialogFragment {

    private EditText etAmount, etDate, etCategory, etNote;
    private Spinner spCurrency;
    private Button btnSaveTransaction;
    private RadioGroup radioGroup;
    private RadioButton rbIncome, rbExpense;

    private OnTransactionSavedListener listener;

    public AddTransactionFragment() {
        // Required empty public constructor
    }

    public interface OnTransactionSavedListener {
        void onTransactionSaved();
    }

    public void setOnTransactionSavedListener(OnTransactionSavedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_transaction, container, false);

        etAmount = view.findViewById(R.id.etAmount);
        etDate = view.findViewById(R.id.etDate);
        etCategory = view.findViewById(R.id.etCategory);
        etNote = view.findViewById(R.id.etNote);
        spCurrency = view.findViewById(R.id.spCurrency);
        btnSaveTransaction = view.findViewById(R.id.btnSaveTransaction);
        radioGroup = view.findViewById(R.id.radioGroup);
        rbIncome = view.findViewById(R.id.rbIncome);
        rbExpense = view.findViewById(R.id.rbExpense);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.currencies_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCurrency.setAdapter(adapter);

        // Set default selection if needed
        rbIncome.setChecked(true);

        // Add listener to RadioGroup
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateAmountSign();
            }
        });

        // Add listener to EditText to format amount based on selection
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateAmountSign();
            }
        });

        btnSaveTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTransaction();
            }
        });

        return view;
    }

    private void updateAmountSign() {
        String amountText = etAmount.getText().toString();
        if (!amountText.isEmpty()) {
            try {
                double amount = Double.parseDouble(amountText);
                if (radioGroup.getCheckedRadioButtonId() == R.id.rbIncome) {
                    if (amount < 0) {
                        etAmount.setText(String.valueOf(-amount));
                    }
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbExpense) {
                    if (amount > 0) {
                        etAmount.setText(String.valueOf(-amount));
                    }
                }
                etAmount.setSelection(etAmount.getText().length());
            } catch (NumberFormatException e) {
                etAmount.setText("");
            }
        }
    }

    private void saveTransaction() {
        String amount = etAmount.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String note = etNote.getText().toString().trim();
        String currency = spCurrency.getSelectedItem() != null ? spCurrency.getSelectedItem().toString() : "";

        // Data validation
        if (amount.isEmpty() || date.isEmpty() || category.isEmpty() || currency.isEmpty()) {
            Toast.makeText(getContext(), "请填写所有必填字段", Toast.LENGTH_SHORT).show();
            return;
        }

        Transaction transaction = new Transaction(amount, date, category, note, currency);
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        databaseHelper.addTransaction(transaction);

        // Log the transaction details for debugging
        Log.d("AddTransactionFragment", "Transaction saved: " + transaction.toString());

        Toast.makeText(getContext(), "Transaction saved", Toast.LENGTH_SHORT).show();

        if (listener != null) {
            listener.onTransactionSaved();
        }

        dismiss();
    }
}
