package net.mikrowonk.game;

import java.io.*;

public class Save {
    private Player player;

    public Save() {
    }
    public Save (Player player) {
        this.player = player;
    }

    public void saving () throws IOException {
        File file = new File("src/net/mikrowonk/saves/Save.txt");
        PrintWriter out = new PrintWriter(file);
        out.print(this.player.getStrength() + " ");
        out.print(this.player.getHealth() + " ");
        out.print(this.player.getSpecialMove() + " ");
        out.print(this.player.getName() + " ");
        out.print(this.player.getSpecialMoveStrength() + " ");
        out.print(this.player.getLevel() + " ");
        out.print(this.player.getLevelXp() + " ");
        out.print(this.player.getPointsSpent());
        out.close();
    }

    /**
     *
     * @return a player, mit den Werten des gespeichertem
     */
    public Player getPlayerSave() throws IOException{
        int i = 0;
        int statCounter = 0;
        int startOfNext = 0;
        Player playerReturn = new Player();
        File save = new File("src/net/mikrowonk/saves/Save.txt");

        BufferedReader br = new BufferedReader(new FileReader(save));
        String stats = br.readLine();
        int len = stats.length();

        do {
            if (i == len - 1) {
                playerReturn.setLevelXp(Integer.parseInt(stats.substring(startOfNext, len)));
            }
            else if (stats.charAt(i) == ' ') {
                //final int temp = Integer.parseInt(stats.substring(startOfNext, i));
                switch (statCounter) {
                    // TODO optimierung
                    // int temp = Integer.parseInt(stats.substring(startOfNext, i)); geht nicht, wieso?
                    case 0 -> playerReturn.setStrength(Integer.parseInt(stats.substring(startOfNext, i)));
                    case 1 -> playerReturn.setHealth(Integer.parseInt(stats.substring(startOfNext, i)));
                    case 2 -> playerReturn.setSpecialMove(stats.substring(startOfNext, i));
                    case 3 -> playerReturn.setName(stats.substring(startOfNext, i));
                    case 4 -> playerReturn.setSpecialMoveStrength(Integer.parseInt(stats.substring(startOfNext, i)));
                    case 5 -> playerReturn.setLevel(Integer.parseInt(stats.substring(startOfNext, i)));
                    case 6 -> playerReturn.setPointsSpent(Integer.parseInt(stats.substring(startOfNext, i)));
                }
                startOfNext = i + 1;
                statCounter++;
            }

            i++;
        } while (i < len);

        br.close();
        return playerReturn;
    }
}