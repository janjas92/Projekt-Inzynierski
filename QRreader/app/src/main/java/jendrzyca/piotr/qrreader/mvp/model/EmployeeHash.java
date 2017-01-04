package jendrzyca.piotr.qrreader.mvp.model;

/**
 * Created by Piotr Jendrzyca on 14.10.2016.
 */

public class EmployeeHash {

    String hashCode;

    public EmployeeHash(String hashCode) {
        this.hashCode = hashCode;
    }

    public EmployeeHash() {
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }
}
