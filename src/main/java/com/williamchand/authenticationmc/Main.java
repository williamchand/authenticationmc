package com.williamchand.authenticationmc;

import java.util.Map;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("authenticationmc")
public class Main {
    public static final String MOD_ID = "authenticationmc";

    public Main() {
        // Register the event handlers
        MinecraftForge.EVENT_BUS.register(new AuthEvents());
        MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);

        // Load data from auth.json at mod initialization
        Map<String, String> loadedData = AuthStorage.load();
        loadedData.forEach((username, password) -> PlayerAuthHandler.register(username, password));
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        // Register the commands
        event.getDispatcher().register(AuthCommand.registerCommand());
        event.getDispatcher().register(AuthCommand.loginCommand());
        event.getDispatcher().register(AuthCommand.changePasswordCommand());
    }
}
