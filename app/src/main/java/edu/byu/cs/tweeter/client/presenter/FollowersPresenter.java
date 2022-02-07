package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowerService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter {
    private static final int PAGE_SIZE = 10;

    //Interfaces
    public interface View {
        void displayErrorMessage(String s);
        void setLoadingStatus(boolean status);
        void addFollowers(List<User> followers);
        void displayUser(User user);
    }

    //Private members
    private View view;
    private FollowerService followerService;
    private UserService userService;

    private User lastFollower;
    private boolean hasMorePages;
    private boolean isLoading;

    //Constructor
    public FollowersPresenter(View view) {
        this.view = view;
        followerService = new FollowerService();
        userService = new UserService();
    }

    //Getters and Setters
    public boolean hasMorePages() {
        return hasMorePages;
    }
    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }
    public boolean isLoading() {
        return isLoading;
    }
    public void setLoading(boolean loading) {
        isLoading = loading;
    }
    
    //Methods
    public void loadMoreItems(User user) {
        if (!isLoading) {
            isLoading = true;
            view.setLoadingStatus(isLoading);

            followerService.getFollowers(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastFollower, new GetFollowersObserver());
        }
    }
    public void onClickUser(String userAlias) {
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), userAlias, new GetUserObserver());
    }

    //Observer classes
    public class GetFollowersObserver implements FollowerService.GetFollowersObserver {

        @Override
        public void handleSuccess(List<User> followers, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingStatus(isLoading);

            lastFollower = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addFollowers(followers);
        }

        @Override
        public void handleSuccess(int count) {

        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingStatus(isLoading);

            view.displayErrorMessage("Failed to get followers: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            isLoading = false;
            view.setLoadingStatus(isLoading);

            view.displayErrorMessage("Failed to get followers because of exception: " + exception.getMessage());
        }
    }
    public class GetUserObserver implements UserService.GetUserObserver {

        @Override
        public void handleSuccess(User user) {
            view.displayUser(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to get user's profile because of exception: " + exception.getMessage());
        }
    }
}
