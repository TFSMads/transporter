package ml.volder.transporter.dev.test;

import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.api.draw.impl.Laby4DrawAPI;
import net.labymod.api.client.gui.screen.ScreenContext;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.render.matrix.Stack;

@AutoActivity
public class TestActivity extends SimpleActivity {

    @Override
    public void render(ScreenContext context) {
        Stack currentStack = Laby4DrawAPI.CURRENT_RENDER_STACK;
        Laby4DrawAPI.CURRENT_RENDER_STACK = context.stack();

        DrawAPI.getAPI().drawString("Hello World!", 10, 10, 0xFFFFFF, 1.0F);

        Laby4DrawAPI.CURRENT_RENDER_STACK = currentStack;
    }
}
