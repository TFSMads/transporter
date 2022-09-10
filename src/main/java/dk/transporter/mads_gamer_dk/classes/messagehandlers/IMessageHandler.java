package dk.transporter.mads_gamer_dk.classes.messagehandlers;

public interface IMessageHandler {
    boolean messageReceived(final String msg, final String clean);
}
