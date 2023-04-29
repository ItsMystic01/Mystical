package mys.serone.mystical.functions;

/**
 * ENUM for Permissions needed for every command used by a user/player
 */
public enum MysticalPermission {

        /**
         * Command Permission For Fly : /fly
         */
        FLY("mystical.fly"),
        /**
         * Command Permission For Kit : /kit
         */
        KIT("mystical.kit"),
        /**
         * Command Permission For Message : /message
         */
        MESSAGE("mystical.message"),
        /**
         * Command Permission For Mystical Help : /mysticalhelp
         */
        MYSTICAL_HELP("mystical.help"),
        /**
         * Command Permission For Rename : /rename
         */
        RENAME("mystical.rename"),
        /**
         * Command Permission For Create Kit : /createkit
         */
        CREATE_KIT("mystical.createkit"),
        /**
         * Command Permission For Delete Kit : /deletekit
         */
        DELETE_KIT("mystical.deletekit"),
        /**
         * Command Permission For Edit Kit : /editkit
         */
        EDIT_KIT("mystical.editkit"),
        /**
         * Command Permission For Set Kit Prefix : /setkitprefix
         */
        SET_KIT_PREFIX("mystical.setkitprefix"),
        /**
         * Command Permission For Check All Rank : /checkallrank
         */
        CHECK_ALL_RANK("mystical.checkallrank"),
        /**
         * Command Permission For Check Player Rank : /checkplayerrank
         */
        CHECK_PLAYER_RANK("mystical.checkplayerrank"),
        /**
         * Command Permission For Create Rank : /createrank
         */
        CREATE_RANK("mystical.createrank"),
        /**
         * Command Permission For Delete All Rank : /deleteallrank
         */
        DELETE_ALL_RANK("mystical.deleteallrank"),
        /**
         * Command Permission For Delete Rank : /deleterank
         */
        DELETE_RANK("mystical.deleterank"),
        /**
         * Command Permission For Give Permission : /givepermission
         */
        GIVE_PERMISSION("mystical.givepermission"),
        /**
         * Command Permission For Give Rank : /giverank
         */
        GIVE_RANK("mystical.giverank"),
        /**
         * Command Permission For Remove Permission : /removepermission
         */
        REMOVE_PERMISSION("mystical.removepermission"),
        /**
         * Command Permission For Remove Rank : /removerank
         */
        REMOVE_RANK("mystical.removerank");

        private final String permission;

        /**
         * @param permission : Permission requirement for the user to execute the command
         */
        MysticalPermission(String permission) {
            this.permission = permission;
        }

        /**
         * @return String : Returns the string format of the required permission for the command
         */
        public String getPermission() {
            return permission;
        }


}
