package com.shaik.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;

/**
 * Created by jabbars on 1/31/2017.
 */
@JsonDeserialize(builder = FileDetails.Builder.class)
public class FileDetails {
    @JsonProperty("filePath")
    private String filePath;

    // Defaulted to C drive, we can change by clients request
    @JsonProperty("logPath")
    private String logPath;

    public FileDetails(Builder builder) {
        this.filePath = builder.filePath;
        this.logPath = builder.logPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getLogPath() {
        return logPath;
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
    public static class Builder{

        private String filePath;
        private String logPath;

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder logPath(String logPath) {
            this.logPath = logPath;
            return this;
        }
        public FileDetails build(){
            return new FileDetails(this);
        }
    }
}
