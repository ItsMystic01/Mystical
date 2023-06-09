package mys.serone.mystical.rankSystem;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for setters and getters for Ranks
 */
public class Rank {
    private UUID id;
    private String name;
    private String prefix;
    private int priority;
    private List<String> permissions;
    private List<Map<String, Object>> kit;
    private String kitName;
    private String startingHexColor;
    private String endingHexColor;

    /**
     * @return id : Generated UUID for the rank
     */
    public UUID getId() { return id; }

    /**
     * @return name : Rank name provided by the CreateRank command
     * @see mys.serone.mystical.roleCommands.CreateRank
     */
    public String getName() { return name; }

    /**
     * @return prefix : Rank prefix provided by the CreateRank command
     * @see mys.serone.mystical.roleCommands.CreateRank
     */
    public String getPrefix() { return prefix; }

    /**
     * @return priority : Rank priority provided by the CreateRank command
     * @see mys.serone.mystical.roleCommands.CreateRank
     */
    public int getPriority() { return priority; }

    /**
     * @return permissions : Rank permission provided by the CreateRank command
     * Rank permission is configurable and checked by ranks.yml
     * @see RanksManager
     * @see mys.serone.mystical.roleCommands.CreateRank
     */
    public List<String> getPermissions() { return permissions; }

    /**
     * @return kit : Rank kit is configurable in ranks.yml
     * @see mys.serone.mystical.roleCommands.CreateRank
     */
    public List<Map<String, Object>> getKit() { return kit; }

    /**
     * @return kitName
     * @see mys.serone.mystical.roleCommands.CreateRank
     */
    public String getKitName() { return kitName; }

    /**
     * @return startingHexColor
     */
    public String getStartingHexColor() { return startingHexColor; }

    /**
     * @return endingHexColor
     */
    public String getEndingHexColor() { return endingHexColor; }

    /**
     * @param id : To set the UUID generated ID for the rank
     */
    public void setId(UUID id) { this.id = id; }

    /**
     * @param name : To set the name for the rank
     */
    public void setName(String name) { this.name = name; }

    /**
     * @param prefix : To set the prefix for the rank
     */
    public void setPrefix(String prefix) { this.prefix = prefix; }

    /**
     * @param priority : To set the priority number for the rank
     */
    public void setPriority(int priority) { this.priority = priority; }

    /**
     * @param permissions : To set the permissions for the rank
     */
    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    /**
     * @param kit : To set the kit for the rank
     */
    public void setKit(List<Map<String, Object>> kit) { this.kit = kit; }

    /**
     * @param kitName : To set the kit name for the rank
     */
    public void setKitName(String kitName) { this.kitName = kitName; }

    /**
     * @param startingHexColor : To set the starting HEX color for the name of the rank
     */
    public void setStartingHexColor(String startingHexColor) {

        if (startingHexColor == null) { this.startingHexColor = null; return; }

        Pattern pattern = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
        Matcher matcher = pattern.matcher(startingHexColor);

        if (matcher.matches() && startingHexColor.length() > 0) {
            this.startingHexColor = startingHexColor;
        } else {
            this.startingHexColor = null;
        }
    }
    /**
     * @param endingHexColor : To set the ending HEX color for the name of the rank
     */
    public void setEndingHexColor(String endingHexColor) {
        if (endingHexColor == null) { this.endingHexColor = null; return; }

        Pattern pattern = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
        Matcher matcher = pattern.matcher(endingHexColor);

        if (matcher.matches() && endingHexColor.length() > 0) {
            this.endingHexColor = endingHexColor;
        } else {
            this.endingHexColor = null;
        }
    }
}

