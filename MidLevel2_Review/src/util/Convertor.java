package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Scanner;

public class Convertor {
	static final String fpath = "C:\\Users\\Changsun Song\\Desktop\\알고리즘자격준비\\연습문제\\MidLevel2_Review\\data\\1000EWG.txt";
	static final String gpath = "C:\\Users\\Changsun Song\\Desktop\\알고리즘자격준비\\연습문제\\MidLevel2_Review\\data\\G1000EWG.txt";

	public static void main(String args[]) throws Exception {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(gpath)));
		Scanner scan = new Scanner(new FileInputStream(new File(fpath)));

		int N = scan.nextInt(), M = scan.nextInt(), S = 1;
		bw.append("" + N);
		bw.append(" " + M);
		bw.append(" " + S);
		bw.append("\n");
		for (int m = 0; m < M; m++) {
			int u = scan.nextInt() + 1, v = scan.nextInt() + 1;
			int w = (int) (scan.nextDouble() * 10000);
			bw.append("" + u);
			bw.append(" " + v);
			bw.append(" " + w);
			bw.append("\n");
		}
		scan.close();
		bw.flush();
		bw.close();
	}
}
