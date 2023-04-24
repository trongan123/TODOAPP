package com.example.todoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;

import androidx.viewpager2.widget.ViewPager2;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.todoapp.Adapter.TabItemAdapter;
import com.example.todoapp.databinding.FragmentMainBinding;
import com.example.todoapp.model.TodoItem;
import com.example.todoapp.viewmodel.TodoItemViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    private FragmentMainBinding fragmentMainBinding;
    private View mView;
    private TodoItemViewModel todoItemViewModel;



    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        fragmentMainBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                        .addSharedElement(fragmentMainBinding.btnAdd, "add_fragment")
                        .build();
                Navigation.findNavController(view).navigate(R.id.addItemFragment, null, null, extras);
            }
        });

        ViewPager2 viewPager2 = getView().findViewById(R.id.vpg);
        fragmentMainBinding.vpg.setAdapter(new TabItemAdapter(getActivity(), todoItemViewModel));
        TabLayout tabLayout = getView().findViewById(R.id.tlomenu);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("All");
                        break;
                    case 1:
                        tab.setText("Pending");
                        break;
                    case 2:
                        tab.setText("Completed");
                        break;
                }
            }
        }
        );
        tabLayoutMediator.attach();
        fragmentMainBinding.svSearch
                .getEditText()
                .setOnEditorActionListener(
                        (v, actionId, event) -> {
                            fragmentMainBinding.searchBar.setText(fragmentMainBinding.svSearch.getText());
                            fragmentMainBinding.svSearch.hide();
                            todoItemViewModel.getStringMutableLiveData().postValue(fragmentMainBinding.svSearch.getText().toString());
                            return false;
                        });

        todoItemViewModel.getListMutableLiveDataCheck().observe(requireActivity(), new Observer<List<Long>>() {
            @Override
            public void onChanged(List<Long> longs) {
                if (longs.size() <=0 ) {
                    fragmentMainBinding.btnclearall.setEnabled(false);
                } else {
                    fragmentMainBinding.btnclearall.setEnabled(true);
                }

            }
        });


        fragmentMainBinding.btnclearall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearItem();
            }
        });
    }


    private void clearItem() {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm Clear All")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        todoItemViewModel.clearItem();
                        Toast.makeText(getActivity(), "Clear successfully", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(getView()).navigate(R.id.mainFragment);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false);
        mView = fragmentMainBinding.getRoot();
        todoItemViewModel = new ViewModelProvider(this).get(TodoItemViewModel.class);

        fragmentMainBinding.setMainFragViewModel(todoItemViewModel);
        // Inflate the layout for this fragment
        return mView;
    }


}