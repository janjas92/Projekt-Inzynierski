package jendrzyca.piotr.qrreader.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Piotr Jendrzyca on 14.10.2016.
 */

public class EmployeeCode extends RealmObject {

    @Required
    @PrimaryKey
    String hashCode;

    public EmployeeCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public EmployeeCode(){}

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }
}
