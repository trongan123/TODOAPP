package com.example.todoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.viewpager2.widget.ViewPager2;

import com.example.todoapp.Adapter.TabItemAdapter;
import com.example.todoapp.databinding.FragmentMainBinding;
import com.example.todoapp.viewmodel.TodoItemViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class MainFragment extends Fragment {
    private FragmentMainBinding fragmentMainBinding;
    private TodoItemViewModel todoItemViewModel;
    public MainFragment() {
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //set shared element back for recylerview
        postponeEnterTransition();
        final ViewGroup parentView = (ViewGroup) view.getParent();
        parentView.getViewTreeObserver()
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        parentView.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
        super.onViewCreated(view, savedInstanceState);


        fragmentMainBinding.btnAdd.setOnClickListener(view1 -> {
            FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                    .addSharedElement(fragmentMainBinding.btnAdd, "add_fragment")
                    .build();
            Navigation.findNavController(view1).navigate(R.id.addItemFragment, null, null, extras);
        });

        ViewPager2 viewPager2 = requireView().findViewById(R.id.vpg);
        fragmentMainBinding.vpg.setAdapter(new TabItemAdapter(requireActivity(), todoItemViewModel));
        TabLayout tabLayout = requireView().findViewById(R.id.tlomenu);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager2, (tab, position) -> {
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
        );

        tabLayoutMediator.attach();
        fragmentMainBinding.svSearch
                .getEditText()
                .setOnEditorActionListener(
                        (v, actionId, event) -> {
                            fragmentMainBinding.searchBar.setText(fragmentMainBinding.svSearch.getText());
                            fragmentMainBinding.svSearch.hide();
                            todoItemViewModel.getStringMutableLiveData().postValue(Objects.requireNonNull(fragmentMainBinding.svSearch.getText()).toString());
                            return false;
                        });

        todoItemViewModel.getListMutableLiveDataCheck().observe(requireActivity(), longs -> fragmentMainBinding.btnclearall.setEnabled(longs.size() > 0));
        fragmentMainBinding.btnclearall.setOnClickListener(view12 -> clearItem());
    }


    private void clearItem() {
        new MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_App_MaterialAlertDialog)
                .setTitle("Confirm Clear All")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    todoItemViewModel.clearItem();
                    Toast.makeText(getActivity(), "Clear successfully", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false);
        View mView = fragmentMainBinding.getRoot();
        todoItemViewModel = new ViewModelProvider(this).get(TodoItemViewModel.class);

        fragmentMainBinding.setMainFragViewModel(todoItemViewModel);
        // Inflate the layout for this fragment
        return mView;
    }


}