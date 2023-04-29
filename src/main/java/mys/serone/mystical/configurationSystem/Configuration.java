package mys.serone.mystical.configurationSystem;

/**
 * Class for setters and getters for Configuration
 */
public class Configuration {
    private String defaultRank;

    /**
     * Default Rank is "Member"
     * @return defaultRank : Default Rank is provided by OnFirstJoinEventListener and/or mystical_configuration.yml
     * @see mys.serone.mystical.listeners.OnFirstJoinListener
     */
    public String getDefaultRank() { return defaultRank; }

    /**
     * Default Rank is "Member"
     * @param defaultRank : To set the defaultRank that is saved in mystical_configuration.yml
     */
    public void setDefaultRank(String defaultRank) { this.defaultRank = defaultRank; }
}

