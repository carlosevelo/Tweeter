package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter {

    //Interface
    public interface View {
        void displayErrorMessage(String s);
        void displayUserRegistered(User registeredUser);
    }

    //Private Members
    private View view;
    private UserService userService;

    //Constructor
    public RegisterPresenter(View view) {
        this.view = view;
        userService = new UserService();
    }

    //Methods
    public void onClickRegister(String firstName, String lastName, String alias, String password, String imageBytesBase64) {
        userService.registerUser(firstName, lastName, alias, password, imageBytesBase64, new GetRegisterObserver());
    }

    //Observer Classes
    public class GetRegisterObserver implements UserService.RegisterObserver {

        @Override
        public void handleSuccess(User registeredUser, AuthToken authToken) {
            view.displayUserRegistered(registeredUser);
            Cache.getInstance().setCurrUser(registeredUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);

        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to register: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to register because of exception: " + ex.getMessage());
        }
    }
}
