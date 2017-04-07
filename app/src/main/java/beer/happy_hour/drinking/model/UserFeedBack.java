package beer.happy_hour.drinking.model;

/**
 * Created by brcon on 07/04/2017.
 */

/**
 * Singleton implementation
 */
public class UserFeedBack {

    private static UserFeedBack instance;

    private boolean authorize_contact_information = true;
    private String feedBack = "";

    private UserFeedBack(){}

    public static UserFeedBack getInstance(){
        if (instance == null) {
            synchronized (UserFeedBack.class) {
                if (instance == null) {
                    instance = new UserFeedBack();
                }
            }
        }

        return instance;
    }

    public boolean isAuthorize_contact_information() {
        return authorize_contact_information;
    }

    public void setAuthorize_contact_information(boolean authorize_contact_information) {
        this.authorize_contact_information = authorize_contact_information;
    }

    public String getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(String feedBack) {
        this.feedBack = feedBack;
    }
}
