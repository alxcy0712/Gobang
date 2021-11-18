import javax.swing.*;
import java.util.*;


class ChildNodes {
    int x;
    int y;
    int score;

    ChildNodes(int x, int y, int score) {
        this.x = x;
        this.y = y;
        this.score = score;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

class ChessArray {
    int[][] chessArray;

    public boolean isEnd() {//判断输赢函数
        boolean flag = true;
        for (int i = 0; i < chessArray.length; i++) {
            for (int j = 0; j < chessArray.length; j++) {
                if (chessArray[i][j] == 0) {
                    flag = false;
                    break;
                }
            }
        }
        if (flag) {//下满了
            JOptionPane.showMessageDialog(null, "accept", "结束，和棋", JOptionPane.PLAIN_MESSAGE);
            return flag;
        }

        for (int i = 0; i < chessArray.length; i++) {
            for (int j = 0; j < chessArray.length; j++) {

                if ((i < chessArray.length - 4 && chessArray[i][j] != 0 && chessArray[i][j] == chessArray[i + 1][j] && chessArray[i + 1][j] == chessArray[i + 2][j] && chessArray[i + 2][j] == chessArray[i + 3][j] && chessArray[i + 3][j] == chessArray[i + 4][j])
                        || (j < chessArray.length - 4 && chessArray[i][j] != 0 && chessArray[i][j] == chessArray[i][j + 1] && chessArray[i][j + 1] == chessArray[i][j + 2] && chessArray[i][j + 2] == chessArray[i][j + 3] && chessArray[i][j + 3] == chessArray[i][j + 4])
                        || (j < chessArray.length - 4 && i < chessArray.length - 4 && chessArray[i][j] != 0 && chessArray[i][j] == chessArray[i + 1][j + 1] && chessArray[i + 1][j + 1] == chessArray[i + 2][j + 2] && chessArray[i + 2][j + 2] == chessArray[i + 3][j + 3] && chessArray[i + 3][j + 3] == chessArray[i + 4][j + 4])
                        || (i < chessArray.length - 4 && j < chessArray.length - 4 && chessArray[i][j + 4] != 0 && chessArray[i][j + 4] == chessArray[i + 1][j + 3] && chessArray[i + 1][j + 3] == chessArray[i + 2][j + 2] && chessArray[i + 2][j + 2] == chessArray[i + 3][j + 1] && chessArray[i + 3][j + 1] == chessArray[i + 4][j])) {

                    if (chessArray[i][j] == 1) {
                        System.out.println("white win");
                        JOptionPane.showMessageDialog(null, "white win", "结束，白棋胜", JOptionPane.PLAIN_MESSAGE);
                    } else if (chessArray[i][j]==2){
                        System.out.println("black win");
                        JOptionPane.showMessageDialog(null, "black win", "结束，黑棋胜", JOptionPane.PLAIN_MESSAGE);
                    }else{
                        System.out.println("black win");
                        JOptionPane.showMessageDialog(null, "black win", "结束，黑棋胜", JOptionPane.PLAIN_MESSAGE);
                    }

                    return true;
                }


            }
        }
        return false;
    }


    public ChessArray(int[][] chess) {
        this.chessArray = chess;
    }

}


public class Compute {


    Compute() {

    }

    public static boolean isEnd(ChessArray chessArray) {
        return chessArray.isEnd();
    }

    public static boolean isEmpty(int[][] chessArray) {
        for (int i = 0; i < chessArray.length; i++) {
            for (int j = 0; j < chessArray.length; j++) {
                if (chessArray[i][j] != 0)
                    return false;
            }
        }
        return true;

    }

    public static boolean isVisited(int chess[][], int x, int y) {
        if (chess[x][y] == 0)
            return false;
        else
            return true;
    }

    static void go(int chessState) throws InterruptedException {
        /**
         * chessState=1为电脑下白棋 玩家下黑棋
         * chessState=2为电脑下黑棋 玩家下白棋
         */
        int[][] chess1 = DrawChessBoard.chessBoard;
        if (isEmpty(chess1)) {
            DrawChessBoard.chessBoard[7][7] = 3 - chessState;
            System.out.print("B(H"+","+8+");");
        } else {
            int[][] chess = DrawChessBoard.chessBoard;

            Queue<ChildNodes> queue = new LinkedList<>();
            Queue<ChildNodes> queueOppo = new LinkedList<>();
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    if (isVisited(chess, i, j))
                        continue;
                    else {
                        chess[i][j] = 3;
                        ChildNodes childNodes = new ChildNodes(i, j, getScore(i, j, chess, 3 - chessState));
                        chess[i][j] = 0;
                        queue.add(childNodes);

                        chess[i][j] = 3;
                        ChildNodes childNodesOppo = new ChildNodes(i, j, getScore(i, j, chess, chessState));
                        chess[i][j] = 0;
                        queueOppo.add(childNodesOppo);
                    }
                }
            }
            int maxX = 0, maxY = 0, max = 0;
            while (!queue.isEmpty()) {
                ChildNodes childNodes = queue.poll();
                if (childNodes.score > max) {
                    max = childNodes.score;
                    maxX = childNodes.getX();
                    maxY = childNodes.getY();
                }
            }
            int maxXOppo = 0, maxYOppo = 0, maxOppo = 0;
            while (!queueOppo.isEmpty()) {
                ChildNodes childNodes = queueOppo.poll();
                if (childNodes.score > maxOppo) {
                    maxOppo = childNodes.score;
                    maxXOppo = childNodes.getX();
                    maxYOppo = childNodes.getY();
                }
            }
            int finalX=0,finalY=0;
            if (maxOppo < max) {
                finalX=maxX;
                finalY=maxY;
            } else if (maxOppo == max) {
                if (DrawChessBoard.computerIsFirst) {
                    finalX=maxX;
                    finalY=maxY;
                } else {
                    finalX=maxXOppo;
                    finalY=maxYOppo;
                }
            } else {
                finalX=maxXOppo;
                finalY=maxYOppo;
            }

            DrawChessBoard.chessBoard[finalX][finalY]=3-chessState;
            char character;
            switch(finalX){
                case 0:character='A';break;
                case 1:character='B';break;
                case 2:character='C';break;
                case 3:character='D';break;
                case 4:character='E';break;
                case 5:character='F';break;
                case 6:character='G';break;
                case 7:character='H';break;
                case 8:character='I';break;
                case 9:character='J';break;
                case 10:character='K';break;
                case 11:character='L';break;
                case 12:character='M';break;
                case 13:character='N';break;
                default:character='O';break;
            }
            int verticalLocation=15-finalY;
            if(DrawChessBoard.chessStatus==2)
                System.out.print("B("+character+","+verticalLocation+");" );
            else
                System.out.print("W("+character+","+verticalLocation+");" );
        }
    }


    public static int getScore(int i, int j, int chess[][], int chessState) {
        //获取该棋手如果在当前点下棋的得分
        //chessState=1为电脑下白棋 玩家下黑棋
        //chessState=2为电脑下黑棋 玩家下白棋
        int score = 0;
        //横
        StringBuilder stringBuilderHorizon = new StringBuilder();
        for (int x = 0; x < 15; x++)
            stringBuilderHorizon.append(chess[x][j]);
        score += calculate(stringBuilderHorizon, chessState);
        //竖
        StringBuilder stringBuilderVertical = new StringBuilder();
        for (int x = 0; x < 15; x++)
            stringBuilderVertical.append(chess[i][x]);
        score += calculate(stringBuilderVertical, chessState);
        //主对角线
        StringBuilder stringBuilderMainDiagonal = new StringBuilder();
        int sum = i + j;
        if (sum < chess.length) {
            for (int a = 0; a < chess.length && sum - a >= 0; a++)
                stringBuilderMainDiagonal.append(chess[a][sum - a]);
        } else {
            for (int a = chess.length - 1; sum - a < chess.length && a >= 0 && sum - a >= 0; a--)
                stringBuilderMainDiagonal.append(chess[sum - a][a]);
        }
        score += calculate(stringBuilderMainDiagonal, chessState);
        //副对角线
        StringBuilder stringBuilderSecondaryDiagonal = new StringBuilder();
        for (int x = Math.min(i, j); x > 0 && i - x >= 0 && j - x >= 0; x--) {
            stringBuilderSecondaryDiagonal.append(chess[i - x][j - x]);
        }
        for (int x = 0; x < 15 && i + x < 15 && j + x < 15; x++) {
            stringBuilderSecondaryDiagonal.append(chess[i + x][j + x]);
        }
        score += calculate(stringBuilderSecondaryDiagonal, chessState);
        return score;
    }

    static int calculate(StringBuilder stringBuilder, int chessState) {
        int score = 0;
        if (stringBuilder.toString().contains("3" + chessState + "" + chessState + "" + chessState + "" + chessState) ||
                stringBuilder.toString().contains(chessState + "3" + chessState + "" + chessState + "" + chessState) ||
                stringBuilder.toString().contains(chessState + "" + chessState + "3" + chessState + "" + chessState) ||
                stringBuilder.toString().contains(chessState + "" + chessState + "" + chessState + "3" + chessState) ||
                stringBuilder.toString().contains(chessState + "" + chessState + "" + chessState + "" + chessState + "3"))//五子连珠
            score += 9999999;
        else if (stringBuilder.toString().contains((3 - chessState) + "3" + chessState + "" + chessState + "" + chessState + "" + (3 - chessState)) ||
                stringBuilder.toString().contains((3 - chessState) + "" + chessState + "3" + chessState + "" + chessState + "" + (3 - chessState)) ||
                stringBuilder.toString().contains((3 - chessState) + "" + chessState + "" + chessState + "3" + chessState + "" + (3 - chessState)) ||
                stringBuilder.toString().contains((3 - chessState) + "" + chessState + "" + chessState + "" + chessState + "3" + (3 - chessState))) //死四
            score += 10;
        else if (stringBuilder.toString().contains("03" + chessState + "" + chessState + "" + chessState + "0") ||
                stringBuilder.toString().contains("0" + chessState + "3" + chessState + "" + chessState + "0") ||
                stringBuilder.toString().contains("0" + chessState + "" + chessState + "3" + chessState + "0") ||
                stringBuilder.toString().contains("0" + chessState + "" + chessState + "" + chessState + "30"))//活四
            score += 200000;
        else if (stringBuilder.toString().contains("03" + chessState + "" + chessState + "" + chessState + "" + (3 - chessState)) ||
                stringBuilder.toString().contains((3 - chessState) + "3" + chessState + "" + chessState + "" + chessState + "0") ||
                stringBuilder.toString().contains("0" + chessState + "3" + chessState + "" + chessState) ||
                stringBuilder.toString().contains(chessState + "3" + chessState + "" + chessState + "0") ||
                stringBuilder.toString().contains("0" + chessState + "" + chessState + "3" + chessState) ||
                stringBuilder.toString().contains(chessState + "" + chessState + "3" + chessState + "0") ||
                stringBuilder.toString().contains("0" + chessState + "" + chessState + "" + chessState + "3") ||
                stringBuilder.toString().contains(chessState + "" + chessState + "" + chessState + "30"))//半活四
            score += 10000;
        else if (stringBuilder.toString().contains("03" + chessState + "" + chessState + "0") ||
                stringBuilder.toString().contains("0" + chessState + "3" + chessState + "0") ||
                stringBuilder.toString().contains("0" + chessState + "" + chessState + "30"))//活三
            score += 9000;
        else if (stringBuilder.toString().contains("3" + chessState + "" + chessState + "0") || stringBuilder.toString().contains("03" + chessState + "" + chessState) ||
                stringBuilder.toString().contains(chessState + "3" + chessState + "0") || stringBuilder.toString().contains("0" + chessState + "3" + chessState) ||
                stringBuilder.toString().contains(chessState + "" + chessState + "30") || stringBuilder.toString().contains("0" + chessState + "" + chessState + "3"))//半活三
            score += 300;

        else if (stringBuilder.toString().contains("3" + chessState + chessState) ||
                stringBuilder.toString().contains(chessState + "3" + chessState) ||
                stringBuilder.toString().contains(chessState + chessState + "3"))//死三
            score += 10;
        else if (stringBuilder.toString().contains("03" + chessState + "0") ||
                stringBuilder.toString().contains("0" + chessState + "30"))//活二
            score += 1000;
        else if (stringBuilder.toString().contains("3" + chessState + "0") ||
                stringBuilder.toString().contains("03" + chessState) ||
                stringBuilder.toString().contains(chessState + "30") ||
                stringBuilder.toString().contains("0" + chessState + "3"))//半活二
            score += 50;
        else if (stringBuilder.toString().contains("3" + chessState) ||
                stringBuilder.toString().contains(chessState + "3"))//死二
            score += 10;

        return score;
    }
}
