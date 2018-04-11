package it.redlor.bakingapp.ui;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import it.redlor.bakingapp.R;
import it.redlor.bakingapp.databinding.FragmentSinglestepBinding;
import it.redlor.bakingapp.utils.ConnectivityUtils;

import static it.redlor.bakingapp.ui.DetailsActivity.mTwoPane;
import static it.redlor.bakingapp.utils.Constants.DESCRIPTION;
import static it.redlor.bakingapp.utils.Constants.IMAGE_URL;
import static it.redlor.bakingapp.utils.Constants.VIDEO_URL;


public class SingleStepFragment extends Fragment implements ExoPlayer.EventListener {



    FragmentSinglestepBinding fragmentSimpleStepBinding;

    SimpleExoPlayer simpleExoPlayer;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;

    public SingleStepFragment() {}

    public static  SingleStepFragment newInstance(String description, String videoUrl,
                                            String imageUrl) {
        Bundle arguments = new Bundle();
        arguments.putString(DESCRIPTION, description);
        arguments.putString(VIDEO_URL, videoUrl);
        arguments.putString(IMAGE_URL, imageUrl);
        SingleStepFragment singleStepFragment = new SingleStepFragment();
        singleStepFragment.setArguments(arguments);
        return  singleStepFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            fragmentSimpleStepBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_singlestep, container, false);
            return fragmentSimpleStepBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (ConnectivityUtils.internetAvailable(getContext())) {
            setOnlineUI();
            String description = getArguments().getString(DESCRIPTION);
            String videoUrl = getArguments().getString(VIDEO_URL);
            String imageUrl = getArguments().getString(IMAGE_URL);

            if (description != null && !description.isEmpty()) {
                fragmentSimpleStepBinding.stepDescription.setText(description);
            }
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.with(fragmentSimpleStepBinding.stepImage.getContext())
                        .load(imageUrl)
                        .into(fragmentSimpleStepBinding.stepImage);
                fragmentSimpleStepBinding.stepImage.setVisibility(View.VISIBLE);
            } else {
                fragmentSimpleStepBinding.stepImage.setVisibility(View.GONE);
            }

            int orientation = getResources().getConfiguration().orientation;
            if (videoUrl != null && !videoUrl.isEmpty()) {


                fragmentSimpleStepBinding.stepVideo.setVisibility(View.VISIBLE);
                initializeVideo();
                initializePlayer(Uri.parse(videoUrl));

                if (orientation == Configuration.ORIENTATION_LANDSCAPE && !mTwoPane) {
                    expandVideoView(fragmentSimpleStepBinding.stepVideo);
                    fragmentSimpleStepBinding.stepDescriptionCard.setVisibility(View.GONE);
                    hideSystemUI();
                }
            } else {
                fragmentSimpleStepBinding.stepVideo.setVisibility(View.GONE);
            }
        } else {
            setOfflineUI();
        }
    }

    private void setOnlineUI() {
        fragmentSimpleStepBinding.stepVideo.setVisibility(View.VISIBLE);
        fragmentSimpleStepBinding.stepDescription.setVisibility(View.VISIBLE);
        fragmentSimpleStepBinding.stepImage.setVisibility(View.VISIBLE);
        fragmentSimpleStepBinding.noInternetImage.setVisibility(View.GONE);
        fragmentSimpleStepBinding.noInternetText.setVisibility(View.GONE);
    }

    private void setOfflineUI() {
        fragmentSimpleStepBinding.stepVideo.setVisibility(View.GONE);
        fragmentSimpleStepBinding.stepDescription.setVisibility(View.GONE);
        fragmentSimpleStepBinding.stepImage.setVisibility(View.GONE);
        fragmentSimpleStepBinding.noInternetImage.setVisibility(View.VISIBLE);
        fragmentSimpleStepBinding.noInternetText.setVisibility(View.VISIBLE);
    }


    @Override
    public void onPause() {
        super.onPause();
        releaseVideoPlayer();
    }

    private void hideSystemUI() {


        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }


    private void expandVideoView(PlayerView exoPlayer) {
        exoPlayer.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        exoPlayer.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
    }

    // Initialize the Media Session for the Video
    private void initializeVideo() {
        mediaSession = new MediaSessionCompat(getContext(), SingleStepFragment.class.getSimpleName());
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                              MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setMediaButtonReceiver(null);
        stateBuilder = new PlaybackStateCompat.Builder().setActions(
                PlaybackStateCompat.ACTION_PLAY |
                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                simpleExoPlayer.setPlayWhenReady(true);
            }

            @Override
            public void onPause() {
                simpleExoPlayer.setPlayWhenReady(true);
            }

            @Override
            public void onSkipToPrevious() {
                simpleExoPlayer.seekTo(0);
            }
        });
        mediaSession.setActive(true);
    }

    // Initialize the player
    private void initializePlayer(Uri mediaUri) {
        if (simpleExoPlayer == null) {

            TrackSelector selector = new DefaultTrackSelector();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), selector);
            fragmentSimpleStepBinding.stepVideo.setPlayer(simpleExoPlayer);
            simpleExoPlayer.addListener(this);

            String agent = Util.getUserAgent(getContext(), "video");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), agent), new DefaultExtractorsFactory(), null, null);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releaseVideoPlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }

        if (mediaSession != null) {
            mediaSession.setActive(false);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if ((playbackState == Player.STATE_READY) && playWhenReady) {
                stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, simpleExoPlayer.getCurrentPosition(), 1f);
            } else if ((playbackState == Player.STATE_READY)) {
                stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, simpleExoPlayer.getCurrentPosition(), 1f);
        }
        mediaSession.setPlaybackState(stateBuilder.build());
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}
