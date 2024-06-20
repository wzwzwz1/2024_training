package com.example.myapplication;

import android.content.Intent;
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

public class EditTransactionFragment extends DialogFragment {

    private EditText etAmount, etDate, etCategory, etNote;
    private Spinner spCurrency;
    private Button btnSave;
    private RadioGroup radioGroup;
    private RadioButton rbIncome, rbExpense;
    private Transaction transaction;
    private DatabaseHelper databaseHelper;
    private OnTransactionUpdatedListener listener;

    public EditTransactionFragment() {
        // Required empty public constructor
    }

    public interface OnTransactionUpdatedListener {
        void onTransactionUpdated();
    }

    public void setOnTransactionUpdatedListener(OnTransactionUpdatedListener listener) {
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
        btnSave = view.findViewById(R.id.btnSaveTransaction);
        radioGroup = view.findViewById(R.id.radioGroup);
        rbIncome = view.findViewById(R.id.rbIncome);
        rbExpense = view.findViewById(R.id.rbExpense);

        databaseHelper = new DatabaseHelper(getContext());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.currencies_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCurrency.setAdapter(adapter);

        Bundle args = getArguments();
        if (args != null) {
            transaction = (Transaction) args.getSerializable("transaction");
            if (transaction != null) {
                etAmount.setText(transaction.getAmount());
                etDate.setText(transaction.getDate());
                etCategory.setText(transaction.getCategory());
                etNote.setText(transaction.getNote());
                int spinnerPosition = adapter.getPosition(transaction.getCurrency());
                spCurrency.setSelection(spinnerPosition);

                // Set radio button based on the amount
                if (Double.parseDouble(transaction.getAmount()) > 0) {
                    rbIncome.setChecked(true);
                } else {
                    rbExpense.setChecked(true);
                }
            }
        }

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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEditedTransaction();
            }
        });

        return view;
    }

    private void updateAmountSign() {
        String amountText = etAmount.getText().toString();
        if (!amountText.isEmpty()) {
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
        }
    }

    private void saveEditedTransaction() {
        String amount = etAmount.getText().toString();
        String date = etDate.getText().toString();
        String category = etCategory.getText().toString();
        String note = etNote.getText().toString();
        String currency = spCurrency.getSelectedItem().toString();

        transaction.setAmount(amount);
        transaction.setDate(date);
        transaction.setCategory(category);
        transaction.setNote(note);
        transaction.setCurrency(currency);

        databaseHelper.updateTransaction(transaction);

        Toast.makeText(getContext(), "Transaction updated", Toast.LENGTH_SHORT).show();

        if (listener != null) {
            listener.onTransactionUpdated();
        }

        dismiss();
    }
}

