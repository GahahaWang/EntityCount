package com.gahaha.entitycount.client.config;

import com.gahaha.entitycount.client.screen.EntityCountConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class CallConfig implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return EntityCountConfigScreen::new;
    }
}
