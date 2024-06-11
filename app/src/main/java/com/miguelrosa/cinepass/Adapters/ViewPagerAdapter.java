package com.miguelrosa.cinepass.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.miguelrosa.cinepass.Activities.Fragments.MoviesFragment;
import com.miguelrosa.cinepass.Activities.Fragments.TvSeriesFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            default:
                return new MoviesFragment();
            case 1:
                return new TvSeriesFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
