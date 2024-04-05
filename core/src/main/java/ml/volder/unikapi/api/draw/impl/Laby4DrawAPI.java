package ml.volder.unikapi.api.draw.impl;

import java.awt.*;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import ml.volder.unikapi.SupportedClient;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.api.minecraft.MinecraftAPI;
import ml.volder.unikapi.loader.Laby4Loader;
import ml.volder.unikapi.types.Material;
import ml.volder.unikapi.types.ResourceLocation;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.badge.PositionType;
import net.labymod.api.client.entity.player.badge.renderer.BadgeRenderer;
import net.labymod.api.client.gfx.pipeline.util.MatrixTracker;
import net.labymod.api.client.gfx.texture.GFXGetTextureParameter;
import net.labymod.api.client.gui.HorizontalAlignment;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.theme.Theme;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.client.render.draw.batch.BatchResourceRenderer;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.client.render.font.text.TextRenderer;
import net.labymod.api.client.render.font.text.TextRenderer.StringStart;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.util.bounds.Point;
import net.labymod.api.util.bounds.Rectangle;

@SupportedClient(clientBrand = "labymod4", minecraftVersion = "*")
public class Laby4DrawAPI implements DrawAPI {

  private String namespace = Laby4Loader.namespace();
  public static Stack CURRENT_RENDER_STACK;
  private static Laby4DrawAPI instance;

  public static Laby4DrawAPI getAPI() {
    if(instance == null)
      instance = new Laby4DrawAPI();
    return instance;
  }

  public static Theme getVanillaTheme() {
    return Laby.labyAPI().themeService().getThemeByName("vanilla");
  }

  public static Stack getRenderStack() {
    return CURRENT_RENDER_STACK == null ? MatrixTracker.TEXTURE_MATRIX : CURRENT_RENDER_STACK;
  }
  public static TextRenderer getTextRenderer() {
    return getVanillaTheme().textRenderer();
  }

  //region Other stuff
  @Override
  public int getScaledWidth() {
    return Laby.labyAPI().minecraft().minecraftWindow().getScaledWidth();
  }

  @Override
  public int getScaledHeight() {
    return Laby.labyAPI().minecraft().minecraftWindow().getScaledHeight();
  }

  @Override
  public int getTextureWidth() {
    return GFXGetTextureParameter.TEXTURE_WIDTH.getHandle();
  }

  @Override
  public int getTextureHeight() {
    return GFXGetTextureParameter.TEXTURE_HEIGHT.getHandle();
  }

  @Override
  public void registerTransporterBadgeRenderer(Predicate<UUID> predicate) {
    Laby.references().badgeRegistry().register("transporter-badge", PositionType.RIGHT_TO_NAME, new TransporterBadgeRenderer(predicate));
  }

  @Override
  public void drawHoverText(List<String> text, int x, int y) {
    Component component = Component.empty();
    for (String s : text) {
      component = component.append(Component.text(s));
    }
    RenderableComponent renderableComponent = RenderableComponent.of(component, 200, HorizontalAlignment.LEFT);
    Laby.references().tooltipService().renderFixedTooltip(getRenderStack(), Point.fixed(x, y), renderableComponent);
  }

  private class TransporterBadgeRenderer extends BadgeRenderer {
    private final Icon icon;
    private final Predicate<UUID> predicate;

    public TransporterBadgeRenderer(Predicate<UUID> predicate) {
      this.predicate = predicate;
      this.icon = Icon.sprite(
          net.labymod.api.client.resources.ResourceLocation.create(namespace, "textures/badge.png"), 0, 0, 32, 32, 32, 32);
    }

    public void render(Stack stack, float x, float y, NetworkPlayerInfo player) {
      if(predicate.test(player.profile().getUniqueId())){
        this.icon.render(stack, x, y, 8.0F);
      }
    }

    public boolean isVisible(NetworkPlayerInfo player) {
      return true;
    }

    public int getSize() {
      return 7;
    }
  }

  //endregion

  //region Texture related

  //TODO include blend boolean in texture draw call (since GL calls through drawAPI is no longer supported)
  //TODO create textureRenderer API
  public static void bindLabyTexture(net.labymod.api.client.resources.ResourceLocation resourceLocation) {
    if (instance != null && instance instanceof Laby4DrawAPI) {
      instance.bindTexture(resourceLocation);
    }
  }

  public void drawTexturedModalRect(double x, double y, double textureX, double textureY, double width, double height) {
    BatchResourceRenderer renderer = Laby.references().resourceRenderer().beginBatch(getRenderStack(), boundTexture);
    Laby.references().resourceRenderContext().blit((float) x,(float)y,(float)textureX,(float)textureY,(float)width,(float)height);
    renderer.upload();
  }


  public void drawTexturedModalRect(double left, double top, double right, double bottom) {
    this.drawTexturedModalRect(left, top, 0, 0, right - left, bottom - top);
  }


  public void drawTexture(double x, double y, double texturePosX, double texturePosY, double imageWidth, double imageHeight, double maxWidth, double maxHeight, float alpha) {

    if (alpha <= 1.0F) {
      Laby.gfx().enableBlend();
    }

    Laby.references().resourceRenderer()
        .pos((float) x, (float) y)
        .color(1F, 1F, 1F, alpha)
        .sprite((float) texturePosX, (float) texturePosY, (float) imageWidth, (float) imageHeight)
        .size((float) maxWidth, (float) maxHeight)
        .texture(boundTexture)
        .render(getRenderStack());

    if (alpha <= 1.0F) {
      Laby.gfx().disableBlend();
    }
  }

  public void drawTexture(double x, double y, double imageWidth, double imageHeight, double maxWidth, double maxHeight, float alpha) {
    this.drawTexture(x, y, 0, 0, imageWidth, imageHeight, maxWidth, maxHeight, alpha);
  }

  public void drawTexture(double x, double y, double imageWidth, double imageHeight, double maxWidth, double maxHeight) {
    this.drawTexture(x, y, imageWidth, imageHeight, maxWidth, maxHeight, 1.0F);
  }

  public void drawTexture(double x, double y, double texturePosX, double texturePosY, double imageWidth, double imageHeight, double maxWidth, double maxHeight) {
    this.drawTexture(x, y, texturePosX, texturePosY, imageWidth, imageHeight, maxWidth, maxHeight, 1.0F);
  }

  private net.labymod.api.client.resources.ResourceLocation boundTexture;

  @Override
  public void bindTexture(ResourceLocation resourceLocation) {
    this.boundTexture = Laby.references().resourceLocationFactory().create(namespace, resourceLocation.getResourcePath());
    Laby.references().glStateBridge().bindTexture(boundTexture);
  }

  public void bindTexture(net.labymod.api.client.resources.ResourceLocation resourceLocation) {
    this.boundTexture = resourceLocation;
    Laby.references().glStateBridge().bindTexture(boundTexture);
  }

  //endregion

  //region String related

  public String trimStringToWidth(String text, int width)
  {
    return this.trimStringToWidth(text, width, false);
  }

  public String trimStringToWidth(String text, int width, boolean reverse)
  {
    return getTextRenderer().trimStringToWidth(text, width, !reverse ? StringStart.LEFT : StringStart.RIGHT);
  }
  @Override
  public int getFontHeight() {
    return (int) getTextRenderer().height();
  }

  public int getStringWidth(String text) {
    return (int) getTextRenderer().width(text);
  }

  public void renderString(String text, float x, float y, boolean shadow, boolean centered, float scale, int color)
  {
    Stack stack = getRenderStack();
    TextRenderer textRenderer = getTextRenderer();

    textRenderer
        .pos(centered ? x - getStringWidth(text)*scale / 2: x, y)
        .shadow(shadow)
        .scale(scale)
        .color(color)
        .text(text);

    textRenderer.render(stack);
  }

  //endregion

  //region Rectangle related
  public void drawRect(double left, double top, double right, double bottom, int color) {
    double j;
    if (left < right) {
      j = left;
      left = right;
      right = j;
    }

    if (top < bottom) {
      j = top;
      top = bottom;
      bottom = j;
    }
    Laby.references().rectangleRenderer()
        .pos((float) left, (float) top, (float) right, (float) bottom)
        .color(color)
        .render(getRenderStack());
  }

  public void drawGradientRect(int left, int top, int right, int bottom, Color startColor, Color endColor)
  {
    Laby.references().rectangleRenderer()
        .pos((float) left, (float) top, (float) right, (float) bottom)
        .gradientVertical(
            startColor.getRed(),
            startColor.getGreen(),
            startColor.getBlue(),
            startColor.getAlpha(),
            endColor.getRed(),
            endColor.getGreen(),
            endColor.getBlue(),
            endColor.getAlpha())
        .render(getRenderStack());
  }

  //endregion

  //region Background related
  public void drawBackground(int tint, double scrolling, int brightness) {
    Rectangle bounds = Rectangle.absolute(0, 0, getScaledWidth(), getScaledHeight());
    Laby.references().resourceRenderer()
        .texture(Laby.labyAPI().minecraft().textures().backgroundTexture())
        .pos(bounds)
        .sprite(bounds.getX() * 8.0F, bounds.getY() * 8.0F, bounds.getWidth() * 8.0F, bounds.getHeight() * 8.0F)
        .color(brightness / 255.0F, brightness / 255.0F, brightness / 255.0F, 1.0F)
        .render(getRenderStack());
  }

  public void drawOverlayBackground(int startY, int endY) {
    float brightness = 64;
    Rectangle bounds = Rectangle.absolute(0, startY, getScaledWidth(), endY);
    Laby.references().resourceRenderer()
        .texture(Laby.labyAPI().minecraft().textures().backgroundTexture())
        .pos(bounds)
        .sprite(bounds.getX() * 8.0F, bounds.getY() * 8.0F, bounds.getWidth() * 8.0F, bounds.getHeight() * 8.0F)
        .color(brightness / 255.0F, brightness / 255.0F, brightness / 255.0F, 1.0F)
        .render(getRenderStack());
  }

  public void drawDimmedOverlayBackground(int left, int top, int right, int bottom) {
    float brightness = 64;
    Rectangle bounds = Rectangle.absolute(left, top, right, bottom);
    Laby.references().resourceRenderer()
        .texture(Laby.labyAPI().minecraft().textures().backgroundTexture())
        .pos(bounds)
        .sprite(bounds.getX() * 8.0F, bounds.getY() * 8.0F, bounds.getWidth() * 8.0F, bounds.getHeight() * 8.0F)
        .color(brightness / 255.0F, brightness / 255.0F, brightness / 255.0F, 1.0F)
        .render(getRenderStack());
  }
  //endregion

  //region Item related

  public void renderItemIntoGUI(Material material, int itemDamage, double x, double y, double scale) {
    if(material == null)
      return;
    Stack stack = getRenderStack();
    stack.scale((float) scale, (float) scale, 0);

    ItemStack itemStack;
    if(MinecraftAPI.getAPI().isLegacy()) {
      itemStack = Laby.references().itemStackFactory().create(material.getNamespace(), material.getPath(true, false), 1);
      itemStack.setLegacyItemData(material.getItemDamage());
    }else {
      itemStack = Laby.references().itemStackFactory().create(material.getNamespace(), material.getPath(false), 1);
    }
    //TODO fiks så blocks ikke er mørke og så chest ikke ser mærklig ud.
    if(itemStack == null)
      return;
    Laby.labyAPI().minecraft().itemStackRenderer().renderItemStack(
        stack,
        itemStack,
        (int) (x / scale),
        (int) (y / scale)
    );
    //Laby.references().glStateBridge().disableCull();

    stack.scale((float) (1.0F / scale), (float) (1.0F / scale), 0);
  }


  //endregion

}
