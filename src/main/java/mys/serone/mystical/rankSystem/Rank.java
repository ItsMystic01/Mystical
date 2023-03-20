package mys.serone.mystical.rankSystem;

import java.util.List;

public class Rank {

    private String name;
    private String prefix;
    private List<String> permissions;
    private boolean isDefault;

    public String getName() { return name; }
    public String getPrefix() { return prefix; }
    public List<String> getPermissions() { return permissions; }
    public boolean getIsDefault() { return isDefault; }

    public void setName(String name) { this.name = name; }
    public void setPrefix(String prefix) { this.prefix = prefix; }
    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
    public void setIsDefault(boolean isDefault) { this.isDefault = isDefault; }
}

