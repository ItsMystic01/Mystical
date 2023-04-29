package mys.serone.mystical.kitSystem;

/**
 * Class for setters and getters for PersonalKit
 */
public class PersonalKit {
    private String kitName;
    private String kitCodeName;

    /**
     * @return kitName : Kit Name set by CreateKit command
     * @see mys.serone.mystical.kit.CreateKit
     */
    public String getKitName() { return kitName; }

    /**
     * @return kitCodeName : Kit Code Name set by CreateKit command
     * @see mys.serone.mystical.kit.CreateKit
     */
    public String getKitCodeName() { return kitCodeName; }

    /**
     * @param kitName : To set the name of the kit, which can be used in claiming kits using Kit Command
     * @see mys.serone.mystical.commands.Kit
     */
    public void setKitName(String kitName) { this.kitName = kitName; }

    /**
     * @param kitCodeName : To set the colored name to be added to the items of the kit in claiming kits using Kit Command
     * @see mys.serone.mystical.commands.Kit
     */
    public void setKitCodeName(String kitCodeName) { this.kitCodeName = kitCodeName; }
}

