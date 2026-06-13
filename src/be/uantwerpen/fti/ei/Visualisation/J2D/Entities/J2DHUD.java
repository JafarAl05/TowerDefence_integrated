package be.uantwerpen.fti.ei.Visualisation.J2D.Entities;

import be.uantwerpen.fti.ei.Game.Entities.HUD;
import be.uantwerpen.fti.ei.Game.Rules.TowerStats;
import be.uantwerpen.fti.ei.Game.Rules.TowerType;
import be.uantwerpen.fti.ei.Visualisation.J2D.Drawable;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public final class J2DHUD extends HUD implements Drawable {
    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(new Color(0, 0, 0, 50));
        graphics2D.fillRoundRect(10, 10, 650, 100, 14, 14);

        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(new Font("SansSerif", Font.BOLD, 16));

        // First line: main game stats
        graphics2D.drawString("Score: " + getScore(), 25, 35);
        graphics2D.drawString("Gold: " + getGold(), 150, 35);
        graphics2D.drawString("Base: " + getLives(), 255, 35);
        graphics2D.drawString("Wave: " + getWave() + "/" + getMaxWaves(), 390, 35);

        // Second line: selected tower
        TowerType selected = getSelectedTowerType();
        int cost = selected == null ? 0 : TowerStats.forType(selected).getCost();

        graphics2D.drawString("Selected tower: " + selected + "    Cost: " + cost, 25, 65);

        // Third line: controls
        graphics2D.setFont(new Font("SansSerif", Font.PLAIN, 13));
        graphics2D.drawString("Click green spots to build.  L/R Arrows change tower.  H repairs base( 500 GOLD).", 25, 90);
    }
}