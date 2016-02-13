package com.iknow.mobilegcm;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import java.util.List;

import com.androidexample.mobilegcm.R;


public class FragmentTabAdapter implements RadioGroup.OnCheckedChangeListener{
    private List<Fragment> fragments; // һ��tabҳ���Ӧһ��Fragment
    private RadioGroup rgs; // �����л�tab
    private FragmentActivity fragmentActivity; // Fragment������Activity
    private int fragmentContentId; // Activity����Ҫ���滻�������id

    private int currentTab; // ��ǰTabҳ������

    private OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener; // �����õ��������л�tabʱ�������µĹ���

    public FragmentTabAdapter(FragmentActivity fragmentActivity, List<Fragment> fragments, int fragmentContentId, RadioGroup rgs) {
        this.fragments = fragments;
        this.rgs = rgs;
        this.fragmentActivity = fragmentActivity;
        this.fragmentContentId = fragmentContentId;

        // Ĭ����ʾ��һҳ
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        ft.add(fragmentContentId, fragments.get(0));
        ft.commit();

        rgs.setOnCheckedChangeListener(this);


    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        for(int i = 0; i < rgs.getChildCount(); i++){
        	
            if(rgs.getChildAt(i).getId() == checkedId){
                Fragment fragment = fragments.get(i/2);
                FragmentTransaction ft = obtainFragmentTransaction(i);
                
                getCurrentFragment().onPause(); // ��ͣ��ǰtab
//                getCurrentFragment().onStop(); // ��ͣ��ǰtab

                if(fragment.isAdded()){
//                    fragment.onStart(); // ����Ŀ��tab��onStart()
                    fragment.onResume(); // ����Ŀ��tab��onResume()
                    
                }else{
                    ft.add(fragmentContentId, fragment);
                }
                
                showTab(i); // ��ʾĿ��tab
                ft.commit();

                // ����������л�tab���⹦�ܹ��ܽӿ�
                if(null != onRgsExtraCheckedChangedListener){
                    onRgsExtraCheckedChangedListener.OnRgsExtraCheckedChanged(radioGroup, checkedId, i);
                }

            }
        }

    }

    /**
     * �л�tab
     * @param idx
     */
    private void showTab(int idx){
        for(int i = 0; i < fragments.size(); i++){
            Fragment fragment = fragments.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(idx/2);

            if(idx/2 == i){
                ft.show(fragment);
            }else{
            	
            		ft.hide(fragment);
            }
            ft.commit();
        }
        currentTab = idx/2; // ����Ŀ��tabΪ��ǰtab
    }

    /**
     * ��ȡһ����������FragmentTransaction
     * @param index
     * @return
     */
    private FragmentTransaction obtainFragmentTransaction(int index){
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        // �����л�����
    //    ft.setCustomAnimations(R.anim.main_page_come, R.anim.main_page_gone);
        if(index > currentTab){
            ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
        }else{
            ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
        }
        return ft;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public Fragment getCurrentFragment(){
        return fragments.get(currentTab/2);
    }

    public OnRgsExtraCheckedChangedListener getOnRgsExtraCheckedChangedListener() {
        return onRgsExtraCheckedChangedListener;
    }

    public void setOnRgsExtraCheckedChangedListener(OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener) {
        this.onRgsExtraCheckedChangedListener = onRgsExtraCheckedChangedListener;
    }

    /**
     *  �л�tab���⹦�ܹ��ܽӿ�
     */
    static class OnRgsExtraCheckedChangedListener{
        public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index){

        }
    }

}