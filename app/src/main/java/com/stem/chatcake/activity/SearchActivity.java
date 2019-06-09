package com.stem.chatcake.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.stem.chatcake.R;
import com.stem.chatcake.adapter.SearchResultsAdapter;
import com.stem.chatcake.databinding.ActivitySearchBinding;
import com.stem.chatcake.model.User;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.LocalStorageService;
import com.stem.chatcake.service.StateService;
import com.stem.chatcake.viewmodel.SearchViewModel;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private SearchViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        View parent = binding.getRoot();

        ListView resultsListView = parent.findViewById(R.id.search_results_list_view);

        HttpService httpService = HttpService.getInstance();
        LocalStorageService localStorageService = LocalStorageService.getInstance(this);
        StateService stateService = StateService.getInstance();

        // results adapter construction
        SearchResultsAdapter resultsAdapter = new SearchResultsAdapter(this, 0, new ArrayList<User>())
                .setHttpService(httpService)
                .setLocalStorageService(localStorageService)
                .setStateService(stateService);

        // dependency injection
        viewModel = SearchViewModel.builder()
                .context(this)
                .httpService(httpService)
                .localStorageService(localStorageService)
                .stateService(stateService)
                .resultsListView(resultsListView)
                .resultsAdapter(resultsAdapter)
                .build();

        binding.setViewModel(viewModel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.init();
    }
}
