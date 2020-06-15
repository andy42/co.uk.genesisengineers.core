package ui.activity;

import ui.view.View;
import ui.view.ViewGroup;

import java.util.*;

public class FragmentManager {

    private Activity activity;

    public FragmentManager(Activity activity){
        this.activity = activity;
    }

    private Map<Integer, Stack<Fragment>> fragmentIdMap = new HashMap<>();

    private Stack<Fragment> getBackStack(int id){
        if(fragmentIdMap.containsKey(id) == false){
            fragmentIdMap.put(id, new Stack<>());
            return fragmentIdMap.get(id);
        }
        return fragmentIdMap.get(id);
    }

    private void addFragment(int viewId, Fragment newFragment, boolean addToBackStack){
        Stack<Fragment> backStack = getBackStack(viewId);

        if(backStack.size() > 0){
            Fragment oldFragment = backStack.peek();
            oldFragment.onDestroyView();

            if(addToBackStack == false){
                oldFragment.onDestroy();
                oldFragment.onDetach();
            }
        }
        backStack.add(newFragment);
    }

    public void replace( int viewIdToBeReplaced, Fragment newFragment, boolean addToBackStack){
        View view = activity.findViewById(viewIdToBeReplaced);
        if(view == null) return;

        newFragment.onAttach(activity);

        newFragment.onCreate(activity);
        View fragmentView = newFragment.onCreateView(view.getParent());
        newFragment.setView(fragmentView);


        if(view.getParent() == null){
            activity.setView(newFragment.getView());
        } else {
            view.getParent().replace(view, newFragment.getView());
        }
        fragmentView.invalidateLayout();
        newFragment.onViewCreated(fragmentView);

        fragmentView.setId(viewIdToBeReplaced);

        addFragment(viewIdToBeReplaced, newFragment, addToBackStack);
    }

    public boolean popBackStack(int viewId){
        Stack<Fragment> backStack = getBackStack(viewId);
        if(backStack.size() <= 1) {
            return false;
        }
        Fragment currentFragment = backStack.pop();
        currentFragment.onPause();

        View currentView = currentFragment.getView();


        Fragment newFragment = backStack.peek();
        newFragment.onAttach(activity);

        View fragmentView = newFragment.onCreateView(currentFragment.getView().getParent());
        newFragment.setView(fragmentView);

        fragmentView.onMeasure(currentView.getMeasuredWidth(), currentView.getMeasuredHeight());
        fragmentView.onLayout(currentView.getMeasuredWidth(), currentView.getMeasuredHeight(), (int)currentView.getPosition().x, (int)currentView.getPosition().y);

        currentView.getParent().replace(currentView, newFragment.getView());

        fragmentView.invalidateLayout();
        newFragment.onViewCreated(fragmentView);
        fragmentView.setId(viewId);

        newFragment.onResume();

        currentFragment.onDestroyView();
        currentFragment.onDestroy();
        currentFragment.onDetach();
        return true;
    }
}
