package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter {

    //Interface
    public interface View {
        void displayErrorMessage(String s);
        void displayUserLoggedIn(User loggedInUser);
    }

    //Private Members
    private View view;
    private UserService userService;

    //Constructor
    public LoginPresenter(View view) {
        this.view = view;
        userService = new UserService();
    }

    //Getters and Setters

    //Methods
    public void onClickLogin(String alias, String password) {
        userService.loginUser(alias, password, new GetLoginObserver());
    }

    //Observer Classes
    public class GetLoginObserver implements UserService.LoginObserver {

        @Override
        public void handleSuccess(User loggedInUser, AuthToken authToken) {
            // Cache user session information
            Cache.getInstance().setCurrUser(loggedInUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);

            view.displayUserLoggedIn(loggedInUser);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to login: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to login because of exception: " + ex.getMessage());
        }
    }
}
