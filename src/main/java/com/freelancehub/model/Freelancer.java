
package com.freelancehub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "freelancers")
public class Freelancer extends User {

    private String skills;

    private int experienceYears;

    protected Freelancer() {
    }

    public Freelancer(String name, String email, String password,
                      String skills, int experienceYears) {
        super(name, email, password);
        this.skills = skills;
        this.experienceYears = experienceYears;
    }

    @Override
    public String getAccountType() {
        return "FREELANCER";
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }
}
