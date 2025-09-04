package tests.UI.pages.AutomationPlayground.webForm;

import java.util.List;

public class FormData {
    private final String fullName;
    private final String password;
    private final String comments;
    private final String age;
    private final String email;
    private final String website;
    private final String phone;
    private final String gender;
    private final List<String> skills;
    private final String experience;
    private final String country;
    private final String state;
    private final String city;
    private final String dob;
    private final String time;
    private final String datetime;
    private final String month;
    private final String week;
    private final String favColor;
    private final String volume;
    private final String search;
    private final String uploadFile;
    private final String iframeRadio;
    private final List<String> iframeCheckboxes;
    private final String iframeText;

    // Private constructor
    private FormData(Builder builder) {
        this.fullName = builder.fullName;
        this.password = builder.password;
        this.comments = builder.comments;
        this.age = builder.age;
        this.email = builder.email;
        this.website = builder.website;
        this.phone = builder.phone;
        this.gender = builder.gender;
        this.skills = builder.skills;
        this.experience = builder.experience;
        this.country = builder.country;
        this.state = builder.state;
        this.city = builder.city;
        this.dob = builder.dob;
        this.time = builder.time;
        this.datetime = builder.datetime;
        this.month = builder.month;
        this.week = builder.week;
        this.favColor = builder.favColor;
        this.volume = builder.volume;
        this.search = builder.search;
        this.uploadFile = builder.uploadFile;
        this.iframeRadio = builder.iframeRadio;
        this.iframeCheckboxes = builder.iframeCheckboxes;
        this.iframeText = builder.iframeText;
    }

    // Getters
    public String getFullName() { return fullName; }
    public String getPassword() { return password; }
    public String getComments() { return comments; }
    public String getAge() { return age; }
    public String getEmail() { return email; }
    public String getWebsite() { return website; }
    public String getPhone() { return phone; }
    public String getGender() { return gender; }
    public List<String> getSkills() { return skills; }
    public String getExperience() { return experience; }
    public String getCountry() { return country; }
    public String getState() { return state; }
    public String getCity() { return city; }
    public String getDob() { return dob; }
    public String getTime() { return time; }
    public String getDatetime() { return datetime; }
    public String getMonth() { return month; }
    public String getWeek() { return week; }
    public String getFavColor() { return favColor; }
    public String getVolume() { return volume; }
    public String getSearch() { return search; }
    public String getUploadFile() { return uploadFile; }
    public String getIframeRadio() { return iframeRadio; }
    public List<String> getIframeCheckboxes() { return iframeCheckboxes; }
    public String getIframeText() { return iframeText; }

    // ==== Builder Class ====
    public static class Builder {
        private String fullName;
        private String password;
        private String comments;
        private String age;
        private String email;
        private String website;
        private String phone;
        private String gender;
        private List<String> skills;
        private String experience;
        private String country;
        private String state;
        private String city;
        private String dob;
        private String time;
        private String datetime;
        private String month;
        private String week;
        private String favColor;
        private String volume;
        private String search;
        private String uploadFile;
        private String iframeRadio;
        private List<String> iframeCheckboxes;
        private String iframeText;

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }
        public Builder password(String password) { this.password = password; return this; }
        public Builder comments(String comments) { this.comments = comments; return this; }
        public Builder age(String age) { this.age = age; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder website(String website) { this.website = website; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder gender(String gender) { this.gender = gender; return this; }
        public Builder skills(List<String> skills) { this.skills = skills; return this; }
        public Builder experience(String experience) { this.experience = experience; return this; }
        public Builder country(String country) { this.country = country; return this; }
        public Builder state(String state) { this.state = state; return this; }
        public Builder city(String city) { this.city = city; return this; }
        public Builder dob(String dob) { this.dob = dob; return this; }
        public Builder time(String time) { this.time = time; return this; }
        public Builder datetime(String datetime) { this.datetime = datetime; return this; }
        public Builder month(String month) { this.month = month; return this; }
        public Builder week(String week) { this.week = week; return this; }
        public Builder favColor(String favColor) { this.favColor = favColor; return this; }
        public Builder volume(String volume) { this.volume = volume; return this; }
        public Builder search(String search) { this.search = search; return this; }
        public Builder uploadFile(String uploadFile) { this.uploadFile = uploadFile; return this; }
        public Builder iframeRadio(String iframeRadio) { this.iframeRadio = iframeRadio; return this; }
        public Builder iframeCheckboxes(List<String> iframeCheckboxes) { this.iframeCheckboxes = iframeCheckboxes; return this; }
        public Builder iframeText(String iframeText) { this.iframeText = iframeText; return this; }

        public FormData build() { return new FormData(this); }
    }
}

