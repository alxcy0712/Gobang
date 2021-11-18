import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrawGobang {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new DrawChessBoard());
		frame.setSize(750, 700);
		frame.setVisible(true);
	}
}

class DrawChessBoard extends JPanel implements MouseListener,ActionListener
{
	static final int sx = 80;//游戏区域方块的起始横坐标
	static final int sy = 40;//游戏区域方块的起始纵坐标
	static final int w = 40;//每个小方格的边长
	static final int rw = 560;//游戏区域方块的边长
	static final int Rows = 15;//行列数
	int chessmanWidth = 30;	//棋子直径
	static int[][] chessmanArray = new int[Rows][Rows];//棋盘状态，0无子，1白子，2黑子
	static final int computer = 1;
	static final int player = 0;
	int user = -1;//该谁下，1电脑，0玩家
	static int color = 2;//该落什么颜色的棋子，1白，2黑
	JButton computerBtn=new JButton("电脑先手");
	JButton playerBtn=new JButton("玩家先手");
	JButton comformBtn = new JButton();

	public DrawChessBoard()
	{
		addMouseListener(this);
		add(computerBtn);
		add(playerBtn);
		add(comformBtn);
		computerBtn.addActionListener(this);
		playerBtn.addActionListener(this);
		comformBtn.addActionListener(this);
		comformBtn.setVisible(false);
	}

	/**画棋盘**/
	void DrawBoard(Graphics g)
	{
		g.setColor(new Color(230, 189, 128));
		g.fillRect(sx, sy, rw, rw);
		g.setColor(Color.RED);
		for (int i = 0; i < Rows; i++) {
			g.drawLine(sx, sy + i * w, sx + rw, sy + i * w);
			g.drawLine(sx + i * w, sy, sx + i * w, sy + rw);
		}
	}

	/**画棋子**/
	void DrawChessMan(Graphics g)
	{
		for (int i = 0; i < Rows; i++) {
			for (int j = 0; j < Rows; j++) {
				if (chessmanArray[j][i]==1) {
					g.setColor(Color.WHITE);
					g.fillOval(sx + w * i - chessmanWidth/2, sy + w * j - chessmanWidth/2, chessmanWidth, chessmanWidth);
				}
				else if (chessmanArray[j][i]==2) {
					g.setColor(Color.BLACK);
					g.fillOval(sx + w * i - chessmanWidth/2, sy + w * j - chessmanWidth/2, chessmanWidth, chessmanWidth);
				}
			}
		}
	}


	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		DrawBoard(g);
		DrawChessMan(g);
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if (user!=player) {
			System.out.println("user:"+user);
			return;
		}
		int x = e.getX();
		int y = e.getY();
		int px = (x - (sx - chessmanWidth / 2)) / w;
		int py = (y - (sy - chessmanWidth / 2)) / w;
		if (px >= 0 && px < Rows && py >= 0 && py < Rows
				&& (x - (sx - chessmanWidth / 2)) % w <= chessmanWidth
				&& (y - (sy - chessmanWidth / 2)) % w <= chessmanWidth
				&& chessmanArray[py][px]==0)
		{
			chessmanArray[py][px] = color;
			System.out.println("color1:"+color+" y:"+py + " x:"+px);
			if (Evaluate.isEnd(new Point(px, py), color)) {
				comformBtn.setVisible(true);
				comformBtn.setText("玩家获胜");
				user = -1;
			}
			else {
				color = 3 - color;
				user = 1 - user;
				Point p = Evaluate.computerGo();
				chessmanArray[p.y][p.x] = color;
				System.out.println("color2:"+color+" y:"+p.y + " x:"+p.x);
				if (Evaluate.isEnd(p, color)) {
					comformBtn.setVisible(true);
					comformBtn.setText("电脑获胜");
					user = -1;
				}
				else {
					color = 3 - color;
					user = 1 - user;
				}
			}
			repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==computerBtn){
			color = 2;
			computerBtn.setVisible(false);
			playerBtn.setVisible(false);
			chessmanArray = new int[Rows][Rows];
			chessmanArray[7][7] = color;
			user = player;
			color = 1;
			repaint();
		}
		else if (e.getSource()==playerBtn) {
			user = player;
			color = 2;
			computerBtn.setVisible(false);
			playerBtn.setVisible(false);
			chessmanArray = new int[Rows][Rows];
			repaint();
		}
		else if (e.getSource()==comformBtn) {
			computerBtn.setVisible(true);
			playerBtn.setVisible(true);
			comformBtn.setVisible(false);
		}

	}
}
