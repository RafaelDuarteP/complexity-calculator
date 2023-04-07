package main;

import static calculator.CyclomaticComplexityCalculator.calculateCyclomaticComplex;
import static calculator.NPathComplexityCalculator.calculateNPathComplex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class Calculate {

	public static void main(String[] args) {
		String output, inputPath;
		if (args.length >= 1) {
			inputPath = args[0];
			try {
				output = args[1] + "output.csv";
			} catch (ArrayIndexOutOfBoundsException e) {
				output = "./output.csv";
			}
			try {
				System.out.println("Calculando métricas . . .");
				File javaFile = new File(inputPath);
				JavaParser jp = new JavaParser();
				CompilationUnit cu = jp.parse(javaFile).getResult().orElseThrow();
				int cc = calculateCyclomaticComplex(cu);
				int npath = calculateNPathComplex(cu);
				System.out.println("Metricas extraídas");
				writeCSV(inputPath, cc, npath, output);
			} catch (FileNotFoundException e) {
				System.out.println("Arquivo " + inputPath + " não encontrado");
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("Erro ao executar");
				e.printStackTrace();
			}
		} else {
			System.out.println("Parametro de input é obrigatório");
		}
	}

	public static void writeCSV(String fileName, int cc, int npath, String csvFile) {
		FileWriter writer;

		try {
			writer = new FileWriter(csvFile);
			writer.append("arquivo,cc,npath\n");
			writer.append(fileName + "," + cc + "," + npath + "\n");
			writer.flush();
			writer.close();
			System.out.println("Arquivo salvo em " + csvFile);
		} catch (IOException e) {
			System.out.println("Erro ao escrever o csv: " + e.getMessage());
		}
	}

}
