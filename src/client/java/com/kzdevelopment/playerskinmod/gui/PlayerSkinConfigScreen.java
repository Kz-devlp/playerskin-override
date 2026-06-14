package com.kzdevelopment.playerskinmod.gui;

import com.kzdevelopment.playerskinmod.PlayerOverrideManager;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;

import java.util.*;

public class PlayerSkinConfigScreen extends Screen {
    private final List<PlayerListEntry> players;
    private final Map<UUID, TextFieldWidget> fields = new LinkedHashMap<>();
    private int scrollOffset = 0;
    private static final int ENTRIES_PER_PAGE = 8;

    public PlayerSkinConfigScreen() {
        super(Text.literal("Skin & Nametag Override"));
        MinecraftClient mc = MinecraftClient.getInstance();
        players = new ArrayList<>(mc.getNetworkHandler() != null
                ? mc.getNetworkHandler().getPlayerList()
                : List.of());
    }

    @Override
    protected void init() {
        int y = 40;
        for (int i = scrollOffset; i < Math.min(players.size(), scrollOffset + ENTRIES_PER_PAGE); i++) {
            PlayerListEntry info = players.get(i);
            // authlib 7.x: GameProfile is a record — accessors are id() and name()
            GameProfile profile = info.getProfile();
            UUID uuid = profile.id();
            String current = Optional.ofNullable(PlayerOverrideManager.getNameOverride(uuid)).orElse("");

            TextFieldWidget field = new TextFieldWidget(
                    textRenderer, width / 2, y, 140, 18, Text.literal("fake name"));
            field.setText(current);
            field.setPlaceholder(Text.literal("enter username"));
            addDrawableChild(field);
            fields.put(uuid, field);
            y += 30;
        }

        addDrawableChild(ButtonWidget.builder(Text.literal("Apply"), btn -> applyAll())
                .dimensions(width / 2 - 50, height - 30, 100, 20).build());

        if (players.size() > ENTRIES_PER_PAGE) {
            addDrawableChild(ButtonWidget.builder(Text.literal("▲"), btn -> scroll(-1))
                    .dimensions(width / 2 + 80, 40, 20, 20).build());
            addDrawableChild(ButtonWidget.builder(Text.literal("▼"), btn -> scroll(1))
                    .dimensions(width / 2 + 80, 70, 20, 20).build());
        }
    }

    private void scroll(int delta) {
        scrollOffset = Math.max(0, Math.min(players.size() - ENTRIES_PER_PAGE, scrollOffset + delta));
        clearChildren();
        init();
    }

    private void applyAll() {
        fields.forEach((uuid, field) -> {
            String val = field.getText().trim();
            if (val.isEmpty()) PlayerOverrideManager.removeOverride(uuid);
            else PlayerOverrideManager.setOverride(uuid, val);
        });
        MinecraftClient.getInstance().setScreen(null);
    }

    @Override
    public void render(DrawContext context, int mx, int my, float dt) {
        renderBackground(context, mx, my, dt);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 15, 0xFFFFFF);
        int y = 40;
        for (int i = scrollOffset; i < Math.min(players.size(), scrollOffset + ENTRIES_PER_PAGE); i++) {
            // authlib 7.x record: name() not getName()
            String name = players.get(i).getProfile().name();
            context.drawTextWithShadow(textRenderer, Text.literal(name), width / 2 - 150, y + 4, 0xAAAAAA);
            y += 30;
        }
        super.render(context, mx, my, dt);
    }
}
