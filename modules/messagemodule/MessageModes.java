package ml.volder.transporter.modules.messagemodule;

public enum MessageModes {
    NO_MESSAGES("Ingen"),
    CHAT_MESSAGES("Chat"),
    ACTIONBAR_MESSAGES("Actionbar");

    private String displayName;

    MessageModes(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return this.displayName;
    }
}
