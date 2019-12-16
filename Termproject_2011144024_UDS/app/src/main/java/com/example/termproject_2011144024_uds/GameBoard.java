package com.example.termproject_2011144024_uds;

public class GameBoard {
    private static final int BOARD_MAX = 10;
    private static final int EMPTY = 0;
    private static final int PLAYER = 1;
    private static final int TARGET = 2;
    private static final int WORK = 1;
    private static final int END = 0;

    private int[][] board = new int[BOARD_MAX][BOARD_MAX];
    private int playerX;
    private int playerY;
    private int targetX;
    private int targetY;
    private int gameStat;

    public GameBoard(){
        gameStat = WORK;

        playerX = (int) (Math.random()*BOARD_MAX);
        playerY = (int) (Math.random()*BOARD_MAX);
        board[playerY][playerX] = PLAYER;

        boolean isSame = true;
        while (isSame){
            targetX = (int) (Math.random()*BOARD_MAX);
            targetY = (int) (Math.random()*BOARD_MAX);
            if(playerX != targetX || playerY != targetY) {
                isSame = false;
                board[targetY][targetX] = TARGET;
            }
        }
    }

    public int getLocateX(int who){      // who = 1 : player, who = 2 : target
        switch(who){
            case PLAYER:
                return playerX;
            case TARGET:
                return targetX;
        }

        return -1;
    }

    public int getLocateY(int who){      // who = 1 : player, who = 2 : target
        switch(who){
            case PLAYER:
                return playerY;
            case TARGET:
                return targetY;
        }

        return -1;
    }


    public int getDistance(){
        int distanceX = playerX - targetX;
        int distanceY = playerY - targetY;
        if(distanceX < 0) distanceX *= -1;
        if(distanceY < 0) distanceY *= -1;
        if(distanceX > distanceY) return distanceX;
        else return distanceY;
    }

    public int MovePlayer(int direction){
        int pastX = playerX;
        int pastY = playerY;

        switch (direction){
            case 1:
                if(playerX == 0) return -1;
                if(playerY == 0) return -1;
                playerX -= 1;
                playerY -= 1;
                break;
            case 2:
                if(playerY == 0) return -1;
                playerY -= 1;
                break;
            case 4:
                if(playerX == BOARD_MAX - 1) return -1;
                if(playerY == 0) return -1;
                playerX += 1;
                playerY -= 1;
                break;
            case 8:
                if(playerX == 0) return -1;
                playerX -= 1;
                break;
            case 16:
                break;
            case 32:
                if(playerX == BOARD_MAX - 1) return -1;
                playerX += 1;
                break;
            case 64:
                if(playerX == 0) return -1;
                if(playerY == BOARD_MAX - 1) return -1;
                playerX -= 1;
                playerY += 1;
                break;
            case 128:
                if(playerY == BOARD_MAX - 1) return -1;
                playerY += 1;
                break;
            case 256:
                if(playerX == BOARD_MAX - 1) return -1;
                if(playerY == BOARD_MAX - 1) return -1;
                playerX += 1;
                playerY += 1;
                break;
            default:
        }

        if(board[playerY][playerX] == TARGET) {
            gameStat = END;
            return 0;
        }

        board[pastY][pastX] = EMPTY;
        board[playerY][playerX] = PLAYER;

        return getDistance();
    }

    public int RunAway(int threshold){      // 임계치에 걸리면 도망
        int distanceX = playerX - targetX;
        int distanceY = playerY - targetY;
        if(Math.abs(distanceX) > threshold || Math.abs(distanceY) > threshold) return 0;

//        if(distanceX > 0 && distanceY > 0){             // target이 좌상단
//
//        } else if(distanceX < 0 && distanceY > 0){      // target이 우상단
//
//        } else if(distanceX > 0 && distanceY < 0){      // target이 좌하단
//
//        } else if(distanceX < 0 && distanceY < 0){      // target이 우하단
//
//        } else if(distanceX == 0 && distanceY > 0){     // target이 윗쪽
//
//        } else if(distanceX < 0 && distanceY == 0){     // target이 오른쪽
//
//        } else if(distanceX == 0 && distanceY < 0){     // target이 아랫쪽
//
//        } else if(distanceX > 0 && distanceY == 0){     // target이 왼쪽
//
//        }

        int direction = (int) (Math.random()*2);          // 0:이동안함, 1:이동함
        if(direction == 0) return 0;

        boolean isCorrect = false;
        while(!isCorrect){
            direction = (int) (Math.random()*4) + 1;          // 1:상, 2:우, 3:하, 4:좌
            switch (direction){
                case 1:
                    if(targetY != 0) {
                        if(targetX == playerX && targetY - 1 == playerY) continue;
                        else targetY -= 1;
                        isCorrect = true;
                    }
                    break;
                case 2:
                    if(targetX != BOARD_MAX - 1) {
                        if(targetX + 1 == playerX && targetY == playerY) continue;
                        else targetX += 1;
                        isCorrect = true;
                    }
                    break;
                case 3:
                    if(targetY != BOARD_MAX - 1) {
                        if(targetX == playerX && targetY + 1 == playerY) continue;
                        else targetY += 1;
                        isCorrect = true;
                    }
                    break;
                case 4:
                    if(targetX != 0) {
                        if(targetX - 1 == playerX && targetY== playerY) continue;
                        else targetX -= 1;
                        isCorrect = true;
                    }
                    break;
            }
        }

        return direction;
    }

}
