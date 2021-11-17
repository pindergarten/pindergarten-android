package com.example.pindergarten_android.databinding;
import com.example.pindergarten_android.R;
import com.example.pindergarten_android.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityLoginBindingImpl extends ActivityLoginBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.textView, 4);
        sViewsWithIds.put(R.id.textView2, 5);
        sViewsWithIds.put(R.id.imageView, 6);
        sViewsWithIds.put(R.id.editText2, 7);
        sViewsWithIds.put(R.id.textView4, 8);
        sViewsWithIds.put(R.id.imageView5, 9);
        sViewsWithIds.put(R.id.editText, 10);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    // variables
    // values
    // listeners
    private OnClickListenerImpl mActivityBtnClickAndroidViewViewOnClickListener;
    // Inverse Binding Event Handlers

    public ActivityLoginBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 11, sIncludes, sViewsWithIds));
    }
    private ActivityLoginBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.EditText) bindings[10]
            , (android.widget.EditText) bindings[7]
            , (android.widget.TextView) bindings[2]
            , (android.widget.ImageView) bindings[6]
            , (android.widget.ImageView) bindings[9]
            , (android.widget.TextView) bindings[3]
            , (android.widget.ImageButton) bindings[1]
            , (android.widget.TextView) bindings[4]
            , (android.widget.TextView) bindings[5]
            , (android.widget.TextView) bindings[8]
            );
        this.findPwd.setTag(null);
        this.join.setTag(null);
        this.login.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.vm == variableId) {
            setVm((com.example.pindergarten_android.PindergartenViewModel) variable);
        }
        else if (BR.activity == variableId) {
            setActivity((com.example.pindergarten_android.LoginActivity) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setVm(@Nullable com.example.pindergarten_android.PindergartenViewModel Vm) {
        this.mVm = Vm;
    }
    public void setActivity(@Nullable com.example.pindergarten_android.LoginActivity Activity) {
        this.mActivity = Activity;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.activity);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        com.example.pindergarten_android.LoginActivity activity = mActivity;
        android.view.View.OnClickListener activityBtnClickAndroidViewViewOnClickListener = null;

        if ((dirtyFlags & 0x6L) != 0) {



                if (activity != null) {
                    // read activity::btnClick
                    activityBtnClickAndroidViewViewOnClickListener = (((mActivityBtnClickAndroidViewViewOnClickListener == null) ? (mActivityBtnClickAndroidViewViewOnClickListener = new OnClickListenerImpl()) : mActivityBtnClickAndroidViewViewOnClickListener).setValue(activity));
                }
        }
        // batch finished
        if ((dirtyFlags & 0x6L) != 0) {
            // api target 1

            this.findPwd.setOnClickListener(activityBtnClickAndroidViewViewOnClickListener);
            this.join.setOnClickListener(activityBtnClickAndroidViewViewOnClickListener);
            this.login.setOnClickListener(activityBtnClickAndroidViewViewOnClickListener);
        }
    }
    // Listener Stub Implementations
    public static class OnClickListenerImpl implements android.view.View.OnClickListener{
        private com.example.pindergarten_android.LoginActivity value;
        public OnClickListenerImpl setValue(com.example.pindergarten_android.LoginActivity value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.btnClick(arg0); 
        }
    }
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): vm
        flag 1 (0x2L): activity
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}