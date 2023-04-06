package mys.serone.mystical.functions;

public class PermissionENUM {

    public enum permissionENUM {
        FLY("mystical.fly"),
        KIT("mystical.kit"),
        MESSAGE("mystical.message"),
        MYSTICAL_HELP("mystical.help"),
        RENAME("mystical.rename"),
        BALANCE("mystical.balance"),
        PAY("mystical.pay"),
        CREATE_KIT("mystical.createkit"),
        DELETE_KIT("mystical.deletekit"),
        EDIT_KIT("mystical.editkit"),
        SET_KIT_PREFIX("mystical.setkitprefix"),
        CHECK_ALL_RANK("mystical.checkallrank"),
        CHECK_PLAYER_RANK("mystical.checkplayerrank"),
        CREATE_RANK("mystical.createrank"),
        DELETE_RANK("mystical.deleterank"),
        GIVE_PERMISSION("mystical.givepermission"),
        GIVE_RANK("mystical.giverank"),
        REMOVE_PERMISSION("mystical.removepermission"),
        REMOVE_RANK("mystical.removerank");

        private final String permission;

        permissionENUM(String permission) {
            this.permission = permission;
        }

        public String getPermission() {
            return permission;
        }
    }


}
