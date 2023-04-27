package mys.serone.mystical.rankSystem;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Rank {
    private UUID id;
    private String name;
    private String prefix;
    private int priority;
    private List<String> permissions;
    private List<Map<String, Object>> kit;
    private String kitName;

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getPrefix() { return prefix; }
    public int getPriority() { return priority; }
    public List<String> getPermissions() { return permissions; }
    public List<Map<String, Object>> getKit() { return kit; }
    public String getKitName() { return kitName; }

    public void setId(UUID id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrefix(String prefix) { this.prefix = prefix; }
    public void setPriority(int priority) { this.priority = priority; }
    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
    public void setKit(List<Map<String, Object>> kit) { this.kit = kit; }
    public void setKitName(String kitName) { this.kitName = kitName; }
}

