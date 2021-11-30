package com.pindergarten.pindergarten_android;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.pindergarten.pindergarten_android.databinding.ActivityFindpwd2BindingImpl;
import com.pindergarten.pindergarten_android.databinding.ActivityFindpwdBindingImpl;
import com.pindergarten.pindergarten_android.databinding.ActivityJoin2BindingImpl;
import com.pindergarten.pindergarten_android.databinding.ActivityJoin3BindingImpl;
import com.pindergarten.pindergarten_android.databinding.ActivityJoinBindingImpl;
import com.pindergarten.pindergarten_android.databinding.ActivityLoginBindingImpl;
import com.pindergarten.pindergarten_android.databinding.ActivityMainBindingImpl;
import com.pindergarten.pindergarten_android.databinding.ActivityOnboardingBindingImpl;
import com.pindergarten.pindergarten_android.databinding.ActivitySplashBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_ACTIVITYFINDPWD = 1;

  private static final int LAYOUT_ACTIVITYFINDPWD2 = 2;

  private static final int LAYOUT_ACTIVITYJOIN = 3;

  private static final int LAYOUT_ACTIVITYJOIN2 = 4;

  private static final int LAYOUT_ACTIVITYJOIN3 = 5;

  private static final int LAYOUT_ACTIVITYLOGIN = 6;

  private static final int LAYOUT_ACTIVITYMAIN = 7;

  private static final int LAYOUT_ACTIVITYONBOARDING = 8;

  private static final int LAYOUT_ACTIVITYSPLASH = 9;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(9);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.pindergarten.pindergarten_android.R.layout.activity_findpwd, LAYOUT_ACTIVITYFINDPWD);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.pindergarten.pindergarten_android.R.layout.activity_findpwd2, LAYOUT_ACTIVITYFINDPWD2);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.pindergarten.pindergarten_android.R.layout.activity_join, LAYOUT_ACTIVITYJOIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.pindergarten.pindergarten_android.R.layout.activity_join2, LAYOUT_ACTIVITYJOIN2);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.pindergarten.pindergarten_android.R.layout.activity_join3, LAYOUT_ACTIVITYJOIN3);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.pindergarten.pindergarten_android.R.layout.activity_login, LAYOUT_ACTIVITYLOGIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.pindergarten.pindergarten_android.R.layout.activity_main, LAYOUT_ACTIVITYMAIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.pindergarten.pindergarten_android.R.layout.activity_onboarding, LAYOUT_ACTIVITYONBOARDING);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.pindergarten.pindergarten_android.R.layout.activity_splash, LAYOUT_ACTIVITYSPLASH);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_ACTIVITYFINDPWD: {
          if ("layout/activity_findpwd_0".equals(tag)) {
            return new ActivityFindpwdBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_findpwd is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYFINDPWD2: {
          if ("layout/activity_findpwd2_0".equals(tag)) {
            return new ActivityFindpwd2BindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_findpwd2 is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYJOIN: {
          if ("layout/activity_join_0".equals(tag)) {
            return new ActivityJoinBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_join is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYJOIN2: {
          if ("layout/activity_join2_0".equals(tag)) {
            return new ActivityJoin2BindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_join2 is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYJOIN3: {
          if ("layout/activity_join3_0".equals(tag)) {
            return new ActivityJoin3BindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_join3 is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYLOGIN: {
          if ("layout/activity_login_0".equals(tag)) {
            return new ActivityLoginBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_login is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYMAIN: {
          if ("layout/activity_main_0".equals(tag)) {
            return new ActivityMainBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_main is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYONBOARDING: {
          if ("layout/activity_onboarding_0".equals(tag)) {
            return new ActivityOnboardingBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_onboarding is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYSPLASH: {
          if ("layout/activity_splash_0".equals(tag)) {
            return new ActivitySplashBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_splash is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(3);

    static {
      sKeys.put(0, "_all");
      sKeys.put(1, "activity");
      sKeys.put(2, "vm");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(9);

    static {
      sKeys.put("layout/activity_findpwd_0", com.pindergarten.pindergarten_android.R.layout.activity_findpwd);
      sKeys.put("layout/activity_findpwd2_0", com.pindergarten.pindergarten_android.R.layout.activity_findpwd2);
      sKeys.put("layout/activity_join_0", com.pindergarten.pindergarten_android.R.layout.activity_join);
      sKeys.put("layout/activity_join2_0", com.pindergarten.pindergarten_android.R.layout.activity_join2);
      sKeys.put("layout/activity_join3_0", com.pindergarten.pindergarten_android.R.layout.activity_join3);
      sKeys.put("layout/activity_login_0", com.pindergarten.pindergarten_android.R.layout.activity_login);
      sKeys.put("layout/activity_main_0", com.pindergarten.pindergarten_android.R.layout.activity_main);
      sKeys.put("layout/activity_onboarding_0", com.pindergarten.pindergarten_android.R.layout.activity_onboarding);
      sKeys.put("layout/activity_splash_0", com.pindergarten.pindergarten_android.R.layout.activity_splash);
    }
  }
}
