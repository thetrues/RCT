package com.rctapp.adapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thetrues on 4/26/2018.
 */

public class SectionPageAdpter extends FragmentPagerAdapter {

  private final List<Fragment> mFragmentList = new ArrayList<>();
  private final List<String> mFragmentTitleList = new ArrayList<>();

  public void  addFragment(Fragment fragment, String title){
    mFragmentList.add(fragment);
    mFragmentTitleList.add(title);

  }

  public SectionPageAdpter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public CharSequence getPageTitle(int position){
    return  mFragmentTitleList.get(position);
  }

  @Override
  public Fragment getItem(int position) {
    return mFragmentList.get(position);
  }

  @Override
  public int getCount() {
    return mFragmentList.size();
  }
}
 