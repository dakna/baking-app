package app.knapp.udacity.bakingapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import app.knapp.udacity.bakingapp.R;
import app.knapp.udacity.bakingapp.model.Step;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;


public class RecipeStepFragment extends Fragment {

    private static final String TAG = "RecipeFragment";
    private static final String KEY_POSITION = "current-position";
    private SharedViewModel viewModel;

    private static final String POSITION_KEY = "pos_k";
    private static final String PLAY_WHEN_READY_KEY = "play_when_ready_k";

    @Nullable
    @BindView(R.id.instructions_container)
    NestedScrollView instructionsContainer;

    @BindView(R.id.simple_exo_player_view)
    PlayerView playerView;

    @Nullable
    @BindView(R.id.iv_step_thumbnail)
    ImageView ivThumbnail;

    @Nullable
    @BindView(R.id.tv_description)
    TextView tvDescription;

    @Nullable
    @BindView(R.id.btnNext)
    Button btnNext;

    @Nullable
    @BindView(R.id.btnPrevious)
    Button btnPrevious;

    private SimpleExoPlayer simpleExoPlayer;
    private Step step;

    private String videoURL;
    private Uri videoURI;
    private BandwidthMeter bandwidthMeter;
    private Handler handler;
    private long currentPosition = 0;
    private boolean playWhenReady = true;

    public static RecipeStepFragment newInstance() {
        return new RecipeStepFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_step_fragment, container, false);
        ButterKnife.bind(this, view);
        if(savedInstanceState != null) {
            currentPosition = savedInstanceState.getLong(KEY_POSITION);
            Log.d(TAG, "onCreateView: currentPosition" + currentPosition);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        Log.d(TAG, "onActivityCreated: selected step index " + viewModel.getSelectedStepIndex().getValue());
        //step = viewModel.getSelectedRecipe().getValue().getSteps().get(viewModel.getSelectedStepIndex().getValue());
        viewModel.getSelectedStepIndex().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                step = viewModel.getSelectedRecipe().getValue().getSteps().get(viewModel.getSelectedStepIndex().getValue());
                setupView();
            }
        });
        getActivity().setTitle(viewModel.getSelectedRecipe().getValue().getName());



    }

    public void setupView() {

        View rootView = getView();
        tvDescription.setText(step.getDescription());

        if(viewModel.getSelectedStepIndex().getValue() == 0) {
            btnPrevious.setEnabled(false);
        } else {
            btnPrevious.setEnabled(true);
        }

        if(viewModel.getSelectedStepIndex().getValue() == viewModel.getSelectedRecipe().getValue().getSteps().size() - 1) {
            btnNext.setEnabled(false);
        } else {
            btnNext.setEnabled(true);
        }

        if (!step.getThumbnailURL().isEmpty()) {
            Picasso.get()
                    .load(step.getThumbnailURL())
                    .placeholder(R.drawable.ic_cake)
                    .into(ivThumbnail);
        }

        //playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        videoURL = step.getVideoURL();
        Log.d(TAG, "setupView: step " + step.getId() + " videoUrl " + videoURL);

        if (!videoURL.isEmpty()) {
            videoURI = Uri.parse(step.getVideoURL());
            Log.d(TAG, "setupView: step " + step.getId() + " videoUrI " + videoURI);
            playerView.setVisibility(View.VISIBLE);
            initializeVideoPlayer(videoURI);

/*            if (rootView.findViewWithTag("sw600dp-port-recipe_step_detail") != null && getActivity() != null) {
                //getActivity().findViewById(R.id.fragment_container).setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
                //playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            } else if (isLandscapeMode(getContext())) {

            }*/
        } else {
            simpleExoPlayer = null;
            playerView.setVisibility(View.INVISIBLE);
/*            if (getContext() != null) {
                //playerView.setForeground(ContextCompat.getDrawable(getContext(), R.drawable.ic_no_video));
                if (!isLandscapeMode(getContext())) {
                    if(rootView.findViewWithTag("sw600dp-port-recipe_step_detail") != null) {
                        playerView.setLayoutParams(new FrameLayout.LayoutParams(1400, 700));
                    }
                    if (rootView.findViewWithTag("sw600dp-land-recipe_step_detail") != null) {
                        playerView.setLayoutParams(new LinearLayout.LayoutParams(1400, 1400));
                    }

                    if(rootView.findViewWithTag("land-recipe_step_detail") != null) {
                        playerView.setLayoutParams(new LinearLayout.LayoutParams(600, 600));
                    }
                } else {
                    playerView.setLayoutParams(new LinearLayout.LayoutParams(900, 500));
                }
            }
            else {
                playerView.setLayoutParams(new LinearLayout.LayoutParams(1200, 600));
            }*/
        }
    }




    private void initializeVideoPlayer(Uri videoUri) {

        handler = new Handler(Looper.getMainLooper());

        if ((simpleExoPlayer == null)  && (getContext() != null)) {

            TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(trackSelectionFactory);

            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            playerView.setPlayer(simpleExoPlayer);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), "baking-app"));
            if(videoUri != null) {
                Log.d(TAG, "initializeVideoPlayer: videoUri " + videoUri);
                MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(videoUri);
                if (currentPosition != C.TIME_UNSET) simpleExoPlayer.seekTo(currentPosition);
                simpleExoPlayer.prepare(videoSource);
                simpleExoPlayer.setPlayWhenReady(true);
                playerView.setVisibility(View.VISIBLE);
            }


        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        super.onSaveInstanceState(currentState);
        Log.d(TAG, "onSaveInstanceState: current position if available");
        if(simpleExoPlayer != null) {
            currentPosition = simpleExoPlayer.getCurrentPosition();
            Log.d(TAG, "onSaveInstanceState: currentPosition" + currentPosition);
            currentState.putLong(KEY_POSITION, currentPosition);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        initializeVideoPlayer(videoURI);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(simpleExoPlayer != null && videoURI != null) {
            initializeVideoPlayer(videoURI);
            simpleExoPlayer.seekTo(currentPosition);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayerFinally();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }


    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }


    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            playWhenReady = simpleExoPlayer.getPlayWhenReady();
            currentPosition = simpleExoPlayer.getCurrentPosition();

            simpleExoPlayer.stop();
            simpleExoPlayer.release();
        }
    }

    private void releasePlayerFinally() {
        if (simpleExoPlayer != null) {
            playWhenReady = simpleExoPlayer.getPlayWhenReady();
            currentPosition = simpleExoPlayer.getCurrentPosition();

            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    @Optional
    @OnClick(R.id.btnPrevious)
    public void selectPreviousStep() {
        Log.d(TAG, "selectPreviousStep: ");
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
        }
        viewModel.selectStepIndex(viewModel.getSelectedStepIndex().getValue() - 1);
    }

    @Optional
    @OnClick(R.id.btnNext)
    public void selectNextStep() {
        Log.d(TAG, "selectNextStep: ");
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
        }
        viewModel.selectStepIndex(viewModel.getSelectedStepIndex().getValue() + 1);
    }

/*    public boolean isLandscapeMode( Context context ) {
        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }*/

}
