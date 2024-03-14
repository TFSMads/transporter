package ml.volder.transporter.modules.messagemodule;

public interface IMessageHandler {
    boolean messageReceived(final String msg, final String clean);
}
