package org.lds.mediafinder.constants;

/**
 * Provides build args for each environment
 * @author Allen Sudweeks
 */
public enum Environment {
    
    DEV("dev"), TEST("test"), UAT("uat"), STAGE("stage"), PROD("prod");
    
    private String buildArg;
    
    Environment(String buildArg) {
        this.buildArg = buildArg;
    }
    
    public String getBuildArg() {
        return buildArg;
    }
}
