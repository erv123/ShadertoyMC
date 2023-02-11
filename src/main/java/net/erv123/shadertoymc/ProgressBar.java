package net.erv123.shadertoymc;

import me.senseiwells.arucas.core.Interpreter;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.BossBarManager;
import net.minecraft.entity.boss.CommandBossBar;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ProgressBar {
    private static CommandBossBar bossBar;
    public static void deleteAllBossBars() {
        BossBarManager manager = ShaderUtils.SERVER.getBossBarManager();
        CommandBossBar[] bossBars = manager.getAll().toArray(CommandBossBar[]::new);
        for (CommandBossBar bar : bossBars) {
            bar.clearPlayers();
            manager.remove(bar);
        }
    }
    public static void hideBossBar() {
        if (bossBar != null) {
            bossBar.clearPlayers();
        }
    }
    public static void generateBossBar(Interpreter interpreter) {
        if (bossBar == null) {
            BossBarManager manager = ShaderUtils.SERVER.getBossBarManager();
            Identifier id = Identifier.of("Shadertoy", "bar");
            bossBar = manager.get(id);
            if (bossBar == null) {
                bossBar = manager.add(id, Text.of("Progress"));
            }
        } else {
            bossBar.setName(Text.of("Progress"));
        }
        bossBar.setPercent(0);
        bossBar.setColor(BossBar.Color.GREEN);
        bossBar.addPlayer(ShaderUtils.getPlayerForInterpreter(interpreter));
    }

    public static void setPercentage(float p){
        bossBar.setPercent(p);
    }
}
