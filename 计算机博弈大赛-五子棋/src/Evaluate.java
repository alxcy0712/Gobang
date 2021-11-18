

class Dir
{
	int x;
	int y;
	public Dir(int x,int y) {
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
	}
}
class Point
{
	int x;
	int y;
	public Point(int x,int y) {
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
	}
	public Point newPoint(int len, Dir dir)
	{
		return new Point(x + dir.x * len, y + dir.y * len);
	}
}

public class Evaluate {

	static final Dir d1 = new Dir(0, 1);	//横
	static final Dir d2 = new Dir(1, 0);	//竖
	static final Dir d3 = new Dir(1, -1);	//撇
	static final Dir d4 = new Dir(1, 1);	//捺

	public static boolean isInBoard(Point p) {
		return p.x >= 0 && p.x < DrawChessBoard.Rows
				&& p.y >= 0 && p.y < DrawChessBoard.Rows;
	}

	public static boolean isEnd(Point p,int color)
	{
		for(int i = 1; i <= 4; i ++){
			Dir d = null;
			int count = 0;
			switch(i){
				case 1:
					d = d1;
					break;
				case 2:
					d = d2;
					break;
				case 3:
					d = d3;
					break;
				case 4:
					d = d4;
					break;
			}
			for(int j = -4; j <= 4; j ++){
				Point p1 = p.newPoint(j,d);
				if(isInBoard(p1) && DrawChessBoard.chessmanArray[p1.y][p1.x] == color){
					count ++;
				}
				else{
					count = 0;
				}
				if(count == 5){
					return true;
				}
			}
		}
		return false;
	}

	public static Point computerGo() {
		int computer = DrawChessBoard.color;
		int player = 3 - DrawChessBoard.color;
		Point attackPoint = null, defendPoint = null;
		int attScore = 0, b1 = 0, defScore = 0, b2 = 0;
		for(int i = 0; i < 15; i ++){
			for(int j = 0; j < 15; j ++){
				if(DrawChessBoard.chessmanArray[j][i] != 0){
					continue;
				}
				Point cur = new Point(i, j);
				//寻找最佳进攻点
				int m1 = getScore(cur, computer);
				int m2 = getScore(cur, player);
				if(m1 > attScore || (m1 == attScore && m2 > b1)){
					attackPoint = cur;
					attScore = m1;
					b1 = m2;
				}
				//寻找最佳防守点
				int n1 = getScore(cur, player);
				int n2 = getScore(cur, computer);
				if(n1 > defScore || (n1 == defScore && n2 > b2)){
					defendPoint = cur;
					defScore = n1;
					b2 = n2;
				}
			}
		}
		if(attScore >= defScore){
			return attackPoint;
		}
		else{
			return defendPoint;
		}
	}

	/*判断棋型，评估分数*/
	public static int getScore(Point p,int color)
	{
		int win5 = 0, alive4 = 0, die4 = 0, ddie4 = 0, alive3 = 0,
				dalive3 = 0, die3 = 0, alive2 = 0, dalive2 = 0, die2 = 0, nothing = 0;
		int opp = 3 - color;
		for(int j = 1; j <= 4; j++){
			Dir d = null;
			switch(j){
				case 1:
					d = d1;
					break;
				case 2:
					d = d2;
					break;
				case 3:
					d = d3;
					break;
				case 4:
					d = d4;
					break;
			}
			int count = 1;
			Point le, ri, p1;
			int[] left = new int[5], right = new int[5];
			//计算连子数量
			p1 = p.newPoint(-1, d);
			le = p;
			while(isInBoard(p1) && DrawChessBoard.chessmanArray[p1.y][p1.x] == color){
				le = p1;
				p1 = p1.newPoint(-1,d);
				count++;
			}
			p1 = p.newPoint(1,d);
			ri = p;
			while(isInBoard(p1) && DrawChessBoard.chessmanArray[p1.y][p1.x] == color){
				ri = p1;
				p1 = p1.newPoint(1,d);
				count++;
			}
			//计算连子左右棋子情况
			for(int i = 1; i <= 4; i++){
				p1 = le.newPoint(-i,d);
				if(isInBoard(p1)){
					left[i] = DrawChessBoard.chessmanArray[p1.y][p1.x];
				}
				else{
					left[i] = opp;
				}
				p1 = ri.newPoint(i,d);
				if(isInBoard(p1)){
					right[i] = DrawChessBoard.chessmanArray[p1.y][p1.x];
				}
				else{
					right[i] = opp;
				}
			}
			//具体棋型判断
			if(count == 5){	//连五
				win5++;
			}
			else if(count == 4){
				if(left[1] == 0 && right[1] == 0){	//活四
					alive4++;
				}
				else if(left[1] == 0 || right[1] == 0){	//死四
					die4++;
				}
				else{	//nothing
					nothing++;
				}
			}
			else if(count == 3){
				if((left[1] == 0 && left[2] == color) || (right[1] == 0 && right[2] == color)){//冲四
					ddie4 ++;
				}
				else if(left[1] == 0 && right[1] == 0 && (left[2] == 0 || right[2] == 0)){//活三
					alive3 ++;
				}
				else if((left[1] == 0 && left[2] == 0) || (right[1] == 0 && right[2] == 0)){//死三
					die3 ++;
				}
				else if(left[1] == 0 && right[1] == 0){//死三
					die3 ++;
				}
				else{//nothing
					nothing ++;
				}
			}
			else if(count == 2){
				if((left[1] == 0 && left[2] == color && left[3] == color) &&
						(right[1] == 0 && right[2] == color && right[3] == color)){//冲四
					ddie4 ++;
				}
				else if(left[1] == 0 && right[1] == 0 &&
						((left[2] == color && left[3] == 0) || (right[2] == color && right[3] == 0))){//跳活3
					dalive3 ++;
				}
				else if((left[1] == 0 && left[3] == 0 && left[2] == color) ||
						(right[1] == 0 && right[3] == 0 && right[2] == color)){//die3
					die3 ++;
				}
				else if((left[1] == 0 && right[1] == 0) &&
						(left[2] == color || right[2] == color)){//die3
					die3 ++;
				}
				else if((left[1] == 0 && left[2] == 0 && left[3] == color) ||
						(right[1] == 0 && right[2] == 0 && right[3] == color)){//die3
					die3 ++;
				}
				else if((left[1] == 0 && right[1] == 0 && right[2] == 0 && right[3] == 0) ||
						(left[1] == 0 && left[2] == 0 && right[1] == 0 && right[2] == 0) ||
						(left[1] == 0 && left[2] == 0 && left[3] == 0 && right[1] == 0)){//alive2
					alive2 ++;
				}
				else if((left[1] == 0 && left[2] == 0 && left[3] == 0) ||
						(right[1] == 0 && right[2] == 0 && right[3] == 0)){//die2
					die2 ++;
				}
				else{//nothing
					nothing ++;
				}
			}
			else if(count == 1){
				if((left[1] == 0 && left[2] == color && left[3] == color && left[4] == color) ||
						(right[1] == 0 && right[2] == color && right[3] == color && right[4] == color)){//ddie4
					ddie4 ++;
				}
				else if((left[1] == 0 && right[1] == 0) && ((left[2] == color && left[3] == color && left[4] == 0) ||
						(right[2] == color && right[3] == color && right[4] == 0))){//dalive3
					dalive3 ++;
				}
				else if((left[1] == 0 && right[1] == 0) &&
						((left[2] == color && left[3] == color) || (right[2] == color && right[3] == color))){//die3
					die3 ++;
				}
				else if((left[1] == 0 && left[4] == 0 && left[2] == color && left[3] == color) ||
						(right[1] == 0 && right[4] == 0 && right[2] == color && right[3] == color)){//die3
					die3 ++;
				}
				else if((left[1] == 0 && left[2] == 0 && left[3] == color && left[4] == color) ||
						(right[1] == 0 && right[2] == 0 && right[3] == color && right[4] == color)){//die3
					die3 ++;
				}
				else if((left[1] == 0 && left[3] == 0 && left[2] == color && left[4] == color) ||
						(right[1] == 0 && right[3] == 0 && right[2] == color && right[4] == color)){//die3
					die3 ++;
				}
				else if((left[1] == 0 && right[1] == 0 && right[3] == 0 && right[2] == color) && (left[2] == 0 || right[4] == 0)){//dalive2
					dalive2 ++;
				}
				else if((right[1] == 0 && left[1] == 0 && left[3] == 0 && left[2] == color) &&
						(right[2] == 0 || left[4] == 0)){//dalive2
					dalive2 ++;
				}
				else if((left[1] == 0 && right[1] == 0 && right[2] == 0 && right[4] == 0 && right[3] == color) ||
						(right[1] == 0 && left[1] == 0 && left[2] == 0 && left[4] == 0 && left[3] == color)){//dalive2
					dalive2 ++;
				}
				else if((left[1] == 0 && left[3] == 0 && left[4] == 0 && left[2] == color) ||
						(right[1] == 0 && right[3] == 0 && right[4] == 0 && right[2] == color)){//die2
					die2 ++;
				}
				else if((left[1] == 0 && right[1] == 0 && right[2] == 0 && left[2] == color) ||
						(right[1] == 0 && left[1] == 0 && left[2] == 0 && right[2] == color)){//die2
					die2 ++;
				}
				else if((left[1] == 0 && left[2] == 0 && left[4] == 0 && left[3] == color) ||
						(right[1] == 0 && right[2] == 0 && right[4] == 0 && right[3] == color)){//die2
					die2 ++;
				}
				else if((left[1] == 0 && left[2] == 0 && right[1] == 0 && left[3] == color) ||
						(right[1] == 0 && right[2] == 0 && left[1] == 0 && right[3] == color)){//die2
					die2 ++;
				}
				else if((left[1] == 0 && left[2] == 0 && left[3] == 0 && left[4] == color) ||
						(right[1] == 0 && right[2] == 0 && right[3] == 0 && right[4] == color)){//die2
					die2 ++;
				}
				else{//nothing
					nothing ++;
				}
			}
		}
		//计算分值
		if (win5 >= 1)
			return 14;//赢5

		if (alive4 >= 1 || die4 >= 2 || (die4 >= 1 && alive3 >= 1))
			return 13;//活4 双死4 死4活3

		if (alive3 >= 2)
			return 12;//双活3

		if (die3 >= 1 && alive3 >= 1)
			return 11;//死3高级活3

		if (die4 >= 1)
			return 10;//高级死4

		if (ddie4 >= 1)
			return 9;//低级死4

		if (alive3 >= 1)
			return 8;//单活3

		if (dalive3 >= 1)
			return 7;//跳活3

		if (alive2 >= 2)
			return 6;//双活2

		if (alive2 >= 1)
			return 5;//活2

		if (dalive2 >= 1)
			return 4;//低级活2

		if (die3 >= 1)
			return 3;//死3

		if (die2 >= 1)
			return 2;//死2

		return 1;//没有威胁
	}


}
