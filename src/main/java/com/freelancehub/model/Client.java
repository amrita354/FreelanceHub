
package com.freelancehub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "clients")
public class Client extends User {

    private String companyName;

    protected Client() {
    }

    public Client(String name, String email, String password,
                  String companyName) {
        super(name, email, password);
        this.companyName = companyName;
    }

    @Override
    public String getAccountType() {
        return "CLIENT";
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
