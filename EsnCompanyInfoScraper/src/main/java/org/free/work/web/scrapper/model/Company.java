package org.free.work.web.scrapper.model;

public class Company {
    private String href;
    private String companyName;
    private String companySize;
    private String companyType;
    private String companyDescription;
    private String websiteLink;

    public String getWebsiteLink() {
        return websiteLink;
    }

    public void setWebsiteLink(String websiteLink) {
        this.websiteLink = websiteLink;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanySize() {
        return companySize;
    }

    public void setCompanySize(String companySize) {
        this.companySize = companySize;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public Company(String href, String companyName, String companySize, String companyType, String companyDescription, String websiteLink) {
        this.href = href;
        this.companyName = companyName;
        this.companySize = companySize;
        this.companyType = companyType;
        this.companyDescription = companyDescription;
        this.websiteLink = websiteLink;  // new field
    }

    public void printDetails() {
        System.out.println("Company Name: " + companyName);
        System.out.println("Company Size: " + companySize);
        System.out.println("Company Type: " + companyType);
        System.out.println("Company Description: " + companyDescription);
        System.out.println("Link: " + href);
        System.out.println("Website: " + websiteLink);
    }
}
