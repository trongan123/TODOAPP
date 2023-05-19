package com.example.todoapp;


import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.transition.ChangeBounds;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.todoapp.databinding.FragmentAddItemBinding;
import com.example.todoapp.model.TodoItem;
import com.example.todoapp.viewmodel.TodoItemViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;


public class AddItemFragment extends Fragment {

    private static final String STRING_DATE_FORMAT = "yyyy-MM-dd";
    private final TodoItem todoItem = new TodoItem();
    private FragmentAddItemBinding fragmentAddItemBinding;
    private TodoItemViewModel todoItemViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setSharedElementEnterTransition(new ChangeBounds());
        fragmentAddItemBinding = FragmentAddItemBinding.inflate(inflater, container, false);
        View mView = fragmentAddItemBinding.getRoot();

        todoItemViewModel = new ViewModelProvider(this).get(TodoItemViewModel.class);
        fragmentAddItemBinding.setTodoItemViewModel(todoItemViewModel);

        fragmentAddItemBinding.edtCompletedDate.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
        fragmentAddItemBinding.edtCreatedDate.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);

        // Inflate the layout for this fragment
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialDatePicker<Long> datePickerCreated;
        datePickerCreated = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();

        String[] type = new String[]{"pending", "completed"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_menu_popup_item, R.id.txtStyle, type);
        fragmentAddItemBinding.dropDownStatus.setAdapter(adapter);

        fragmentAddItemBinding.btnAdd.setEnabled(false);
        fragmentAddItemBinding.btnAdd.setOnClickListener(view1 -> {
            try {
                addItem();
            } catch (ParseException ignored) {
                //empty
            }
        });
        fragmentAddItemBinding.btnClear.setOnClickListener(view2 -> clearText());

        fragmentAddItemBinding.edtCreatedDate.setOnClickListener(view3 ->
                addDatePicker(fragmentAddItemBinding.edtCreatedDate, datePickerCreated));

        fragmentAddItemBinding.edtCompletedDate.setOnClickListener(view4 ->
                addDatePicker(fragmentAddItemBinding.edtCompletedDate, datePickerCreated));
        fragmentAddItemBinding.edtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Method is empty
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Method is empty
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setErrorEditText(fragmentAddItemBinding.edtTitle, "Field title can't empty");
            }
        });
        fragmentAddItemBinding.edtDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Method is empty
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Method is empty
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setErrorEditText(fragmentAddItemBinding.edtDescription, "Field description can't empty");
            }
        });
        fragmentAddItemBinding.edtCreatedDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Method is empty
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Method is empty
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setErrorEditText(fragmentAddItemBinding.edtCreatedDate, "Field created date can't empty");
            }
        });
        fragmentAddItemBinding.edtCompletedDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Method is empty
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Method is empty
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setErrorEditText(fragmentAddItemBinding.edtCompletedDate, "Field completed date can't empty");
            }
        });
        fragmentAddItemBinding.dropDownStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Method is empty
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Method is empty
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (Objects.requireNonNull(fragmentAddItemBinding.dropDownStatus.getText()).toString().trim().isEmpty()) {
                    fragmentAddItemBinding.dropDownStatus.setError("Please choice a status");
                } else {
                    fragmentAddItemBinding.dropDownStatus.setError(null);
                }
                boolean check = checkValidate();
                fragmentAddItemBinding.btnAdd.setEnabled(check);
            }
        });
    }

    private void addItem() throws ParseException {
        if (validation()) {
            String stringTitle = Objects.requireNonNull(fragmentAddItemBinding.edtTitle.getText()).toString().trim();
            String strDescription = Objects.requireNonNull(fragmentAddItemBinding.edtDescription.getText()).toString().trim();
            Date createDate = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault()).parse(Objects.requireNonNull(fragmentAddItemBinding.edtCreatedDate.getText()).toString().trim());
            Date completeDate = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault()).parse(Objects.requireNonNull(fragmentAddItemBinding.edtCompletedDate.getText()).toString().trim());
            String strStatus = fragmentAddItemBinding.dropDownStatus.getText().toString().trim();

            //update database
            todoItem.setTitle(stringTitle);
            todoItem.setDescription(strDescription);
            todoItem.setCreatedDate(createDate);
            todoItem.setCompletedDate(completeDate);
            todoItem.setStatus(strStatus);

            todoItemViewModel.addItem(todoItem);
            Toast.makeText(getActivity(), "Add success", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).navigate(R.id.mainFragment);
        }
    }

    //create date picker
    private void addDatePicker(TextInputEditText textDate, MaterialDatePicker<Long> datePickerCreated) {
        if (datePickerCreated.isAdded()) {
            return;
        }
        datePickerCreated.show(getParentFragmentManager(), "Material_Date_Picker");
        datePickerCreated.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            SimpleDateFormat format = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault());
            String formattedDate = format.format(calendar.getTime());
            textDate.setText(formattedDate);
        });
    }

    private void clearText() {
        fragmentAddItemBinding.edtTitle.setText("");
        fragmentAddItemBinding.edtTitle.setError(null);
        fragmentAddItemBinding.edtDescription.setText("");
        fragmentAddItemBinding.edtDescription.setError(null);
        fragmentAddItemBinding.edtCreatedDate.setText("");
        fragmentAddItemBinding.edtCreatedDate.setError(null);
        fragmentAddItemBinding.edtCompletedDate.setText("");
        fragmentAddItemBinding.edtCompletedDate.setError(null);
        fragmentAddItemBinding.dropDownStatus.setText("", false);
        fragmentAddItemBinding.dropDownStatus.setError(null);
    }

    private void setErrorEditText(TextInputEditText textEdit, String errorMessage) {
        if (Objects.requireNonNull(textEdit.getText()).toString().trim().isEmpty()) {
            textEdit.setError(errorMessage);
        } else {
            textEdit.setError(null);
        }
        boolean check = checkValidate();
        fragmentAddItemBinding.btnAdd.setEnabled(check);
    }

    private boolean checkValidate() {
        boolean check = !Objects.requireNonNull(fragmentAddItemBinding.edtTitle.getText()).toString().trim().isEmpty();

        if (Objects.requireNonNull(fragmentAddItemBinding.edtDescription.getText()).toString().trim().isEmpty()) {
            check = false;
        }
        if (Objects.requireNonNull(fragmentAddItemBinding.edtCreatedDate.getText()).toString().trim().isEmpty()) {
            check = false;
        }
        if (Objects.requireNonNull(fragmentAddItemBinding.edtCompletedDate.getText()).toString().trim().isEmpty()) {
            check = false;
        }
        if (Objects.requireNonNull(fragmentAddItemBinding.dropDownStatus.getText()).toString().trim().isEmpty()) {
            check = false;
        }
        return check;
    }

    private boolean validation() throws ParseException {

        boolean check = true;
        if (Objects.requireNonNull(fragmentAddItemBinding.edtTitle.getText()).toString().trim().isEmpty()) {
            fragmentAddItemBinding.edtTitle.setError("Field title can't empty");
            check = false;
        }
        if (Objects.requireNonNull(fragmentAddItemBinding.edtDescription.getText()).toString().trim().isEmpty()) {
            fragmentAddItemBinding.edtDescription.setError("Field description can't empty");
            check = false;
        }
        if (Objects.requireNonNull(fragmentAddItemBinding.edtCreatedDate.getText()).toString().trim().isEmpty()) {
            fragmentAddItemBinding.edtCreatedDate.setError("Field created date can't empty");
            check = false;
        }
        if (Objects.requireNonNull(fragmentAddItemBinding.edtCompletedDate.getText()).toString().trim().isEmpty()) {
            fragmentAddItemBinding.edtCompletedDate.setError("Field completed date can't empty");
            check = false;
        }
        if (fragmentAddItemBinding.dropDownStatus.getText().toString().trim().isEmpty()) {
            fragmentAddItemBinding.dropDownStatus.setError("Please choice a status");
            check = false;
        }
        if (!check) {
            return false;
        }
        Date createdDate = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault())
                .parse(fragmentAddItemBinding.edtCreatedDate.getText().toString().trim());
        Date completedDate = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault())
                .parse(fragmentAddItemBinding.edtCompletedDate.getText().toString().trim());
        assert createdDate != null;
        if (createdDate.compareTo(completedDate) > 0) {
            fragmentAddItemBinding.edtCompletedDate.setError("Completed date must be after created date");
            check = false;
        }
        return check;
    }
}