import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;


// ================================================================================================================== //
// ========                                          项目名称：五子棋                                           ======= //
// ========                       版权信息：@copyright 辽宁大学信息学院18级 柳笑辰 马永超 李日骞                     ======= //
// ========                                       完成日期：2020.10.9                                          ======== //
// ========                                           version 1.0                                            ======== //
// ================================================================================================================== //

public class DrawGraphic extends JFrame {


    public static void main(String[] args) {
        JFrame main = new JFrame("五子棋");
        /**
         *  加入棋盘
         */
        DrawChessBoard graphic = new DrawChessBoard();    //新建一个自定义的DrawChessBoard类型
        graphic.setVisible(true);               //将这个类设置为可见
        main.add(graphic);              //向main组件中加入DrawChessBoard类
        main.setDefaultCloseOperation(3);// 窗体可直接关闭
        main.setSize(621, 600);//设置窗体大小
        main.setLocation(410, 20);//设置窗体左上角位置
        main.setResizable(false);//设置窗体不可变化
        main.setVisible(true);      //main组件可见
    }
}

class DrawChessBoard extends JPanel implements MouseListener, ActionListener {

    static final int startX = 50;     //游戏起始横坐标
    static final int startY = 50;     //游戏起始纵坐标
    static final int craftLength = 35;    //方格边长
    static final int rowsAndLines = 15;   //列和行数
    static final int wholeLength = craftLength * (rowsAndLines - 1);   //整个方格区域边长
    static final int chessWidth = 28;     //棋子直径
    public static int[][] chessBoard = new int[15][15];    //棋盘大小：15*15   1为黑子 2为白子
    public static int chessStatus = 1;            //该处棋盘的棋子状态 1为黑子（先手） 2为白子（后手）
    public static boolean computerIsFirst = false;

    JButton playerFirst = new JButton("玩家先手");
    JButton computerFirst = new JButton("电脑先手");
    JButton restartGame = new JButton("重新开始");


    boolean hasChoseModel = false;
    boolean isComputerFirst;

    @Override
    public void setVisible(boolean aFlag) {     //组件可见
        super.setVisible(aFlag);
    }

    public DrawChessBoard() {
        addMouseListener(this);

        playerFirst.setSize(30, 10);
        playerFirst.setLocation(10, 10);
        computerFirst.setSize(30, 10);
        restartGame.setSize(30, 10);
        restartGame.setFocusPainted(false);

        // ======================================================================================================== //
        // =======                                     美化                                                 ======= //
        // ======================================================================================================= //
        playerFirst.setBackground(Color.cyan);
        playerFirst.setFocusPainted(false);
        computerFirst.setBackground(Color.pink);
        computerFirst.setFocusPainted(false);
        restartGame.setBackground(Color.decode("#e9ccff"));

        add(playerFirst);
        add(computerFirst);
        add(restartGame);

        playerFirst.setVisible(true);
        computerFirst.setVisible(true);
        restartGame.setVisible(false);
        playerFirst.addActionListener(this);
        computerFirst.addActionListener(this);
        restartGame.addActionListener(this);
    }

    void DrawBoard(Graphics g) {
        g.setColor(new Color(211, 152, 83));
        g.fillRect(startX, startY, wholeLength, wholeLength);
        g.setColor(Color.RED);
        for (int i = 0; i < rowsAndLines; i++) {
            g.drawLine(startX, startY + i * craftLength, startX + wholeLength, startY + i * craftLength);
            g.drawLine(startX + i * craftLength, startY, startX + i * craftLength, startY + wholeLength);
        }
    }

    void DrawChess(Graphics g) {
        for (int i = 0; i < rowsAndLines; i++) {
            for (int j = 0; j < rowsAndLines; j++) {
                if (chessBoard[i][j] == 1) {
                    //白棋
                    g.setColor(Color.WHITE);
                    g.fillOval(startX + craftLength * i - chessWidth / 2, startY + craftLength * j - chessWidth / 2, chessWidth, chessWidth);
                } else if (chessBoard[i][j] == 2) {
                    //黑棋
                    g.setColor(Color.BLACK);
                    g.fillOval(startX + craftLength * i - chessWidth / 2, startY + craftLength * j - chessWidth / 2, chessWidth, chessWidth);
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D graphics2D=(Graphics2D)g;
        GradientPaint gradientPaint=new GradientPaint(0,0,Color.decode("#85ff4b"),300,680,Color.decode("#53caca"));
        graphics2D.setPaint(gradientPaint);
        Rectangle2D rectangle2D=new Rectangle.Double(0,35,700,700);
        graphics2D.fill(rectangle2D);
        DrawBoard(g);
        DrawChess(g);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == computerFirst) {
            isComputerFirst = true;
            computerFirst.setVisible(false);
            playerFirst.setVisible(false);
            restartGame.setVisible(true);
            hasChoseModel = true;
            chessStatus = 1;
            computerIsFirst = true;
            try {
                Compute.go(chessStatus);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            repaint();
        }
        if (e.getSource() == playerFirst) {
            isComputerFirst = false;
            computerFirst.setVisible(false);
            playerFirst.setVisible(false);
            restartGame.setVisible(true);
            chessStatus = 2;
            computerIsFirst = false;
            hasChoseModel = true;
        }
        if (e.getSource() == restartGame) {
            chessBoard = new int[rowsAndLines][rowsAndLines];
            computerFirst.setVisible(true);
            playerFirst.setVisible(true);
            restartGame.setVisible(false);
            hasChoseModel = false;
            repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!hasChoseModel) ;
        else {
            int x = e.getX();
            int y = e.getY();
            int px = (x - (startX - chessWidth / 2)) / craftLength;
            int py = (y - (startY - chessWidth / 2)) / craftLength;

            if (px >= 0 && px < wholeLength && py >= 0 && py < wholeLength
                    && (x - (startX - chessWidth / 2)) % craftLength <= chessWidth
                    && (y - (startY - chessWidth / 2)) % craftLength <= chessWidth
                    && chessBoard[px][py] == 0 && hasChoseModel) {
                chessBoard[px][py] = chessStatus;
                char character;
                switch(px){
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
                int verticalLocation=15-py;
                if(chessStatus==1)
                    System.out.print("B("+character+","+verticalLocation+");" );
                else
                    System.out.print("W("+character+","+verticalLocation+");" );
                if (Compute.isEnd(new ChessArray(chessBoard))) {//如果此时一局比赛结束 则直接暂停界面 可以选择返回主界面
                    hasChoseModel = false;
                } else {    //比赛仍在继续...
                    try {
                        Compute.go(chessStatus);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                repaint();
            }
            if (hasChoseModel && Compute.isEnd(new ChessArray(chessBoard))) {
                hasChoseModel = false;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}

