package com.algosheets.backend.model;

import lombok.Data;

import java.util.List;


@Data
public class UserProfile {
    private List<EmailAddress> emailAddresses;
    private List<Name> names;
    private List<Photo> photos;

    @Data
    public static class EmailAddress {
        private String value;
    }

    @Data
    public static class Name {
        private String displayName;
    }

    @Data
    public static class Photo {
        private String url;
    }

}
