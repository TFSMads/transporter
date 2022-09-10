package dk.transporter.mads_gamer_dk.messageSendingSettings;

import dk.transporter.mads_gamer_dk.listeners.messageReceiveListener;

public enum messageSettings
{
    ACTIONBAR(new String[] { "actionbar", "ab", "bar" }),
    CHAT(new String[] { "chat"}),
    INGEN(new String[] { "ingen", "none", "no" });

    private static final messageSettings DEFAULT_ACTION;
    String[] aliases;

    private messageSettings(final String[] aliases) {
        this.aliases = aliases;
    }

    public static messageSettings getDefaultAction() {
        return messageSettings.DEFAULT_ACTION;
    }

    public static messageSettings getUploadService(String input) {
        input = input.toLowerCase().trim();
        for (final messageSettings action : values()) {
            for (final String s : action.getAliases()) {
                if (s.toLowerCase().equalsIgnoreCase(input)) {
                    return action;
                }
            }
        }
        return messageSettings.DEFAULT_ACTION;
    }

    public int getId(){
        if(this == messageSettings.ACTIONBAR){
            return 2;
        }else if(this == messageSettings.INGEN){
            return 1;
        }else if(this == messageSettings.CHAT){
            return 0;
        }
        return 0;
    }

    public String[] getAliases() {
        return this.aliases;
    }

    static {
        DEFAULT_ACTION = messageSettings.ACTIONBAR;
    }
}
