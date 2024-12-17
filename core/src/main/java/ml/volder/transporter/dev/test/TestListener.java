package ml.volder.transporter.dev.test;

import ml.volder.transporter.events.TestEvent;
import ml.volder.unikapi.UnikAPI;
import net.labymod.api.event.Subscribe;

public class TestListener {

    @Subscribe
    public void onTestEvent(TestEvent event) {
        UnikAPI.LOGGER.debug("TestEvent received");
    }
}
