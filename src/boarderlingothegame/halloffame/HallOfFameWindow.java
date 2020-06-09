package boarderlingothegame.halloffame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JPanel;

	class Panel extends JPanel {
		
		Map<String,Integer> chattersToCausedDeaths = new TreeMap<>();

		Panel() {
			setLayout(null);

		}
		
		public void reset() {
			chattersToCausedDeaths = new TreeMap<String, Integer>();
		}
		public void update(String chatteWhoKilledMe) {
			int oldValue = chattersToCausedDeaths.getOrDefault(chatteWhoKilledMe, 0);
			if(oldValue == 0)
				chattersToCausedDeaths.put(chatteWhoKilledMe, 1);
			else {
				chattersToCausedDeaths.remove(chatteWhoKilledMe);
				chattersToCausedDeaths.put(chatteWhoKilledMe, oldValue+1);
			}
			validate();
			repaint();
			System.out.println(chattersToCausedDeaths.get(chatteWhoKilledMe));
		}
		public void paintComponent(Graphics g) {

			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;


			setFocusable(true);
			int index=0;
			if(chattersToCausedDeaths.isEmpty())
				return;
			//Sort
			chattersToCausedDeaths = chattersToCausedDeaths.entrySet().stream()
            .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
			//
			Double biggestElement = new Double((int)chattersToCausedDeaths.values().toArray()[0]);
			for (Entry<String, Integer> entry : chattersToCausedDeaths.entrySet()) {
				String name = entry.getKey();
				
				g2d.setColor(Color.BLACK);
				g2d.drawString(name, 10, 18+15*index);
				g2d.setColor(new Color(255-(index*25), 0, index*5, 255));
				g2d.fillRect(150, 10+15*index,(int) (100*(double)entry.getValue()/biggestElement), 10);
				g2d.setColor(Color.BLACK);
				g2d.drawString(entry.getValue()+"", 260, 18+15*index);
				index++;
			}
		} 



	}

	public class HallOfFameWindow extends JFrame {

		Panel panel = new Panel();

		public HallOfFameWindow() {
			add(panel);
			setSize(300, 200);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);

		}

		public static void main(String[] args) {
			new HallOfFameWindow();
		}
		public void update(String chatteWhoKilledMe) {
			panel.update(chatteWhoKilledMe);
			setAlwaysOnTop(false);
		}
		public void reset() {
			panel.reset();
		}

	}

