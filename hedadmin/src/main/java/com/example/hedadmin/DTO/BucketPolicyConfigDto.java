package com.example.hedadmin.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BucketPolicyConfigDto {
    private String Version;
    private List<Statement> Statement;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Statement {
        private String Effect;
        private Principal Principal;
        private String[] Action;
        private String[] Resource;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Principal {
        private String[] AWS;
    }
}