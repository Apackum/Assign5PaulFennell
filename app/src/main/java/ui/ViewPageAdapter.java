package ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.sdafinalass.AchievementListFragment;
import com.example.sdafinalass.CurrentGamesList;
import com.example.sdafinalass.RssFeedFragment;
import com.example.sdafinalass.WelcomeFragment;

public class ViewPageAdapter extends FragmentPagerAdapter {

    private Context context;

    /**
     *
     * @param fm
     * @param behavior
     * @param nContext
     */
    public ViewPageAdapter(FragmentManager fm, int behavior, Context nContext) {
        super(fm, behavior);
        context = nContext;
    }

    /**
     * sets the fragments position
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = new Fragment();

        //finds the tab position (note array starts at 0)
        position = position+1;

        //finds the fragment
        switch (position)
        {
            case 1:
                //code
                fragment = new WelcomeFragment();
                break;
            case 2:
                //code
                fragment = new CurrentGamesList();
                break;
            case 3:
                //code
                fragment = new RssFeedFragment();
                break;
            case 4:
                //code
                fragment = new AchievementListFragment();
                break;



        }

        return fragment;
    }

    /**
     * gets count
     * @return
     */
    @Override
    public int getCount() {
        return 4;
    }

    /**
     * adds a page title the pager and its position
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        position = position+1;

        CharSequence tabTitle = "";

        //finds the fragment
        switch (position)
        {
            case 1:
                //code
                tabTitle = "Welcome";
                break;
            case 2:
                //code
                tabTitle = "Games";
                break;
            case 3:
                //code
                tabTitle = "News";
                break;
            case 4:
                //code
                tabTitle = "Achievement";
                break;
        }

        return tabTitle;
    }
}