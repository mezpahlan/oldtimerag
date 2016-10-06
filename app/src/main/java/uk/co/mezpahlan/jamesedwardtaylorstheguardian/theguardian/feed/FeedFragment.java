package uk.co.mezpahlan.jamesedwardtaylorstheguardian.theguardian.feed;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import uk.co.mezpahlan.jamesedwardtaylorstheguardian.R;
import uk.co.mezpahlan.jamesedwardtaylorstheguardian.base.StateMaintainer;
import uk.co.mezpahlan.jamesedwardtaylorstheguardian.data.model.Result;

/**
 *  Fragment for TheGuardian.Feed. Part of the View Layer.
 */
public class FeedFragment extends Fragment implements FeedMvp.View {

    private static final String TAG = "FeedFragment";

    private StateMaintainer stateMaintainer;
    private FeedRecyclerViewAdapter listAdapter;
    private FeedMvp.Presenter presenter;

    private View loadingView;
    private View contentView;

    public static FeedFragment newInstance() {
        return new FeedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_theguardian_feed, container, false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);

        listAdapter = new FeedRecyclerViewAdapter();
        listAdapter.setClickListener(resultClickListener);
        recyclerView.setAdapter(listAdapter);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Pull-to-refresh
        SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.content_view);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.load(true);
            }
        });

        return root;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingView = view.findViewById(R.id.loadingView);
        contentView = view.findViewById(R.id.content_view);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupStateMaintainer();
        checkForRetainedState();
    }

    private void setupStateMaintainer() {
        if (stateMaintainer == null) {
            stateMaintainer = new StateMaintainer(getActivity().getFragmentManager(), TAG);
        }
    }

    private void checkForRetainedState() {
        try {
            if (stateMaintainer.isFirstTimeIn()) {
                initialise(this);
            } else {
                reinitialise(this);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void initialise(FeedMvp.View view) throws InstantiationException, IllegalAccessException{
        presenter = new FeedPresenter(view);
        stateMaintainer.put(FeedMvp.Presenter.class.getSimpleName(), presenter);
        presenter.load(false);
    }

    private void reinitialise(FeedMvp.View view) throws InstantiationException, IllegalAccessException {
        presenter = stateMaintainer.get(FeedMvp.Presenter.class.getSimpleName());

        if (presenter == null) {
            // If we can't find a presenter assume that its not there and initialise it again.
            initialise(view);
        } else {
            // Otherwise tell it that the configuration has changed
            presenter.onConfigurationChanged(view);
        }
    }

    @Override
    public void showLoading(boolean active) {
        loadingView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showContent() {
        contentView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void showError() {

    }

    @Override
    public void updateContent(List<Result> resultList) {
        listAdapter.updateItems(resultList);
        listAdapter.notifyDataSetChanged();
    }

    /**
     * Listener for clicks on items in the RecyclerView.
     */
    ResultClickListener resultClickListener = new ResultClickListener() {
        @Override
        public void onResultClick(int result) {
            presenter.onSelectResult(result);
        }
    };

    @Override
    public void showGuardianArticle(int position) {
        // TODO: Complete this after you've set up the Article component. For now just show a Toast.
        Toast.makeText(getActivity(), "Moving to new activity to show an article", Toast.LENGTH_SHORT).show();

//        // Naive implementation using an intent to move between activities
//        Intent intent = new Intent(getActivity(), ArticleActivity.class);
//        intent.putExtra(ArticleActivity.EXTRA_ARTICLE_TITLE, guardianArticleTitle);
//        intent.putExtra(ArticleActivity.EXTRA_ARTICLE_URL, guardianArticleUrl);
//        startActivity(intent);
    }

    public interface ResultClickListener {
        void onResultClick(int result);
    }
}