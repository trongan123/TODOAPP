package com.example.todoapp.bottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.adapter.TodoItemBottomSheetAdapter;
import com.example.todoapp.databinding.FragmentAllItemBinding;
import com.example.todoapp.databinding.UpdateBottomSheetLayoutBinding;
import com.example.todoapp.model.TodoItem;
import com.example.todoapp.viewmodel.TodoItemViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;


public class AllItemBottomSheetFragment extends Fragment {

    private static final String STRING_DATE_FORMAT = "yyyy-MM-dd";
    private final int tabNumber;
    private final TodoItemViewModel todoItemViewModel;
    MaterialAlertDialogBuilder materialAlertDialogBuilder;
    private FragmentAllItemBinding fragmentAllItemBinding;
    private BottomSheetDialog bottomSheetDialog;
    private UpdateBottomSheetLayoutBinding updateBottomSheetLayoutBinding;

    public AllItemBottomSheetFragment(TodoItemViewModel todoItemViewModel, int tabNumber) {
        this.todoItemViewModel = todoItemViewModel;
        this.tabNumber = tabNumber;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentAllItemBinding = FragmentAllItemBinding.inflate(inflater, container, false);
        updateBottomSheetLayoutBinding = UpdateBottomSheetLayoutBinding.inflate(inflater, container, false);
        View mView = fragmentAllItemBinding.getRoot();
        //      fragmentAllItemBinding.setAllItemViewModel(todoItemViewModel);
        // Inflate the layout for this fragment
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayListTodo();
        postponeEnterTransition();
    }

    public void displayListTodo() {
        TodoItemBottomSheetAdapter todoItemAdapter;
        RecyclerView rcvItem = fragmentAllItemBinding.rcvTodoItem;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvItem.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        rcvItem.addItemDecoration(dividerItemDecoration);
        todoItemAdapter = new TodoItemBottomSheetAdapter(new TodoItemBottomSheetAdapter.TodoItemDiff(), todoItemViewModel);
        todoItemAdapter.setHasStableIds(true);

        //set data to recyclerview
        todoItemViewModel.getStringMutableLiveData().observe(requireActivity(), s -> {
            switch (tabNumber) {
                case 1:
                    //set data for tab all item
                    todoItemViewModel.getAllList().observe(requireActivity(), todoItemAdapter::submitList);
                    break;
                case 2:
                    //set data for tab pending item
                    todoItemViewModel.getPendingList().observe(requireActivity(), todoItemAdapter::submitList);
                    break;
                default:
                    //set data for tab completed item
                    todoItemViewModel.getCompletedList().observe(requireActivity(), todoItemAdapter::submitList);
                    break;
            }
        });

        todoItemAdapter.setClickListener(new TodoItemBottomSheetAdapter.IClickItemToDo() {
            @Override
            public void detailItem(TodoItem todoItem) {
                clickDetailItem(todoItem);
            }

            @Override
            public void clearItem(TodoItem todoItem, long id, boolean check) {
                todoItemViewModel.setClearAll(todoItem.getId(), check);
                todoItemViewModel.setCheckItem(id, check);
            }
        });
        rcvItem.setAdapter(todoItemAdapter);
    }

    //method create new bottom sheet
    private void setBottomSheetDialog() {
        bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(updateBottomSheetLayoutBinding.getRoot());
        String[] type = new String[]{"pending", "completed"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_menu_popup_item, R.id.txtStyle, type);
        updateBottomSheetLayoutBinding.dropDownStatus.setAdapter(adapter);

        MaterialDatePicker<Long> datePickerCreated;
        datePickerCreated = MaterialDatePicker.Builder.datePicker().setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();

        updateBottomSheetLayoutBinding.edtCreatedDate.setOnClickListener(view ->
                addDatePicker(updateBottomSheetLayoutBinding.edtCreatedDate, datePickerCreated));

        updateBottomSheetLayoutBinding.edtCompletedDate.setOnClickListener(view ->
                addDatePicker(updateBottomSheetLayoutBinding.edtCompletedDate, datePickerCreated));
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

    private void clickDetailItem(TodoItem todoItem) {
        if (bottomSheetDialog == null) {
            //create new bottom sheet if bottom sheet not exist
            setBottomSheetDialog();
        }
        updateBottomSheetLayoutBinding.btnAdd.setText(R.string.update);
        updateBottomSheetLayoutBinding.btnAdd.setOnClickListener(view1 -> {
            try {
                updateItemTodo(todoItem);
            } catch (ParseException e) {
                //Method empty
            }
        });
        updateBottomSheetLayoutBinding.btnClear.setText(R.string.delete);
        updateBottomSheetLayoutBinding.btnClear.setOnClickListener(view12 -> {
            try {
                deleteItemTodo(todoItem);
            } catch (ParseException e) {
                //Method empty
            }
        });
        initUi(todoItem);
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.show();
    }

    private void initUi(TodoItem item) {
        DateFormat dateFormat = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault());
        if (item != null) {
            updateBottomSheetLayoutBinding.edtTitle.setText(item.getTitle());
            updateBottomSheetLayoutBinding.edtDescription.setText(item.getDescription());
            updateBottomSheetLayoutBinding.edtCreatedDate.setText(dateFormat.format(item.getCreatedDate()));
            updateBottomSheetLayoutBinding.edtCompletedDate.setText(dateFormat.format(item.getCompletedDate()));
            updateBottomSheetLayoutBinding.dropDownStatus.setText(item.getStatus(), false);
        }
    }

    private void deleteItemTodo(TodoItem todoItem) throws ParseException {
        String stringTitle = Objects.requireNonNull(updateBottomSheetLayoutBinding.edtTitle.getText()).toString().trim();
        String stringDescription = Objects.requireNonNull(updateBottomSheetLayoutBinding.edtDescription.getText()).toString().trim();
        Date createdDate = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault())
                .parse(Objects.requireNonNull(updateBottomSheetLayoutBinding.edtCreatedDate.getText()).toString().trim());
        Date completedDate = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault())
                .parse(Objects.requireNonNull(updateBottomSheetLayoutBinding.edtCompletedDate.getText()).toString().trim());
        String stringStatus = updateBottomSheetLayoutBinding.dropDownStatus.getText().toString().trim();

        //update database
        todoItem.setTitle(stringTitle);
        todoItem.setDescription(stringDescription);
        todoItem.setCreatedDate(createdDate);
        todoItem.setCompletedDate(completedDate);
        todoItem.setStatus(stringStatus);

        if (materialAlertDialogBuilder == null) {
            materialAlertDialogBuilder = new MaterialAlertDialogBuilder(requireContext(),
                    R.style.ThemeOverlay_App_MaterialAlertDialog)
                    .setTitle("Confirm delete").setMessage("Are you sure?")
                    .setNegativeButton("No", null);
        }

        materialAlertDialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> {
            todoItemViewModel.deleteItem(todoItem);
            Toast.makeText(getActivity(), "Delete successfully", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.cancel();
        }).show();
    }

    private void updateItemTodo(TodoItem todoItem) throws ParseException {
        if (validation()) {
            String stringTitle = Objects.requireNonNull(updateBottomSheetLayoutBinding.edtTitle.getText()).toString().trim();
            String stringDescription = Objects.requireNonNull(updateBottomSheetLayoutBinding
                    .edtDescription.getText()).toString().trim();
            Date createdDate = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault())
                    .parse(Objects.requireNonNull(updateBottomSheetLayoutBinding.edtCreatedDate.getText()).toString().trim());
            Date completedDate = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault())
                    .parse(Objects.requireNonNull(updateBottomSheetLayoutBinding.edtCompletedDate.getText()).toString().trim());
            String stringStatus = updateBottomSheetLayoutBinding.dropDownStatus.getText().toString().trim();

            //update database
            todoItem.setTitle(stringTitle);
            todoItem.setDescription(stringDescription);
            todoItem.setCreatedDate(createdDate);
            todoItem.setCompletedDate(completedDate);
            todoItem.setStatus(stringStatus);

            todoItemViewModel.updateItem(todoItem);
            Toast.makeText(getActivity(), "Update successfully", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.cancel();
        }
    }

    private boolean validation() throws ParseException {
        boolean check = true;
        if (Objects.requireNonNull(updateBottomSheetLayoutBinding.edtTitle.getText()).toString().trim().isEmpty()) {
            updateBottomSheetLayoutBinding.edtTitle.setError("Field title can't empty");
            check = false;
        }
        if (Objects.requireNonNull(updateBottomSheetLayoutBinding.edtDescription.getText()).toString().trim().isEmpty()) {
            updateBottomSheetLayoutBinding.edtDescription.setError("Field description can't empty");
            check = false;
        }
        if (Objects.requireNonNull(updateBottomSheetLayoutBinding.edtCreatedDate.getText()).toString().trim().isEmpty()) {
            updateBottomSheetLayoutBinding.edtCreatedDate.setError("Field created date can't empty");
            check = false;
        }
        if (Objects.requireNonNull(updateBottomSheetLayoutBinding.edtCompletedDate.getText()).toString().trim().isEmpty()) {
            updateBottomSheetLayoutBinding.edtCompletedDate.setError("Field completed date can't empty");
            check = false;
        }
        if (updateBottomSheetLayoutBinding.dropDownStatus.getText().toString().trim().isEmpty()) {
            updateBottomSheetLayoutBinding.dropDownStatus.setError("Please choice a status");
            check = false;
        }
        if (!check) {
            return false;
        }
        Date createdDate = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault())
                .parse(updateBottomSheetLayoutBinding.edtCreatedDate.getText().toString().trim());
        Date completedDate = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault())
                .parse(updateBottomSheetLayoutBinding.edtCompletedDate.getText().toString().trim());
        assert createdDate != null;
        if (createdDate.compareTo(completedDate) > 0) {
            updateBottomSheetLayoutBinding.edtCompletedDate.setError("Completed date must be after created date");
            check = false;
        }
        return check;
    }
}