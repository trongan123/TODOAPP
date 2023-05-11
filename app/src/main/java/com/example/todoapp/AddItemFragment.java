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
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;


public class AddItemFragment extends Fragment {

    private FragmentAddItemBinding fragmentAddItemBinding;
    private TodoItemViewModel todoItemViewModel;
    private MaterialDatePicker<Long> datePickerCompleted;
    private MaterialDatePicker<Long> datePickerCreated;
    private final TodoItem todoItem = new TodoItem();

    public AddItemFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        String[] type = new String[]{"pending", "completed"};
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(),
                        R.layout.dropdown_menu_popup_item, R.id.txtstyle,
                        type);
        fragmentAddItemBinding.dropdownstatus.setAdapter(adapter);
        if (datePickerCreated.isAdded()) {
            return;
        }
        fragmentAddItemBinding.btnAdd.setEnabled(false);
        fragmentAddItemBinding.btnAdd.setOnClickListener(view1 -> {
            try {
                addItem();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        //and action for button clear
        fragmentAddItemBinding.btnclear.setOnClickListener(view12 -> {
            fragmentAddItemBinding.edttitle.setText("");
            fragmentAddItemBinding.edtdescription.setText("");
            fragmentAddItemBinding.edtcreatedDate.setText("");
            fragmentAddItemBinding.edtcompletedDate.setText("");
            fragmentAddItemBinding.dropdownstatus.setText("", false);
        });

        fragmentAddItemBinding.edtcreatedDate.setOnClickListener(view13 -> {
            if (datePickerCreated.isAdded()) {
                return;
            }
            datePickerCreated.show(getParentFragmentManager(), "Material_Date_Picker");
            datePickerCreated.addOnPositiveButtonClickListener(selection -> {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis(selection);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String formattedDate = format.format(calendar.getTime());
                fragmentAddItemBinding.edtcreatedDate.setText(formattedDate);
            });
        });

        //create datePicker
        fragmentAddItemBinding.edtcompletedDate.setOnClickListener(view14 -> {
            if (datePickerCompleted.isAdded()) {
                return;
            }
            datePickerCompleted.show(getParentFragmentManager(), "Material_Date_Picker");
            datePickerCompleted.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                @Override
                public void onPositiveButtonClick(Object selection) {
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    calendar.setTimeInMillis((Long) selection);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String formattedDate = format.format(calendar.getTime());
                    fragmentAddItemBinding.edtcompletedDate.setText(formattedDate);
                }
            });
        });
        fragmentAddItemBinding.edttitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (Objects.requireNonNull(fragmentAddItemBinding.edttitle.getText()).toString().trim().isEmpty()) {
                    fragmentAddItemBinding.tiltitle.setError("Field title can't empty");
                } else {
                    fragmentAddItemBinding.tiltitle.setError(null);
                    fragmentAddItemBinding.btnAdd.setEnabled(true);
                }
            }
        });
    }
    //method add new todoitem
    private void addItem() throws ParseException {
        if (validation()) {
            String strtitle = Objects.requireNonNull(fragmentAddItemBinding.edttitle.getText()).toString().trim();
            String strDes = Objects.requireNonNull(fragmentAddItemBinding.edtdescription.getText()).toString().trim();
            Date credate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .parse(Objects.requireNonNull(fragmentAddItemBinding.edtcreatedDate.getText()).toString().trim());
            Date comdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .parse(Objects.requireNonNull(fragmentAddItemBinding.edtcompletedDate.getText()).toString().trim());
            String strStt = fragmentAddItemBinding.dropdownstatus.getText().toString().trim();

            //update database
            todoItem.setTitle(strtitle);
            todoItem.setDescription(strDes);
            todoItem.setCreatedDate(credate);
            todoItem.setCompletedDate(comdate);
            todoItem.setStatus(strStt);

            todoItemViewModel.addItem(todoItem);
            Toast.makeText(getActivity(), "Add success", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(getView()).navigate(R.id.mainFragment);
        }
    }

    //method validate value to add new item
    private boolean validation() throws ParseException {
        boolean check = true;

        if (Objects.requireNonNull(fragmentAddItemBinding.edttitle.getText()).toString().trim().isEmpty()) {
            fragmentAddItemBinding.tiltitle.setError("Field title can't empty");
            check = false;
        }
        if (Objects.requireNonNull(fragmentAddItemBinding.edtdescription.getText()).toString().trim().isEmpty()) {
            fragmentAddItemBinding.tildescription.setError("Field description can't empty");
            check = false;
        }
        if (Objects.requireNonNull(fragmentAddItemBinding.edtcreatedDate.getText()).toString().trim().isEmpty()) {
            fragmentAddItemBinding.tilcreatedDate.setError("Field created date can't empty");
            check = false;
        }
        if (Objects.requireNonNull(fragmentAddItemBinding.edtcompletedDate.getText()).toString().trim().isEmpty()) {
            fragmentAddItemBinding.tilcompletedDate.setError("Field completed date can't empty");
            check = false;
        }
        if (Objects.requireNonNull(fragmentAddItemBinding.dropdownstatus.getText()).toString().trim().isEmpty()) {
            fragmentAddItemBinding.tilstatus.setError("Please choice a status");
            check = false;
        }
        if (!check) {
            return false;
        }

        Date credate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .parse(fragmentAddItemBinding.edtcreatedDate.getText().toString().trim());
        Date comdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .parse(fragmentAddItemBinding.edtcompletedDate.getText().toString().trim());
        assert credate != null;
        if (credate.compareTo(comdate) > 0) {
            fragmentAddItemBinding.tilcompletedDate.setError("Completed date must be after created date");
            check = false;
        }
        return check;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //set navigation share element for add fragment
        setSharedElementEnterTransition(new ChangeBounds());

        //set data binding
        fragmentAddItemBinding = FragmentAddItemBinding.inflate(inflater, container, false);
        View mView = fragmentAddItemBinding.getRoot();

        //set view model
        todoItemViewModel = new ViewModelProvider(this).get(TodoItemViewModel.class);
        fragmentAddItemBinding.setTodoItemViewModel(todoItemViewModel);

        datePickerCreated = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date").setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        datePickerCompleted = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date").setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        fragmentAddItemBinding.edtcompletedDate.setInputType(InputType.TYPE_CLASS_DATETIME
                | InputType.TYPE_DATETIME_VARIATION_DATE);
        fragmentAddItemBinding.edtcreatedDate.setInputType(InputType.TYPE_CLASS_DATETIME
                | InputType.TYPE_DATETIME_VARIATION_DATE);

        // Inflate the layout for this fragment
        return mView;
    }
}