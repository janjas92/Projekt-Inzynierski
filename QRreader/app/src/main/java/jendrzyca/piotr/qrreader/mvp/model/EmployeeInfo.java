package jendrzyca.piotr.qrreader.mvp.model;

/**
 * Created by Piotr Jendrzyca on 10/13/16.
 */

public class EmployeeInfo {
    String name;
    Integer status;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFirstName() {
        return name;
    }

    public void setFirstName(String firstName) {
        this.name = firstName;
    }

}
