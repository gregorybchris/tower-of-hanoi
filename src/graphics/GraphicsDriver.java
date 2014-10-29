package graphics;
import javax.swing.JFrame;

public class GraphicsDriver {
	public static void main(String[] args) {
		GraphicsFrame f = new GraphicsFrame("Tower of Hanoi - Chris Gregory");
		f.setBounds(0, 0, 900, 600);
		f.setLocationRelativeTo(null);
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
