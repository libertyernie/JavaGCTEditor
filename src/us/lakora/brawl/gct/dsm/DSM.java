package us.lakora.brawl.gct.dsm;

import us.lakora.brawl.gct.DynamicCode;
import us.lakora.brawl.gct.Line;

/**
 * Default Settings Modifier
 * by Igglyboo, Brkirch, and FMK
 */
public class DSM extends DynamicCode {
	
	public final static String[] SEARCH = {
		"24494A98 80000000",
		"20523300 00000000",
		"04523300 DEADBEEF",
		"42000000 90000000",
		"0417F360 0000",
		"0417F364",
		"0417F368",
		"0417F36C",
		"E0000000 80008000",
	};

	/**
	 * The version of the code used in P:M demo 2.
	 */
	public final static String[] DEFAULT = {
		"24494A98 80000000",
		"20523300 00000000",
		"04523300 DEADBEEF",
		"42000000 90000000",
		"0417F360 00000104",
		"0417F364 04000a00",
		"0417F368 08010101",
		"0417F36C 01000000",
		"E0000000 80008000",
	};
	
	private Line[] lines;
	
	public DSM() {
		this(
				new Line(DEFAULT[0]),
				new Line(DEFAULT[1]),
				new Line(DEFAULT[2]),
				new Line(DEFAULT[3]),
				new Line(DEFAULT[4]),
				new Line(DEFAULT[5]),
				new Line(DEFAULT[6]),
				new Line(DEFAULT[7]),
				new Line(DEFAULT[8]));
	}
	
	public DSM(Line... array) {
		if (array.length != 9) {
			throw new IndexOutOfBoundsException("DSM code must be 9 lines");
		}
		lines = array;
	}
	
	public Line[] getLineArray() {
		return lines;
	}
	
	public byte getGameType() {
		return lines[4].data[6];
	}
	/**
	 * Sets the game type: 0 = time, 1 = stock, 2 = coin
	 * @param b
	 */
	public void setGameType(byte b) {
		if (b < 0 || b > 2) {
			throw new IndexOutOfBoundsException("Game type must be 1, 2, or 3");
		}
		lines[4].data[6] = b;
	}

	public byte getTimeLimit() {
		return lines[4].data[7];
	}
	/**
	 * Sets the time limit: 0 for infinite, or any amount from 1 through 63.
	 */
	public void setTimeLimit(byte b) {
		if (b < 0 || b > 99) {
			throw new IndexOutOfBoundsException("Time limit must be 1-99, inclusive, or 0 for infinite");
		}
		lines[4].data[7] = b;
	}

	public byte getStock() {
		return lines[5].data[4];
	}
	public void setStock(byte b) {
		if (b < 1 || b > 99) {
			throw new IndexOutOfBoundsException("Stock must be 1-99, inclusive");
		}
		lines[5].data[4] = b;
	}

	public byte getHandicap() {
		return lines[5].data[5];
	}
	/**
	 * Sets the handicap: 0 = off; 1 = auto; 2 = on
	 */
	public void setHandicap(byte b) {
		if (b < 0 || b > 2) {
			throw new IndexOutOfBoundsException("Handicap must be 0 (off), 1 (auto) or 2 (on)");
		}
		lines[5].data[5] = b;
	}

	/**
	 * Returns the damage ratio, multiplied by 10.
	 */
	public byte getDamageRatio() {
		return lines[5].data[6];
	}
	/**
	 * Sets the damage ratio, multiplied by 10.
	 */
	public void setDamageRatio(byte b) {
		if (b < 5 || b > 20) {
			throw new IndexOutOfBoundsException("Damage ratio must be from 5 through 20 - invalid value " + (b*10.0));
		}
		lines[5].data[6] = b;
	}
	/**
	 * Sets the damage ratio.
	 */
	public void setDamageRatio(double d) {
		byte b = (byte)Math.round(d*10);
		setDamageRatio(b);
	}
	
	public byte getStageMethod() {
		return lines[5].data[7];
	}
	/**
	 * Sets the stage selection method: 0 = choose, 1 = random, 2 = turns, 3 = ordered, 4 = loser's pick
	 */
	public void setStageMethod(byte b) {
		if (b < 0 || b > 4) {
			throw new IndexOutOfBoundsException("Stage method must be 0, 1, 2, 3, or 4");
		}
		lines[5].data[7] = b;
	}
	
	public byte getStockTimeLimit() {
		return lines[6].data[4];
	}
	/**
	 * Sets the stock time limit: 0 for infinite, or any amount from 1 through 63.
	 */
	public void setStockTimeLimit(byte b) {
		if (b < 0 || b > 99) {
			throw new IndexOutOfBoundsException("Stock time limit must be 1-63 or 0 for infinite");
		}
		lines[6].data[4] = b;
	}
	
	public boolean getTeamAttack() {
		return (lines[6].data[5] != 0);
	}
	public void setTeamAttack(boolean b) {
		if (b) {
			lines[6].data[5] = 1;
		} else {
			lines[6].data[5] = 0;
		}
	}
	
	public boolean getPause() {
		return (lines[6].data[6] != 0);
	}
	public void setPause(boolean b) {
		if (b) {
			lines[6].data[6] = 1;
		} else {
			lines[6].data[6] = 0;
		}
	}
	
	public boolean getScoreDisplay() {
		return (lines[6].data[7] != 0);
	}
	/**
	 * Sets whether to display the score in a timed match.
	 * @param b
	 */
	public void setScoreDisplay(boolean b) {
		if (b) {
			lines[6].data[7] = 1;
		} else {
			lines[6].data[7] = 0;
		}
	}
	
	public boolean getDamageGauge() {
		return (lines[7].data[4] != 0);
	}
	/**
	 * Sets whether to display the damage gauge.
	 * @param b
	 */
	public void setDamageGauge(boolean b) {
		if (b) {
			lines[7].data[4] = 1;
		} else {
			lines[7].data[4] = 0;
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Type: ");
		int i = getGameType();
		if (i == 0) {
			sb.append("time");
		} else if (i == 1) {
			sb.append("stock");
		} else if (i == 2) {
			sb.append("coin");
		}
		sb.append(" Time: ");
		sb.append(Integer.toString(getTimeLimit(), 16));
		sb.append(" Stock: ");
		sb.append(Integer.toString(getStock(), 16));
		sb.append(" Handicap: ");
		sb.append(getHandicap());
		sb.append(" Damage Ratio: ");
		sb.append(getDamageRatio() / 10.0);
		sb.append(" Stage Method: ");
		sb.append(getStageMethod());
		sb.append(" Stock Time: ");
		sb.append(Integer.toString(getStockTimeLimit(), 16));
		sb.append("\nTeam Attack: ");
		sb.append(getTeamAttack());
		sb.append(" Pause: ");
		sb.append(getPause());
		sb.append(" Score Display: ");
		sb.append(getScoreDisplay());
		sb.append(" Damage Gauge: ");
		sb.append(getDamageGauge());
		return sb.toString();
	}
	
	public String description() {
		StringBuilder sb = new StringBuilder("Default Settings Modifier: ");
		int i = getGameType();
		if (i == 0) {
			sb.append("time");
		} else if (i == 1) {
			sb.append("stock");
		} else if (i == 2) {
			sb.append("coin");
		}
		sb.append(" mode; time ");
		sb.append(Integer.toString(getTimeLimit(), 16));
		sb.append("; stock ");
		sb.append(Integer.toString(getStock(), 16));
		sb.append("; etc.");
		return sb.toString();
	}

}
