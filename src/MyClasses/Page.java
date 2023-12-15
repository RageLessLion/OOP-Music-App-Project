package MyClasses;

import lombok.Getter;

public class Page {
    @Getter
    private String type;
    @Getter
    private String watchedUser;
    @Getter
    private String pageName;

    public Page(String type, String watchedUser,String pageName) {
        this.type = type;
        this.watchedUser = watchedUser;
        this.pageName = pageName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setWatchedUser(String watchedUser) {
        this.watchedUser = watchedUser;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
}
